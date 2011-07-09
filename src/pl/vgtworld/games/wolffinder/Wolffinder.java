package pl.vgtworld.games.wolffinder;

import java.awt.EventQueue;
import pl.vgtworld.games.wolffinder.gui.mainwindow.MainWindow;

public class Wolffinder
	{
	public static void main(String[] args)
		{
		EventQueue.invokeLater(new Runnable()
			{
			@Override public void run()
				{
				MainWindow frame = new MainWindow();
				frame.setVisible(true);
				}
			});
		}
	}
