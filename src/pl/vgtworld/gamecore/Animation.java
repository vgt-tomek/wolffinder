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
	private List<AnimationFrame> frames;
	private int currentFrame;
	private int currentTime;
	private int animationDuration;
	public Animation()
		{
		frames = new ArrayList<AnimationFrame>();
		animationDuration = 0;
		reset();
		}
	public void addFrame(Image image, int duration)
		{
		animationDuration+= duration;
		AnimationFrame newFrame = new AnimationFrame(image, animationDuration);
		frames.add(newFrame);
		}
	public void reset()
		{
		currentFrame = 0;
		currentTime = 0;
		}
	public void update(long elapsedTime)
		{
		if (frames.size() > 1)
			{
			currentTime += elapsedTime;
			if (currentTime > animationDuration)
				{
				currentTime = currentTime % animationDuration;
				currentFrame = 0;
				}
			while (frames.get(currentFrame).getEndTime() < currentTime)
				++currentFrame;
			}
		}
	public Image getImage()
		{
		if (frames.size() == 0)
			throw new IllegalStateException("no frames defined");
		return frames.get(currentFrame).getImage();
		}
	}

class AnimationFrame
	{
	private Image image;
	private int endTime;
	public AnimationFrame(Image image, int endTime)
		{
		this.image = image;
		this.endTime = endTime;
		}
	public Image getImage()
		{
		return image;
		}
	public int getEndTime()
		{
		return endTime;
		}
	}
