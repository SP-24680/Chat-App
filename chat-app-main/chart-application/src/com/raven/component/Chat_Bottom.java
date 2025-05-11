package com.raven.component;

import com.formdev.flatlaf.FlatClientProperties;
import com.raven.model.MessageType;
import com.raven.event.PublicEvent;
import com.raven.model.Model_Send_Message;
import com.raven.model.Model_User_Account;
import com.raven.service.Service;
import com.raven.swing.JIMSendTextPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Chat_Bottom extends JPanel {

    private Model_User_Account user;
    private MigLayout mig;
    private Panel_More panelMore;

    public Chat_Bottom() {
        initComponents();
        init();
    }

    public Model_User_Account getUser() {
        return user;
    }

    public void setUser(Model_User_Account user) {
        this.user = user;
        panelMore.setUser(user);
    }

    private void init() {
        mig = new MigLayout("fill", "0[]0[fill,grow]0[]2", "2[fill]2[]0");
        setLayout(mig);
        JScrollPane scroll = new JScrollPane();
        scroll.setBorder(null);
        JIMSendTextPane txt = new JIMSendTextPane();
        txt.setBorder(new EmptyBorder(5, 5, 5, 5));
        txt.setHintText("Write Message Here ...");

        txt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ke) {
                refresh();
                if (ke.getKeyChar() == 10 && ke.isControlDown()) {
                    eventSend(txt);
                }
            }
        });

        scroll.setViewportView(txt);
        JScrollBar sb = new JScrollBar();
        sb.putClientProperty(FlatClientProperties.STYLE,
                "width:2;" + "thumbInsets:0,0,0,0;" + "track:#E5E5E5;");
        sb.setUnitIncrement(10);
        scroll.setVerticalScrollBar(sb);
        add(sb, "h 0:10:");
        add(scroll, "w 100%");

        JPanel panel = new JPanel(new MigLayout("filly", "0[]3[]0", "0[bottom]0"));
        panel.setPreferredSize(new Dimension(30, 28));
        panel.setBackground(Color.WHITE);

        JButton cmdSend = new JButton();
        cmdSend.setBorder(null);
        cmdSend.setContentAreaFilled(false);
        cmdSend.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdSend.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/send.png")));
        cmdSend.addActionListener(e -> eventSend(txt));

        JButton cmdMore = new JButton();
        cmdMore.setBorder(null);
        cmdMore.setContentAreaFilled(false);
        cmdMore.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
        cmdMore.addActionListener(e -> toggleMorePanel(cmdMore));

        panel.add(cmdMore);
        panel.add(cmdSend);
        add(panel, "wrap");

        panelMore = new Panel_More();
        panelMore.setVisible(false);

        // File sending buttons
        JButton fileBtn = new JButton("Send File");
        fileBtn.addActionListener(e -> chooseFile());

        panelMore.setLayout(new FlowLayout());
        panelMore.add(fileBtn);

        add(panelMore, "dock south,h 0!");
    }

    private void toggleMorePanel(JButton cmdMore) {
        if (panelMore.isVisible()) {
            cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more_disable.png")));
            panelMore.setVisible(false);
            mig.setComponentConstraints(panelMore, "dock south,h 0!");
            revalidate();
        } else {
            cmdMore.setIcon(new ImageIcon(getClass().getResource("/com/raven/icon/more.png")));
            panelMore.setVisible(true);
            mig.setComponentConstraints(panelMore, "dock south,h 170!");
            revalidate();
        }
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            PublicEvent.getInstance().getEventChat().sendFile(file);
        }
    }

    private void eventSend(JIMSendTextPane txt) {
        String text = txt.getText().trim();
        if (!text.isEmpty()) {
            Model_Send_Message message = new Model_Send_Message(
                    MessageType.TEXT,
                    Service.getInstance().getUser().getUserID(),
                    user.getUserID(),
                    text
            );
            send(message);
            PublicEvent.getInstance().getEventChat().sendMessage(message);
            txt.setText("");
            txt.grabFocus();
            refresh();
        } else {
            txt.grabFocus();
        }
    }

    private void send(Model_Send_Message data) {
        Service.getInstance().getClient().emit("send_to_user", data.toJsonObject());
    }

    private void refresh() {
        revalidate();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setBackground(new Color(229, 229, 229));
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 40, Short.MAX_VALUE)
        );
    }
}
