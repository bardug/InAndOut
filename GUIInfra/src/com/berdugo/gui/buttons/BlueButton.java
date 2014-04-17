package com.berdugo.gui.buttons;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * User: ami
 * Date: 15/09/13
 * Time: 09:28
 */
public class BlueButton extends JPanel{

    private String text;

    public BlueButton(String text) {
        this.text = text;
        buildUI();
        addListeners();
    }

    private void buildUI() {
        setLayout(new CardLayout(4,4));
        JLabel label = new JLabel(text);
        label.setForeground(new Color(238, 245, 254));
        add(label);
        mouseOut();
    }

    private void addListeners() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if ( isEnabled() ) {
                    setBackground(new Color(87, 183, 247));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if ( isEnabled() ) {
                    mouseOut();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if ( isEnabled() ) {
                    mouseIn();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if ( isEnabled() ) {
                    mouseOut();
                }
            }
        });
    }

    private void mouseOut() {
        setBackground(new Color(77, 100, 253));
        setBorder(new LineBorder(new Color(77, 143, 253)));
    }

    private void mouseIn() {
        setBackground(new Color(93, 161, 247));
        setBorder(new LineBorder(new Color(47, 91, 183)));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if ( enabled ) {
            mouseOut();
        } else {
            setBackground(new Color(157, 177, 197));
        }
    }

/*
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(77, 143, 253));
        g.fillRoundRect(0,0, getWidth()-1, getHeight()-1, 5, 5);
        g.setColor(new Color(77, 143, 253));
        g.drawRoundRect(0,0, getWidth()-1, getHeight()-1, 5, 5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = Color.RED;
        Color color2 = Color.GREEN;
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
*/
}
