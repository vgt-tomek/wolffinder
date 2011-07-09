package pl.vgtworld.games.wolffinder.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import pl.vgtworld.games.wolffinder.model.map.Map;

public class MapPanel
	extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private Map map = null;
	private int xOffset;
	private int yOffset;
	private int xMousePosition = -1;
	private int yMousePosition = -1;
	private int sectorSize;
	private Color colorEmptyField = new Color(200, 200, 200);
	private Color colorHighlight = new Color(255, 0, 0);
	private Color colorGrid = new Color(160, 160, 160);
	public MapPanel()
		{
		this(null);
		}
	public MapPanel(Map map)
		{
		MapPanelMouseAdapter listener = new MapPanelMouseAdapter();
		addMouseMotionListener(listener);
		addMouseListener(listener);
		addMouseWheelListener(listener);
		setMap(map);
		}
	public int getMapX()
		{
		return (xMousePosition - xOffset) / sectorSize;
		}
	public int getMapY()
		{
		return (yMousePosition - yOffset) / sectorSize;
		}
	public void setMap(Map map)
		{
		this.map = map;
		xOffset = 0;
		yOffset = 0;
		sectorSize = 64;
		}
	@Override protected void paintComponent(Graphics g)
		{
		super.paintComponent(g);
		if (map != null)
			{
			int drawableLeftMapCoord = -(xOffset / sectorSize);
			int drawableTopMapCoord = -(yOffset / sectorSize);
			int xDrawStart = xOffset % sectorSize;
			int yDrawStart = yOffset % sectorSize;
			if (xDrawStart > 0)
				{
				xDrawStart-= sectorSize;
				--drawableLeftMapCoord;
				}
			if (yDrawStart > 0)
				{
				yDrawStart-= sectorSize;
				--drawableTopMapCoord;
				}
			
			int xScreen = xDrawStart;
			int yScreen = yDrawStart;
			int xMap = drawableLeftMapCoord;
			int yMap = drawableTopMapCoord;
			while (xScreen < getWidth())
				{
				yScreen = yDrawStart;
				yMap = drawableTopMapCoord;
				while (yScreen < getHeight())
					{
					if (xMap >= 0 && xMap < map.getWidth() && yMap >= 0 && yMap < map.getHeight())
						{
						g.setColor(colorGrid);
						g.drawRect(xScreen, yScreen, sectorSize, sectorSize);
						
						int wallIndex = map.getWall(xMap, yMap);
						if (wallIndex == -1)
							{
							g.setColor(colorEmptyField);
							g.fillRect(xScreen + 2, yScreen + 2, sectorSize - 3, sectorSize - 3);
							}
						else
							{
							BufferedImage texture = map.getTextures().get(wallIndex).getThumb();
							g.drawImage(
									texture,
									xScreen + 2,
									yScreen + 2,
									xScreen + 2 + (sectorSize - 3),
									yScreen + 2 + (sectorSize - 3),
									0,
									0,
									texture.getWidth(),
									texture.getHeight(),
									null
									);
							}
						
//						if (xMapHighlighted == xMap && yMap == yMapHighlighted)
						if (getMapX() == xMap && yMap == getMapY())
							{
							g.setColor(colorHighlight);
							g.drawRect(xScreen + 1, yScreen + 1, sectorSize - 2, sectorSize - 2);
							}
						}
					yScreen+= sectorSize;
					++yMap;
					}
				xScreen+= sectorSize;
				++xMap;
				}
			}
		}
	private class MapPanelMouseAdapter
		extends MouseAdapter
		{
		private Point startPoint = new Point(-1, -1);
		private Point endPoint = new Point(-1, -1);
		private boolean doDrag = false;
		@Override public void mouseEntered(MouseEvent event)
			{
			xMousePosition = event.getX();
			yMousePosition = event.getY();
			repaint();
			}
		@Override public void mouseExited(MouseEvent event)
			{
			xMousePosition = -1;
			yMousePosition = -1;
			repaint();
			}
		@Override public void mouseMoved(MouseEvent event)
			{
			xMousePosition = event.getX();
			yMousePosition = event.getY();
			startPoint.setLocation(event.getX(), event.getY());
			repaint();
			}
		@Override public void mouseWheelMoved(MouseWheelEvent e)
			{
			int mapX = getMapX();
			int mapY = getMapY();
			float xSquareOffset = ((xMousePosition - xOffset) % sectorSize) / (float)sectorSize;
			float ySquareOffset = ((yMousePosition - yOffset) % sectorSize) / (float)sectorSize;
			
			if (e.getWheelRotation() < 0)
				{
				sectorSize+= sectorSize / 4;
				if (sectorSize < 4)
					++sectorSize;
				}
			else
				{
				sectorSize-= sectorSize / 4;
				if (sectorSize < 4 && sectorSize > 2)
					--sectorSize;
				}
			
			xOffset = xMousePosition - (mapX * sectorSize + (int)(xSquareOffset * sectorSize));
			yOffset = yMousePosition - (mapY * sectorSize + (int)(ySquareOffset * sectorSize));
			repaint();
			}
		@Override public void mousePressed(MouseEvent event)
			{
			if (event.getButton() == MouseEvent.BUTTON3)
				doDrag = true;
			else
				doDrag = false;
			}
		@Override public void mouseDragged(MouseEvent event)
			{
			dragOffset(event);
			repaint();
			}
		@Override public void mouseReleased(MouseEvent event)
			{
			dragOffset(event);
			repaint();
			}
		private void dragOffset(MouseEvent event)
			{
			endPoint.setLocation(event.getX(), event.getY());
			int dx = (int)endPoint.getX() - (int)startPoint.getX();
			int dy = (int)endPoint.getY() - (int)startPoint.getY();
			startPoint.setLocation(endPoint);
			xMousePosition+=dx;
			yMousePosition+=dy;
			if (doDrag)
				{
				xOffset+=dx;
				yOffset+=dy;
				}
			}
		}
	}