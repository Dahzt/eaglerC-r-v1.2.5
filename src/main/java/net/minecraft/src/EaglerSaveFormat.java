package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.PeytonPlayz585.fileutils.File;
import net.PeytonPlayz585.fileutils.FileEntry;
import net.PeytonPlayz585.fileutils.FilesystemUtils;

public class EaglerSaveFormat implements ISaveFormat {
	
	private String saveDir;
	
	public EaglerSaveFormat(String s) {
		this.saveDir = s;
	}

	@Override
	public String getFormatName() {
		return "Modded eagler SaveFormat";
	}

	@Override
	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		if(var1.contains("saves")) {
			var1 = var1.replace("saves/", "");
		}
		return new EaglerSaveHandler(File.makePath(this.saveDir, var1));
	}

	@Override
	public List<SaveFormatComparator> getSaveList() {
		ArrayList<SaveFormatComparator> lst = new ArrayList<>();
		File.listFilesAndDirectories(saveDir).forEach(new Consumer<FileEntry>() {
			@Override
			public void accept(FileEntry t) {
				if(!t.isDirectory) {
					return;
				}
				String folderName = t.getName();
				String dir = t.path;
				byte[] lvl = File.readFile(File.makePath(t.path, "/level.dat"));
				//byte[] lvl = File.readFile(dir + "/level.dat");
				if(lvl != null) {
					try {
						NBTBase nbt = NBTBase.readNamedTag(new DataInputStream(new ByteArrayInputStream(lvl)));
						if(nbt instanceof NBTTagCompound) {
							WorldInfo w =  new WorldInfo((NBTTagCompound)nbt);
							String s1 = w.getWorldName();
							if (s1 == null || MathHelper.stringNullOrLengthZero(s1)) {
								s1 = folderName;
							}
							lst.add(new SaveFormatComparator(folderName, s1, w.getLastTimePlayed(), w.getSizeOnDisk(), w.getGameType(), false, w.isHardcoreModeEnabled()));
						}else {
							throw new IOException("file '" + dir + "/level.dat' does not contain an NBTTagCompound");
						}
					}catch(IOException e) {
						System.err.println("Failed to load world data for '" + dir + "/level.dat'");
						System.err.println("It will be kept for future recovery");
						e.printStackTrace();
					}
				}
			}
		});
		return lst;
	}

	@Override
	public void flushCache() {
	}

	@Override
	public WorldInfo getWorldInfo(String var1) {
		String file1 = File.makePath(this.saveDir, var1);
		String file = File.makePath(file1, "level.dat");
		byte[] lvl = File.readFile(file);
		if(lvl != null) {
			try {
				NBTBase nbt = NBTBase.readNamedTag(new DataInputStream(new ByteArrayInputStream(lvl)));
				if(nbt instanceof NBTTagCompound) {
					return new WorldInfo((NBTTagCompound)nbt);
				}else {
					throw new IOException("file '" + file + "' does not contain an NBTTagCompound");
				}
			}catch(IOException e) {
				System.err.println("Failed to load world data for '" + file + "'");
				System.err.println("It will be kept for future recovery");
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void deleteWorldDirectory(String var1) {
		String path = saveDir + "/" + var1;
		if(path.contains("/saves/saves/")) {
			path = path.replace("/saves/saves/", "/saves/");
		}
		FilesystemUtils.deleteDirectory(path);
	}

	@Override
	public void renameWorld(String var1, String var2) {
		// TODO fix the fucking renaming system
		
		
//		if(var1.contains("/saves/saves/")) {
//			var1 = var1.replace("/saves/saves/", "/saves/");
//		}
//		if(var2.contains("/saves/saves/")) {
//			var2 = var2.replace("/saves/saves/", "/saves/");
//		}
//		byte[] lvl = File.readFile(saveDir + "/" + var1 + "/level.dat");
//		if(lvl != null) {
//			try {
//				NBTBase nbt = NBTBase.readNamedTag(new DataInputStream(new ByteArrayInputStream(lvl)));
//				if(nbt instanceof NBTTagCompound) {
//					NBTTagCompound w = (NBTTagCompound)nbt;
//					w.setString("LevelName", var2);
//					ByteArrayOutputStream out = new ByteArrayOutputStream(lvl.length + 16 + var2.length() * 2);
//					NBTBase.writeNamedTag(w, new DataOutputStream(out));
//					File.writeFile(saveDir + "/" + var1 + "/level.dat", out.toByteArray());
//				}else {
//					throw new IOException("file '" + saveDir + "/" + var1 + "/level.dat' does not contain an NBTTagCompound");
//				}
//			}catch(IOException e) {
//				System.err.println("Failed to load world data for '" + saveDir + "/level.dat'");
//				System.err.println("It will be kept for future recovery");
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public boolean isOldMapFormat(String var1) {
		return false;
	}

	@Override
	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}

}
