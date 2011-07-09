package pl.vgtworld.games.wolffinder.gui.editor;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import pl.vgtworld.games.wolffinder.gui.mappanel.MapPanel;
import pl.vgtworld.games.wolffinder.model.map.Map;

public class EditorWindow
	extends JFrame
	{
	public static final int EDIT_MODE_PUT = 1;
	public static final int EDIT_MODE_REMOVE = 2;
	public static final int EDIT_MODE_START = 3;
	public static final int EDIT_MODE_FINISH = 4;
	private static final long serialVersionUID = 1L;
	private int currentMode = EDIT_MODE_PUT;
	private Menu menu = null;
	private Textures textures = null;
	private Map map = null;
	private MapPanel mapPanel = null;
	private boolean mapModified = false;
	public EditorWindow()
		{
		menu = new Menu(this);
		textures = new Textures(this);
		mapPanel = new MapPanel();
		WallBuilderMouseListener listener = new WallBuilderMouseListener();
		mapPanel.addMouseMotionListener(listener);
		mapPanel.addMouseListener(listener);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		
		buildGUI();
		}
	public Map getMap()
		{
		return map;
		}
	public boolean isModified()
		{
		return mapModified;
		}
	public void setModified()
		{
		mapModified = true;
		}
	public void setCurrentMode(int mode)
		{
		currentMode = mode;
		}
	void createNewMap(int width, int height)
		{
		map = new Map(width, height);
		mapPanel.setMap(map);
		mapPanel.repaint();
		textures.setTextures(map.getTextures());
		textures.setActive(true);
		menu.setActive(true);
		mapModified = false;
		}
	void loadMap(Map map)
		{
		this.map = map;
		mapPanel.setMap(map);
		mapPanel.repaint();
		textures.setTextures(map.getTextures());
		textures.setActive(true);
		menu.setActive(true);
		mapModified = false;
		}
	private void buildGUI()
		{
		add(menu, BorderLayout.PAGE_START);
		add(textures, BorderLayout.LINE_END);
		add(mapPanel, BorderLayout.CENTER);
		}
	private class WallBuilderMouseListener
		extends MouseAdapter
		{
		private int button;
		@Override public void mouseDragged(MouseEvent event)
			{
			clickProcess();
			}
		@Override public void mousePressed(MouseEvent event)
			{
			button = event.getButton();
			clickProcess();
			}
		private void clickProcess()
			{
			if (button != MouseEvent.BUTTON1)
				return;
			if (mapPanel.getMapX() < 0 || mapPanel.getMapY() < 0 || mapPanel.getMapX() >= map.getWidth() || mapPanel.getMapY() >= map.getHeight())
				return;
			if (currentMode == EDIT_MODE_PUT)
				{
				int textureIndex = textures.getSelectedTextureIndex();
				if (textureIndex == -1)
					{
					JOptionPane.showMessageDialog(EditorWindow.this, "Texture not selected", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				int currentIndex = map.getWall(mapPanel.getMapX(), mapPanel.getMapY());
				if (currentIndex != textureIndex)
					{
					setModified();
					map.setWall(mapPanel.getMapX(), mapPanel.getMapY(), textureIndex);
					mapPanel.repaint();
					}
				}
			else if (currentMode == EDIT_MODE_REMOVE)
				{
				int currentIndex = map.getWall(mapPanel.getMapX(), mapPanel.getMapY());
				if (currentIndex != -1)
					{
					setModified();
					map.setWall(mapPanel.getMapX(), mapPanel.getMapY(), -1);
					mapPanel.repaint();
					}
				}
			else if (currentMode == EDIT_MODE_START)
				{
				int currentIndex = map.getWall(mapPanel.getMapX(), mapPanel.getMapY());
				if (currentIndex == -1)
					{
					map.setStartPosition(mapPanel.getMapX(), mapPanel.getMapY());
					}
				}
			else if (currentMode == EDIT_MODE_FINISH)
				{
				int currentIndex = map.getWall(mapPanel.getMapX(), mapPanel.getMapY());
				if (currentIndex == -1)
					{
					map.setEndPosition(mapPanel.getMapX(), mapPanel.getMapY());
					}
				}
			}
		}
	}
