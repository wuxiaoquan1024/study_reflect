# study_reflect
学习反射与ClassLoader

一. 反射
    1. 反射的作用， 为什么要引入反射

        反射： 在运行时能够动态的访问JVM 中的类、方法、属性的编译语言能力， 可以绕过编译器对类的检测。
        世间万物都有两面性，能给你带来好处，同时也会带来缺陷
        缺点：
            a. 开销大
                由于反射是在运行期执行设计到类的动态分析类型，所有无法执行相关的JVM优化处理。 因此需要避免对性能敏感的功能频繁的操作反射
				a.1 invoke时参数是object数组, 对于基本数据类型需要转换成包装类型
				a.2 getXXX 时需要检测可见性
				a.3 需要检测参数的合法性.
				a.4 JIT 无法执行

            b. 安全验证
                由于反射在运行时执行，所以无法在安全监测（security manager）模式下执行反射

            c. OOM 被打破
                反射可以通过Class 加载类，同时可以修改元素(方法、 成员变量)的access权限，这些相关操作已经偏离的OOM思想

        1.1 反射优化
            反射存在性能上的优化，那采用那些操作能够优化反射的性能呢

            a. 缓存反射元素（Class, Method, Field等等）

            b. 选择合适的API.如果需要反射public修饰的元素调用getXXX， 如果需要反射自身定义的元素调用getDeclaredXXX方法

    2. 反射的基本使用
        2.1 Java 中有三种方式可以获得Class

            a. Class.forName()

            b. 类名.class

            c. 对象.getClass()

        2.2 三种方式的区别

            a. 类名.class 将二进制字节码数据加载到JVM中,并在Heap中生成Class, 并不对Class进行初始化操作

            b. Class.forName() 将二进制字节码数据加载到JVM中,并在Heap中生成Class, 根据传递进来的init 操作是否决定进行静态初始化操作

            c. 对象.getClass() 因需要有对象,因此在调用getClass 方法前需要进行完整的初始化操作

        不管上面三种中的任意几种重复获取Class, 只要ClassLoader相同,获得Class都是同一个.

    3. 反射核心对象

        3.1 核心对象

            a. Class

            b. Method

            c. Construct

            d. Field

            e. Array

            f. Annotation

        3.1.2 注意点
            3.1.2.1 getDeclaredXXX()与getXXX()的却别
                getDeclaredXXX(): 只能获得Class类中自身定义的元素, 不能包好父类中的. 如果需要获得父类中的可以通过getParent 递归获取
                getXXX(): 获取本身及父类中public修饰的元素

            3.1.2.2 getSuperclass 与 getGenericSuperclass
                getSuperclass: 返回类的父类原始类型, 默认为Object
                getGenericSuperclass: 返回带有泛型的父类类型


        3.2 反射中的泛型

            反射中的泛型主要是有Type类来表示.涉及的类包含 Class, ParameterizedType, TypeVariable, WildcardType 与 GenericArrayType.

                ParameterizedType: 参数化类型, 不管<>有多少层只获取第一层

                TypeVariable: 泛型的类型变量,可以获得泛型上线/下线.
                              在使用extends 指定泛型类型时可以 使用& 限定多个上线类型. & 拼接的类型必须是接口,不能是类
                              例如:List<Obj extends Object & Compareable & Serializable> list = new ArrayList();

                WildcardType: 通配符泛型

                GenericArrayType: 数组泛型

                Class: 原始类型

            Type类型的使用和差异异步查看代码中generic包中的实例

            参考: https://cloud.tencent.com/developer/article/1497707


