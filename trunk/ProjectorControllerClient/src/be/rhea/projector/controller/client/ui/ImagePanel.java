package be.rhea.projector.controller.client.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel implements Runnable {
	private static final long serialVersionUID = -1929591366297219001L;
	private static final int MAX_WAIT_MILLIS = 1000;
	private Image image;
	private Image currentImage;
	private float alpha = 0f;
	private boolean running = true;
	private int sleepTime = 0;
	private boolean fadeIn;
	private Thread thread;
	
	public ImagePanel() {
		this.setBackground(Color.BLACK);
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void play(int fadeInTime) {
		int waitTime = 0;
		while (image == null && waitTime < MAX_WAIT_MILLIS) {
			waitTime += 10;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		currentImage = image;
		image = null;
		if (fadeInTime == 0) {
			alpha = 1.0f;
			repaint();
			revalidate();
		} else {
			alpha = 0f;
			sleepTime = fadeInTime / 30;
			fadeIn = true;
			startFadeThread();
		}
		
	}

	public void fadeOut(int fadeOutTime) {
		alpha = 1f;
		sleepTime = fadeOutTime / 100;
		fadeIn = false;
		startFadeThread();
		
	}
	
	@Override
	public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeComposite(alpha));
        g2d.drawImage(currentImage, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	public void run() {
		if (fadeIn) {
			while (alpha < 1 && running ) {
				repaint();
				revalidate();
				alpha += .01f;
				if (alpha > 1) {
					alpha = 1;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			while (alpha > 0 && running ) {
				repaint();
				revalidate();
				alpha -= .01f;
				if (alpha < 0) {
					alpha = 0;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
    private AlphaComposite makeComposite(float alpha) {
    	if (fadeIn) {
	        int type = AlphaComposite.SRC_OVER;
	        return (AlphaComposite.getInstance(type, alpha));
    	} else {
	        int type = AlphaComposite.SRC_IN;
	        return (AlphaComposite.getInstance(type, alpha));
    	}
    }

	private void startFadeThread() {
		if (thread != null && thread.isAlive()) {
			running = false;
			while (thread.isAlive()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
			running = true;
		}
		thread = new Thread(this);
		thread.start();
	}
}
