package pl.vgtworld.games.wolffinder.model.texture;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.imageio.ImageIO;

public class Texture
	implements Serializable
	{
	private static final long serialVersionUID = 1L;
	private static int thumbSize = 64;
	private byte[] stream = null;
	transient private int width;
	transient private int height;
	transient private BufferedImage texture = null;
	transient private BufferedImage thumb = null;
	transient private BufferedImage[] slices = null;
	public Texture()
		{
		}
	public Texture(String path)
		throws IOException
		{
		this(new File(path));
		}
	public Texture(File file)
		throws IOException
		{
		setTexture(file);
		}
	public Texture(BufferedImage image)
		throws IOException
		{
		setTexture(image);
		}
	public int getWidth()
		{
		return width;
		}
	public int getHeight()
		{
		return height;
		}
	public BufferedImage getThumb()
		{
		return thumb;
		}
	public BufferedImage getSlice(double offset)
		{
		int index = (int)(offset * slices.length);
		return slices[index];
		}
	public void setTexture(File file)
		throws IOException
		{
		BufferedImage image = ImageIO.read(file);
		if (image == null)
			throw new IOException("Not an image");
		setTexture(image);
		}
	public void setTexture(BufferedImage image)
		throws IOException
		{
		texture = image;
		serializeTexture();
		prepareTexture();
		}
	private void prepareTexture()
		{
		width = texture.getWidth();
		height = texture.getHeight();
		createThumb();
		createSlices();
		texture = null;
		}
	private void createThumb()
		{
		thumb = new BufferedImage(thumbSize, thumbSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)thumb.getGraphics();
		g.drawImage(texture, 0, 0, thumb.getWidth(), thumb.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), null);
		}
	private void createSlices()
		{
		int count = texture.getWidth();
		slices = new BufferedImage[count];
		for (int i = 0; i < count; ++i)
			{
			BufferedImage slice = new BufferedImage(1, texture.getHeight(), BufferedImage.TYPE_INT_RGB);
			slice.getGraphics().drawImage(texture, 0, 0, 1, slice.getHeight(), i, 0, i + 1, texture.getHeight(), null);
			slices[i] = slice;
			}
		}
	private void serializeTexture()
		throws IOException
		{
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		ImageIO.write(texture, "png", byteArray);
		stream = byteArray.toByteArray();
		}
	private void rebuildTexture()
		throws IOException
		{
		ByteArrayInputStream byteArray = new ByteArrayInputStream(stream);
		texture = ImageIO.read(byteArray);
		prepareTexture();
		}
	private void readObject(ObjectInputStream stream)
		throws IOException, ClassNotFoundException
		{
		stream.defaultReadObject();
		rebuildTexture();
		}
	}
