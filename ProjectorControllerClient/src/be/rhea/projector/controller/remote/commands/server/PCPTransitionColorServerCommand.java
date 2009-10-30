package be.rhea.projector.controller.remote.commands.server;

import java.awt.Color;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.ColorPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPTransitionColorServerCommand implements
		SimpleProtocolServerExecuteCommand, Runnable {
	
	private final ColorPanel mediaPanel;
	private final JFrame frame;
	private int fromR;
	private int fromG;
	private int fromB;
	private int toR;
	private int toG;
	private int toB;
	private int time;

	public PCPTransitionColorServerCommand(JFrame frame, ColorPanel colorPanel) {
		this.frame = frame;
		this.mediaPanel = colorPanel;
	}

	@Override
	public String execute(String[] parameters) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);
		
		fromR = Integer.valueOf(parameters[0]);
		fromG = Integer.valueOf(parameters[1]);
		fromB = Integer.valueOf(parameters[2]);
		
		toR = Integer.valueOf(parameters[3]);
		toG = Integer.valueOf(parameters[4]);
		toB = Integer.valueOf(parameters[5]);
		
		time = Integer.valueOf(parameters[6]);
		
		mediaPanel.setBackground(new Color(Integer.valueOf(parameters[0]), Integer.valueOf(parameters[1]), Integer.valueOf(parameters[2])));
		mediaPanel.setVisible(true);
		mediaPanel.validate();
		Thread transitionTread = new Thread(this); 
		transitionTread.start();
		
		
		return "TransitionColor Loaded";
	}

	@Override
	public void run() {
		int sleeptime = time / 255;
		float deltaR = ((float)(toR - fromR)) / 255;
		float deltaG = ((float)(toG - fromG)) / 255;
		float deltaB = ((float)(toB - fromB)) / 255;
//		System.out.println("toR=" + toR +",toG=" + toG +",toB=" + toB);
//		System.out.println("fromR=" + fromR +",fromG=" + fromG +",fromB=" + fromB);
//		System.out.println("deltaR=" + deltaR +",deltaG=" + deltaG +",deltaB=" + deltaB);
		for (int i = 0; i < 255; i++) {
			try {
				Thread.sleep(sleeptime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			System.out.println("i = " + i + " R=" + (int)(fromR + (deltaR * i)) + ",G=" + (int)(fromG + (deltaG * i)) + ",B=" + (int)(fromB + (deltaB * i)));
			int red = (int)(fromR + (deltaR * i));
			if (red < 0) {
				red = 0;
			} 
			if (red > 255) {
				red = 255;
			}
			int green = (int)(fromG + (deltaG * i));
			if (green < 0) {
				green = 0;
			} 
			if (green > 255) {
				green = 255;
			}
			int blue = (int)(fromB + (deltaB * i));
			if (blue < 0) {
				blue = 0;
			} 
			if (blue > 255) {
				blue = 255;
			}			
			mediaPanel.setBackground(new Color(red, green, blue));
			mediaPanel.revalidate();
		}
	}
}
