package pl.vgtworld.games.wolffinder.model.map;

import java.util.ArrayList;

public class MapValidator
	{
	private Map validatedMap = null;
	public void setMap(Map map)
		{
		validatedMap = map;
		}
	public boolean fullValidation()
		{
		if (validateStartPoint() == false)
			return false;
		if (validateEndPoint() == false)
			return false;
		if (validateClearPath() == false)
			return false;
		if (validateLeaks() == false)
			return false;
		return true;
		}
	public boolean validateStartPoint()
		{
		int x = (int)validatedMap.getStartPosition().getX();
		int y = (int)validatedMap.getStartPosition().getY();
		return isNotWall(x, y);
		}
	public boolean validateEndPoint()
		{
		int x = (int)validatedMap.getEndPosition().getX();
		int y = (int)validatedMap.getEndPosition().getY();
		return isNotWall(x, y);
		}
	public boolean validateClearPath()
		{
		ArrayList<Coords> coordsList = buildWalkableArea();
		Coords endPoint = new Coords();
		
		endPoint.x = (int)validatedMap.getEndPosition().getX();
		endPoint.y = (int)validatedMap.getEndPosition().getY();
		if (coordsList.contains(endPoint))
			return true;
		return false;
		}
	public boolean validateLeaks()
		{
		ArrayList<Coords> coordsList = buildWalkableArea();
		int maxX = validatedMap.getWidth() - 1;
		int maxY = validatedMap.getHeight() - 1;
		
		for (Coords coord : coordsList)
			{
			if (coord.x == 0 || coord.x == maxX)
				return false;
			if (coord.y == 0 || coord.y == maxY)
				return false;
			}
		return true;
		}
	private boolean isNotWall(int x, int y)
		{
		if (x < 0 || x >= validatedMap.getWidth())
			return false;
		if (y < 0 || y >= validatedMap.getHeight())
			return false;
		if (validatedMap.getWall(x, y) != -1)
			return false;
		return true;
		}
	private ArrayList<Coords> buildWalkableArea()
		{
		ArrayList<Coords> coordsList = new ArrayList<Coords>();
		Coords coords = new Coords();
		
		//load start position
		coords.x = (int)validatedMap.getStartPosition().getX();
		coords.y = (int)validatedMap.getStartPosition().getY();
		coordsList.add(coords);
		
		int loadedCount = 0;
		while (loadedCount < coordsList.size())
			scanNeighbours(coordsList, loadedCount++);
		
		return coordsList;
		}
	private void scanNeighbours(ArrayList<Coords> coordsList, int index)
		{
		Coords point = coordsList.get(index);
		int x, y;
		
		//left
		x = point.x - 1;
		y = point.y;
		addNeighbour(coordsList, x, y);
		//top
		x = point.x;
		y = point.y - 1;
		addNeighbour(coordsList, x, y);
		//right
		x = point.x + 1;
		y = point.y;
		addNeighbour(coordsList, x, y);
		//bottom
		x = point.x;
		y = point.y + 1;
		addNeighbour(coordsList, x, y);
		}
	private void addNeighbour(ArrayList<Coords> coordsList, int x, int y)
		{
		if (x < 0 || x >= validatedMap.getWidth())
			return;
		if (y < 0 || y >= validatedMap.getHeight())
			return;
		
		Coords neighbour = new Coords();
		neighbour.x = x;
		neighbour.y = y;
		
		if (validatedMap.getWall(neighbour.x, neighbour.y) == -1 && !coordsList.contains(neighbour))
			coordsList.add(neighbour);
		}
	private class Coords
		{
		int x;
		int y;
		@Override public boolean equals(Object object)
			{
			if (object == this)
				return true;
			
			if (!(object instanceof Coords))
				return false;
			
			Coords coords = (Coords)object;
			
			if (coords.x == x && coords.y == y)
				return true;
			return false;
			}
		}
	}
