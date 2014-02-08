package pl.vgtworld.games.wolffinder.model.texture;

import javax.swing.DefaultListModel;

public class TextureListModel
	extends DefaultListModel
	{
	private static final long serialVersionUID = 1L;
	public TextureListModel(TextureList textures)
		{
		for (Texture texture : textures)
			addElement(texture);
		}
	}
