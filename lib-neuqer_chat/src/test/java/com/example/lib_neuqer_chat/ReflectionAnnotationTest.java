package com.example.lib_neuqer_chat;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Author 落叶飞翔的蜗牛
 * @Date 2018/3/10
 * @Description
 */
public class ReflectionAnnotationTest {
 
    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        //获取Bar实例
        Bar bar = new Bar();
        //获取Bar的val字段
        Field field = bar.getClass().getDeclaredField("value");
        //获取val字段上的Foo注解实例
        Foo foo = field.getAnnotation(Foo.class);
        //获取Foo注解实例的 value 属性值
        String value =  foo.value();
        //打印该值
        System.out.println("修改之前的注解值：" + value);


        System.out.println("------------以下是修改注解的值------------");

        //获取 foo 这个代理实例所持有的 InvocationHandler
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(foo);
        // 获取 AnnotationInvocationHandler 的 memberValues 字段
        Field declaredField = invocationHandler.getClass().getDeclaredField("memberValues");
        // 因为这个字段事 private final 修饰，所以要打开权限
        declaredField.setAccessible(true);
        // 获取 memberValues
        Map memberValues = (Map) declaredField.get(invocationHandler);
        // 修改 value 属性值
        memberValues.put("value", "test.annotation.new.value");
        // 获取 foo 的 value 属性值
        String newValue = foo.value();
        System.out.println("修改之后的注解值：" + newValue);

    }
}