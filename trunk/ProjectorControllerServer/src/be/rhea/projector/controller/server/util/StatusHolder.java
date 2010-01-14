package be.rhea.projector.controller.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class StatusHolder implements Serializable {
	private static final String PROJECTOR_CONTROLLER_SETTINGS_FILENAME = "ProjectorControllerSettings.obj";
	private static final long serialVersionUID = 1L;
	private LimitedList recentlyUsedFiles = new LimitedList();
	private File lastAccessedDir = new File(System.getProperty("user.home"));
	private static transient StatusHolder statusHolder;
	
	public static StatusHolder getInstance() {
		if (statusHolder == null) {
			String homeDir = System.getProperty("user.home");
			File statusFile = new File(homeDir, PROJECTOR_CONTROLLER_SETTINGS_FILENAME);
			if (statusFile.exists()) {
				try {
					statusHolder = (StatusHolder) new ObjectInputStream(new FileInputStream(statusFile)).readObject();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
			} else {
				statusHolder = new StatusHolder();
			}
		}
		
		return statusHolder;
	}
	
	public static void saveSettings() {
		if (statusHolder != null) {
			String homeDir = System.getProperty("user.home");
			File statusFile = new File(homeDir, PROJECTOR_CONTROLLER_SETTINGS_FILENAME);
			try {
				new ObjectOutputStream(new FileOutputStream(statusFile)).writeObject(statusHolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setLastAccessedDir(File lastAccessedDir) {
		this.lastAccessedDir = lastAccessedDir;
	}

	public File getLastAccessedDir() {
		return lastAccessedDir;
	}

	public void setRecentlyUsedFiles(LimitedList recentlyUsedFiles) {
		this.recentlyUsedFiles = recentlyUsedFiles;
	}

	public LimitedList getRecentlyUsedFiles() {
		return recentlyUsedFiles;
	}

	public void addRecentlyUsedFile(File selectedFile) {
		recentlyUsedFiles.add(selectedFile);
	}
}
