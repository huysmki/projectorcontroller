package be.rhea.projector.controller.remote.commands.server;

import javax.media.Player;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStopServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final VideoMediaPanel mediaPanel;

	public PCPVideoMediaStopServerCommand(VideoMediaPanel mediaPanel) {
		this.mediaPanel = mediaPanel;
	}

	public String execute(String[] parameters) {
		Player mediaPlayer = mediaPanel.getMediaPlayer();
		mediaPlayer.stop();
		mediaPanel.setVisible(false);
		mediaPanel.invalidate();
		
		return "Video Media Stopped";
	}

}
