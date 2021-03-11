package com.study.reflect;

import jdk.internal.util.xml.impl.Pair;

import java.lang.reflect.*;
import java.sql.Struct;

public class ReflectMain {

    public static void main(String[] args) {
        try {
            Class<?> aClass = Class.forName("java.lang.String");
            System.out.println(aClass.getName());

            System.out.println(System.getProperty("sun.boot.class.path"));
            System.out.println(System.getProperty("java.class.path"));

            //使用Class.forName 获得数组类型
            // forName中的类全限定路径必须是JVM能够识别的签名形式[L${ComponentType}; 基本数据类型无法通这种方式获得
            Class<?> sClass = Class.forName("[Ljava.lang.String;");
            System.out.println(sClass.getCanonicalName());
            System.out.println(sClass.isArray() + "-----"
                    + sClass.isAssignableFrom(String[].class));


            //无法获得基本数据类型的Class类型
//            Class.forName("int");
//            Class.forName("I");

            Object array = Array.newInstance(Class.forName("java.lang.String"), 10);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return null;
            }
        };

        IProxy ip = (IProxy) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[]{IProxy.class}, handler);

        System.out.println("----------GetDeclaredMethods------------");
        Class<Student> studentClass = Student.class;
        Method[] declaredMethods = studentClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println("Method Name :" + method.getName());
        }

        System.out.println("----------GetMethods------------");
        Method[] methods = studentClass.getMethods();
        for (Method method : methods) {
            System.out.println("Method Name :" + method.getName());
        }

        System.out.println("----------------------------");
        Type[] genericInterfaces = studentClass.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            System.out.println("type : " + type.getTypeName());
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type argument = parameterizedType.getActualTypeArguments()[0];
                System.out.println("argument:" + argument);
            }
        }

        Type genericSuperclass = studentClass.getGenericSuperclass();
        System.out.println("Superclass type : " + genericSuperclass.getTypeName());


        System.out.println("---------------------");
        new Student().executeCallback(new Callback<String>() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });

    }

//    public <T extends String> void print() {
//        Pair<String>[] table = new Pair<>[10];
//        T[] t = new T[10];
//    }


}
