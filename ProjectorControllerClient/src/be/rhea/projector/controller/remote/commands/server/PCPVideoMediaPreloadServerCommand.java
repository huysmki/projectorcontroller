package be.rhea.projector.controller.remote.commands.server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaPreloadServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private final Map<String, VideoMediaPanel> mediaPanelMap;
	private String mediaDir;

	public PCPVideoMediaPreloadServerCommand(Map<String, VideoMediaPanel> mediaPanelMap) {
		this.mediaPanelMap = mediaPanelMap;
	}

	public String execute(String[] parameters) {
		String videoFileName = parameters[0];

		VideoMediaPanel videoMediaPanel = mediaPanelMap.get(videoFileName);
		if (videoMediaPanel == null) {
			videoMediaPanel = new VideoMediaPanel();
			mediaPanelMap.put(videoFileName, videoMediaPanel);
			
			try {
				URL url = new File(mediaDir + "/" + videoFileName).toURI().toURL();
				videoMediaPanel.realize(url);
				mediaPanelMap.put(videoFileName, videoMediaPanel);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return "Video Media PreLoaded";
	}


	
	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

}
