package com.study.reflect.generic;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * public interface ParameterizedType extends Type {
 *     // 获得泛型类型数组
 *     Type[] getActualTypeArguments();
 *
 *     // 获得泛型原始类型, 例如: List<String> list; 返回的类型是java.util.List
 *     Type getRawType();
 *
 *     // 获得类型的包装原始类型(类型如果是内部类,会返回外部类),一般返回null
 *     Type getOwnerType();
 * }
 *
 * ParameterizedType 参数化类型, 可以通过getActualTypeArguments方法获得泛型参数的类型
 */
public class ParameterizedTypeTest {

    public Map<String, String> map;
    public Set<String> set;
    public Hodler<String> hodler;
    public Class<?> clazz;
    public List<String> list;
    public ArrayList<Map<String, String>> arrayList;
    public Map.Entry<String, String> entry;

    String str;
    Integer i;
    List sList; // 没有带泛型的, GenericType作为Class类型
    Set sSet;

    public class Hodler<V> {

    }

    public static void main(String[] args) {
        Field[] declaredFields = ParameterizedTypeTest.class.getDeclaredFields();
        for (Field field : declaredFields) {
            Type genericType = field.getGenericType(); // 获得泛型类型
            System.out.println(field.getName() + ":");
            if (genericType instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType)genericType;
                System.out.println("\t ParameterizedType ActualTypeArgument Rawtype:" + type.getRawType());
                System.out.println("\t ParameterizedType ActualTypeArgument Ownertype:" + type.getOwnerType());
                Type[] actualTypeArguments = type.getActualTypeArguments();
                for (Type t : actualTypeArguments) {
                    if (t instanceof TypeVariable) {
                        System.out.println("\t\t TypeVariable type:" + ((TypeVariable)t).getBounds());
                    } else if (t instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType)t;
                        System.out.println("\t\t ActualTypeArgument type:" + pt);
                        System.out.println("\t\t ActualTypeArgument Rawtype:" + pt.getRawType());


                        //继续获得泛型参数中的类型
                        //arrayList<Map<String, String>> arrayList
                        for (Type st : pt.getActualTypeArguments()) {
                            System.out.println("\t\t\t ActualTypeArgument type:" + st);
                        }
                    } else {
                        System.out.println("\t\t ActualTypeArgument type:" + t);

                    }
                }
            } else if (genericType instanceof TypeVariable) {
                System.out.println("\t TypeVariable type:" + ((TypeVariable)genericType).getBounds());
            } else if (genericType instanceof WildcardType) {
                System.out.println("\t TypeVariable type:" + ((WildcardType)genericType).getUpperBounds());
            } else {
                System.out.println("\t type:" + genericType);
            }
            System.out.println("\n");
        }
    }

}
