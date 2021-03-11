package com.study.reflect.generic;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * public interface WildcardType extends Type {
 *     // 获得泛型中的上边界.对应extends
 *     Type[] getUpperBounds();
 *
 *     // 获得泛型中的下边界 对应super
 *     Type[] getLowerBounds();
 * }
 *
 *  WildcardType 获取通配符修饰的泛型类型, 可以获得泛型类型的上边界(extends)与下边界(super)
 *
 *  通配符在没有指定extends 上面界,上边界默认为Object
 */
public class WildcardGenericType<T> {

    List<? extends Number> list;
    List<? super String> sList;
    Map<? extends String, ?> map;
    Map<? extends Number, ? super String> map2;

    Class<?> clazz;
    List simpleList;
    List<String> ssList;
    List<T> tList;  //返回的类型为TypeVariable

    public static void printBounds(WildcardType type) {
        StringBuilder builder = new StringBuilder();
        builder.append("\tTypeName:").append(type.getTypeName())
                .append("\n\t\t").append("上边界:");

        Type[] upperBounds = type.getUpperBounds();
        if (upperBounds != null) {
            for (Type t : upperBounds) {
                builder.append("\n\t\t\t").append(t);
            }
        } else {
            builder.append("null");
        }

        builder.append("\n\t\t").append("下边界:");
        Type[] lowerBounds = type.getLowerBounds();
        if (lowerBounds != null) {
            for (Type t : lowerBounds) {
                builder.append("\n\t\t\t").append(t);
            }
        } else {
            builder.append("null");
        }
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        for (Field field : WildcardGenericType.class.getDeclaredFields()) {
            System.out.println(field.getName() + ": ");
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                System.out.println("\tParameterizedType:" + type.getTypeName());
                for (Type t : actualTypeArguments) {
                    if (t instanceof WildcardType) {
                        System.out.println("\tWildcardType:" + type.getTypeName());
                        printBounds((WildcardType) t);
                    } else if (t instanceof TypeVariable) {
                        System.out.println("\t\t TypeVariable:" + t.getTypeName());
                    } else {
                        System.out.println("\t\t actualTypeArguments:" + t.getTypeName());
                    }
                }
            } else if (type instanceof WildcardType) {
                System.out.println("\tWildcardType:" + type.getTypeName());
                printBounds((WildcardType) type);
            } else {
                System.out.println("\tClass:" + type);
            }
        }
    }
}
