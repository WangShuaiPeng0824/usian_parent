package com.usian.proxy.staticProxy;

public class Client {

    public static void main(String[] args) {
        Star star = new RealStar();
        Star proxy = new ProxyStar(star);
        //对接代理人
        proxy.confer();
        proxy.bookTicket();
        proxy.confer();
        proxy.sing();
        proxy.collectMoney();

    }

}
