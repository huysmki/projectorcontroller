package be.rhea.projector.controller.client.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.filter.AlphaFilter;

public class ImagePanel extends JPanel implements Runnable {
	
	private static final int MAX_WAIT_MILLIS = 1000;
	private Image image;
	private Image currentImage;
    private AlphaFilter f = new AlphaFilter();
	
	public ImagePanel() {
		this.setBackground(Color.BLACK);
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void play() {
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
	    f.setLevel(100);
		
		repaint();
		revalidate();
//		Thread t = new Thread(this);
//		t.start();
	}

	@Override
	public void paint(Graphics g) {
//		System.out.println("repaint");
//	    FilteredImageSource fis = new FilteredImageSource(currentImage.getSource(), f);
//		g.drawImage(this.createImage(fis), 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawImage(currentImage, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	@Override
	public void run() {
		int level = 0;
		while (level < 255) {
			level++;
			f.setLevel(level);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
			revalidate();
		}
		
	}
	
}
