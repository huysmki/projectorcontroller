package be.rhea.projector.controller.remote.commands.server;

import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPStopServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final Map<String, VideoMediaPanel> mediaPanelMap;
	private final JPanel panel;

	public PCPStopServerCommand(JPanel panel, Map<String, VideoMediaPanel> mediaPanelMap) {
		this.panel = panel;
		this.mediaPanelMap = mediaPanelMap;
	}

	public String execute(String[] parameters) {
		panel.removeAll();
		panel.invalidate();
		panel.repaint();

		Set<String> keySet = mediaPanelMap.keySet();
		for (String key : keySet) {
			VideoMediaPanel videoMediaPanel = mediaPanelMap.get(key);
			videoMediaPanel.getMediaPlayer().stop();
			videoMediaPanel.setVisible(false);
			videoMediaPanel.invalidate();
			videoMediaPanel.killCurrentPlayer();
		}
		mediaPanelMap.clear();
		
		
		return "Stopped";
	}

}
