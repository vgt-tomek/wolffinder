/**
 * 
 */
package pl.vgtworld.tools;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * @author VGT
 *
 */
public class GBC
	extends GridBagConstraints
	{
	private static final long serialVersionUID = 1L;
	public GBC()
		{
		}
	public GBC(int iGridX, int iGridY)
		{
		gridx = iGridX;
		gridy = iGridY;
		}
	public GBC(int iGridX, int iGridY, int iGridWidth, int iGridHeight)
		{
		gridx = iGridX;
		gridy = iGridY;
		gridwidth = iGridWidth;
		gridheight = iGridHeight;
		}
	public GBC setGrid(int iGridX, int iGridY)
		{
		gridx = iGridX;
		gridy = iGridY;
		return this;
		}
	public GBC setGridSize(int iGridWidth, int iGridHeight)
		{
		gridwidth = iGridWidth;
		gridheight = iGridHeight;
		return this;
		}
	public GBC setAnchor(int iAnchor)
		{
		anchor = iAnchor;
		return this;
		}
	public GBC setFill(int iFill)
		{
		fill = iFill;
		return this;
		}
	public GBC setWeight(double fWeightX, double fWeightY)
		{
		weightx = fWeightX;
		weighty = fWeightY;
		return this;
		}
	public GBC setInsets(int iDistance)
		{
		return setInsets(iDistance, iDistance, iDistance, iDistance);
		}
	public GBC setInsets(int iTopBottom, int iLeftRight)
		{
		return setInsets(iTopBottom, iLeftRight, iTopBottom, iLeftRight);
		}
	public GBC setInsets(int iTop, int iLeft, int iBottom, int iRight)
		{
		insets = new Insets(iTop, iLeft, iBottom, iRight);
		return this;
		}
	public GBC setIpad(int iIPadX, int iIPadY)
		{
		ipadx = iIPadX;
		ipady = iIPadY;
		return this;
		}
	}
