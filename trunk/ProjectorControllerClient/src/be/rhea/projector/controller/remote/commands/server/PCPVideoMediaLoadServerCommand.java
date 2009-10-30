package be.rhea.projector.controller.remote.commands.server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.MediaLocator;
import javax.media.bean.playerbean.MediaPlayer;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaLoadServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final VideoMediaPanel mediaPanel;
	private String mediaDir;

	public PCPVideoMediaLoadServerCommand(VideoMediaPanel mediaPanel) {
		this.mediaPanel = mediaPanel;
	}

	@Override
	public String execute(String[] parameters) {
		
	    URL url = null;
		try {
			url = new File(mediaDir + "/" + parameters[0]).toURI().toURL();
			System.out.println(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		MediaPlayer mediaPlayer = mediaPanel.getMediaPlayer();
		mediaPlayer.setPlaybackLoop(true);
		mediaPlayer.setMediaLocator(new MediaLocator(url));
		mediaPlayer.realize();
		//TODO check this. Causes java.net.SocketTimeoutException: Read timed out
		//but is safer because client answers when media is fully loaded
		while (mediaPlayer.getState() != MediaPlayer.Realized) {
			try {  
				System.out.println(mediaPlayer.getState());
				Thread.sleep (500);
				
			} catch (InterruptedException e) {
			}
		}
		
		
		return "Video Media Loaded";
	}

	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}
}
