package pl.vgtworld.games.wolffinder.gui.mainwindow;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Menu
	extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private MainWindow mainWindow = null;
	private JButton start = new JButton();
	private JButton stop = new JButton();
	private JButton loadMap = new JButton();
	private JButton editor = new JButton();
	public Menu(MainWindow mainWindow)
		{
		this.mainWindow = mainWindow;
		
		start.setText(mainWindow.getResourceBundle().getString("window.main.button.start"));
		start.setFocusable(false);
		start.setEnabled(false);
		start.addActionListener(new ActionStartGame());
		stop.setText(mainWindow.getResourceBundle().getString("window.main.button.stop"));
		stop.setFocusable(false);
		stop.setEnabled(false);
		stop.addActionListener(new ActionStopGame());
		loadMap.setText(mainWindow.getResourceBundle().getString("window.main.button.load"));
		loadMap.setFocusable(false);
		loadMap.addActionListener(new ActionLoadMap());
		editor.setText(mainWindow.getResourceBundle().getString("window.main.button.editor"));
		editor.setFocusable(false);
		editor.addActionListener(new ActionEditor());
		
		buildGUI();
		}
	public void fireButtonEnabledCheck(boolean gameRunning)
		{
		if (gameRunning)
			{
			start.setEnabled(false);
			stop.setEnabled(true);
			loadMap.setEnabled(false);
			editor.setEnabled(false);
			}
		else
			{
			start.setEnabled(true);
			stop.setEnabled(false);
			loadMap.setEnabled(true);
			editor.setEnabled(true);
			}
		}
	private void buildGUI()
		{
		add(start);
		add(stop);
		add(loadMap);
		add(editor);
		}
	private class ActionStartGame
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent arg0)
			{
			mainWindow.startGame();
			}
	}
	private class ActionStopGame
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent e)
			{
			mainWindow.stopGame();
			}
		}
	private class ActionLoadMap
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			mainWindow.chooseMap();
			start.setEnabled(true);
			}
	}
	private class ActionEditor
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			mainWindow.displayEditor();
			}
		}
	}
