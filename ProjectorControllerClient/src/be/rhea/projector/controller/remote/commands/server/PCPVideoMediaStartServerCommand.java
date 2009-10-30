package be.rhea.projector.controller.remote.commands.server;

import javax.media.bean.playerbean.MediaPlayer;
import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStartServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final VideoMediaPanel mediaPanel;
	private final JFrame frame;

	public PCPVideoMediaStartServerCommand(JFrame frame, VideoMediaPanel mediaPanel) {
		this.frame = frame;
		this.mediaPanel = mediaPanel;
	}

	@Override
	public String execute(String[] parameters) {
		frame.getContentPane().removeAll();
		frame.getContentPane().add(mediaPanel);

		MediaPlayer mediaPlayer = mediaPanel.getMediaPlayer();
		mediaPlayer.setVisible(true);
		mediaPlayer.start();

		mediaPanel.repaint();
		mediaPanel.revalidate();
		mediaPanel.setVisible(true);
		
		return "Video Media Started";
	}

}
