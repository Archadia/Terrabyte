package com.projectkroma.terrabyte.ui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;
import org.json.JSONObject;

public class ScreenElement
{

	private ScreenElement parent;
	private List<ScreenElement> children;

	private JSONObject graphicsData;
	private JSONObject elementData;

	private ScreenSpace positionPolicy;
	private ScreenSpace dimensionPolicy;

	public ScreenElement(ScreenElement parent, ScreenSpace positionPolicy, ScreenSpace dimensionPolicy)
	{
		this.children = new ArrayList<ScreenElement>();
		this.parent = parent;
		this.graphicsData = new JSONObject();
		this.elementData = new JSONObject();
		this.positionPolicy = positionPolicy;
		this.dimensionPolicy = dimensionPolicy;
		if (this.parent != null)
		{
			parent.add(this);
		}
	}

	public JSONObject data()
	{
		return elementData;
	}

	public JSONObject graphics()
	{
		return graphicsData;
	}

	public ScreenElement getParent()
	{
		return parent;
	}

	public void add(ScreenElement element)
	{
		this.children.add(element);
	}

	public ScreenSpace getPositionPolicy()
	{
		return positionPolicy;
	}

	public ScreenSpace getDimensionPolicy()
	{
		return dimensionPolicy;
	}

	public int getWidth()
	{
		if (dimensionPolicy.getPolicy() == ScreenSpaceType.ABSOLUTE)
		{
			return (int) dimensionPolicy.getA();
		}
		if (dimensionPolicy.getPolicy() == ScreenSpaceType.RELATIVE)
		{
			if (parent != null)
			{
				return (int) (dimensionPolicy.getA() * parent.getWidth());
			}
		}
		return 0;
	}

	public int getHeight()
	{
		if (dimensionPolicy.getPolicy() == ScreenSpaceType.ABSOLUTE)
		{
			return (int) dimensionPolicy.getB();
		}
		if (dimensionPolicy.getPolicy() == ScreenSpaceType.RELATIVE)
		{
			if (parent != null)
			{
				return (int) (dimensionPolicy.getB() * parent.getHeight());
			}
		}
		return 0;
	}

	public int getX()
	{
		if (positionPolicy.getPolicy() == ScreenSpaceType.ABSOLUTE)
		{
			return (int) positionPolicy.getA();
		}
		if (positionPolicy.getPolicy() == ScreenSpaceType.RELATIVE)
		{
			if (parent != null)
			{
				return (int) (parent.getX() + positionPolicy.getA() * parent.getWidth());
			}
		}
		return 0;
	}

	public int getY()
	{
		if (positionPolicy.getPolicy() == ScreenSpaceType.ABSOLUTE)
		{
			return (int) positionPolicy.getB();
		}
		if (positionPolicy.getPolicy() == ScreenSpaceType.RELATIVE)
		{
			if (parent != null)
			{
				return (int) (parent.getY() + positionPolicy.getB() * parent.getHeight());
			}
		}
		return 0;
	}

	public boolean intersects(Rectangle rectangle)
	{
		Rectangle element = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return element.intersects(rectangle);
	}

	public boolean inside(Vector2i point)
	{
		Rectangle element = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return element.contains(point.x, point.y);
	}

	public boolean inside(int x, int y)
	{
		Rectangle element = new Rectangle(getX(), getY(), getWidth(), getHeight());
		return element.contains(x, y);
	}
}
