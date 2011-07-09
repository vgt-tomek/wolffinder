package pl.vgtworld.games.wolffinder.gui.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import pl.vgtworld.games.wolffinder.gui.editor.newmap.NewMapDialog;
import pl.vgtworld.games.wolffinder.gui.editor.validation.ValidationWindow;
import pl.vgtworld.games.wolffinder.model.map.Map;

public class Menu
	extends JPanel
	{
	private static final long serialVersionUID = 1L;
	private EditorWindow parent = null;
	private JButton buttonNewMap = new JButton();
	private JButton buttonSaveMap = new JButton();
	private JButton buttonLoadMap = new JButton();
	private JButton buttonPutWall = new JButton();
	private JButton buttonRemoveWall = new JButton();
	private JButton buttonStartLocation = new JButton();
	private JButton buttonFinishLocation = new JButton();
	private JButton buttonValidateMap = new JButton();
	private NewMapDialog dialogNewMap = null;
	private ValidationWindow windowValidation = null;
	private JFileChooser fileChooser = new JFileChooser();
	public Menu(EditorWindow parent)
		{
		this.parent = parent;
		dialogNewMap = new NewMapDialog(parent);
		windowValidation = new ValidationWindow(parent);
		
		setActive(false);
		buttonNewMap.setText("New map");
		buttonNewMap.addActionListener(new ActionNewMap());
		buttonSaveMap.setText("Save map");
		buttonSaveMap.addActionListener(new ActionSaveMap());
		buttonLoadMap.setText("Load map");
		buttonLoadMap.addActionListener(new ActionLoadMap());
		buttonPutWall.setText("Put wall");
		buttonPutWall.addActionListener(new ActionPutWall());
		buttonRemoveWall.setText("Remove wall");
		buttonRemoveWall.addActionListener(new ActionRemoveWall());
		buttonStartLocation.setText("Start location");
		buttonStartLocation.addActionListener(new ActionStart());
		buttonFinishLocation.setText("Finish location");
		buttonFinishLocation.addActionListener(new ActionFinish());
		buttonValidateMap.setText("Validate map");
		buttonValidateMap.addActionListener(new ActionValidate());
		buildGUI();
		}
	public void setActive(boolean state)
		{
		if (state == true)
			{
			buttonSaveMap.setEnabled(true);
			buttonPutWall.setEnabled(true);
			buttonRemoveWall.setEnabled(true);
			buttonStartLocation.setEnabled(true);
			buttonFinishLocation.setEnabled(true);
			buttonValidateMap.setEnabled(true);
			}
		else
			{
			buttonSaveMap.setEnabled(false);
			buttonPutWall.setEnabled(false);
			buttonRemoveWall.setEnabled(false);
			buttonStartLocation.setEnabled(false);
			buttonFinishLocation.setEnabled(false);
			buttonValidateMap.setEnabled(false);
			}
		}
	private void buildGUI()
		{
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(buttonNewMap);
		add(buttonLoadMap);
		add(buttonSaveMap);
		add(buttonPutWall);
		add(buttonRemoveWall);
		add(buttonStartLocation);
		add(buttonFinishLocation);
		add(buttonValidateMap);
		}
	private class ActionNewMap
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			if (parent.isModified())
				{
				int choice = JOptionPane.showConfirmDialog(parent, "Current map not saved. Continue?", "Error", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.NO_OPTION)
					return;
				}
			
			dialogNewMap.setLocationRelativeTo(parent);
			dialogNewMap.setVisible(true);
			
			if (dialogNewMap.isConfirmed())
				{
				parent.createNewMap(dialogNewMap.getMapWidth(), dialogNewMap.getMapHeight());
				}
			}
		}
	private class ActionSaveMap
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			int choice = fileChooser.showSaveDialog(parent);
			if (choice == JFileChooser.APPROVE_OPTION)
				{
				File file = fileChooser.getSelectedFile();
				ObjectOutputStream stream = null;
				try
					{
					stream = new ObjectOutputStream(new FileOutputStream(file));
					stream.writeObject(parent.getMap());
					}
				catch (IOException e)
					{
					JOptionPane.showMessageDialog(parent, "Can't save map", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				finally
					{
					try { stream.close(); } catch (IOException e) {}
					}
				JOptionPane.showMessageDialog(parent, "Map saved", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	private class ActionLoadMap
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			if (parent.isModified())
				{
				int choice = JOptionPane.showConfirmDialog(parent, "Current map not saved. Continue?", "Error", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.NO_OPTION)
					return;
				}
			
			int choice = fileChooser.showOpenDialog(parent);
			if (choice == JFileChooser.APPROVE_OPTION)
				{
				File file = fileChooser.getSelectedFile();
				ObjectInputStream stream = null;
				try
					{
					stream = new ObjectInputStream(new FileInputStream(file));
					Map map = (Map)stream.readObject();
					parent.loadMap(map);
					}
				catch (IOException e)
					{
					JOptionPane.showMessageDialog(parent, "Can't load map", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				catch (ClassNotFoundException e)
					{
					JOptionPane.showMessageDialog(parent, "Can't load map", "Error", JOptionPane.ERROR_MESSAGE);
					return;
					}
				finally
					{
					if (stream != null)
						try { stream.close(); } catch (IOException e) {}
					}
				JOptionPane.showMessageDialog(parent, "Map loaded", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	private class ActionPutWall
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			parent.setCurrentMode(EditorWindow.EDIT_MODE_PUT);
			}
		}
	private class ActionRemoveWall
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			parent.setCurrentMode(EditorWindow.EDIT_MODE_REMOVE);
			}
		}
	private class ActionStart
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			parent.setCurrentMode(EditorWindow.EDIT_MODE_START);
			}
		}
	private class ActionFinish
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			parent.setCurrentMode(EditorWindow.EDIT_MODE_FINISH);
			}
		}
	private class ActionValidate
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			windowValidation.setVisible(true);
			}
		}
	}
