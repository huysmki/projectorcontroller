package be.rhea.projector.controller.server.filefilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XMLFileFilter extends FileFilter {
	@Override
	public boolean accept(File file) {
		return file.getName().toLowerCase().endsWith(".xml") || file.isDirectory();
	}

	@Override
	public String getDescription() {
		return "ProjectorContoller Scenario (*.xml)";
	}
}
