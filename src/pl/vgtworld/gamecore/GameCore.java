package pl.vgtworld.gamecore;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class GameCore
	implements Runnable
	{
	protected ScreenManager screenManager = null;
	protected InputManager inputManager = null;
	private static final int DEFAULT_WIDTH = 640;
	private static final int DEFAULT_HEIGHT = 480;
	private int screenWidth;
	private int screenHeight;
	private int ticks;
	private int maxFrameLimit;
	private boolean isRunning;
	private long updateTime;
	private long drawTime;
	private long ticksCount;
	private long framesCount;
	private long timeCount;
	public GameCore()
		{
		screenWidth = DEFAULT_WIDTH;
		screenHeight = DEFAULT_HEIGHT;
		screenManager = new ScreenManager();
		}
	public ScreenManager getScreenManager()
		{
		return screenManager;
		}
	public InputManager getInputManager()
		{
		return inputManager;
		}
	public void setSize(int width, int height)
		{
		screenWidth = width;
		screenHeight = height;
		screenManager = new ScreenManager(screenWidth, screenHeight);
		}
	public void setTicks(int ticks)
		{
		this.ticks = ticks;
		}
	public void setMaxFps(int fps)
		{
		maxFrameLimit = fps;
		}
	public void createInputManager()
		{
		inputManager = new InputManager();
		}
	@Override public void run()
		{
		init();
		mainLoop();
		}
	public void stop()
		{
		isRunning = false;
		}
	protected void init()
		{
		isRunning = true;
		}
	protected void mainLoop()
		{
		long tickOverhead = 0;
		int avgTickLength = 0;
		if (ticks > 0)
			avgTickLength = 1000 / ticks;
		long renderOverhead = 0;
		int avgRenderLength = 0;
		if (maxFrameLimit > 0)
			avgRenderLength = Math.round(1000f / maxFrameLimit);
		long lastFrameDrawn = 0;
		
		long loopStartTime = System.currentTimeMillis();
		while (isRunning)
			{
			long buf;
			long elapsedTime = System.currentTimeMillis() - loopStartTime;
			loopStartTime+= elapsedTime;
			tickOverhead-= avgTickLength;
			//liczenie fps i tps
			++ticksCount;
			timeCount+= elapsedTime;
			if (timeCount > 5000)
				{
				float fps = framesCount / (timeCount / 1000f);
				float tps = ticksCount / (timeCount / 1000f);
				timeCount-= 2000;
				framesCount-= (2 * fps);
				ticksCount-= (2 * tps);
				}
			
			//update
			buf = System.currentTimeMillis();
			update(elapsedTime);
			updateTime = System.currentTimeMillis() - buf;
			
			//render
			boolean rendered = false;
			if (
				(ticks > 0 && tickOverhead + updateTime + drawTime <= 0)
				|| ticks == 0
				|| loopStartTime - lastFrameDrawn > 2000
				)
				{
				rendered = true;
				if (renderOverhead + drawTime >= 0)
					{
					lastFrameDrawn = System.currentTimeMillis();
					renderOverhead-= avgRenderLength;
					++framesCount;
					buf = System.currentTimeMillis();
					Graphics2D g = screenManager.getGraphics();
					draw(g);
					g.dispose();
					screenManager.update();
					drawTime = System.currentTimeMillis() - buf;
					}
				}
			
			//sleep
			long sleep = 0;
			if (ticks > 0)
				{
				if (rendered)
					sleep = avgTickLength - (System.currentTimeMillis() - loopStartTime);
				}
			else if (maxFrameLimit > 0)
				{
				sleep = avgRenderLength - (System.currentTimeMillis() - loopStartTime);
				}
			try
				{
				if (sleep > 0)
					Thread.sleep(sleep);
				}
			catch (InterruptedException e)
				{}
			tickOverhead+= (System.currentTimeMillis() - loopStartTime);
			renderOverhead+= (System.currentTimeMillis() - loopStartTime);
			}
		}
	protected void drawDebug(Graphics2D g)
		{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 120, 60);
		g.setColor(Color.GREEN);
		g.drawString("update: " + updateTime + " ms", 5, 12);
		g.drawString("draw: " + drawTime + " ms", 5, 24);
		float fps;
		float tps;
		if (timeCount / 10000f == 0)
			{
			fps = 0;
			tps = 0;
			}
		else
			{
			fps = (float)((int)(framesCount / (timeCount / 10000f)) / 10f);
			tps = (float)((int)(ticksCount / (timeCount / 10000f)) / 10f);
			}
		g.drawString("FPS: " + fps, 5, 36);
		g.drawString("TPS: " + tps, 5, 48);
		}
	public abstract void update(long elapsedTime);
	public abstract void draw(Graphics2D g);
	}
