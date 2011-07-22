/**
 * 
 */
package pl.vgtworld.gamecore;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

/**
 * @author VGT
 * 
 */
public class Sprite
	{
	private Map<Integer, Animation> animations = new HashMap<Integer, Animation>();
	private int activeAnimation = 0;
	private float x = 0;
	private float y = 0;
	private float dx = 0;
	private float dy = 0;
	public Sprite()
		{
		this(null);
		}
	public Sprite(Animation animation)
		{
		if (animation != null)
			{
			addAnimation(1, animation);
			setActiveAnimation(1);
			}
		}
	public int getScreenX()
		{
		return (int) x;
		}
	public int getScreenY()
		{
		return (int) y;
		}
	public float getX()
		{
		return x;
		}
	public float getY()
		{
		return y;
		}
	public float getDX()
		{
		return dx;
		}
	public float getDY()
		{
		return dy;
		}
	public int getWidth()
		{
		return getImage().getWidth(null);
		}
	public int getHeight()
		{
		return getImage().getHeight(null);
		}
	public void setX(float fX)
		{
		this.x = fX;
		}
	public void setY(float fY)
		{
		this.y = fY;
		}
	public void setDX(float fDX)
		{
		this.dx = fDX;
		}
	public void setDY(float fDY)
		{
		this.dy = fDY;
		}
	public void addAnimation(int key, Animation animation)
		{
		animations.put(key, animation);
		}
	public void setActiveAnimation(int key)
		{
		if (activeAnimation != key)
			{
			activeAnimation = key;
			animations.get(key).reset();
			}
		}
	public void update(long elapsedTime)
		{
		x += dx * elapsedTime;
		y += dy * elapsedTime;
		animations.get(activeAnimation).update(elapsedTime);
		}
	public Image getImage()
		{
		return animations.get(activeAnimation).getImage();
		}
	}
