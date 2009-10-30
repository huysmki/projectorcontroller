package be.rhea.projector.controller.client.ui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.filter.AlphaFilter;

public class ImagePanel extends JPanel {
	
	private Image image;
	private Image currentImage;
	
	public ImagePanel() {
		this.setBackground(Color.BLACK);
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void play() {
		currentImage = image;
		repaint();
		revalidate();
	}

	@Override
	public void paint(Graphics g) {
//	    AlphaFilter f = new AlphaFilter();
//	    f.setLevel(50);
//	    FilteredImageSource fis = new FilteredImageSource(currentImage.getSource(), f);
//		g.drawImage(this.createImage(fis), 0, 0, this.getWidth(), this.getHeight(), null);
		g.drawImage(currentImage, 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
}
