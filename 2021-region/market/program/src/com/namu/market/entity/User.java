package com.namu.market.entity;

import java.sql.Date;

public class User {
	
	public int no;
	public String userId;
	public String password;
	public String name;
	public Date age;
	public int coupon10;
	public int coupon30;
	
	public User(int no, String userId, String password, String name, Date age, int coupon10, int coupon30) {
		super();
		this.no = no;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.age = age;
		this.coupon10 = coupon10;
		this.coupon30 = coupon30;
	}
	
}
