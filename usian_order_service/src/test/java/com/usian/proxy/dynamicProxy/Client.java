package com.usian.proxy.dynamicProxy;

import com.usian.proxy.staticProxy.ProxyStar;

import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        //Star realStar = new RealStar();
        RealCoder realCoder = new RealCoder();
        ProxyClass proxyStar = new ProxyClass(realCoder);
        //生成代理类的对象
        Coder proxy = (Coder)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Star.class,Coder.class}, proxyStar);
        proxy.code();

    }
}
