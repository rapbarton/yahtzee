package net.bartonhome.game.yahtzee.gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicFrame extends JFrame {
	private static final long serialVersionUID = 2341412341243L;
	private static final int DEFAULT_WIDTH = 540;
  private static final int DEFAULT_HEIGHT = 440;
  private static JFrame frame = null;

	public static void launch(final String title, final JPanel content) {
		if (null != frame) {
			frame.setVisible(true);
			return;
		}
		Runnable r = new Runnable() {
      public void run() {
      	frame = new BasicFrame(title, content);
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.validate();
        
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
          frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
          frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        frame.addWindowListener(new WindowListener() {
					public void windowOpened(WindowEvent e) {}
					public void windowIconified(WindowEvent e) {}
					public void windowDeiconified(WindowEvent e) {}
					public void windowDeactivated(WindowEvent e) {}
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
					public void windowActivated(WindowEvent e) {}
					public void windowClosed(WindowEvent e) {
						System.exit(0);
					}
				});
      }
		};
		try {
      javax.swing.SwingUtilities.invokeAndWait(r);
    } catch (InterruptedException e) {
    } catch (InvocationTargetException e) {
    }
		
	}
	
	private BasicFrame (String title, JPanel content) {
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try {
			this.setTitle(title);
			this.getContentPane().add(content, BorderLayout.CENTER);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
