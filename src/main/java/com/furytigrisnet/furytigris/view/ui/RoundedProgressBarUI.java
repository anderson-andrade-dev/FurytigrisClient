package com.furytigrisnet.furytigris.view.ui;

import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;

/**
 * @author Anderson Andrade Dev
 * @Data de Criação 09/01/2025
 */

public class RoundedProgressBarUI extends BasicProgressBarUI {
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

        int progress = (int) (barWidth * progressBar.getPercentComplete());
        g2.setColor(progressBar.getForeground());
        g2.fillRoundRect(b.left, b.top, progress, barHeight, arc, arc);
    }
}
