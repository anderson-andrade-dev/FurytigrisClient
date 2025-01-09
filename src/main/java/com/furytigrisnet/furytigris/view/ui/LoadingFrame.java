package com.furytigrisnet.furytigris.view.ui;


import com.furytigrisnet.furytigris.util.OSHelper;
import com.furytigrisnet.furytigris.util.UnzipUtility;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class LoadingFrame {

    public JFrame frame;
    public JPanel panel;
    public JProgressBar progressBar;
    private JLabel statusLabel;
    private JLabel imageLabel;
    private JLabel imageLogo;

    public LoadingFrame() {
        frame = new JFrame("FuryTigris Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(44, 47, 51));
        frame.getContentPane().add(panel);

        ImageIcon logo = new ImageIcon("furytigris/images/iconmenu.png");
        Image img = logo.getImage();
        Image resizedImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        logo = new ImageIcon(resizedImg);

        ImageIcon icon = new ImageIcon("furytigris/images/iconmenu.png");
        frame.setIconImage(icon.getImage());

        imageLabel = new JLabel(logo);
        imageLabel.setBounds(100, 30, 200, 200);
        panel.add(imageLabel);

        ImageIcon logoMenu = new ImageIcon("furytigris/images/logoclient.png");
        Image imgl = logoMenu.getImage();
        Image resizedImgl = imgl.getScaledInstance(300, 40, Image.SCALE_SMOOTH);
        logoMenu = new ImageIcon(resizedImgl);

        imageLogo = new JLabel(logoMenu);
        imageLogo.setBounds(50, 230, 300, 40);
        panel.add(imageLogo);
        imageLogo.setVisible(false);

        statusLabel = new JLabel("[FuryTigris Client Logs] " + "Iniciando a instalação...");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setBounds(140, 420, 200, 30);
        panel.add(statusLabel);

        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(100, 149, 237));
        progressBar.setBackground(new Color(44, 47, 51));
        progressBar.setPreferredSize(new Dimension(300, 20));

        progressBar.setUI(new BasicProgressBarUI() {
            @Override
            protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Insets b = progressBar.getInsets();
                int barWidth = progressBar.getWidth() - b.left - b.right;
                int barHeight = progressBar.getHeight() - b.top - b.bottom;

                int arc = barHeight;
                g2.setColor(progressBar.getBackground());
                g2.fillRoundRect(b.left, b.top, barWidth, barHeight, arc, arc);

                int progress = (int) ((barWidth * progressBar.getPercentComplete()));
                g2.setColor(progressBar.getForeground());
                g2.fillRoundRect(b.left, b.top, progress, barHeight, arc, arc);
            }
        });

        progressBar.setBorder(BorderFactory.createEmptyBorder());
        progressBar.setBounds(50, 450, 300, 20);
        panel.add(progressBar);

        frame.setVisible(true);

        new Thread(() -> {
            try {
                installFiles();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void installFiles() throws Exception {
        File minecraftDirectory = new File(OSHelper.getOS().getMc());
        File minecraftAssets = new File(minecraftDirectory.toString() + "assets");

        File natives = new File(System.getProperty("user.dir") + File.separator + "natives.zip");
        File libraries = new File(System.getProperty("user.dir") + File.separator + "libraries.zip");
        File jar = new File(System.getProperty("user.dir") + File.separator + "client.jar");

        try {
            updateStatus("Baixando Natives.zip");
            System.out.println("[FuryTigris Client Logs] " + "Baixando Natives.zip");
            progressBar.setValue(6);
            FileUtils.copyURLToFile(new URL("https://download1584.mediafire.com/ohxueeoflwagXrAzSP5dUXqcdDZtVQky4NnsO6Reoc6Cw5VYqnlSGg5L1yahPMmW7RcBKhiWjDUtlnR069Y5q6Rz5wlscrqm4DKrZHV_u0A5vLdJWGUxNKBm97z0WnRNvynBhZXazf4YnzABJ_Q0_WX9WzCdFfsrKAJ3aN3TsBoFygI/mzr3tnutaj1izv4/natives.zip"), natives);
            progressBar.setValue(10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            updateStatus("Baixando Libraries.zip");
            System.out.println("[FuryTigris Client Logs] " + "Baixando Libraries.zip");
            progressBar.setValue(15);
            FileUtils.copyURLToFile(new URL("https://download1500.mediafire.com/dw65rqhv7pkgv0HdSXdn6MtI60ScB-zyZmygYdyroa39MQsYozv_TpcTxiaxJjZZGVYfzQ9K0HwHIwFESyefURmdHS5iKMvvmEt0QondTB5jhq2oxlkR1y2srdf_QQALWTKlwlAzGQtVVvdDKoJJI3kbxDLoQixwiJ7vcuPsP0gihXA/h5z12385dpsjl2p/libraries.zip"), libraries);
            progressBar.setValue(20);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            updateStatus("Baixando Client.jar");
            System.out.println("[FuryTigris Client Logs] " + "Baixando Client.jar" );
            progressBar.setValue(23);
            FileUtils.copyURLToFile(new URL("https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar"), jar);
            progressBar.setValue(25);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateStatus("Extraindo arquivos...");
        UnzipUtility unzipper = new UnzipUtility();
        progressBar.setValue(28);
        Thread.sleep(2000);

        unzipper.unzip(natives.toString(), System.getProperty("user.dir") + File.separator + "natives");
        System.out.println("[FuryTigris Client Logs] " + "Extraindo natives" );
        natives.delete();
        progressBar.setValue(32);
        Thread.sleep(2000);

        unzipper.unzip(libraries.toString(), System.getProperty("user.dir") + File.separator + "libraries");
        System.out.println("[FuryTigris Client Logs] " + "Extraindo libraries" );
        progressBar.setValue(33);
        Thread.sleep(2000);
        libraries.delete();
        progressBar.setValue(34);
        Thread.sleep(2000);

        updateStatus("Excluindo Arquivos zip");
        System.out.println("[FuryTigris Client Logs] " + "Excluindo Arquivos zip" );
        progressBar.setValue(45);
        Thread.sleep(2000);

        updateStatus("Checagem de Arquivos");
        System.out.println("[FuryTigris Client Logs] " + "Checagem de Arquivos" );
        File nativescheck = new File(System.getProperty("user.dir") + File.separator + "natives.zip");
        if (nativescheck.exists()) {
            updateStatus("Checagem de Natives");
            System.out.println("[FuryTigris Client Logs] " + "Checagem de Natives" );
        }
        progressBar.setValue(50);
        Thread.sleep(2000);

        File librariescheck = new File(System.getProperty("user.dir") + File.separator + "libraries.zip");
        if (librariescheck.exists()) {
            updateStatus("Checagem de Libraries");
            System.out.println("[FuryTigris Client Logs] " + "Checagem de Libraries" );
        }
        progressBar.setValue(60);
        Thread.sleep(2000);

        File jarcheck = new File(System.getProperty("user.dir") + File.separator + "client.jar");
        if (jarcheck.exists()) {
            updateStatus("Checagem de Jar");
            System.out.println("[FuryTigris Client Logs] " + "Checagem de Jar" );
        }
        progressBar.setValue(70);
        Thread.sleep(2000);


        String pathdata = "../data";
        String pathjava = "../data/java";
        String pathnamejava = "../data/java/jdk1.8.0_202";

        File folderdata = new File(pathdata);
        File folderjava = new File(pathjava);
        File foldernamejava = new File(pathnamejava);

        if(folderdata.mkdir()){
        } else {
            System.out.println("Erro de Criação de Pasta Data");
        }

        if(folderjava.mkdir()){
        } else {
            System.out.println("Erro de Criação de Pasta Java");
        }

        if(foldernamejava.mkdir()){
        } else {
            System.out.println("Erro de Criação de Pasta JavaName");
        }

        File javacheck = new File(System.getProperty("user.dir") + File.separator + "../data/java/jdk1.8.0_202/bin/java.exe");
        if(javacheck.exists()) {
            updateStatus("Checagem de Java");
            System.out.println("[FuryTigris Client Logs] " + "Checagem de Java" );
        } else {
            System.out.println("Erro: Java não Localizado!");
        }

        progressBar.setValue(80);
        Thread.sleep(2000);

        updateStatus("Checagem Finalizada");
        System.out.println("[FuryTigris Client Logs] " + "Checagem Finalizada" );
        progressBar.setValue(90);
        Thread.sleep(2000);

        updateStatus("Finalizando");
        System.out.println("[FuryTigris Client Logs] " + "Checagem Concluida" );
        progressBar.setValue(100);
        Thread.sleep(2000);

        try {
            panel.setBackground(new Color(100, 149, 237));
            imageLabel.setVisible(false);
            statusLabel.setVisible(false);
            progressBar.setVisible(false);
            imageLogo.setVisible(true);

            Thread.sleep(2000);

            new LauncherFrame();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        frame.dispose();
    }

    private void updateStatus(String message) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(message));
    }
}
