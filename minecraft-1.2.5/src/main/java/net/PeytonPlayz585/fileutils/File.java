package net.PeytonPlayz585.fileutils;

import java.util.ArrayList;
import java.util.Collection;

public class File {
	
	public static final boolean fileExists(String path) {
		return FSImpl.fileExists(path);
	}
	
	public static final boolean directoryExists(String path) {
		return FSImpl.directoryExists(path);
	}
	
	public static final boolean pathExists(String path) {
		return FSImpl.pathExists(path);
	}
	
	public static final boolean exists(String path) {
		return FSImpl.exists(path);
	}
	
	public static final void mkdir(String path) {
		FSImpl.mkdir(path);
	}
	
	public static final void writeFile(String path, byte[] data) {
		FSImpl.writeFile(path, data);
	}
	
	public static final byte[] readFile(String path) {
		return FSImpl.readFile(path);
	}
	
	public static final long getLastModified(String path) {
		return FSImpl.getLastModified(path);
	}
	
	public static final int getFileSize(String path) {
		return FSImpl.getFileSize(path);
	}
	
	public static final void renameFile(String oldPath, String newPath) {
		FSImpl.renameFile(oldPath, newPath);
	}
	
	public static final void copyFile(String oldPath, String newPath) {
		FSImpl.copyFile(oldPath, newPath);
	}
	
	public static final void deleteFile(String path) {
		FSImpl.deleteFile(path);
	}

	public static final Collection<FileEntry> listFiles(String path, boolean listDirs, boolean recursiveDirs) {
		return FSImpl.listFiles(path, listDirs, recursiveDirs);
	}
	
	public static final Collection<FileEntry> listFilesAndDirectories(String path) {
		return listFiles(path, true, false);
	}
	
	public static final Collection<FileEntry> listFilesRecursive(String path) {
		return listFiles(path, false, true);
	}
	
	public static final FileEntry[] listFiles(String path) {
		Collection<FileEntry> entries = listFilesAndDirectories(path);
		FileEntry[] entryArray = new FileEntry[entries.size()];
		
		int i = 0;
		for(FileEntry entry : entries) {
			entryArray[i] = entry;
			i = i + 1;
		}
		return entryArray;
	}
	
	public static boolean isCompressed(byte[] b) {
		if(b == null || b.length < 2) {
			return false;
		}
		return (b[0] == (byte) 0x1F) && (b[1] == (byte) 0x8B);
	}
	
	public static String makePath(String var3, String string) {
		if(string.startsWith("/")) {
			throw new IllegalArgumentException("Child path cannot start with '/'");
		}
		if(var3.endsWith("/")) {
			throw new IllegalArgumentException("Parent path cannot end with '/'");
		}
		return var3 + "/" + string;
	}
	
	static {
		if(FSImpl.openDaFileSystem("_net_PeytonPlayz585_eaglercraft_IndexedDBFilesystem_1_2_5") != FSImpl.OpenState.OPENED) {
			//Trigger eagler's incompatible screen with IndexedDB error
		}
	}
}
