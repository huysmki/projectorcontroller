package be.rhea.projector.controller.remote.commands.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import be.rhea.remote.client.SimpleProtocolClientStreamingCommand;

import com.Ostermiller.util.Base64;
import com.ice.tar.TarEntry;
import com.ice.tar.TarOutputStream;

public class PCPMediaTarFileTransferClientCommand implements SimpleProtocolClientStreamingCommand {

	private String md5Value;
	private File sourceDir;

	public void execute(OutputStream outputStream) {
		File[] filesToTransfer = sourceDir.listFiles();

		TarOutputStream tarOutputStream = new TarOutputStream(outputStream);

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA1");

			for (int i = 0; i < filesToTransfer.length; i++) {
				File file = filesToTransfer[i];
				TarEntry tarEntry;
				tarEntry = new TarEntry(file);
				tarEntry.setName(file.getName());
				tarOutputStream.putNextEntry(tarEntry);

				byte[] buffer = new byte[1024];
				int length;
				FileInputStream fileInputStream = new FileInputStream(file);
				while ((length = fileInputStream.read(buffer)) > 0) {
					digest.update(buffer, 0, length);
					tarOutputStream.write(buffer, 0, length);
				}
				fileInputStream.close();
				tarOutputStream.closeEntry();
			}
			tarOutputStream.flush();
			tarOutputStream.finish();
			tarOutputStream.close();

			md5Value = Base64.encode(new String(digest.digest()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String getHashOfFiles() {
		return md5Value;
	}

	public void setSourceDir(File sourceDir) {
		this.sourceDir = sourceDir;
	}

}
