package com.impl.chartapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/7 10:49
 */
public class SetNameFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static JTextField txtName;
    private static JButton btnOk;
    private static JLabel label;

    public SetNameFrame() {
        this.setLayout(null);
        Toolkit kit = Toolkit.getDefaultToolkit();
        int w = kit.getScreenSize().width;
        int h = kit.getScreenSize().height;
        this.setBounds(w / 2 - 230 / 2, h / 2 - 200 / 2, 230, 200);
        this.setTitle("设置名称");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        txtName = new JTextField(4);
        this.add(txtName);
        txtName.setBounds(10, 10, 100, 25);
        btnOk = new JButton("OK");
        this.add(btnOk);
        btnOk.setBounds(120, 10, 80, 25);
        label = new JLabel("[w:" + w + ",h:" + h + "]");
        this.add(label);
        label.setBounds(10, 40, 200, 100);
        label.setText("<html>在上面的文本框中输入名字<br/>显示器宽度：" + w + "<br/>显示器高度：" + h
                + "</html>");

        btnOk.addActionListener(e -> {
            String uname = txtName.getText();
            ChatClient service = ChatClient.getInstance();
            ChatFrame chatFrame = new ChatFrame(service, uname);
            chatFrame.show();
            setVisible(false);
        });

    }

    public static void main(String[] args) {
        SetNameFrame setNameFrame = new SetNameFrame();
        setNameFrame.setVisible(true);
    }


}
