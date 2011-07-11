package pl.vgtworld.games.wolffinder.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import pl.vgtworld.games.wolffinder.model.map.Map;


public class GraphicEngine
	{
	private static final double PROJECTION_PLANE_DISTANCE = 10.0;
	private Map map = null;
	private Point2D position = new Point2D.Double();
	private float fov = 60;
	private float lookAngle = 0;
	private Wall wallStruct = new Wall();
	public void setMap(Map map)
		{
		this.map = map;
		}
	public void setFov(float fov)
		{
		this.fov = fov;
		}
	public void setPosition(double x, double y)
		{
		position.setLocation(x, y);
		}
	public void setLookAngle(float angle)
		{
		lookAngle = angle;
		}
	public void draw(Graphics2D g, int width, int height)
		{
		float leftAngle;
		
		leftAngle = lookAngle - fov / 2;
		while (leftAngle < 0) leftAngle+=360;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.GREEN);
		double verticalFov = fov / (width / (double)height);
		double projectionPlaneHeight = (Math.sin(Math.toRadians(verticalFov/2)) * PROJECTION_PLANE_DISTANCE) * 2;
		//System.out.println(verticalFov + " " + projectionPlaneHeight);
		for (int i = 0; i < width; ++i)
			{
			double angleDifference = ((float)i / width) * fov - fov/2;
			angleDifference = angleDifference * ((fov/2 - Math.abs(angleDifference)) / (fov/2) * 0.1 + 0.9);
			double checkedAngle = lookAngle + angleDifference;
			while (checkedAngle > 360) checkedAngle-= 360;
			while (checkedAngle < 0) checkedAngle+= 360;
			calculateDistanceToWall(wallStruct, checkedAngle);
			double distance = removeFishEye(wallStruct.distance, Math.abs(angleDifference));
			double wallPercentHeight = (PROJECTION_PLANE_DISTANCE / distance) / projectionPlaneHeight;
			int wallHeight = (int)(height * wallPercentHeight);
			g.setColor(map.getCeilingColor());
			g.drawLine(i, 0, i, (height - wallHeight) / 2);
			g.setColor(map.getFloorColor());
			g.drawLine(i, wallHeight + (height - wallHeight) / 2, i, height);
			if (wallStruct.texture != null)
				{
				g.drawImage(
						wallStruct.texture,
						i, 0 + (height - wallHeight) / 2, i+1, wallHeight + (height - wallHeight) / 2,
						0, 0, 1, wallStruct.texture.getHeight(), 
						null, null
						);
				}
//			int floorPixelCount = (height - wallHeight) / 2;
//			double dx = wallStruct.xWallHit - position.getX();
//			double dy = wallStruct.yWallHit - position.getY();
//			for (int j = 0; j < floorPixelCount; ++j)
//				{
//				int currentFloorPixel = (wallHeight + (height - wallHeight) / 2) + j;
//				double currentDistance = 1/(((currentFloorPixel - ((double)height - currentFloorPixel))/height)/(width / (double)height * 0.75));
//				double floorX = Math.abs(wallStruct.xWallHit + dx * currentDistance/distance);
//				double floorY = Math.abs(wallStruct.yWallHit + dy * currentDistance/distance);
//				int color = map.getFloorPixel(floorX - (int)floorX, floorY - (int)floorY);
//				g.setColor(new Color(color));
//				g.drawLine(
//						i, currentFloorPixel,
//						i, currentFloorPixel
//						);
//				}
			}
//		g.drawString(""+lookAngle, 100, 100);
//		g.drawString(""+calculateDistanceToWall(lookAngle), 100, 110);
		}
	private double removeFishEye(double distance, double angleFromCenter)
		{
		return Math.cos(Math.toRadians(angleFromCenter)) * distance;
		}
	private void calculateDistanceToWall(Wall struct, double angle)
		{
		double totalDistance = 0;
		double xCurrentMapPosition = position.getX();
		double yCurrentMapPosition = position.getY();
		int xCurrentMapSquare = (int)position.getX();
		int yCurrentMapSquare = (int)position.getY();
		int mapSquareDX = 0;
		int mapSquareDY = 0;
		int quarter = 0;
		double xEdgeDistance = 0;
		double yEdgeDistance = 0;
		double xEdgeCrossDistance = 0;
		double yEdgeCrossDistance = 0;
		
		if (angle < 90)
			{
			mapSquareDX = 1;
			mapSquareDY = -1;
			quarter = 1;
			}
		else if (angle < 180)
			{
			mapSquareDX = 1;
			mapSquareDY = 1;
			quarter = 2;
			}
		else if (angle < 270)
			{
			mapSquareDX = -1;
			mapSquareDY = 1;
			quarter = 3;
			}
		else
			{
			mapSquareDX = -1;
			mapSquareDY = -1;
			quarter = 4;
			}
//		System.out.println("quarter: " + quarter);
		while (true)
			{
//			System.out.println("===================");
//			System.out.println("Map square: " + xCurrentMapSquare + ", " + yCurrentMapSquare);
//			System.out.println("Position: " + xCurrentMapPosition + ", " + yCurrentMapPosition);
			switch (quarter)
				{
				case 1:
					xEdgeDistance = xCurrentMapSquare + 1 - xCurrentMapPosition;
					yEdgeDistance = yCurrentMapPosition - yCurrentMapSquare;
					break;
				case 2:
					xEdgeDistance = xCurrentMapSquare + 1 - xCurrentMapPosition;
					yEdgeDistance = yCurrentMapSquare + 1 - yCurrentMapPosition;
					break;
				case 3:
					xEdgeDistance = xCurrentMapPosition - xCurrentMapSquare;
					yEdgeDistance = yCurrentMapSquare + 1 - yCurrentMapPosition;
					break;
				case 4:
					xEdgeDistance = xCurrentMapPosition - xCurrentMapSquare;
					yEdgeDistance = yCurrentMapPosition - yCurrentMapSquare;
					break;
				}
			xEdgeCrossDistance = Math.abs(xEdgeDistance / Math.sin(Math.toRadians(angle)));
			yEdgeCrossDistance = Math.abs(yEdgeDistance / Math.cos(Math.toRadians(angle)));
//			System.out.println("Edge distance: " + xEdgeDistance + ", " + yEdgeDistance);
//			System.out.println("Edge cross distance: " + xEdgeCrossDistance + ", " + yEdgeCrossDistance);
			if (xEdgeCrossDistance < yEdgeCrossDistance)
				{
				xCurrentMapSquare += mapSquareDX;
				if (mapSquareDX == 1)
					xCurrentMapPosition = xCurrentMapSquare;
				else
					xCurrentMapPosition = xCurrentMapSquare + 1;
				yCurrentMapPosition -= Math.cos(Math.toRadians(angle)) * xEdgeCrossDistance;
				totalDistance += xEdgeCrossDistance;
				}
			else
				{
				yCurrentMapSquare += mapSquareDY;
				xCurrentMapPosition += Math.sin(Math.toRadians(angle)) * yEdgeCrossDistance;
				if (mapSquareDY == 1)
					yCurrentMapPosition = yCurrentMapSquare;
				else
					yCurrentMapPosition = yCurrentMapSquare + 1;
				totalDistance += yEdgeCrossDistance;
				}
//			System.out.println("total distance: " + totalDistance);
			if (xCurrentMapSquare < 0 || xCurrentMapSquare >= map.getWidth()
					|| yCurrentMapSquare < 0 || yCurrentMapSquare >= map.getHeight()
					)
					{
					struct.distance = 0;
					struct.texture = null;
					return;
					}
			int mapField = map.getWall(xCurrentMapSquare, yCurrentMapSquare);
			if (mapField >= 0)
				{
				double textureOffset;
				struct.distance = totalDistance;
				if (xEdgeCrossDistance < yEdgeCrossDistance)
					textureOffset = yCurrentMapPosition - (int)yCurrentMapPosition;
				else
					textureOffset = xCurrentMapPosition - (int)xCurrentMapPosition;
				struct.texture = map.getWallTextureSlice(xCurrentMapSquare, yCurrentMapSquare, textureOffset);
				struct.xWallHit = xCurrentMapPosition;
				struct.yWallHit = yCurrentMapPosition;
				return;
				}
			}
		}
	@SuppressWarnings("unused")
	private class Wall
		{
		double distance;
		double xWallHit;
		double yWallHit;
		BufferedImage texture;
		
		}
	}
