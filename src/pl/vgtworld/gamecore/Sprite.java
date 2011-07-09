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
	private Map<Integer, Animation> oAnimations;
	private int iActiveAnimation;
	private float fX;
	private float fY;
	private float fDX;
	private float fDY;
	public Sprite()
		{
		this(null);
		}
	public Sprite(Animation oAnimation)
		{
		oAnimations = new HashMap<Integer, Animation>();
		iActiveAnimation = 0;
		fX = 0;
		fY = 0;
		fDX = 0;
		fDY = 0;
		if (oAnimation != null)
			{
			addAnimation(1, oAnimation);
			setActiveAnimation(1);
			}
		}
	public int getScreenX()
		{
		return (int) fX;
		}
	public int getScreenY()
		{
		return (int) fY;
		}
	public float getX()
		{
		return fX;
		}
	public float getY()
		{
		return fY;
		}
	public float getDX()
		{
		return fDX;
		}
	public float getDY()
		{
		return fDY;
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
		this.fX = fX;
		}
	public void setY(float fY)
		{
		this.fY = fY;
		}
	public void setDX(float fDX)
		{
		this.fDX = fDX;
		}
	public void setDY(float fDY)
		{
		this.fDY = fDY;
		}
	public void addAnimation(int iKey, Animation oAnimation)
		{
		oAnimations.put(iKey, oAnimation);
		}
	public void setActiveAnimation(int iKey)
		{
		if (iActiveAnimation != iKey)
			{
			iActiveAnimation = iKey;
			oAnimations.get(iKey).reset();
			}
		}
	public void update(long iElapsedTime)
		{
		fX += fDX * iElapsedTime;
		fY += fDY * iElapsedTime;
		oAnimations.get(iActiveAnimation).update(iElapsedTime);
		}
	public Image getImage()
		{
		return oAnimations.get(iActiveAnimation).getImage();
		}
	}
