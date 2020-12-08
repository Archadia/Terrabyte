package com.projectkroma.terrabyte.opengl.shader;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.projectkroma.terrabyte.engine.TerrabyteEngine;
import com.projectkroma.terrabyte.util.IBindable;

public class Shader implements IBindable
{
	private int glProgramID;
	private Map<Integer, Integer> shaders;
	private Map<String, Integer> locations;

	public Shader()
	{
		this.glProgramID = GL30.glCreateProgram();
		this.shaders = new HashMap<Integer, Integer>();
		this.locations = new HashMap<String, Integer>();
	}
	
	public Shader attachShader(String source, int type)
	{
		int shader = GL30.glCreateShader(type);
		
		GL30.glShaderSource(shader, source);
		GL30.glCompileShader(shader);
		
		int success = GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE)
		{
			System.err.println(TerrabyteEngine.LOG_PREFIX + " Failed to compile " + type + ".");
			int logLength = GL30.glGetShaderi(shader, GL30.GL_INFO_LOG_LENGTH);
			if (logLength > 1)
			{
				String log = GL30.glGetShaderInfoLog(shader);
				System.err.println("\t" + log);
			}
		}
		GL20.glAttachShader(glProgramID, shader);
		shaders.put(type, shader);
		return this;
	}
	
	public Shader link()
	{
		GL30.glLinkProgram(glProgramID);
		GL30.glValidateProgram(glProgramID);

		int success = GL30.glGetProgrami(glProgramID, GL30.GL_LINK_STATUS);
		if (success == GL11.GL_FALSE)
		{
			System.err.println(TerrabyteEngine.LOG_PREFIX + " Failed to link program.");
			int logLength = GL30.glGetProgrami(glProgramID, GL30.GL_INFO_LOG_LENGTH);
			if (logLength > 1)
			{
				String log = GL30.glGetProgramInfoLog(glProgramID);
				System.err.println("\t" + log);
			}
		}
		return this;
	}
	
	private int getLocation(String key)
	{
		if (locations.containsKey(key))
		{
			return locations.get(key);
		} else
		{
			int location = GL30.glGetUniformLocation(glProgramID, key);
			locations.put(key, location);
			return location;
		}
	}
	
	public void load(String key, Object value)
	{
		int location = getLocation(key);
		if (value instanceof Integer)
		{
			GL30.glUniform1i(location, (int) value);
		} else if (value instanceof Float)
		{
			GL30.glUniform1f(location, (float) value);
		} else if (value instanceof Vector2f)
		{
			GL30.glUniform2f(location, ((Vector2f) value).x, ((Vector2f) value).y);
		} else if (value instanceof Vector3f)
		{
			Vector3f vec = (Vector3f) value;
			GL20.glUniform3f(location, vec.x, vec.y, vec.z);
		} else if (value instanceof Vector4f)
		{
			Vector4f vec = (Vector4f) value;
			GL20.glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
		} else if (value instanceof Boolean)
		{
			GL20.glUniform1f(location, (boolean) value ? 1 : 0);
		} else if (value instanceof Matrix4f)
		{
			FloatBuffer buffer = MemoryUtil.memAllocFloat(16);
			((Matrix4f) value).get(buffer);
			GL20.glUniformMatrix4fv(location, false, buffer);
			MemoryUtil.memFree(buffer);
		} else if (value instanceof Color)
		{
			Color vec = (Color) value;
			GL20.glUniform4f(location, vec.getRed() / 255f, vec.getGreen() / 255f, vec.getBlue() / 255f, vec.getAlpha() / 255f);
		}
	}
	
	public void free()
	{
		unbind();
		for(int type : shaders.keySet())
		{
			GL30.glDeleteShader(shaders.get(type));			
		}
		GL30.glDeleteProgram(glProgramID);
	}
	
	@Override
	public void bind()
	{
		GL30.glUseProgram(glProgramID);
	}
	
	public static void unbind()
	{
		GL30.glUseProgram(0);
	}

	public int getID()
	{
		return glProgramID;
	}
}
