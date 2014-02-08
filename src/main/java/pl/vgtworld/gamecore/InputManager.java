package pl.vgtworld.gamecore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;

public class InputManager
	implements KeyListener
	{
	private static final int NUM_KEY_CODES = 600;
	private Action[] keyActions;
	public InputManager()
		{
		keyActions = new Action[ NUM_KEY_CODES ];
		}
	public void setComponent(JComponent component)
		{
		component.addKeyListener(this);
		}
	public void mapToKey(Action action, int keyCode)
		{
		keyActions[ keyCode ] = action;
		}
	public void clearMap(Action action)
		{
		for (int i = 0; i < NUM_KEY_CODES; ++i)
			if (keyActions[i] == action)
				keyActions[i] = null;
		}
	public void resetAllActions()
		{
		for (int i = 0; i < NUM_KEY_CODES; ++i)
			if (keyActions[i] != null)
				keyActions[i].reset();
		}
	public static String getKeyName(int keyCode)
		{
		return KeyEvent.getKeyText(keyCode);
		}
	@Override public void keyPressed(KeyEvent event)
		{
		Action action = getKeyAction(event);
		if (action != null)
			action.press();
		event.consume();
		}
	@Override public void keyReleased(KeyEvent event)
		{
		Action action = getKeyAction(event);
		if (action != null)
			action.release();
		event.consume();
		}
	@Override public void keyTyped(KeyEvent event)
		{
		event.consume();
		}
	private Action getKeyAction(KeyEvent event)
		{
		int keyCode = event.getKeyCode();
		if (keyCode < keyActions.length)
			return keyActions[ keyCode ];
		else
			return null;
		}
	}
