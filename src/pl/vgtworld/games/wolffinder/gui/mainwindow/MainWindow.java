package pl.vgtworld.games.wolffinder.gui.mainwindow;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pl.vgtworld.games.wolffinder.engine.WolfGameCore;
import pl.vgtworld.games.wolffinder.gui.editor.EditorWindow;
import pl.vgtworld.games.wolffinder.model.map.Map;
import pl.vgtworld.games.wolffinder.model.map.MapValidator;


public class MainWindow
	extends JFrame
	{
	private static final long serialVersionUID = 1L;
	private WolfGameCore gameCore = new WolfGameCore();
	private Map map = null;
	private JLabel label = new JLabel("Wilczek", JLabel.CENTER);
	private Thread mainLoopThread = null;
	private JButton start = new JButton();
	private JButton stop = new JButton();
	private JButton loadMap = new JButton();
	private JButton editor = new JButton();
	private EditorWindow editorWindow = new EditorWindow();
	private JFileChooser mapFileChooser = new JFileChooser();
	public MainWindow()
		{
		setTitle("Wilczek");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		gameCore.setTicks(100);
		addKeyListener(gameCore.getInputManager());

		
		start.setText("start");
		start.setFocusable(false);
		start.setEnabled(false);
		start.addActionListener(new ActionStartGame());
		stop.setText("stop");
		stop.setFocusable(false);
		stop.setEnabled(false);
		stop.addActionListener(new ActionStopGame());
		loadMap.setText("Load map");
		loadMap.setFocusable(false);
		loadMap.addActionListener(new ActionLoadMap());
		editor.setText("Map editor");
		editor.setFocusable(false);
		editor.addActionListener(new ActionEditor());
		
		JPanel buttonsContainer = new JPanel();
		buttonsContainer.add(start);
		buttonsContainer.add(stop);
		buttonsContainer.add(loadMap);
		buttonsContainer.add(editor);
		add(label);
		add(buttonsContainer, BorderLayout.PAGE_END);
		}
	private void startGame()
		{
		if (mainLoopThread == null)
			{
			remove(label);
			gameCore.setMap(map);
			add(gameCore.getScreenManager().getCanvas());
			gameCore.getScreenManager().getCanvas().revalidate();
			
			mainLoopThread = new Thread(gameCore);
			mainLoopThread.start();
			
			start.setEnabled(false);
			stop.setEnabled(true);
			loadMap.setEnabled(false);
			editor.setEnabled(false);
			}
		}
	private void stopGame()
		{
		if (mainLoopThread != null)
			{
			remove(gameCore.getScreenManager().getCanvas());
			add(label);
			label.revalidate();
			label.repaint();
			
			gameCore.stop();
			mainLoopThread = null;
			start.setEnabled(true);
			stop.setEnabled(false);
			loadMap.setEnabled(true);
			editor.setEnabled(true);
			}
		}
	private class ActionStartGame
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent arg0)
			{
			startGame();
			}
		}
	private class ActionStopGame
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent e)
			{
			stopGame();
			}
		}
	private class ActionLoadMap
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			int choice = mapFileChooser.showOpenDialog(MainWindow.this);
			if (choice == JFileChooser.APPROVE_OPTION)
				{
				try
					{
					File file = mapFileChooser.getSelectedFile();
					ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file));
					Map map = (Map)stream.readObject();
					MapValidator validator = new MapValidator();
					
					validator.setMap(map);
					if (validator.fullValidation() == false)
						{
						JOptionPane.showMessageDialog(MainWindow.this, "Map with errors. Validate in map editor for details", "Error", JOptionPane.ERROR_MESSAGE);
						return;
						}
					else
						{
						MainWindow.this.map = map;
						start.setEnabled(true);
						}
					}
				catch (IOException e)
					{
					JOptionPane.showMessageDialog(MainWindow.this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				catch (ClassNotFoundException e)
					{
					JOptionPane.showMessageDialog(MainWindow.this, "Unknown file type", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				
				}
			}
		}
	private class ActionEditor
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			editorWindow.setVisible(true);
			}
		}
	}
