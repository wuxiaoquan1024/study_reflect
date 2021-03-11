package com.study.reflect.generic;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

/**
 * public interface GenericArrayType extends Type {
 *     // 获得数组元素的原始类型
 *     Type getGenericComponentType();
 * }
 *
 * GenericArrayType: 表示数组泛型化.
 *  List<String> 这种返回的泛型类型是ParameterizedType并不是数组,
 *  int[]: 这种返回的是原始数据类型Class, 也不是数组
 */
public class GenricArrayTypeTest<T> {

    public void testFun(ArrayList<String> list, T[] tarray,
                        List<String>[] lists,
                        List[] simpleList,
                        List<? extends Number> lnumber, String[] strs, Object[] objects, int[] ints, long[] longs) {

    }

    public static void main(String[] args) {
        Class<GenricArrayTypeTest> aClass = GenricArrayTypeTest.class;
        System.out.println("\t\tCanonicalName:" + aClass.getCanonicalName() + "\t SimpleName:" + aClass.getSimpleName() + "\tTypeName:" + aClass.getTypeName());
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if ((method.getModifiers() & Modifier.STATIC) != 0
                && method.getName().equals("main")) {
                continue;
            }

            System.out.println("Method " + method.getName() + ":");
            Type[] genericParameterTypes = method.getGenericParameterTypes(); //获得方法泛型化的参数列表
            for (Type type : genericParameterTypes) {
                if (type instanceof GenericArrayType) {
                    GenericArrayType gat = (GenericArrayType) type;
                    System.out.println("\tGenericArrayType: " + type + " GenericComponentType:" + gat.getGenericComponentType());
                } else if (type instanceof ParameterizedType) {
                    System.out.println("\tParameterizedType" + type);
                } else {
                    System.out.println("\tClass:" + type);
//                    try {
//                        Class<?> aClass = Class.forName(type.getTypeName());
//                        System.out.println("\t\tCanonicalName:" + aClass.getCanonicalName() + "\t SimpleName:" + aClass.getSimpleName() + "\tTypeName:" + aClass.getTypeName());
//
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        }
    }
}
