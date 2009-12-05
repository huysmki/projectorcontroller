package be.rhea.projector.controller.client.listener;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;


public class FrameMouseListener extends MouseAdapter implements MouseMotionListener {
	private final JFrame frame;
	private int x;
	private int y;
	private Point originalLocation;

	public FrameMouseListener(JFrame frame) {
		this.frame = frame;
	}

	public void mouseDragged(MouseEvent event) {
		frame.setLocation( event.getXOnScreen() - x, event.getYOnScreen() - y);
	}

	public void mouseClicked(MouseEvent event) {
		
		if (event.getClickCount() == 2) {
			if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
				frame.setLocation(originalLocation);
				frame.setSize( 400, 300 );
				
			} else {
				originalLocation = frame.getLocation();
	            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	            
				
			}
		} else {
			int x2 = event.getX();
			int y2 = event.getY();
			if (x2 > (frame.getWidth() - 20) && y2 < 20) {
				System.exit(0);
			}
		}
	}

	public void mousePressed(MouseEvent event) {
		x = event.getX();
		y = event.getY();
	}
}
