package vip.yeee.memo.integrate.nio.jdk.chartapp;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Description:
 * @Author: yeeeeee
 * @Date: 2021/12/7 11:44
 */
public class ChatFrame {

    private JTextArea readContext = new JTextArea(18, 30);// 显示消息文本框
    private JTextArea writeContext = new JTextArea(6, 30);// 发送消息文本框

    private DefaultListModel modle = new DefaultListModel();// 用户列表模型
    private JList list = new JList(modle);// 用户列表

    private JButton btnSend = new JButton("发送");// 发送消息按钮
    private JButton btnClose = new JButton("关闭");// 关闭聊天窗口按钮

    private JFrame frame = new JFrame("ChatFrame");// 窗体界面

    private String uname;// 用户姓名

    private ChatClient service;// 用于与服务器交互

    private boolean isRun = false;// 是否运行

    public ChatFrame(ChatClient service, String uname) {
        this.isRun = true;
        this.uname = uname;
        this.service = service;
    }

    private void init() {
        frame.setLayout(null);
        frame.setTitle(uname + "聊天窗口");

        frame.setSize(500, 500);
        frame.setLocation(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JScrollPane readScroll = new JScrollPane(readContext);
        readScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(readScroll);
        JScrollPane writeScroll = new JScrollPane(writeContext);
        writeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(writeScroll);
        frame.add(list);
        frame.add(btnSend);
        frame.add(btnClose);
        readScroll.setBounds(10, 10, 320, 300);
        readContext.setBounds(0, 0, 320, 300);
        readContext.setEditable(false);
        readContext.setLineWrap(true);// 自动换行
        writeScroll.setBounds(10, 315, 320, 100);
        writeContext.setBounds(0, 0, 320, 100);
        writeContext.setLineWrap(true);// 自动换行
        list.setBounds(340, 10, 140, 445);
        btnSend.setBounds(150, 420, 80, 30);
        btnClose.setBounds(250, 420, 80, 30);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                isRun = false;
                service.sendMsg("exit_" + uname);
                service.close();
            }
        });

        btnSend.addActionListener(e -> {
            String msg = writeContext.getText().trim();
            if(msg.length() > 0){
                service.sendMsg(uname + "^" + writeContext.getText());
            }
            writeContext.setText(null);
            writeContext.requestFocus();
        });

        btnClose.addActionListener(e -> {
            isRun = false;
            service.sendMsg("exit_" + uname);
            System.exit(0);
        });

        list.addListSelectionListener(e -> {
            // JOptionPane.showMessageDialog(null,
            // list.getSelectedValue().toString());
        });

        writeContext.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String msg = writeContext.getText().trim();
                    if(msg.length() > 0){
                        service.sendMsg(uname + "^" + writeContext.getText());
                    }
                    writeContext.setText(null);
                    writeContext.requestFocus();
                }
            }
        });

    }

    // 此线程类用于轮询读取服务器发送的消息
    private class MsgThread extends Thread {
        @Override
        public void run() {
            while (isRun) {
                String msg = service.receiveMsg();
                if (msg != null) {
                    //如果存在[]，这是verctor装的usernames的toString生成的
                    if (msg.contains("[") && msg.lastIndexOf("]") != -1) {
                        msg = msg.substring(1, msg.length() - 1);
                        String[] userNames = msg.split(",");
                        modle.removeAllElements();
                        for (int i = 0; i < userNames.length; i++) {
                            modle.addElement(userNames[i].trim());
                        }
                    } else {//如果是普通的消息
                        String str = readContext.getText() + msg;
                        readContext.setText(str);
                        readContext.selectAll();
                    }
                }
            }
        }
    }

    // 显示界面
    public void show() {
        this.init();
        service.sendMsg("open_" + uname);
        MsgThread msgThread = new MsgThread();
        msgThread.start();
        this.frame.setVisible(true);
    }

}
