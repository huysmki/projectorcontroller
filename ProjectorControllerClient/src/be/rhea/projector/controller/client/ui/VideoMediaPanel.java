package be.rhea.projector.controller.client.ui;

// Fig 21.6: MediaPanel.java
// A JPanel the plays media from a URL
import java.awt.BorderLayout;
import java.awt.Color;

import javax.media.Player;
import javax.swing.JPanel;

public class VideoMediaPanel extends JPanel {
	private static final long serialVersionUID = 3632647220675028768L;
	private Player mediaPlayer;

	public VideoMediaPanel() {
		setLayout(new BorderLayout()); // use a BorderLayout
		// Use lightweight components for Swing compatibility
		this.setBackground(Color.BLACK);
//		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
//		Manager.setHint(Manager.CACHING, true);
//		Manager.setHint(Manager.PLUGIN_PLAYER, false);
//		
//		mediaPlayer = new MediaPlayer();// Manager.createRealizedPlayer(mediaURL);
//		mediaPlayer.setControlPanelVisible(false);
//		add(mediaPlayer, BorderLayout.CENTER);
//		mediaPlayer.prefetch();
	}

	public void setMediaPlayer(Player mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public Player getMediaPlayer() {
		return mediaPlayer;
	} 

//	public MediaPlayer getMediaPlayer() {
//		return mediaPlayer;
//	}
	
} // end class MediaPanel
