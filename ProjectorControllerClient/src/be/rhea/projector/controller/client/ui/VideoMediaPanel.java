package be.rhea.projector.controller.client.ui;

// Fig 21.6: MediaPanel.java
// A JPanel the plays media from a URL
import java.awt.BorderLayout;

import javax.media.Manager;
import javax.media.bean.playerbean.MediaPlayer;
import javax.swing.JPanel;

public class VideoMediaPanel extends JPanel {
	private static final long serialVersionUID = 3632647220675028768L;
	private MediaPlayer mediaPlayer;

	public VideoMediaPanel() {
		setLayout(new BorderLayout()); // use a BorderLayout
		// Use lightweight components for Swing compatibility
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		mediaPlayer = new MediaPlayer();// Manager.createRealizedPlayer(mediaURL);
		mediaPlayer.setControlPanelVisible(false);
		add(mediaPlayer, BorderLayout.CENTER);
		mediaPlayer.prefetch();
	} 

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}
	
} // end class MediaPanel
