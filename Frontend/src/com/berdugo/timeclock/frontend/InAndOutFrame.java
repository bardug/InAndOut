package com.berdugo.timeclock.frontend;

import com.berdugo.timeclock.backend.Backend;
import com.berdugo.timeclock.common.Callback;
import com.berdugo.timeclock.frontend.about.AboutDialog;
import com.berdugo.timeclock.frontend.time_chart.TimeChartDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InAndOutFrame {

    final Logger logger = LoggerFactory.getLogger(InAndOutFrame.class);

    private static final int DEFAULT_DELAY_FOR_STARTUP_MILLIS = 60 * 1000; // seconds*millis = 1 minute
    private static final int DEFAULT_DELAY_FOR_OUT_REMINDER_MILLIS = 9 * 60 * 60 * 1000; // hours*minutes*seconds*millis = 9 hours
    private static final int DEFAULT_DELAY_FOR_IN_REMINDER_MILLIS = 9 * 60 * 60 * 1000; // hours*minutes*seconds*millis = 9 hours

    private Backend backend;
	private JFrame frmInOut;
	private JLabel textArea;
    private Timer timer;


    /**
	 * Create the application.
	 */
	public InAndOutFrame(Backend backend) {
		this.backend = backend;
        initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		restartTimer(DEFAULT_DELAY_FOR_STARTUP_MILLIS, "Shall we sign in?");

        initializeFrame();
		
		initializeTextArea();

//		initializeOutButton();
		initializeOutLabel();

//		initializeInButton();
		initializeInLabel();

//        initializeTimeChartButton();
        initializeTimeChartLabel();

        initializeBackend();
		
	}

	private void initializeBackend() {
		backend.initTimeChart(new Callback(){

			@Override
			public void runCallback() {
				clearTextArea();
				displayInfo("Ready to go");
			}

			@Override
			public void runCallbackWithText(String text) {
				clearTextArea();
				displayError(text);
			}});
	}

/*
	private void initializeInButton() {
        JButton btnIn = new JButton();
        btnIn.setIcon(new ImageIcon(InAndOutGUIHelper.getBatteryFullIcon()));
        btnIn.setToolTipText("IN");

        btnIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }
        });
        btnIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearTextArea();
                displayInfo("IN pressed");
                backend.signIn(new Callback() {

                    @Override
                    public void runCallback() {
                        clearTextArea();
                        restartTimer(DEFAULT_DELAY_FOR_OUT_REMINDER_MILLIS, "Don't forget to sign out soon");
                        displayInfo("IN time recorded successfully");
                    }

                    @Override
                    public void runCallbackWithText(String text) {
                        displayInfo(text);
                    }
                });
            }
        });
		btnIn.setBounds(60, 60, 130, 130);
		frmInOut.getContentPane().add(btnIn);
	}
*/
	private void initializeInLabel() {
        final JLabel inLabel = new JLabel(new ImageIcon(InAndOutGUIHelper.getBatteryFullIcon()));
        inLabel.setToolTipText("IN");

        inLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                inLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getBatteryFullSmallerIcon()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                inLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getBatteryFullIcon()));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                clearTextArea();
                backend.signIn(new Callback() {

                    @Override
                    public void runCallback() {
                        clearTextArea();
                        restartTimer(DEFAULT_DELAY_FOR_OUT_REMINDER_MILLIS, "Don't forget to sign out soon");
                        displayInfo("IN time recorded successfully");
                    }

                    @Override
                    public void runCallbackWithText(String text) {
                        displayError(text);
                    }
                });
            }
        });

		inLabel.setBounds(90, 60, 90, 130);
//        inLabel.setBorder(new TitledBorder(""));
		frmInOut.getContentPane().add(inLabel);
	}

    private void restartTimer(int delay, final String text) {

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                frmInOut.setVisible(true);
                displayInfo(text);
                timer.stop();
            }
        };
        timer = new Timer(delay, taskPerformer);
        timer.start();
    }

