package be.rhea.projector.controller.remote.commands.server;

import java.util.Map;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStopServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final Map<String, VideoMediaPanel> mediaPanelMap;
	private final JFrame frame;

	public PCPVideoMediaStopServerCommand(JFrame frame, Map<String,VideoMediaPanel> mediaPanelMap) {
		this.frame = frame;
		this.mediaPanelMap = mediaPanelMap;
	}

	public String execute(String[] parameters) {
		String videoFileName = parameters[0];
		frame.getContentPane().removeAll();
		
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
