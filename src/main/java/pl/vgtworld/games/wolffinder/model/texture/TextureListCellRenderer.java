package pl.vgtworld.games.wolffinder.model.texture;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class TextureListCellRenderer
	implements ListCellRenderer
	{
	CellRenderer renderer = new CellRenderer();
	@Override public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus)
		{
		Texture tex = (Texture)object;
		renderer.setTexture(tex);
		renderer.setSelected(isSelected);
		return renderer;
		}
	private class CellRenderer
		extends JPanel
		{
		private static final long serialVersionUID = 1L;
		private Texture texture = null;
		private boolean isSelected = false;
		public CellRenderer()
			{
			setPreferredSize(new Dimension(0, 64));
			}
		public void setTexture(Texture texture)
			{
			this.texture = texture;
			}
		public void setSelected(boolean selected)
			{
			isSelected = selected;
			}
		@Override public void paintComponent(Graphics g)
			{
			if (isSelected)
				{
				g.setColor(new Color(200, 200, 255));
				g.fillRect(0, 0, getWidth(), getHeight());
				}
			if (texture.getThumb() != null)
				{
				int height = getHeight();
				g.drawImage(texture.getThumb(), 0, 0, height - 1, height - 1, 0, 0, texture.getThumb().getWidth() - 1, texture.getThumb().getHeight() - 1, null);
				g.setColor(Color.BLACK);
				g.setFont(new Font("Arial", Font.BOLD, 14));
				g.drawString("" + texture.getWidth() + "x" + texture.getHeight(), 70, 38);
				}
			if (isSelected)
				{
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				}
			}
		}
	}
