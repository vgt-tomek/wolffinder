package pl.vgtworld.games.wolffinder.gui.editor.validation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import pl.vgtworld.games.wolffinder.gui.editor.EditorWindow;
import pl.vgtworld.games.wolffinder.model.map.Map;
import pl.vgtworld.games.wolffinder.model.map.MapValidator;

public class ValidationWindow
	extends JFrame
	{
	private static final long serialVersionUID = 1L;
	private EditorWindow parent = null;
	private MapValidator validator = new MapValidator();
	private JTextArea console = new JTextArea();
	private JButton validate = new JButton();
	private Thread validationThread = null;
	public ValidationWindow(EditorWindow parent)
		{
		this.parent = parent;
		
		console.setPreferredSize(new Dimension(400, 400));
		validate.setText("Validate");
		validate.addActionListener(new ActionValidate());
		
		buildGUI();
		pack();
		}
	public void setMap(Map map)
		{
		validator.setMap(map);
		}
	@Override public void setVisible(boolean state)
		{
		if (isVisible() == false && state == true)
			{
			console.setText("");
			setLocationRelativeTo(parent);
			}
		super.setVisible(state);
		}
	private void buildGUI()
		{
		JPanel buttonsContainer = new JPanel();
		
		buttonsContainer.add(validate);
		
		add(new JScrollPane(console), BorderLayout.CENTER);
		add(buttonsContainer, BorderLayout.PAGE_END);
		}
	private class ValidationThread
		extends Thread
		{
		@Override public void run()
			{
			console.setText("");
			
			console.append("Checking start position... ");
			if (validator.validateStartPoint() == false)
				{
				console.append("ERROR\n\n");
				console.append("Start position not set or inside a wall");
				validate.setEnabled(true);
				return;
				}
			else
				console.append("OK\n");
			
			console.append("Checking end position... ");
			if (validator.validateEndPoint() == false)
				{
				console.append("ERROR\n\n");
				console.append("End position not set or inside a wall");
				validate.setEnabled(true);
				return;
				}
			else
				console.append("OK\n");
			
			console.append("Checking walkable area... ");
			if (validator.validateClearPath() == false)
				{
				console.append("ERROR\n\n");
				console.append("Start and end position not connected");
				validate.setEnabled(true);
				return;
				}
			else
				console.append("OK\n");
			
			console.append("Checking leaks... ");
			if (validator.validateLeaks() == false)
				{
				console.append("ERROR\n\n");
				console.append("Walkable area not fully surrounded by walls");
				validate.setEnabled(true);
				return;
				}
			else
				console.append("OK\n");
			
			console.append("Map validation sucesfull. Ready to play.");
			validate.setEnabled(true);
			}
		}
	private class ActionValidate
		extends AbstractAction
		{
		private static final long serialVersionUID = 1L;
		@Override public void actionPerformed(ActionEvent event)
			{
			validate.setEnabled(false);
			validator.setMap(parent.getMap());
			if (validationThread != null)
				validationThread.interrupt();
			validationThread = new ValidationThread();
			validationThread.start();
			}
		}
	}
