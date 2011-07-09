/**
 * 
 */
package pl.vgtworld.gamecore;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * @author VGT
 * 
 */
public class Animation
	{
	private List<AnimationFrame> oFrames;
	private int iCurrentFrame;
	private int iCurrentTime;
	private int iAnimationDuration;
	public Animation()
		{
		oFrames = new ArrayList<AnimationFrame>();
		iAnimationDuration = 0;
		reset();
		}
	public void addFrame(Image oImage, int iDuration)
		{
		iAnimationDuration+= iDuration;
		AnimationFrame oNewFrame = new AnimationFrame(oImage, iAnimationDuration);
		oFrames.add(oNewFrame);
		}
	public void reset()
		{
		iCurrentFrame = 0;
		iCurrentTime = 0;
		}
	public void update(long iElapsedTime)
		{
		if (oFrames.size() > 1)
			{
			iCurrentTime += iElapsedTime;
			if (iCurrentTime > iAnimationDuration)
				{
				iCurrentTime = iCurrentTime % iAnimationDuration;
				iCurrentFrame = 0;
				}
			while (oFrames.get(iCurrentFrame).getEndTime() < iCurrentTime)
				++iCurrentFrame;
			}
		}
	public Image getImage()
		{
		if (oFrames.size() == 0)
			throw new IllegalStateException("no frames defined");
		return oFrames.get(iCurrentFrame).getImage();
		}
	}

class AnimationFrame
	{
	private Image oImage;
	private int iEndTime;
	public AnimationFrame(Image oImage, int iEndTime)
		{
		this.oImage = oImage;
		this.iEndTime = iEndTime;
		}
	public Image getImage()
		{
		return oImage;
		}
	public int getEndTime()
		{
		return iEndTime;
		}
	}