二. 动态代理

    1. 代理模式
        代理模式包含四个角色:
            Interface: 定义行为
            RealClass: Interface 接口行为的正事实现
            ProxyClass: 代理类, 实现Interface,持有Interface 接口真实实现类的应用
            Client: 访问ProxyClass 调用行为

    2. 动态代理的实现
            2.1 动态代理的实现分为三步:
                a. 提供Interface Class
                b. 创建InvokeHandler 对象
                c. 获得动态代理类的Construct(源码中通过getProxyClass0实现)
                d. 使用Construct 创建对象

            2.2 API实现
                InvocationHandler handler = new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return null;
                    }
                };

                IProxy ip = (IProxy) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{IProxy.class}, handler);

                2.2.1 Proxy.newProxyInstance中的interfaces 参数要求
                    a. 数组Class必须是Interface
                    b. 数组中不能有重复的Interface

                    如果不满足以上要求,直接抛出错误异常

    3. 动态代理原理

            在调用Proxy.newProxyInstance获得代理类时, 源码中通过getProxyClass0或的代理类的Construct 构造方法.如果 代理类无法找到, 源码中通过字节码写入一个以$Proxy+数字的字节码文件.
        字节码文件中包含一下信息:
            a. 一个以InvokeHandler为参数的构造方法
            b. 接口中定义方法(包含toString, equals, hasCode)的Method变量
            c. 实现接口所有接口中定义的方法, 构造方法中传递的InvokeHandler的invoke方法


        3.2 保存生成的动态代理class文件
            1.8及之前
                通过代码设置：
                    System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

                设置JVM参数：
                    -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true

            1.8之后
                通过代码设置：
                    System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");

                设置JVM参数：
                    -Djdk.proxy.ProxyGenerator.saveGeneratedFiles=true

            如何知道JVM 参数名称：
                找到ProxyGenerator.class 中的saveGeneratedFiles 常量,常量中通过GetBooleanAction对象的key就是JVM 设置保存动态代理生成的class的属性名
                    例如：private static final boolean saveGeneratedFiles
                            = (Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"));

            在Intellij IDEA 或者AS中设置JVM参数
                a. 选择Run->Edit Configurations -> 在右侧菜单栏的Application 菜单中选择项目-> 在左侧菜单中选择configuration菜单中找到的VM Options.
                b. 将JVM参数填入其中，如果有多个参数使用分号(;)隔开

三. ClassLoader

    1. 双亲委派
            ClassLoader 通过parent保持着整个ClassLoader链. ClassLoader 调用loadClass方法加载类时,先判断加载的类是否已经加载了.如果没有加载,这是调用父类loadClass
        加载类. 如果父类没有加载或者加载失败,才会调用自己加载类.

            1.1 Java中的双亲委派模型:

                        BootstrapClassLoader
                                 |
                            ExtClassLoader
                                 |
                            AppClassLoader
                             /          \
                            /            \
                   自定义ClassLoader     自定义Class是Loader

                BootstrapClassLoader: Java_Home/lib下面的类库加载到内存中（比如rt.jar）.

                ExtClassLoader: 负责将Java_Home /lib/ext或者由系统变量 java.ext.dir指定位置中的类库加载到内存中。开发者可以直接使用标准扩展类加载器

                AppClassLoader: 它负责将系统类路径（CLASSPATH）中指定的类库加载到内存中. 可以通过ClassLoader.getSystemClassLoader()直接获得,因此也被称作系统加载器.

                1.1.1 为什么设计成双亲委派
                    a. 安全考虑
                        JVM运行之前需要很多核心库需要加载才能运行, BootstrapClassLoader 加载完成后将不再重新加载,避免在运行时被字节码注入的风险.
                    b. 重复利用
                        一个类如果已经被加载过之后,将不会再此解析字节码文件加载到内存中.从而提升效率
                    c. 唯一性
                        类的权限定路径相同,并且ClassLoader为同一个对象,这个类才是同一个

            1.2 双亲委派加载源码
                protected Class<?> loadClass(String name, boolean resolve)
                        throws ClassNotFoundException
                    {
                            // First, check if the class has already been loaded
                            Class c = findLoadedClass(name);
                            if (c == null) {
                                long t0 = System.nanoTime();
                                try {
                                    if (parent != null) {
                                        c = parent.loadClass(name, false);
                                    } else {
                                        c = findBootstrapClassOrNull(name);
                                    }
                                } catch (ClassNotFoundException e) {
                                    // ClassNotFoundException thrown if class not found
                                    // from the non-null parent class loader
                                }

                                if (c == null) {
                                    // If still not found, then invoke findClass in order
                                    // to find the class.
                                    long t1 = System.nanoTime();
                                    c = findClass(name);

                                    // this is the defining class loader; record the stats
                                }
                            }
                            return c;
                    }

    2. 字节码加载到内存中的步骤

        加载----->      链接
                  --------------
                  |
                  |     验证
                  |
                  |     准备
                  |
                  |     解析
                  |
                  |-------------- --------> ----------------
                                            |
                                            |      初始化
                                            |
                                            |       使用
                                            |
                                            |       卸载
                                            |
                                            |----------------

        加载: 根据类的全限定名从.class 文件中加载到内存中
        验证: 根据Class字节码规范验证类的正确性
        准备: 计算对象占用的内存大小,并对静态变量分配内存,并对分配的内存进行初始化
        解析: 解析字节码, 将解析的对象(常量, 方法, 构造方法, 变量) 指向内存中的引用(字符串指向常量池中的引用等)
        初始化: 初始化阶段，虚拟机主要对类变量进行初始化。虚拟机调用< clinit>方法，进行静态元素赋值.
                a.clinit 方法的执行初始化执行先父再子的顺序
                b.静态元素包含静态变量与静态代码块.静态方法直接赋值, 静态代码块将被调用
        使用: 通过new , 反射创建类对象 , 调用静态属性, 子类触发父类方法. 这个过程将初始化全局变量,调用构造方法
        卸载: 从内存中卸载

        1.1 怎么知道一个类被加载了
            i：主动使用
             A:创建一类的实例 new Person();
             B*:访问某个类、接口中的静态变量，或者对于静态变量做读写；
             C：调用类中的静态方法；
             D：反射 （Class.forName("xxx.xxx.xxx")）;
             E*：初始化一个类的子类的，当前类加载。
             F：Java虚拟机标明的启动器类  (Test.class(main)|Person.class|Student.class，此时Test就是启动器类).
            ii：被动使用
             a：引用常量不会导致该类发生初始化[常量它是在编译器确定的]
             b: 通过数组定义类的引用，不会触发该类的初始化
             c：通过子类引用父类的静态变量，不会导致子类初始化。

    3. Android的ClassLoader
        Android中提供PathClassLoader 与 DexClassLoader 来加载dex 文件.
            DexClassLoader: 可以加载为安装的apk/jar/dex
            PathClassLoader: 只能加载已安装的Apk

        DexClassLoader 与 PathClassLoader 都继承BaseDexClassLoader. BaseDexClassLoader 对Dex文件的维护参考第五点热修复初探

四. Enum

    1. Enum的基本使用


    2. Enum 原理


五. 热修复初探

    1. dex 字节修复

        1.1 混淆后的dex 字节码修复

    2. 资源文件修复


    https://blog.csdn.net/sbsujjbcy/article/details/52541803