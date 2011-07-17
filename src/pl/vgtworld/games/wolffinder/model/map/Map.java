package pl.vgtworld.games.wolffinder.model.map;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import pl.vgtworld.gamecore.Sprite;
import pl.vgtworld.games.wolffinder.model.texture.Texture;
import pl.vgtworld.games.wolffinder.model.texture.TextureList;


public class Map
	implements Serializable
	{
	private static final long serialVersionUID = 1L;
	private int mapWidth = 0;
	private int mapHeight = 0;
	private int[][] walls = null;
	private Point startPosition = new Point(-1, -1);
	private Point endPosition = new Point(-1, -1);
	private Color floorColor = new Color(150, 150, 150);
	private Color ceilingColor = new Color(200, 200, 200);
	private TextureList textures = new TextureList();
	private ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	transient private static Texture nullTexture = null;
	static
		{
		nullTexture = getNullTexture();
		}
	public Map(int width, int height)
		{
		if (width <= 0)
			throw new IllegalArgumentException("width must be greater than 0");
		if (height <= 0)
			throw new IllegalArgumentException("height must be greater than 0");
		mapWidth = width;
		mapHeight = height;
		walls = new int[width][height];
		clearWalls();
		}
	public int getWidth()
		{
		return mapWidth;
		}
	public int getHeight()
		{
		return mapHeight;
		}
	public Point getStartPosition()
		{
		return startPosition;
		}
	public Point getEndPosition()
		{
		return endPosition;
		}
	public Color getFloorColor()
		{
		return floorColor;
		}
	public Color getCeilingColor()
		{
		return ceilingColor;
		}
	public int getWall(int x, int y)
		{
		return walls[x][y];
		}
	public ArrayList<Sprite> getSprites()
		{
		return sprites;
		}
	public BufferedImage getWallTextureThumb(int x, int y)
		{
		return getWallTextureThumb(getWall(x, y));
		}
	public BufferedImage getWallTextureThumb(int index)
		{
		return textures.get(index).getThumb();
		}
	public BufferedImage getWallTextureSlice(int x, int y, double offset)
		{
		int index = getWall(x, y);
		Texture texture = textures.get(index);
		if (texture == null)
			{
			if (nullTexture != null)
				return nullTexture.getSlice(offset);
			return null;
			}
		else
			return textures.get(index).getSlice(offset);
		}
	public TextureList getTextures()
		{
		return textures;
		}
	public void setStartPosition(int x, int y)
		{
		startPosition.x = x;
		startPosition.y = y;
		}
	public void setEndPosition(int x, int y)
		{
		endPosition.x = x;
		endPosition.y = y;
		}
	public void setFloorColor(Color color)
		{
		floorColor = color;
		}
	public void setCeilingColor(Color color)
		{
		ceilingColor = color;
		}
	public void setWall(int x, int y, int index)
		{
		walls[x][y] = index;
		}
	public void setEndPositionSprite(Sprite sprite)
		{
		sprite.setX((float)getEndPosition().getX() + 0.5f);
		sprite.setY((float)getEndPosition().getY() + 0.5f);
		sprites.add(sprite);
		}
	public void addTexture(Texture texture)
		{
		textures.add(texture);
		}
	public void deleteTexture(int index)
		{
		textures.remove(index);
		for (int i = 0; i < walls.length; ++i)
			for (int j = 0; j < walls[i].length; ++j)
				{
				if (walls[i][j] == index)
					walls[i][j] = -1;
				else if (walls[i][j] > index)
					--walls[i][j];
				}
		}
	private void clearWalls()
		{
		for (int i = 0; i < walls.length; ++i)
			for (int j = 0; j < walls[i].length; ++j)
				walls[i][j] = -1;
		}
	private static Texture getNullTexture()
		{
		BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < image.getWidth(); ++i)
			for (int j = 0; j < image.getHeight(); ++j)
				{
				int rgb = i^j;
				rgb = rgb | rgb << 8 | rgb << 16;
				image.setRGB(i, j, rgb);
				}
		try
			{
			Texture texture = new Texture(image);
			return texture;
			}
		catch (IOException e)
			{
			return null;
			}
		}
	}
