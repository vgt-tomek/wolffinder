package pl.vgtworld.games.wolffinder.model;

import java.util.MissingResourceException;

public class ResourceBundle
	{
	private java.util.ResourceBundle rb = null;
	public ResourceBundle(String baseName)
		{
		rb = java.util.ResourceBundle.getBundle(baseName);
		}
	public String getString(String key)
		{
		try
			{
			return rb.getString(key);
			}
		catch (MissingResourceException e)
			{
			return key;
			}
		}
	}
