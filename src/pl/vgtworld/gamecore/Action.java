package pl.vgtworld.gamecore;

public class Action
	{
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_INITIAL_PRESS_ONLY = 1;
	public static final int STATE_RELEASED = 0;
	public static final int STATE_PRESSED = 1;
	public static final int STATE_WAITING_FOR_RELEASE = 2;
	private int behaviour;
	private int amount;
	private int state;
	public Action()
		{
		this(TYPE_NORMAL);
		}
	public Action(int behaviour)
		{
		this.behaviour = behaviour;
		reset();
		}
	public void reset()
		{
		amount = 0;
		state = STATE_RELEASED;
		}
	public synchronized void tap()
		{
		press();
		release();
		}
	public synchronized void press()
		{
		press(1);
		}
	public synchronized void press(int amount)
		{
		if (state != STATE_WAITING_FOR_RELEASE)
			{
			this.amount+= amount;
			state = STATE_PRESSED;
			}
		}
	public synchronized void release()
		{
		state = STATE_RELEASED;
		}
	public synchronized boolean isPressed()
		{
		return (getAmount() != 0);
		}
	public synchronized int getAmount()
		{
		int ret = amount;
		if (ret != 0)
			{
			if (state == STATE_RELEASED)
				amount = 0;
			else if (behaviour == TYPE_INITIAL_PRESS_ONLY)
				{
				state = STATE_WAITING_FOR_RELEASE;
				amount = 0;
				}
			}
		return ret;
		}
	}
