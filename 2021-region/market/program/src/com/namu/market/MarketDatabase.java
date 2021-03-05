package com.namu.market;

import com.namu.market.entity.User;

import java.sql.*;

public class MarketDatabase {

    private static Connection connection;

    public static void connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/computer?serverTimezone=UTC&allowLoadLocalInfile=true", "root", "1234");
    }

    public static boolean checkUserIdDuplicate(String userId) {
        return false;
    }

    public static void register(User user) {

    }

    public static User login(String userId, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM market.user where lower(u_id) = ? and lower(u_pw) = ?;");
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            return new User(
                    resultSet.getInt(0),
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getInt(5),
                    resultSet.getInt(6)
            );

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
