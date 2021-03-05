package com.namu.market;

import com.namu.market.entity.User;

import javax.swing.*;

public class Common {

    public static User user = null;

    public static void showInfo(String message) {
        new JOptionPane(message, JOptionPane.INFORMATION_MESSAGE).createDialog("정보").setVisible(true);
    }

    public static void showError(String message) {
        new JOptionPane(message, JOptionPane.ERROR_MESSAGE).createDialog("정보").setVisible(true);
    }

}
