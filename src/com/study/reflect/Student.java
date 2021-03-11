package com.study.reflect;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Student extends People implements Serializable, IInterface<String> {

    private static boolean flag;

    static {
        System.out.println("静态块");
        flag = true;
    }

    {
        System.out.println("代码块");
    }

    public static void println() {
        System.out.println("Flag = " + flag);
    }

    public void print(String log) {
        System.out.println(log);
    }

    public Student() {
        System.out.println("构造方法");
    }


    private void attach() {

    }

    protected void dettach() {

    }

    void printInt() {

    }

    public void executeCallback(Callback<String> callback) {
        Class<? extends Callback> aClass = callback.getClass();
        Type genericSuperclass = aClass.getGenericSuperclass();
        Type[] genericInterfaces = aClass.getGenericInterfaces();
        for (Type type : genericInterfaces) {
            System.out.println("<<< Student type : " + type.getTypeName());
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type argument = parameterizedType.getActualTypeArguments()[0];
                System.out.println("<<< Student argument:" + argument + "\t OwnerType: " + parameterizedType.getOwnerType() + "\t RawType:" + parameterizedType.getRawType());
            }
        }


        System.out.println("<<< Student genericSuperclass: " + genericSuperclass);
    }

}
