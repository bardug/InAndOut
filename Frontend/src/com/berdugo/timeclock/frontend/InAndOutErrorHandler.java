package com.berdugo.timeclock.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: ami
 * Date: 03/06/13
 * Time: 20:45
 */
public class InAndOutErrorHandler {

    public static void popErrorDialog(String text, Component parentComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Error");
        dialog.getOwner().setIconImages(InAndOutGUIHelper.getImages());
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);

        JPanel panel = getErrorDialogInnerPanel(text);
        dialog.add(panel);

        dialog.pack();
        dialog.setVisible(true);
    }

    private static JPanel getErrorDialogInnerPanel(String text) {
        JPanel panel =  new JPanel(new GridBagLayout());

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5,5,5,5);

        JLabel errorMessage = new JLabel(text);
        errorMessage.setForeground(new Color(219, 74, 55));
        errorMessage.setFont(new Font("Tahoma", Font.BOLD, 12));

        panel.add(errorMessage, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
                win.dispose();
            }
        });

        panel.add(okButton, gridBagConstraints);

        return panel;
    }
}
