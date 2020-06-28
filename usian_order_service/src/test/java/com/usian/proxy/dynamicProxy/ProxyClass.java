package com.usian.proxy.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类
 *      InvocationHandler：通过invoke方法调用真实角色
 * 好处：
 *      1、代理任意类型的对象
 *      2、代理类中没有重复方法
 */
public class ProxyClass implements InvocationHandler {

    //真实角色
    private Object realStar;
    public ProxyClass(Object object){
        this.realStar=object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("真正的方法执行前！");
        System.out.println("面谈，签合同，预付款，订机票");

        Object result = method.invoke(realStar, args);//反射调用真实角色的方法

        System.out.println("真正的方法执行后！");
        System.out.println("收尾款");

        return result;
    }
}
