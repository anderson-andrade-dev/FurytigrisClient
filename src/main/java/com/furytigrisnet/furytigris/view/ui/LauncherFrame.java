package com.furytigrisnet.furytigris.view.ui;

import com.furytigrisnet.furytigris.services.MinecraftLauncherService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class LauncherFrame {

    public JFrame frame;
    public JPanel panel;
    public MinecraftLauncherService minecraftLauncherService;

    public LauncherFrame(MinecraftLauncherService minecraftLauncherService) {
        this.minecraftLauncherService = minecraftLauncherService;
    }

    public void initialize() {

        frame = new JFrame("FuryTigris Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(760, 415, 800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        ImageIcon icon = new ImageIcon("furytigris/images/iconmenu.png");
        frame.setIconImage(icon.getImage());

        panel = new JPanel();
        panel.setBounds(0, 0, 600, 300);
        panel.setBackground(new Color(44, 47, 51));
        panel.repaint();
        panel.setVisible(true);
        frame.getContentPane().add(panel);

        JLabel label = new JLabel();
        label.setText("FuryTigris Client");
        label.setForeground(Color.white);
        label.setBounds(3, 3, 0, 0);
        label.setSize(20, 20);
        label.setVisible(true);
        panel.add(label);

        JButton buttonlaunch = new JButton();
        buttonlaunch.setText("Iniciar Client");
        buttonlaunch.setBounds(250, 270, 350, 290);
        buttonlaunch.setBackground(new Color(114, 137, 218));
        buttonlaunch.setForeground(Color.white);
        buttonlaunch.setVisible(true);
        buttonlaunch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        panel.add(buttonlaunch);

        frame.getContentPane().add(panel);
    }
}
