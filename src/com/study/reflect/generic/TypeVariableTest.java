package com.study.reflect.generic;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;

/**
 *  & 的使用:
 *      使用& 使泛型可以加入更多的类型限定.
 *      在extends限定列表中有类的限定必须放在extends 后面,不能使用& 拼接. & 只能拼接接口
 *  泛型super限定不能使用& 拼接多个限定.
 *
 *
 *  public interface TypeVariable<D extends GenericDeclaration> extends Type, AnnotatedElement {
 *
 *     //获得泛型限定类型数组
 *     Type[] getBounds();
 *
 *     // 获得泛型类型定义在那个类中
 *     D getGenericDeclaration();
 *
 *     // 获得名称
 *     String getName();
 *
 *     //获得注解限定数组
 *     AnnotatedType[] getAnnotatedBounds();
 * }
 */
public class TypeVariableTest<T extends Number & Comparable & Serializable, V> {

    private T t;

    private V v;

    @Anno1
    @Anno2
    private V[] varray;

    private List<T> list;

    private String str;

    public <S extends String & Serializable> void println() {
    }

    public static void println(TypeVariable typeVariable) {
        StringBuilder builder = new StringBuilder();
        builder.append("\tName:").append(typeVariable.getName())
                .append("\t").append("GenericDeclaration:").append(typeVariable.getGenericDeclaration());
        builder.append("\n\t\t 边界:\n");
        for (Type bound : typeVariable.getBounds()) {
            builder.append("\t\t\t " + bound).append("\n");
        }

        builder.append("\n\t\t 注解边界:\n");
        for (AnnotatedType bound : typeVariable.getAnnotatedBounds()) {
            for (Annotation annotation : bound.getAnnotations()) {
                builder.append("\t\t\t " + annotation).append("\n");
            }
        }
        System.out.println(builder.toString());
    }

    public static void main(String[] args) {
        for (Field field : TypeVariableTest.class.getDeclaredFields()) {
            System.out.println(field.getName() + ": ");
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                System.out.println("\tParameterizedType:" + type.getTypeName());
                for (Type t : actualTypeArguments) {
                    if (t instanceof WildcardType) {
                        System.out.println("\tWildcardType:" + type.getTypeName());
                    } else if (t instanceof TypeVariable) {
                        System.out.println("\t\t TypeVariable:" + t.getTypeName());
                        println((TypeVariable) t);
                    } else {
                        System.out.println("\t\t actualTypeArguments:" + t.getTypeName());
                    }
                }
            } else if (type instanceof GenericArrayType) {
                System.out.println("\tGenericArrayType:" + type.getTypeName());
                Type genericComponentType = ((GenericArrayType) type).getGenericComponentType();
                if (genericComponentType instanceof TypeVariable) {
                    println((TypeVariable) genericComponentType);
                }
            } else if (type instanceof TypeVariable) {
                System.out.println("\tTypeVariable:" + type.getTypeName());
                println((TypeVariable) type);
            } else {
                System.out.println("\tClass:" + type);
            }
        }
    }
}
