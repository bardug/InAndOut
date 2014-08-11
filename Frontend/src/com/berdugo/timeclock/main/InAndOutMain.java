package com.berdugo.timeclock.main;

import com.berdugo.timeclock.backend.Backend;
import com.berdugo.timeclock.backend.BackendImpl;
import com.berdugo.timeclock.frontend.InAndOutFrame;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.WString;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Properties;

public class InAndOutMain {

    static final Logger logger = LoggerFactory.getLogger(InAndOutMain.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {

        setCurrentProcessExplicitAppUserModelID("InAndOut");

        // since I'm using SLF4J this is the only place where log4j code is used
        initLog4j();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
                    UIManager.setLookAndFeel(
                            UIManager.getSystemLookAndFeelClassName());

                    logger.info("~~~~~~~~~ InAndOut - Attendance Recorder - Starting Now ~~~~~~~~~");

                    Backend backend = new BackendImpl();
                    InAndOutFrame window = new InAndOutFrame(backend);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    private static void initLog4j() throws IOException {
        Properties loggerProperties = new Properties();
        loggerProperties.load(InAndOutMain.class.getResourceAsStream("/res/log/log4j.properties"));
        PropertyConfigurator.configure(loggerProperties);
        logger.info("(Logger initialized successfully)");
    }

    public static void setCurrentProcessExplicitAppUserModelID(final String appID)
    {
        if (SetCurrentProcessExplicitAppUserModelID(new WString(appID)).longValue() != 0) {
            logger.error("unable to set current process explicit AppUserModelID to: " + appID);
            throw new RuntimeException("unable to set current process explicit AppUserModelID to: " + appID);
        }
    }

    private static native NativeLong SetCurrentProcessExplicitAppUserModelID(WString appID);

    static
    {
        Native.register("shell32");
    }
}
