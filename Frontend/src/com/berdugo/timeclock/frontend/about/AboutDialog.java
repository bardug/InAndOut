package com.berdugo.timeclock.frontend.about;

import com.berdugo.timeclock.frontend.InAndOutGUIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutDialog extends JDialog {

    @Override
    protected void dialogInit() {

        super.dialogInit();

        setDialogAttributes();

        createContent();

        pack();
    }

    private void setDialogAttributes() {
        getOwner().setIconImages(InAndOutGUIHelper.getImages());
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
    }

    private void createContent() {

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        createIconPanel();

        createLabels();

        createOkButton();
    }

    private void createIconPanel() {
        JPanel iconLabelPane = new JPanel();
        iconLabelPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel iconLabel = new JLabel(new ImageIcon(InAndOutGUIHelper.getIconSmall()), SwingConstants.CENTER);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabelPane.add(iconLabel);
        getContentPane().add(iconLabelPane);
    }


    private void createLabels() {

        createAndPlaceLabel("IN & OUT", new Font("Tahoma", Font.PLAIN, 14));

        getContentPane().add(new JSeparator());

        createAndPlaceLabel("Version 1.0", InAndOutGUIHelper.DEFAULT_FONT);

        createAndPlaceLabel("Attendance Recorder", InAndOutGUIHelper.DEFAULT_FONT);

        createAndPlaceLabel("By Ami Berdugo", InAndOutGUIHelper.DEFAULT_FONT);

        getContentPane().add(new JSeparator());
    }

    private void createAndPlaceLabel(String labelText, Font labelFont) {
        JPanel wrappingPane = new JPanel();
        wrappingPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setFont(labelFont);
        wrappingPane.add(label);
        getContentPane().add(wrappingPane);
    }

    private void createOkButton() {
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window win = SwingUtilities.getWindowAncestor((Component) e.getSource());
                win.dispose();
            }
        });
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            AboutDialog dialog = new AboutDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
