package com.projectkroma.terrabyte.opengl;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import com.projectkroma.terrabyte.util.IBindable;

public class VAO implements IBindable
{

	private int vao;
	private int size;
	private int indicesSize;
	List<Integer> vbos;

	public VAO()
	{
		this.vao = GL30.glGenVertexArrays();
		this.vbos = new ArrayList<Integer>();
	}

	@Override
	public void bind()
	{
		GL30.glFlush();
		GL30.glBindVertexArray(vao);
	}

	public static void unbind()
	{
		GL30.glBindVertexArray(0);
	}

	public void drawElementsInstanced(int primative, int[] indices, int instances)
	{
		ByteBuffer buf = MemoryUtil.memAlloc(Integer.BYTES * 6);
		for (int i : indices)
		{
			buf.putInt(i);
		}
		buf.flip();
		GL31.glDrawElementsInstanced(primative, GL11.GL_UNSIGNED_INT, buf, instances);
	}

	public void drawArrays(int primitive)
	{
		GL30.glDrawArrays(primitive, 0, size);
	}

	public void drawElements(int primitive)
	{
		GL30.glDrawElements(primitive, indicesSize * 2, GL11.GL_UNSIGNED_SHORT, 0);
	}

	public void bufferIndices(int index, int size, float[] translations)
	{
		FloatBuffer buffer = MemoryUtil.memAllocFloat(translations.length);
		buffer.put(translations).flip();

		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		GL30.glVertexAttribPointer(index, size, GL30.GL_FLOAT, false, 0, 0);
		GL33.glVertexAttribDivisor(index, 1);
		GL30.glEnableVertexAttribArray(index);
	}

	public void buffer(int index, int size, float[] data)
	{
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		buffer.put(data).flip();

		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);

		GL30.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL30.glEnableVertexAttribArray(index);
		if (index == 0)
		{
			this.size = data.length / size;
		}
		MemoryUtil.memFree(buffer);
	}

	public void bufferIndices(int[] data)
	{
		ShortBuffer buffer = MemoryUtil.memAllocShort(data.length);
		for (int i : data)
		{
			buffer.put((short) i);
		}
		buffer.flip();

		int vbo = GL30.glGenBuffers();
		vbos.add(vbo);
		GL30.glBindBuffer(GL30.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL30.glBufferData(GL30.GL_ELEMENT_ARRAY_BUFFER, buffer, GL30.GL_STATIC_DRAW);
		this.indicesSize = data.length;
		MemoryUtil.memFree(buffer);
	}

	public void close()
	{
		GL30.glDeleteVertexArrays(vao);
		vbos.forEach(vbo -> GL30.glDeleteBuffers(vbo));
	}
}
