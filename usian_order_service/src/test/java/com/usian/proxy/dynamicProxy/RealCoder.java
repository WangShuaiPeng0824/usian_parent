package com.usian.proxy.dynamicProxy;

import com.usian.proxy.staticProxy.Star;

public class RealCoder implements Coder{
    @Override
    public void signContract() {
        System.out.println("RealCoder.signContract");
    }

    @Override
    public void code() {
        System.out.println("RealCoder.写代码");
    }

    @Override
    public void collectMoney() {
        System.out.println("RealCoder.collectMoney");
    }
}
