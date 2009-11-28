package be.rhea.projector.controller.remote.commands.server;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.CannotRealizeException;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.StartEvent;
import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStartServerCommand implements
		SimpleProtocolServerExecuteCommand, ControllerListener {
	
	private final VideoMediaPanel mediaPanel;
	private String mediaDir;
	private final JFrame frame;
	private Player mediaPlayer;
	private Component visualComponent;
	private boolean mediaClosed = false;

	public PCPVideoMediaStartServerCommand(JFrame frame, VideoMediaPanel mediaPanel) {
		this.frame = frame;
		this.mediaPanel = mediaPanel;
	}

	@Override
	public String execute(String[] parameters) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);
		
	    URL url = null;
		try {
			url = new File(mediaDir + "/" + parameters[0]).toURI().toURL();
			Player oldPlayer = mediaPanel.getMediaPlayer();
			if (oldPlayer != null) {
				Component[] components = mediaPanel.getComponents();
				for (int i = 0; i < components.length; i++) {
					Component component = components[i];
					mediaPanel.remove(component);
				}
				oldPlayer.stop();
				oldPlayer.close();
				mediaClosed = false;
				int count = 0;
				while (!mediaClosed && count < 20) {
					try {
						count++;
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}
				
				oldPlayer.removeControllerListener(this);
//				oldPlayer.deallocate();
				mediaPanel.setMediaPlayer(null);
				oldPlayer = null;
			}
			Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
			Manager.setHint(Manager.CACHING, true);
			mediaPlayer = Manager.createRealizedPlayer(url);
			mediaPlayer.addControllerListener(this);
			mediaPlayer.start();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoPlayerException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (CannotRealizeException e) {
			e.printStackTrace();
		}
		return "Video Media Loaded";
	}

	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

	@Override
	public void controllerUpdate(ControllerEvent event) {
		if(mediaPlayer == null) {
			return;
		}
		
		if(event instanceof StartEvent)
		{
			if((visualComponent = mediaPlayer.getVisualComponent()) != null) {
				mediaPanel.add(visualComponent);
				mediaPanel.setMediaPlayer(mediaPlayer);
				mediaPanel.repaint();
				mediaPanel.revalidate();
				mediaPanel.setVisible(true);			
			}
		} else if(event instanceof ControllerClosedEvent) {
			mediaClosed = true;
		}
		
	}
}
