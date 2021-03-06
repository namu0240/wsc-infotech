package com.namu.market;

import com.namu.market.entity.User;

import java.sql.*;

public class MarketDatabase {

    private static Connection connection;

    public static void connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/market?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
    }

    public static boolean checkUserIdDuplicate(String userId) {
        try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM market.user where u_id = ?;");
			preparedStatement.setString(1, userId);
			
			return preparedStatement.executeQuery().next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return false;
    }

    public static void register(User user) {
    	try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO market.user (u_id,u_pw,u_name,u_phone,u_age,u_10percent,u_30percent) VALUES (?,?,?,?,?,?,?);");
			preparedStatement.setString(1,user.userId);
			preparedStatement.setString(2,user.password);
			preparedStatement.setString(3,user.name);
			preparedStatement.setString(4,user.phone);
			preparedStatement.setDate(5,user.age);
			preparedStatement.setInt(6,user.coupon10);
			preparedStatement.setInt(7,user.coupon30);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public static User login(String userId, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM market.user where lower(u_id) = ? and lower(u_pw) = ?;");
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
            	return null;
            }
            
            return new User(
                    resultSet.getInt(0),
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5),
                    resultSet.getInt(6),
                    resultSet.getInt(7)
            );

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
