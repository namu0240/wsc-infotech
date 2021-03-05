package com.namu.market;

import java.sql.Connection;
import java.sql.DriverManager;

public class MarketDatabase {
	
	private static Connection connection;
	
	public static void connect() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/computer?serverTimezone=UTC&allowLoadLocalInfile=true","root","1234");
	}
	
	public static void initialize() throws Exception {
		
		// change option
		connection.prepareStatement("SET GLOBAL local_infile = true;").executeUpdate();
		
		// drop database
		connection.prepareStatement("DROP SCHEMA IF EXISTS `market`;").executeUpdate();
		connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS `market` DEFAULT CHARACTER SET utf8 ;").executeUpdate();
		
		// user
		connection.prepareStatement("drop user 'user'@'localhost';").executeUpdate();
		connection.prepareStatement("flush privileges;").executeUpdate();
		connection.prepareStatement("CREATE USER 'user'@'localhost';").executeUpdate();
		connection.prepareStatement("GRANT select, delete, update, insert ON market.* TO 'user'@'localhost';").executeUpdate();
		connection.prepareStatement("flush privileges;").executeUpdate();
		
		// select database
		connection.prepareStatement("USE `market`;").executeUpdate();
		
		// create table
		connection.prepareStatement("CREATE TABLE `market`.`user` (\n"
				+ "  `u_no` INT NOT NULL AUTO_INCREMENT COMMENT '	',\n"
				+ "  `u_id` VARCHAR(20) NULL,\n"
				+ "  `u_pw` VARCHAR(20) NULL,\n"
				+ "  `u_name` VARCHAR(15) NULL,\n"
				+ "  `u_phone` VARCHAR(20) NULL,\n"
				+ "  `u_age` DATE NULL,\n"
				+ "  `u_10percent` INT NULL,\n"
				+ "  `u_30percent` INT NULL,\n"
				+ "  PRIMARY KEY (`u_no`));\n"
				+ "").executeUpdate();
		
		connection.prepareStatement("CREATE TABLE `market`.`category` (\n"
				+ "  `c_no` INT NOT NULL AUTO_INCREMENT,\n"
				+ "  `c_name` VARCHAR(45) NULL,\n"
				+ "  PRIMARY KEY (`c_no`));\n"
				+ "").executeUpdate();
		
		connection.prepareStatement("CREATE TABLE `market`.`product` (\n"
				+ "  `p_no` INT NOT NULL AUTO_INCREMENT,\n"
				+ "  `c_no` INT NULL,\n"
				+ "  `p_name` VARCHAR(20) NULL,\n"
				+ "  `p_price` INT NULL,\n"
				+ "  `p_stock` INT NULL,\n"
				+ "  `p_explaination` VARCHAR(150) NULL,\n"
				+ "  PRIMARY KEY (`p_no`),\n"
				+ "  INDEX `c_no_idx` (`c_no` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `product_c_no`\n"
				+ "    FOREIGN KEY (`c_no`)\n"
				+ "    REFERENCES `market`.`category` (`c_no`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION);\n"
				+ "").executeUpdate();
		
		connection.prepareStatement("CREATE TABLE `market`.`purchase` (\n"
				+ "  `pu_no` INT NOT NULL AUTO_INCREMENT,\n"
				+ "  `p_no` INT NULL,\n"
				+ "  `pu_price` INT NULL,\n"
				+ "  `pu_count` INT NULL,\n"
				+ "  `coupon` INT NULL,\n"
				+ "  `u_no` INT NULL,\n"
				+ "  `pu_date` DATE NULL,\n"
				+ "  PRIMARY KEY (`pu_no`),\n"
				+ "  INDEX `p_no_idx` (`p_no` ASC) VISIBLE,\n"
				+ "  INDEX `u_no_idx` (`u_no` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `purchase_p_no`\n"
				+ "    FOREIGN KEY (`p_no`)\n"
				+ "    REFERENCES `market`.`product` (`p_no`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION,\n"
				+ "  CONSTRAINT `purchase_u_no`\n"
				+ "    FOREIGN KEY (`u_no`)\n"
				+ "    REFERENCES `market`.`user` (`u_no`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION);\n"
				+ "").executeUpdate();
		
		connection.prepareStatement("CREATE TABLE `market`.`attendance` (\n"
				+ "  `a_no` INT NOT NULL AUTO_INCREMENT,\n"
				+ "  `u_no` INT NULL,\n"
				+ "  `a_date` DATE NULL,\n"
				+ "  PRIMARY KEY (`a_no`),\n"
				+ "  INDEX `u_no_idx` (`u_no` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `attendance_u_no`\n"
				+ "    FOREIGN KEY (`u_no`)\n"
				+ "    REFERENCES `market`.`user` (`u_no`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION);\n"
				+ "").executeUpdate();
		
		connection.prepareStatement("CREATE TABLE `market`.`coupon` (\n"
				+ "  `c_no` INT NOT NULL AUTO_INCREMENT,\n"
				+ "  `u_no` INT NULL,\n"
				+ "  `c_date` VARCHAR(15) NULL,\n"
				+ "  `c_10percent` INT NULL,\n"
				+ "  `c_30percent` INT NULL,\n"
				+ "  PRIMARY KEY (`c_no`),\n"
				+ "  INDEX `coupon_u_no_idx` (`u_no` ASC) VISIBLE,\n"
				+ "  CONSTRAINT `coupon_u_no`\n"
				+ "    FOREIGN KEY (`u_no`)\n"
				+ "    REFERENCES `market`.`user` (`u_no`)\n"
				+ "    ON DELETE NO ACTION\n"
				+ "    ON UPDATE NO ACTION);\n"
				+ "").executeUpdate();
		
		// 로드되는 순서가 섞이지 않도록 주의! FK 가 설정되어 있는 테이블은 후 순위로 미뤄서 작업을 해야함
		String[] tableNames = new String[] { "user","category","product","coupon","purchase","attendance" };
		for( String tableName : tableNames) {
			connection.prepareStatement("LOAD DATA LOCAL INFILE './지급자료/" + tableName + ".txt' INTO TABLE " + tableName + " COLUMNS TERMINATED BY '\t';").executeUpdate();
			System.out.println("LOAD DATA LOCAL INFILE './지급자료/" + tableName + ".txt' INTO TABLE " + tableName + " COLUMNS TERMINATED BY '\\t';");
		}
		
	}
	
}
