package pl.vgtworld.games.wolffinder.gui.editor.newmap;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import pl.vgtworld.games.wolffinder.gui.editor.EditorWindow;
import pl.vgtworld.tools.GBC;

public class NewMapDialog
	extends JDialog
	{
	private static final long serialVersionUID = 1L;
	private EditorWindow parent = null;
	private JTextField width = new JTextField(8);
	private JTextField height = new JTextField(8);
	private JButton ok = new JButton();
	private JButton cancel = new JButton();
	private boolean confirmed = false;
	private int choosenWidth;
	private int choosenHeight;
	public NewMapDialog(EditorWindow parent)
		{
		super(parent, true);
		this.parent = parent;
		
		setTitle(parent.getResourceBundle().getString("window.editor.newmap.title"));
		ok.setText(parent.getResourceBundle().getString("ok"));
		ok.addActionListener(new ActionOk());
		cancel.setText(parent.getResourceBundle().getString("cancel"));
		cancel.addActionListener(new ActionCancel());
		
		buildGUI();
		pack();
		}
	public int getMapWidth()
		{
		return choosenWidth;
		}
	public int getMapHeight()
		{
		return choosenHeight;
		}
	public boolean isConfirmed()
		{
		return confirmed;
		}
	@Override public void setVisible(boolean state)
		{
		if (state == true)
			prepareForm();
		super.setVisible(state);
		}
	private void prepareForm()
		{
		confirmed = false;
		width.setText("");
		height.setText("");
		width.requestFocus();
		}
	private void buildGUI()
		{
		GBC gbc = new GBC();
		JLabel widthLabel = new JLabel(parent.getResourceBundle().getString("window.editor.newmap.label.width"));
		JLabel heightLabel = new JLabel(parent.getResourceBundle().getString("window.editor.newmap.label.height"));
		JPanel buttonsContainer = new JPanel();
		
		buttonsContainer.add(ok);
		buttonsContainer.add(cancel);
		
		setLayout(new GridBagLayout());
		gbc.setGrid(0, 0).setGridSize(1, 1).setWeight(0, 0).setInsets(5).setAnchor(GBC.WEST).setFill(GBC.NONE);
		add(widthLabel, gbc);
		gbc.setGrid(1, 0).setGridSize(1, 1).setWeight(100, 0).setInsets(5).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL);
		add(width, gbc);
		gbc.setGrid(0, 1).setGridSize(1, 1).setWeight(0, 0).setInsets(5).setAnchor(GBC.WEST).setFill(GBC.NONE);
		add(heightLabel, gbc);
		gbc.setGrid(1, 1).setGridSize(1, 1).setWeight(100, 0).setInsets(5).setAnchor(GBC.CENTER).setFill(GBC.HORIZONTAL);
		add(height, gbc);
		gbc.setGrid(0, 2).setGridSize(2, 1).setWeight(100, 100).setInsets(5).setAnchor(GBC.NORTH).setFill(GBC.NONE);
		add(buttonsContainer, gbc);
		}
	private void convertFormData()
		{
		try
			{
			choosenWidth = Integer.parseInt(width.getText());
			}
		catch (NumberFormatException e)
			{
			choosenWidth = 0;
			}
		try
			{
			choosenHeight = Integer.parseInt(height.getText());
			}
		catch (NumberFormatException e)
			{
			choosenHeight = 0;
			}
		}
	private class ActionCancel
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			confirmed = false;
			setVisible(false);
			}
		}
	private class ActionOk
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			convertFormData();
			if (choosenWidth <= 0)
				{
				JOptionPane.showMessageDialog(NewMapDialog.this, parent.getResourceBundle().getString("window.editor.newmap.error.width"), parent.getResourceBundle().getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
				}
			if (choosenHeight <= 0)
				{
				JOptionPane.showMessageDialog(NewMapDialog.this, parent.getResourceBundle().getString("window.editor.newmap.error.height"), parent.getResourceBundle().getString("error"), JOptionPane.ERROR_MESSAGE);
				return;
				}
			confirmed = true;
			setVisible(false);
			}
		}
	}
