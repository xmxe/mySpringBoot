package com.mySpringBoot.test.proxy.staticproxy.impl;

import com.mySpringBoot.test.proxy.staticproxy.BuyHouse;

public class BuyHouseProxy implements BuyHouse {
	
	private BuyHouse bh = new BuyHouseImpl();
	
	@Override
	public void buyHouse() {
		// TODO Auto-generated method stub
		System.out.println("BuyHouseProxy1:");
		bh.buyHouse();
		System.out.println("BuyHouseProxy2:");
	}

}
