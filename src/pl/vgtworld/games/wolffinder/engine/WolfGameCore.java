package pl.vgtworld.games.wolffinder.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import pl.vgtworld.gamecore.Action;
import pl.vgtworld.gamecore.GameCore;
import pl.vgtworld.games.wolffinder.model.map.Map;


public class WolfGameCore
	extends GameCore
	{
	private static final double MINIMUM_WALL_DISTANCE = 0.1f;
	private static final double MOVEMENT_SPEED = 2 / 1000d;
	private static final double ROTATE_SPEED = 360 / 4000d;
	private GraphicEngine gfxEngine = new GraphicEngine();
	private Map map = null;
	private Point2D position = new Point2D.Double();
	private float angle = 0;
	private long counter = 0;
	private boolean drawDebug = false;
	private boolean finished = false;
	private ArrayList<Action> actions = new ArrayList<Action>();
	Action actionForward = new Action();
	Action actionBack = new Action();
	Action actionLeft = new Action();
	Action actionRight = new Action();
	Action actionMoveSideways = new Action();
	Action actionRun = new Action();
	Action actionDebug = new Action(Action.TYPE_INITIAL_PRESS_ONLY);
	public WolfGameCore()
		{
		createInputManager();
		actions.add(actionForward);
		actions.add(actionBack);
		actions.add(actionLeft);
		actions.add(actionRight);
		actions.add(actionMoveSideways);
		actions.add(actionRun);
		actions.add(actionDebug);
		}
	public void setMap(Map map)
		{
		this.map = map;
		gfxEngine.setMap(map);
		position.setLocation(map.getStartPosition().getX() + 0.5, map.getStartPosition().getY() + 0.5);
		angle = 0;
		counter = 0;
		finished = false;
		}
	@Override public void update(long elapsedTime)
		{
		double x = position.getX();
		double y = position.getY();
		
		keysActions(elapsedTime);
		if (map.getWall((int)position.getX(), (int)position.getY()) > 0)
			position.setLocation(x, y);
		else
			wallCollisions();
		if (!finished)
			{
			if (isAnyKeyPressed() && counter == 0)
				counter = 1;
			if (counter > 0)
				counter+= elapsedTime;
			
			if ((int)x == (int)map.getEndPosition().getX() && (int)y == (int)map.getEndPosition().getY())
				finished = true;
			}
		}
	@Override public void draw(Graphics2D g)
		{
		gfxEngine.setLookAngle(angle);
		gfxEngine.setPosition(position.getX(), position.getY());
		gfxEngine.draw(g, screenManager.getWidth(), screenManager.getHeight());
		float time = (counter / 100) / 10.0f;
		if (finished)
			drawFinished(g, time);
		else
			g.drawString("" + time, 0, 10);
		if (drawDebug)
			drawDebug(g);
		}
	private void drawFinished(Graphics2D g, float time)
		{
		Font fontFinished = new Font("Arial", Font.BOLD, 30);
		g.setFont(fontFinished);
		g.setColor(Color.WHITE);
		g.drawString("Wolf found", (screenManager.getWidth() - "Wolf found".length() * 13) / 2, screenManager.getHeight() / 2 - 20);
		String yourTime = "Your time is " + time + " seconds"; 
		g.drawString(yourTime, (screenManager.getWidth() - yourTime.length() * 13) / 2, screenManager.getHeight() / 2 + 10);
		}
	@Override protected void init()
		{
		super.init();
		inputManager.mapToKey(actionForward, KeyEvent.VK_UP);
		inputManager.mapToKey(actionBack, KeyEvent.VK_DOWN);
		inputManager.mapToKey(actionLeft, KeyEvent.VK_LEFT);
		inputManager.mapToKey(actionRight, KeyEvent.VK_RIGHT);
		inputManager.mapToKey(actionMoveSideways, KeyEvent.VK_ALT);
		inputManager.mapToKey(actionRun, KeyEvent.VK_SHIFT);
		inputManager.mapToKey(actionDebug, KeyEvent.VK_F2);
		}
	private void keysActions(long elapsedTime)
		{
		float multiplier = 1.0f;
		if (actionRun.isPressed())
			multiplier = 2.0f;
		if (actionForward.isPressed())
			move(MOVEMENT_SPEED * elapsedTime * multiplier, angle);
		if (actionBack.isPressed())
			move(-MOVEMENT_SPEED * elapsedTime * multiplier, angle);
		if (actionLeft.isPressed())
			{
			if (actionMoveSideways.isPressed())
				{
				move(MOVEMENT_SPEED * elapsedTime * multiplier, angle - 90);
				}
			else
				{
				angle-= ROTATE_SPEED * elapsedTime * multiplier;
				while (angle < 0)
					angle+= 360;
				}
			}
		if (actionRight.isPressed())
			{
			if (actionMoveSideways.isPressed())
				{
				move(MOVEMENT_SPEED * elapsedTime * multiplier, angle + 90);
				}
			else
				{
				angle+= ROTATE_SPEED * elapsedTime * multiplier;
				while (angle > 360)
					angle-=360;
				}
			}
		if (actionDebug.isPressed())
			{
			drawDebug = !drawDebug;
			}
		}
	private boolean isAnyKeyPressed()
		{
		for (Action action : actions)
			if (action.isPressed())
				return true;
		return false;
		}
	private void wallCollisions()
		{
		int mapX = (int)position.getX();
		int mapY = (int)position.getY();
		int mapField;
		double newX = position.getX();
		double newY = position.getY();
		boolean update = false;
		if (position.getX() - mapX < MINIMUM_WALL_DISTANCE)
			{
			mapField = map.getWall(mapX - 1, mapY);
			if (mapField >= 0)
				{
				update = true;
				newX = mapX + MINIMUM_WALL_DISTANCE;
				}
			}
		else if (mapX + 1 - position.getX() < MINIMUM_WALL_DISTANCE)
			{
			mapField = map.getWall(mapX + 1, mapY);
			if (mapField >= 0)
				{
				update = true;
				newX = mapX + 1 - MINIMUM_WALL_DISTANCE;
				}
			}
		if (position.getY() - mapY < MINIMUM_WALL_DISTANCE)
			{
			mapField = map.getWall(mapX, mapY - 1);
			if (mapField >= 0)
				{
				update = true;
				newY = mapY + MINIMUM_WALL_DISTANCE;
				}
			}
		else if (mapY + 1 - position.getY() < MINIMUM_WALL_DISTANCE)
			{
			mapField = map.getWall(mapX, mapY + 1);
			if (mapField >= 0)
				{
				update = true;
				newY = mapY + 1 - MINIMUM_WALL_DISTANCE;
				}
			}
		if (update)
			position.setLocation(newX, newY);
		}
	private void move(double distance, float calculatedAngle)
		{
		double x = position.getX();
		double y = position.getY();
		int quarter = 1;
		double firstMovement;
		double secondMovement;
		
		while (calculatedAngle < 0)
			calculatedAngle+= 360;
		while (calculatedAngle > 360)
			calculatedAngle-= 360;
		while (calculatedAngle > 90)
			{
			++quarter;
			calculatedAngle-= 90;
			}
		
		firstMovement = Math.cos(Math.toRadians(calculatedAngle)) * distance;
		secondMovement = Math.sin(Math.toRadians(calculatedAngle)) * distance;
		switch (quarter)
			{
			case 1:
				y-= firstMovement;
				x+= secondMovement;
				break;
			case 2:
				x+= firstMovement;
				y+= secondMovement;
				break;
			case 3:
				x-= secondMovement;
				y+= firstMovement;
				break;
			case 4:
				x-= firstMovement;
				y-= secondMovement;
				break;
			}
		position.setLocation(x, y);
		}
	}
