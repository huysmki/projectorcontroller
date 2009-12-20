package be.rhea.projector.controller.remote.commands.server;

import java.util.Map;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStopServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final Map<String, VideoMediaPanel> mediaPanelMap;
	private final JPanel panel;

	public PCPVideoMediaStopServerCommand(JPanel panel, Map<String,VideoMediaPanel> mediaPanelMap) {
		this.panel = panel;
		this.mediaPanelMap = mediaPanelMap;
	}

	public String execute(String[] parameters) {
		String videoFileName = parameters[0];
		panel.removeAll();
		
		VideoMediaPanel videoMediaPanel = mediaPanelMap.get(videoFileName);
		if (videoMediaPanel != null) {
			videoMediaPanel.getMediaPlayer().stop();
			videoMediaPanel.setVisible(false);
			videoMediaPanel.invalidate();
			videoMediaPanel.killCurrentPlayer();
		}
		mediaPanelMap.remove(videoFileName);
		
		return "Video Media Stopped";
	}

}
