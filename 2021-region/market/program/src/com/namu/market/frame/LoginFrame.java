package com.namu.market.frame;

import com.namu.market.Common;
import com.namu.market.MarketDatabase;
import com.namu.market.entity.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    JPanel inputPanel, registerPanel;

    JLabel titleLabel = new JLabel("기능마켓", JLabel.CENTER);
    JLabel idLabel = new JLabel("아이디", JLabel.RIGHT);
    JLabel pwLabel = new JLabel("비밀번호", JLabel.RIGHT);
    JTextField idField = new JTextField(10);
    JTextField pwField = new JTextField(10);
    JLabel registerLabel = new JLabel("회원가입");
    JButton loginButton = new JButton("로그인");

    public LoginFrame() throws HeadlessException {

        setTitle("로그인");
        setSize(new Dimension(400, 200));
        setLayout(new BorderLayout());

        titleLabel.setFont(new Font("굴림", Font.BOLD, 30));

        inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(pwLabel);
        inputPanel.add(pwField);

        registerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerPanel.add(registerLabel);

        add(titleLabel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.EAST);
        add(registerPanel, BorderLayout.SOUTH);

        loginButton.addActionListener((actionEvent -> login()));

        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                register();
            }
        });

    }

    public void register() {
        this.setVisible(false);
        new RegisterFrame().setVisible(true);
    }

    public void login() {
        String userId = idField.getText().toLowerCase();
        String password = pwField.getText().toLowerCase();

        if (userId.isEmpty() || password.isEmpty()) {
            Common.showError("빈칸이 존재합니다.");
            return;
        }

        if (userId.equals("admin") && password.equals("1234")) {
            Common.showInfo("관리자로 로그인 되었습니다.");
            // TODO: 2021/03/05 ‘상품관리’ 폼으로 이동
            return;
        }

        User user = MarketDatabase.login(userId, password);

        if (user == null) {
            Common.showError("회원정보가 일치하지 않습니다.");
            return;
        }

        Common.user = user;
        Common.showInfo(user.name);
        // TODO: 2021/03/05 ‘상품목록’ 폼으로 이동
    }

}
