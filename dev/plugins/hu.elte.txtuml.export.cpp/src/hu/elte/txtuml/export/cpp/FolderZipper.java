package hu.elte.txtuml.export.cpp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FolderZipper {
	
	private List<String> destinationToZip;
	private File destZipFile;
	
	public FolderZipper(File destZipFile) {
		destinationToZip = new ArrayList<String>();
		this.destZipFile = destZipFile;
		
	}
	
	public void addDestinationToZip(File dest) {
		destinationToZip.add(dest.getAbsolutePath());
	}
	
	public void zipFolder() throws Exception  {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		fileWriter = new FileOutputStream(destZipFile.getAbsolutePath());
		zip = new ZipOutputStream(fileWriter);
		for(String srcDest : destinationToZip) {
			addFileToZip("", srcDest, zip);
		}		
		zip.flush();
		zip.close();
	}
	

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path.equals("") ? folder.getName() : path + File.separator + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
			
			in.close();
		}
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
			}
		}
	}
}
