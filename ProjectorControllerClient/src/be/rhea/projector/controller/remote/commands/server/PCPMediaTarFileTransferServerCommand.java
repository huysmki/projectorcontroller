package be.rhea.projector.controller.remote.commands.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import be.rhea.remote.server.SimpleProtocolServerStreamingCommand;

import com.Ostermiller.util.Base64;
import com.ice.tar.TarEntry;
import com.ice.tar.TarInputStream;

public class PCPMediaTarFileTransferServerCommand implements
	SimpleProtocolServerStreamingCommand {

	private String mediaDir;


	public String execute(InputStream inputStream) {
		File toDirAsFile = new File(mediaDir);
		if (!toDirAsFile.exists()) {
			return "Cannot find media dir " + toDirAsFile;
		}

		TarInputStream tarInputStream = new TarInputStream(new BufferedInputStream(inputStream));
		String md5Value = "";
		TarEntry entry;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");

			while ((entry = tarInputStream.getNextEntry()) != null) {
				byte[] buffer = new byte[1024];
				int length;
				
				FileOutputStream fileOutputStream = new FileOutputStream(new File(mediaDir, entry.getName()));
				
				while ((length = tarInputStream.read(buffer)) > 0) {
					digest.update(buffer, 0, length);
					fileOutputStream.write(buffer, 0, length);
				}
				fileOutputStream.close();
			}
			md5Value = Base64.encode(new String(digest.digest()));
		} catch (FileNotFoundException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		} catch (NoSuchAlgorithmException e) {
			return e.getMessage();
		}
		return md5Value;
	}


	public void setMediaDir(String toDir) {
		this.mediaDir = toDir;
	}


		

}
