package com.projectkroma.terrabyte.opengl;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL40;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryUtil;

public class TextureArray implements ITexture
{

	private int texture;
	private List<Image> textures;

	public TextureArray()
	{
		this.textures = new ArrayList<Image>();
	}

	public int addTexture(Image rawImage)
	{
		this.textures.add(rawImage);
		return textures.indexOf(rawImage);
	}

	public void compile(boolean mipmap, boolean linear, int texSize)
	{
		this.texture = GL11.glGenTextures();
		bind();
		ByteBuffer buf = MemoryUtil.memAlloc(texSize * texSize * 4 * textures.size());
		for (int i = 0; i < textures.size(); i++)
		{
			buf.put(textures.get(i).pixels);
		}
		buf.flip();

		GL46.glTexImage3D(GL46.GL_TEXTURE_2D_ARRAY, 0, GL46.GL_RGBA, texSize, texSize, textures.size(), 0, GL46.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		if (mipmap)
		{
			GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D_ARRAY);
			GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_NEAREST);
		} else
		{
			if (linear)
			{
				GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_LINEAR);
				GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_LINEAR);
			} else
			{
				GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_NEAREST);
				GL11.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_NEAREST);
			}
		}
		GL15.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_WRAP_S, GL15.GL_CLAMP_TO_EDGE);
		GL15.glTexParameteri(GL40.GL_TEXTURE_2D_ARRAY, GL15.GL_TEXTURE_WRAP_T, GL15.GL_CLAMP_TO_EDGE);
		unbind();
		MemoryUtil.memFree(buf);
	}

	public void close()
	{
		GL15.glDeleteTextures(texture);
	}

	public void bind()
	{
		GL11.glBindTexture(GL40.GL_TEXTURE_2D_ARRAY, texture);
	}

	public void unbind()
	{
		GL11.glBindTexture(GL40.GL_TEXTURE_2D_ARRAY, 0);
	}
}
