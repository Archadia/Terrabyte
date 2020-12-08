package com.projectkroma.terrabyte.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import com.projectkroma.terrabyte.util.Hints;

public class Texture implements ITexture
{
	private int glTextureID;
	
	public Texture(int[] dataI32, int width, int height)
	{
		setup();
		GL15.glTexImage2D(GL15.GL_TEXTURE_2D, 0, GL15.GL_RGBA, width, height, 0, GL15.GL_RGBA, GL15.GL_UNSIGNED_BYTE, dataI32);
	}
	
	public Texture(byte[] dataI8, int width, int height)
	{
		setup();
		ByteBuffer buf = MemoryUtil.memAlloc(dataI8.length);
		buf.put(dataI8).flip();
		GL15.glTexImage2D(GL15.GL_TEXTURE_2D, 0, GL15.GL_RGBA, width, height, 0, GL15.GL_RGBA, GL15.GL_UNSIGNED_BYTE, buf);
		MemoryUtil.memFree(buf);
	}
	
	public Texture(char[] dataUI8, int width, int height)
	{
		setup();
		ByteBuffer buf = MemoryUtil.memAlloc(dataUI8.length);
		for(char d : dataUI8)
			buf.putChar(d);
		buf.flip();
		GL15.glTexImage2D(GL15.GL_TEXTURE_2D, 0, GL15.GL_RGBA, width, height, 0, GL15.GL_RGBA, GL15.GL_UNSIGNED_BYTE, buf);
		MemoryUtil.memFree(buf);
	}
	
	public void setup()
	{
		this.glTextureID = GL15.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	}
	
	public void hint(Hints hints)
	{
		for(int key : hints)
		{
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, key, hints.get(key));
		}
	}
	
	@Override
	public void bind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureID);
	}
	
	public static void unbind()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public int getID()
	{
		return glTextureID;
	}
}
