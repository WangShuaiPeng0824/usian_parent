package com.usian.proxy.staticProxy;

import com.sun.tools.javadoc.Start;

/**
 * 静态代理类
 *  缺陷：
 *      1、只能代理star类型的对象
 *      2、代码重复
 */
public class ProxyStar implements Star{

    private Star star;

    public ProxyStar(Star star){
        this.star=star;
    }

    @Override
    public void confer() {
        System.out.println("ProxyStart.confer");
    }

    @Override
    public void signContract() {
        System.out.println("ProxyStart.signContract");
    }

    @Override
    public void bookTicket() {
        System.out.println("ProxyStart.bookTicket");
    }

    @Override
    public void sing() {
        System.out.println("方法执行前");
        star.sing();
        System.out.println("方法执行后");
    }

    @Override
    public void collectMoney() {
        System.out.println("ProxyStart.collectMoney");
    }
}
