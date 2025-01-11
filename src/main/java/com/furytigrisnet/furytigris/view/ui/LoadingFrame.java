package com.furytigrisnet.furytigris.view.ui;

import com.furytigrisnet.furytigris.services.InstallerService;
import com.furytigrisnet.furytigris.services.MinecraftLauncherService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;


public class LoadingFrame {

    private final MinecraftLauncherService minecraftLauncherService;
    private final LauncherFrame launcherFrame;
    private JFrame frame;
    private JPanel panel;
    private JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel imageLabel;
    private JLabel imageLogo;


    public LoadingFrame(MinecraftLauncherService minecraftLauncherService,LauncherFrame launcherFrame){
        this.minecraftLauncherService =minecraftLauncherService;
        this.launcherFrame = launcherFrame;
    }

    @PostConstruct
    public void init() {
        try {
            System.out.println("Initializing frame...");
            configureFrame();
            configurePanel();
            configureImages();
            configureStatusLabel();
            configureProgressBar();
            frame.setVisible(true);
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            throw new IllegalStateException("Failed to initialize loading frame", e);
        }
    }

    private void configureFrame() {
        frame = new JFrame("FuryTigris Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
    }

    private void configurePanel() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(44, 47, 51));
        frame.getContentPane().add(panel);
    }

    private void configureImages() {
        imageLabel = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("static/images/iconmenu.png")));
        imageLabel.setBounds(100, 30, 200, 200);
        panel.add(imageLabel);

        imageLogo = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("static/images/logoclient.png")));
        imageLogo.setBounds(50, 230, 300, 40);
        panel.add(imageLogo);
        imageLogo.setVisible(false);
    }

    private void configureStatusLabel() {
        statusLabel = new JLabel("[FuryTigris Client Logs] Iniciando a instalação...");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setBounds(140, 420, 200, 30);
        panel.add(statusLabel);
    }

    private void configureProgressBar() {
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(100, 149, 237));
        progressBar.setBackground(new Color(44, 47, 51));
        progressBar.setPreferredSize(new Dimension(300, 20));
        progressBar.setUI(new RoundedProgressBarUI());
        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setBounds(50, 450, 300, 20);
        panel.add(progressBar);
    }

    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }


}
