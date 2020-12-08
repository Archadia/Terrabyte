package com.projectkroma.terrabyte.math;

import org.joml.AABBf;
import org.joml.Vector3f;

public class AABB
{

	private float minX;
	private float minY;
	private float minZ;

	private float maxX;
	private float maxY;
	private float maxZ;

	public AABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}

	public AABB(AABB template)
	{
		this(template.minX, template.minY, template.minZ, template.maxX, template.maxY, template.maxZ);
	}

	public AABB(Vector3f min, Vector3f max)
	{
		this(min.x, min.y, min.z, max.x, max.y, max.z);
	}

	public AABB move(Vector3f vec)
	{
		this.minX += vec.x;
		this.minY += vec.y;
		this.minZ += vec.z;
		this.maxX += vec.x;
		this.maxY += vec.y;
		this.maxZ += vec.z;
		return this;
	}

	public AABB move(float x, float y, float z)
	{
		this.minX += x;
		this.minY += y;
		this.minZ += z;
		this.maxX += x;
		this.maxY += y;
		this.maxZ += z;
		return this;
	}

	public AABBf joml()
	{
		return new AABBf(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public boolean intersects(AABB other)
	{
		return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
	}

	public boolean inside(Vector3f point)
	{
		if (point.x >= minX && point.x < maxX)
		{
			if (point.y >= minY && point.y < maxY)
			{
				if (point.z >= minZ && point.z < maxZ)
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean intersects(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
	{
		if (this.minX <= maxX && this.maxX >= minX)
		{
			if (this.minY <= maxY && this.maxY >= minY)
			{
				if (this.minZ <= maxZ && this.maxZ >= minZ)
				{
					return true;
				}
			}
		}
		return false;
	}

	public float getMinX()
	{
		return minX;
	}

	public float getMinY()
	{
		return minY;
	}

	public float getMinZ()
	{
		return minZ;
	}

	public float getMaxX()
	{
		return maxX;
	}

	public float getMaxY()
	{
		return maxY;
	}

	public float getMaxZ()
	{
		return maxZ;
	}

	public Vector3f getMin()
	{
		return new Vector3f(minX, minY, minZ);
	}

	public Vector3f getMax()
	{
		return new Vector3f(maxX, maxY, maxZ);
	}

	public Vector3f getCenterf()
	{
		return new Vector3f((float) (minX + (maxX - minX) / 2f), (float) (minY + (maxY - minY) / 2f), (float) (minZ + (maxZ - minZ) / 2f));
	}

	public float getCenterX()
	{
		return (float) (minX + (maxX - minX) / 2d);
	}

	public float getCenterY()
	{
		return (float) (minY + (maxY - minY) / 2d);
	}

	public float getCenterZ()
	{
		return (float) (minZ + (maxZ - minZ) / 2d);
	}

	public float getWidth()
	{
		return Math.abs(maxX - minX);
	}

	public float getHeight()
	{
		return Math.abs(maxY - minY);
	}

	public float getDepth()
	{
		return Math.abs(maxZ - minZ);
	}
}
