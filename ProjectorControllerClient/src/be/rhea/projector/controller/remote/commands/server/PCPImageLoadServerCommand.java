package be.rhea.projector.controller.remote.commands.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import be.rhea.projector.controller.client.ui.ImagePanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPImageLoadServerCommand implements
		SimpleProtocolServerExecuteCommand, Runnable {
	
	private final ImagePanel mediaPanel;
	private String mediaDir;
	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

	private String imageName;

	public PCPImageLoadServerCommand(ImagePanel imagePanel) {
		this.mediaPanel = imagePanel;
	}

	@Override
	public String execute(String[] parameters) {
		imageName = parameters[0];
		
		Thread t = new Thread(this);
		t.start();
		
		
		return "Image Loaded";
	}

	@Override
	public void run() {
		try {
			File file = new File(mediaDir, imageName);
			BufferedImage image = ImageIO.read(file);
			System.out.println("load image " + file.getAbsolutePath() + " image " + image.getHeight());
			mediaPanel.setImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
