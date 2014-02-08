package pl.vgtworld.gamecore;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class ScreenManager
	{
	private int width = 0;
	private int height = 0;
	private JComponent canvas = new Canvas();
	private BufferedImage frontBuffer = null;
	private BufferedImage backBuffer = null;
	public ScreenManager()
		{
		recreateBuffers();
		
		canvas.addComponentListener(new ComponentResize());
		}
	public ScreenManager(int prefferedWidth, int prefferedHeight)
		{
		width = prefferedWidth;
		height = prefferedHeight;
		
		recreateBuffers();
		
		canvas.addComponentListener(new ComponentResize());
		}
	public Graphics2D getGraphics()
		{
		return (Graphics2D)backBuffer.getGraphics();
		}
	public int getWidth()
		{
		return width;
		}
	public int getHeight()
		{
		return height;
		}
	public JComponent getCanvas()
		{
		return canvas;
		}
	public void update()
		{
		Graphics2D g = (Graphics2D)frontBuffer.getGraphics();
		g.drawImage(backBuffer, 0, 0, frontBuffer.getWidth(), frontBuffer.getHeight(), null);
		canvas.repaint();
		}
	private void recreateBuffers()
		{
		if (width > 0 || height > 0)
			{
			frontBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}
		else
			{
			frontBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			backBuffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
			}
		}
	private class ComponentResize
		extends ComponentAdapter
		{
		@Override public void componentResized(ComponentEvent event)
			{
			width = canvas.getWidth();
			height = canvas.getHeight();
			recreateBuffers();
			}
		}
	private class Canvas
		extends JComponent
		{
		private static final long serialVersionUID = 1L;
		@Override public void paintComponent(Graphics g)
			{
			if (frontBuffer != null)
				g.drawImage(frontBuffer, 0, 0, getWidth(), getHeight(), 0, 0, frontBuffer.getWidth(), frontBuffer.getHeight(), null);
			}
		}
	}
