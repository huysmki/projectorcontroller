package be.rhea.projector.controller.remote.commands.server;

import javax.media.bean.playerbean.MediaPlayer;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStopServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final VideoMediaPanel mediaPanel;

	public PCPVideoMediaStopServerCommand(VideoMediaPanel mediaPanel) {
		this.mediaPanel = mediaPanel;
	}

	@Override
	public String execute(String[] parameters) {
		MediaPlayer mediaPlayer = mediaPanel.getMediaPlayer();
		mediaPlayer.stop();
		mediaPlayer.setVisible(false);
		mediaPanel.setVisible(false);
		mediaPanel.invalidate();
		
		return "Video Media Stopped";
	}

}
