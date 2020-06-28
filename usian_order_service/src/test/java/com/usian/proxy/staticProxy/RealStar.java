package com.usian.proxy.staticProxy;

public class RealStar implements Star {

	@Override
	public void confer() {
		System.out.println("Realster.confer");
	}

	@Override
	public void signContract() {
		System.out.println("Realster.signContract");
	}

	@Override
	public void bookTicket() {
		System.out.println("Realster.bookTicket");
	}

	@Override
	public void sing() {
		System.out.println("Realster.sing");
	}

	@Override
	public void collectMoney() {
		System.out.println("Realster.collectMoney");
	}
}
