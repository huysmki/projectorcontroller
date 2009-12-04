package be.rhea.projector.controller.remote.commands.server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.swing.JFrame;

import be.rhea.projector.controller.client.ui.VideoMediaPanel;
import be.rhea.remote.server.SimpleProtocolServerExecuteCommand;

public class PCPVideoMediaStartServerCommand implements
		SimpleProtocolServerExecuteCommand {
	
	private static final int MAX_WAIT_TIME_MILLIS = 3000;
	private final Map<String, VideoMediaPanel> mediaPanelMap;
	private String mediaDir;
	private final JFrame frame;

	public PCPVideoMediaStartServerCommand(JFrame frame, Map<String, VideoMediaPanel> mediaPanelMap) {
		this.frame = frame;
		this.mediaPanelMap = mediaPanelMap;
	}

	public String execute(String[] parameters) {
		try {
			String videoFileName = parameters[0];
	
			VideoMediaPanel videoMediaPanel = mediaPanelMap.get(videoFileName);
			
			if (videoMediaPanel == null) {
				videoMediaPanel = new VideoMediaPanel();
				URL url = new File(mediaDir + "/" + parameters[0]).toURI().toURL();
				videoMediaPanel.realize(url);
			}
			
			int teller = 0;
			while (teller++ < MAX_WAIT_TIME_MILLIS && !videoMediaPanel.isRealized()) {
				Thread.sleep(1);
			}
			
			frame.getContentPane().removeAll();
			frame.getContentPane().add(videoMediaPanel);
			videoMediaPanel.play();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Video Media Playing";
	}
	
	public void setMediaDir(String mediaDir) {
		this.mediaDir = mediaDir;
	}

}
