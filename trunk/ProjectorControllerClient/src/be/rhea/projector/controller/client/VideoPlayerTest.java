package be.rhea.projector.controller.client;

import java.awt.Component;
import java.io.File;
import java.net.URL;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.StartEvent;
import javax.media.bean.playerbean.MediaPlayer;
import javax.media.protocol.DataSource;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jmapps.util.JMFUtils;

public class VideoPlayerTest extends JFrame implements ControllerListener {
	private Player mediaPlayer;
	private Component visualComponent;
	private JPanel mediaPanel;

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		VideoPlayerTest test = new VideoPlayerTest();
		test.start();

		
	}
	
	public VideoPlayerTest() {
		mediaPanel = new JPanel();
		this.getContentPane().add(mediaPanel);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void start() throws Exception {
		URL url = new File("c:/temp/9000/video(4).mpg").toURI().toURL();
		
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		Manager.setHint(Manager.CACHING, true);
		MediaPlayer player = JMFUtils.createMediaPlayer("file:c:/temp/9000/video(5).mpg", this, null, null);
		player.start();
//		mediaPlayer = Manager.createRealizedPlayer(url);
//		mediaPlayer.addControllerListener(this);
//		mediaPlayer.start();	
//		
//		Thread.sleep(5000);
//		mediaPlayer.stop();
//		url = new File("c:/temp/9000/video(5).mpg").toURI().toURL();
//		
//		com.sun.media.protocol.file.DataSource datasource = new com.sun.media.protocol.file.DataSource();
//		datasource.setLocator(new MediaLocator(url));
//		
//		mediaPlayer.setSource(datasource);
//		mediaPlayer.start();
	}
	
	@Override
	public void controllerUpdate(ControllerEvent event) {
		if(mediaPlayer == null) {
			return;
		}
		
		if(event instanceof StartEvent)
		{
			if((visualComponent = mediaPlayer.getVisualComponent()) != null) {
				Component[] components = mediaPanel.getComponents();
				for (int i = 0; i < components.length; i++) {
					Component component = components[i];
					mediaPanel.remove(component);
				}
				mediaPanel.add(visualComponent);
			}
		}
		
	}	
}
