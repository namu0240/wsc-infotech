package com.namu.market.frame;

import com.namu.market.Common;
import com.namu.market.MarketDatabase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterFrame extends JFrame {

    JPanel namePanel;
    JPanel userIdPanel;
    JPanel passwordPanel;
    JPanel passwordCheckPanel;
    JPanel phonePanel;
    JPanel birthDayPanel;
    JPanel inputPanel;
    JPanel buttonPanel;

    JLabel nameLabel = new JLabel("이름");
    JLabel userIdLabel = new JLabel("아이디");
    JLabel passwordLabel = new JLabel("비밀번호");
    JLabel passwordCheckLabel = new JLabel("비밀번호 체크");
    JLabel phoneLabel = new JLabel("전화번호");
    JLabel birthDayLabel = new JLabel("생년월일:");

    JTextField nameField = new JTextField(15);
    JTextField userIdField = new JTextField(15);
    JTextField passwordField = new JTextField(15);
    JTextField passwordCheckField = new JTextField(15);
    JTextField phoneField = new JTextField(15);
    JTextField birthDayField = new JTextField(15);

    JButton duplicateCheckButton = new JButton("중복체크");
    JButton registerButton = new JButton("회원가입");
    JButton cancelButton = new JButton("취소");

    public RegisterFrame() throws HeadlessException {

        setTitle("회원가입");
        setSize(new Dimension(400, 350));
        setLayout(new BorderLayout());

        namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userIdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordCheckPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthDayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        nameLabel.setPreferredSize(new Dimension(100, 50));
        userIdLabel.setPreferredSize(new Dimension(100, 50));
        passwordLabel.setPreferredSize(new Dimension(100, 50));
        passwordCheckLabel.setPreferredSize(new Dimension(100, 50));
        phoneLabel.setPreferredSize(new Dimension(100, 50));
        birthDayLabel.setPreferredSize(new Dimension(100, 50));

        namePanel.add(nameLabel);
        namePanel.add(nameField);

        userIdPanel.add(userIdLabel);
        userIdPanel.add(userIdField);
        userIdPanel.add(duplicateCheckButton);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);

        passwordCheckPanel.add(passwordCheckLabel);
        passwordCheckPanel.add(passwordCheckField);

        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);

        birthDayPanel.add(birthDayLabel);
        birthDayPanel.add(birthDayField);

        inputPanel.add(namePanel);
        inputPanel.add(userIdPanel);
        inputPanel.add(passwordPanel);
        inputPanel.add(passwordCheckPanel);
        inputPanel.add(phonePanel);
        inputPanel.add(birthDayPanel);

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        add(inputPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        duplicateCheckButton.addActionListener((action) -> isDuplicateUserId());
        registerButton.addActionListener((action) -> register());
        cancelButton.addActionListener((action) -> cancel());
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                /*
                 * 1 하이픈은 자리수에 맞춰서 자동으로 생성(000-0000-0000) 된다.
                 * 2 13자리(-포함) 이상 입력이 불가능하도록 하시오.
                 * 3 문자를 입력한 경우경고 : 문자는 입력이 불가합니다.입력된 내용 삭제 후 커서 유지
                 */
                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    return;
                }

                if (e.getKeyChar() < '0' && e.getKeyChar() > '9') {
                    Common.showError("문자는 입력이 불가합니다.");
                    return;
                }

                if (phoneField.getText().length() >= 13) {
                    phoneField.setText(phoneField.getText().substring(0, 12));
                    return;
                }

                if (phoneField.getText().isEmpty()) {
                    return;
                }

                String phone = phoneField.getText()
                        .replace("-", "")
                        .replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");

                phoneField.setText(phone);
            }
        });

    }

    boolean isDuplicateCheck = false;

    /**
     * 1 중복확인을 하지 않았을 경우  경고 : 아이디 중복확인을 해주세요.
     * 2 비밀번호와 비밀번호 체크가 다를 경우  경고 : 비밀번호가 일치하지 않습니다.
     * 3 비밀번호 형식이 일치하지 않는 경우  경고 : 비밀번호를 확인해주세요.
     * (비밀번호는 4자이상, 숫자, 영문자, 특수기호를 반드시 포함한다.)
     * 4 생년월일은 ‘년4자리-월-일’형태로 입력하되 미래이거나, 없는 날짜일 경우  경고 : 생년월일을 확인해주세요.
     * 5 조건이 만족하면  정보 : 회원가입이 완료되었습니다. [그림 1-1] ‘로그인’폼으로 이동
     */
    public void register() {
        if (!isDuplicateCheck) {
            Common.showError("아이디 중복확인을 해주세요");
            return;
        }

        if (!passwordField.getText().equals(passwordCheckField.getText())) {
            Common.showError("비밀번호가 일치하지 않습니다.");
            return;
        }

        if (!passwordField.getText().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{4,}$")) {
            Common.showError("비밀번호를 확인해주세요.");
            return;
        }

        if (!isValidDate(birthDayField.getText())) {
            Common.showError("생년월일을 확인해주세요.");
            return;
        }

        Common.showInfo("회원가입이 완료되었습니다.");
        this.setVisible(false);
        new LoginFrame().setVisible(true);
    }

    /**
     * 1 아이디가 공백일 경우  경고 : 아이디를 입력하세요.
     * 2 아이디가 중복되지 않는 경우정보 : 사용가능 한 아이디입니다.
     * 3 이미 존재하는 아이디일 경우  경고 : 이미 존재하는 아이디입니다.
     * 입력된 내용 삭제 후 커서 유지
     */
    public void isDuplicateUserId() {
        String userId = userIdField.getText();

        if (userId.isEmpty()) {
            Common.showError("아이디를 입력하세요.");
            return;
        }

        if (MarketDatabase.checkUserIdDuplicate(userId)) {
            Common.showError("이미 존재하는 아이디입니다.");
            return;
        }

        isDuplicateCheck = true;
        Common.showInfo("사용가능 한 아이디입니다.");
    }

    public void cancel() {
        this.setVisible(false);
        new LoginFrame().setVisible(true);
    }

    public boolean isValidDate(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);

            return date.before(new Date(Calendar.getInstance().getTimeInMillis()));
        } catch (ParseException e) {
            return false;
        }
    }

}
