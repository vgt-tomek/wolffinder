package pl.vgtworld.games.wolffinder.model;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import pl.vgtworld.games.wolffinder.data.Constants;

public class MapFileChooser
	extends JFileChooser
	{
	private static final long serialVersionUID = 1L;
	public MapFileChooser()
		{
		super();
		setMapFileFilter();
		}
	public MapFileChooser(File currentDirectory)
		{
		super(currentDirectory);
		setMapFileFilter();
		}
	public MapFileChooser(String currentDirectoryPath)
		{
		super(currentDirectoryPath);
		setMapFileFilter();
		}
	@Override public File getSelectedFile()
		{
		File file = super.getSelectedFile();
		if (file == null)
			return file;
		if (!file.getAbsolutePath().toLowerCase().endsWith("."+Constants.mapFileExtension))
			file = new File(file.getParentFile(), file.getName()+"."+Constants.mapFileExtension);
		return file;
		}
	private void setMapFileFilter()
		{
		setFileFilter(new MapFileFilter());
		}
	private class MapFileFilter
		extends FileFilter
		{
		@Override public boolean accept(File file)
			{
			if (file.isDirectory())
				return true;
			if (file.getAbsolutePath().toLowerCase().endsWith("."+Constants.mapFileExtension))
				return true;
			return false;
			}
		@Override public String getDescription()
			{
			return "Wolffinder map file";
			}
		}
	}