/*
    private void initializeTimeChartButton() {
        JButton btnTimeChart = new JButton();
        btnTimeChart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }
        });
        btnTimeChart.setBounds(10, 10, 30, 30);
        btnTimeChart.setIcon(new ImageIcon(InAndOutGUIHelper.getTableIcon()));
        btnTimeChart.setToolTipText("View/Edit Time Sheet");
        btnTimeChart.setFocusPainted(false);
        frmInOut.getContentPane().add(btnTimeChart);
        btnTimeChart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    TimeChartDialog dialog = new TimeChartDialog(backend);
                    dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
                    dialog.setLocationRelativeTo(frmInOut);
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    System.out.println("REJECTED " + "_" + getClass().getName() + "_" + getClass().getEnclosingMethod() + "_" + ex.getMessage());
                }
            }
        });
    }
*/

    private void initializeTimeChartLabel() {
        final JLabel timeChartLabel = new JLabel(new ImageIcon(InAndOutGUIHelper.getEditIcon()));
        timeChartLabel.setBounds(10, 10, 32, 32);
        timeChartLabel.setToolTipText("View/Edit Time Sheet");
        frmInOut.getContentPane().add(timeChartLabel);

        timeChartLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    logger.debug("time chart button is clicked");
                    TimeChartDialog dialog = new TimeChartDialog(backend);
                    dialog.setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
                    dialog.setLocationRelativeTo(frmInOut);
                    dialog.setVisible(true);
                } catch (Exception ex) {
                    System.out.println("REJECTED " + "_" + getClass().getName() + "_" + getClass().getEnclosingMethod() + "_" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }
            @Override
            public void mousePressed(MouseEvent e) {
                timeChartLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getEditSmallerIcon()));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                timeChartLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getEditIcon()));
            }
        });
    }

/*
    private void initializeOutButton() {
        JButton btnOut = new JButton("OUT");
		btnOut.setFont(new Font("Tahoma", Font.BOLD, 40));
		btnOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }
        });
        btnOut.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearTextArea();
                displayInfo("OUT pressed");
                backend.signOut(new Callback() {

                    @Override
                    public void runCallback() {
                        clearTextArea();
                        restartTimer(DEFAULT_DELAY_FOR_IN_REMINDER_MILLIS, "Good Morning Miss Sunshine!");
                        displayInfo("OUT time recorded successfully");
                    }

                    @Override
                    public void runCallbackWithText(String text) {
                        displayInfo(text);
                    }
                });
            }
        });
		btnOut.setBounds(250, 60, 135, 107);
		frmInOut.getContentPane().add(btnOut);
	}
*/

    private void initializeOutLabel() {
        final JLabel outLabel = new JLabel(new ImageIcon(InAndOutGUIHelper.getBatteryEmptyIcon()) );
        outLabel.setToolTipText("OUT");
		outLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                clearTextArea();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                outLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getBatteryEmptySmallerIcon()));
            }


            @Override
            public void mouseReleased(MouseEvent e) {
                outLabel.setIcon(new ImageIcon(InAndOutGUIHelper.getBatteryEmptyIcon()));
            }
        });
        outLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearTextArea();
                backend.signOut(new Callback() {

                    @Override
                    public void runCallback() {
                        clearTextArea();
                        restartTimer(DEFAULT_DELAY_FOR_IN_REMINDER_MILLIS, "Good Morning Miss Sunshine!");
                        displayInfo("OUT time recorded successfully");
                    }

                    @Override
                    public void runCallbackWithText(String text) {
                        displayError(text);
                    }
                });
            }
        });
		outLabel.setBounds(270, 60, 90, 130);
//        outLabel.setBorder(new TitledBorder(""));
		frmInOut.getContentPane().add(outLabel);
	}

	private void initializeTextArea() {
		textArea = new JLabel("");
		textArea.setHorizontalAlignment(SwingConstants.CENTER);
		textArea.setBounds(60, 230, 325, 40);
        textArea.setBorder(new TitledBorder(""));
		frmInOut.getContentPane().add(textArea);
	}

	private void initializeFrame() {

        if (initializeSystemTray(InAndOutGUIHelper.getIconSmall())) return;

        frmInOut = new JFrame();
        frmInOut.setIconImages(InAndOutGUIHelper.getImages());
		frmInOut.setResizable(false);
		frmInOut.setTitle("IN & OUT");
		frmInOut.setBounds(-1, -1, 450, 320);
        frmInOut.setLocationRelativeTo(null);
		frmInOut.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frmInOut.getContentPane().setLayout(null);
	}



    private boolean initializeSystemTray(Image image) {
        //Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return true;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setToolTip("IN N' OUT");
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutDialog dialog = new AboutDialog();
                dialog.setLocationRelativeTo(frmInOut);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setVisible(true);
            }
        });

        MenuItem showItem = new MenuItem("Show");
        showItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmInOut.setVisible(true);
            }
        });

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cleanUp();
                logger.info("~~~~~~~~~ InAndOut - Exiting Now ~~~~~~~~~");
                System.exit(0);
            }
        });

        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(showItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImage(image);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        return false;
    }

    private void cleanUp() {
        backend.cleanAndClose(new Callback() {
            @Override
            public void runCallback() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void runCallbackWithText(String text) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private void clearTextArea() {
        textArea.setText("");
	}
	
	public JFrame getFrame() {
		return frmInOut;
	}

	private void displayError(String textToDisplay) {
		textArea.setText(textToDisplay);
        textArea.setForeground(new Color(219, 74, 55));
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
    }

	private void displayInfo(String textToDisplay) {
		textArea.setText(textToDisplay);
        textArea.setForeground(new Color(71, 71, 73));
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 12));
    }
}
