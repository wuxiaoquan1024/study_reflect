package com.study.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassByThreeWay {

    public static void main(String[] args) {
        ClassByThreeWay way = new ClassByThreeWay();
//        way.byClassName();
//        way.byClassForName();
        way.byObjectInstacne();
    }


    /**
     * 类名.class
     * 通过类名.class 的方式获得Class只是将class字节码加载到JVM中的Heap中，并不会对Class进行初始化
     * 如果通过Class去invoke静态方法或者静态变量，将对其静态初始化进行初始化(如果有静态块， 将被调用)
     */
    private void byClassName() {
        /*
            无任何输出
         */
        Class<Student> studentClass = Student.class;


        System.out.println("-----------------------------");
//        try {
//
//            //先执行静态代码块， 再获得Field的Value
//            //没有执行Field的getter/setter 访问并不会执行静态初始化
//            Field flag = studentClass.getDeclaredField("flag");
//            flag.setAccessible(true);
//            Object o = flag.get(null);
//            System.out.println("get static field value " + o);
//
//        }  catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

//        try {
//            // 同静态变量一样, 先执行静态初始化, 再执行invoke 的静态方法
//            Method println = studentClass.getDeclaredMethod("println");
//            println.invoke(null, null);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }

        try {
            //不管调用的是constructor.newInstance()还是Class.newInstance() 都会进行初始化操作(静态初始化和初始化操作)
            Constructor<Student> constructor = studentClass.getConstructor();
            Student student = constructor.newInstance();

//            Student student = studentClass.newInstance();  // 内部实现还是通过Construct.newInstance 创建对象

            System.out.println("-----------------------------");
            Method print = studentClass.getDeclaredMethod("print", String.class);
            print.invoke(student, "get Class object by XX.class");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Class.forName() 根据类全限定名加载Class字节码到Heap中,根据 init boolean变量确定是否需要进行静态初始化.
     */
    private void byClassForName() {
        // 只有一个类全限定名参数默认执行静态初始化
//        try {
//        /*
//            输出:
//             静态块
//         */
//            Class<?> aClass = Class.forName("com.study.reflect.Student");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        try {
            // 不进行静态初始化,效果同类名.class 一样
            /*
                没有任何输出
             */
            Class<?> aClass = Class.forName("com.study.reflect.Student", false, ClassLoader.getSystemClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对象.getClass(). 在调用getClass()需要有对象.因此, 不管是通过new, Construct.newInstance 还是Class.newInstance 获得的对象都将先执行初始化操作
     */
    private void byObjectInstacne() {
        /*
            输出:
            静态块
            代码块
            构造方法
         */
        new Student().getClass();
    }
}
