package pl.vgtworld.games.wolffinder.gui.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pl.vgtworld.games.wolffinder.model.texture.Texture;
import pl.vgtworld.games.wolffinder.model.texture.TextureList;
import pl.vgtworld.games.wolffinder.model.texture.TextureListCellRenderer;
import pl.vgtworld.games.wolffinder.model.texture.TextureListModel;

public class Textures
	extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private EditorWindow parent = null;
	private JButton addTexture = new JButton();
	private JButton deleteTexture = new JButton();
	private JList textureList = new JList();
	private TextureListModel model = null;
	private JFileChooser fileChooser = new JFileChooser();
	public Textures(EditorWindow parent)
		{
		this.parent = parent;
		setPreferredSize(new Dimension(200, 100));
		
		addTexture.setText("Add");
		addTexture.addActionListener(new ActionAddTexture());
		deleteTexture.setText("Delete");
		deleteTexture.addActionListener(new ActionDeleteTexture());
		textureList.addMouseListener(new TextureListMouseAdapter());
		textureList.setCellRenderer(new TextureListCellRenderer());
		setActive(false);
		
		buildGUI();
		}
	public int getSelectedTextureIndex()
		{
		return textureList.getSelectedIndex();
		}
	public void setTextures(TextureList textures)
		{
		model = new TextureListModel(textures);
		textureList.setModel(model);
		}
	public void setActive(boolean state)
		{
		if (state == true)
			{
			addTexture.setEnabled(true);
			deleteTexture.setEnabled(false);
			}
		else
			{
			addTexture.setEnabled(false);
			deleteTexture.setEnabled(false);
			}
		repaint();
		}
	private void buildGUI()
		{
		JPanel buttonsContainer = new JPanel();
		
		setLayout(new BorderLayout());
		
		buttonsContainer.add(addTexture);
		buttonsContainer.add(deleteTexture);
		
		add(buttonsContainer, BorderLayout.PAGE_START);
		add(new JScrollPane(textureList), BorderLayout.CENTER);
		}
	private class ActionAddTexture
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			int choice = fileChooser.showOpenDialog(parent);
			if (choice == JFileChooser.APPROVE_OPTION)
				{
				try
					{
					Texture texture = new Texture(fileChooser.getSelectedFile());
					parent.getMap().addTexture(texture);
					model.addElement(texture);
					parent.setModified();
					}
				catch (IOException e)
					{
					JOptionPane.showMessageDialog(parent, "Can't open file", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	private class ActionDeleteTexture
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			int index = textureList.getSelectedIndex();
			if (index == -1)
				return;
			parent.getMap().deleteTexture(index);
			model.removeElementAt(index);
			deleteTexture.setEnabled(false);
			parent.setModified();
			}
		}
	private class TextureListMouseAdapter
		extends MouseAdapter
		{
		@Override public void mouseClicked(MouseEvent event)
			{
			if (textureList.isSelectionEmpty())
				deleteTexture.setEnabled(false);
			else
				deleteTexture.setEnabled(true);
			}
		}
	}
