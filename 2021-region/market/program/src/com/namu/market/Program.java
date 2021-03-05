package com.namu.market;

import com.namu.market.frame.LoginFrame;

public class Program {

	public static void main(String[] args) throws Exception {

		MarketDatabase.connect();

		new LoginFrame().setVisible(true);

	}
	
}
