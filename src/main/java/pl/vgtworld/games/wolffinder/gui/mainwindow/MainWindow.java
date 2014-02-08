package pl.vgtworld.games.wolffinder.gui.mainwindow;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import pl.vgtworld.gamecore.Animation;
import pl.vgtworld.gamecore.Sprite;
import pl.vgtworld.games.wolffinder.data.Constants;
import pl.vgtworld.games.wolffinder.engine.WolfGameCore;
import pl.vgtworld.games.wolffinder.gui.editor.EditorWindow;
import pl.vgtworld.games.wolffinder.model.MapFileChooser;
import pl.vgtworld.games.wolffinder.model.ResourceBundle;
import pl.vgtworld.games.wolffinder.model.map.Map;
import pl.vgtworld.games.wolffinder.model.map.MapValidator;


public class MainWindow
	extends JFrame
	{
	private static final long serialVersionUID = 1L;
	private ResourceBundle rb = new ResourceBundle(Constants.languagesPackage);
	private WolfGameCore gameCore = new WolfGameCore();
	private Map map = null;
	private JLabel label = new JLabel("", JLabel.CENTER);
	private Menu menu = new Menu(this);
	private Thread mainLoopThread = null;
	private EditorWindow editorWindow = new EditorWindow(this);
	private JFileChooser mapFileChooser = new MapFileChooser();
	public MainWindow()
		{
		setTitle(rb.getString("name"));
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		gameCore.setTicks(100);
		addKeyListener(gameCore.getInputManager());

		label.setText(rb.getString("name"));
		
		buildGUI();
		}
	public ResourceBundle getResourceBundle()
		{
		return rb;
		}
	void startGame()
		{
		if (mainLoopThread == null)
			{
			remove(label);
			gameCore.setMap(map);
			add(gameCore.getScreenManager().getCanvas());
			gameCore.getScreenManager().getCanvas().revalidate();
			
			mainLoopThread = new Thread(gameCore);
			mainLoopThread.start();
			menu.fireButtonEnabledCheck(true);
			}
		}
	void stopGame()
		{
		if (mainLoopThread != null)
			{
			remove(gameCore.getScreenManager().getCanvas());
			add(label);
			label.revalidate();
			label.repaint();
			
			gameCore.stop();
			mainLoopThread = null;
			menu.fireButtonEnabledCheck(false);
			}
		}
	void chooseMap()
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
				
				//end position sprite
				BufferedImage frame1 = ImageIO.read(getClass().getResourceAsStream("/pl/vgtworld/games/wolffinder/data/gfx/sprites/wolf/frame1.png"));
				BufferedImage frame2 = ImageIO.read(getClass().getResourceAsStream("/pl/vgtworld/games/wolffinder/data/gfx/sprites/wolf/frame2.png"));
				BufferedImage frame3 = ImageIO.read(getClass().getResourceAsStream("/pl/vgtworld/games/wolffinder/data/gfx/sprites/wolf/frame3.png"));
				BufferedImage frame4 = ImageIO.read(getClass().getResourceAsStream("/pl/vgtworld/games/wolffinder/data/gfx/sprites/wolf/frame4.png"));
				Animation animation = new Animation();
				animation.addFrame(frame1, 100);
				animation.addFrame(frame2, 100);
				animation.addFrame(frame3, 100);
				animation.addFrame(frame4, 100);
				Sprite sprite = new Sprite();
				sprite.addAnimation(0, animation);
				map.setEndPositionSprite(sprite);
				
				validator.setMap(map);
				if (validator.fullValidation() == false)
					{
					JOptionPane.showMessageDialog(MainWindow.this, rb.getString("window.main.error.mapinvalid"), rb.getString("error"), JOptionPane.ERROR_MESSAGE);
					return;
					}
				else
					{
					MainWindow.this.map = map;
					}
				}
			catch (IOException e)
				{
				JOptionPane.showMessageDialog(MainWindow.this, rb.getString("window.main.error.readerror"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
				return;
				}
			catch (ClassNotFoundException e)
				{
				JOptionPane.showMessageDialog(MainWindow.this, rb.getString("window.main.error.unknownformat"), rb.getString("Error"), JOptionPane.ERROR_MESSAGE);
				return;
				}
			
			}
		}
	void displayEditor()
		{
		editorWindow.setVisible(true);
		}
	private void buildGUI()
		{
		add(label, BorderLayout.CENTER);
		add(menu, BorderLayout.PAGE_END);
		}
	}
