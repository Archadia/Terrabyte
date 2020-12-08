package com.projectkroma.terrabyte.opengl.shader;

import java.awt.Color;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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

import com.projectkroma.terrabyte.util.IBindable;

// TODO: Test new shader class, delete this one.
public class ShaderObsolete implements IBindable
{

	private int program;

	private Map<String, Integer> locations;
	private int vertexShader;
	private int fragmentShader;

	private int getLocation(String key)
	{
		if (locations.containsKey(key))
		{
			return locations.get(key);
		} else
		{
			int location = GL30.glGetUniformLocation(program, key);
			locations.put(key, location);
			return location;
		}
	}

	private static int attachShaderFromSource(int program, int type, String source)
	{
		int shader = GL30.glCreateShader(type);

		GL30.glShaderSource(shader, source);
		GL30.glCompileShader(shader);

		int success = GL30.glGetShaderi(shader, GL30.GL_COMPILE_STATUS);
		if (success == GL11.GL_FALSE)
		{
			System.err.println("RuntimeException: failed to compile " + getShaderName(type) + ".");
			int logLength = GL30.glGetShaderi(shader, GL30.GL_INFO_LOG_LENGTH);
			if (logLength > 1)
			{
				String log = GL30.glGetShaderInfoLog(shader);
				System.err.println(log);
			}
		}
		GL20.glAttachShader(program, shader);
		return shader;
	}

	private static int attachShaderFromPath(int program, int type, String filePath) throws IOException
	{
		return attachShaderFromSource(program, type, Files.readString(FileSystems.getDefault().getPath(filePath)));
	}

	public static String getShaderName(int type)
	{
		if (type == GL30.GL_VERTEX_SHADER)
		{
			return "Vertex Shader";
		} else if (type == GL30.GL_FRAGMENT_SHADER)
		{
			return "Fragment Shader";
		}
		return "Unknown Shader";
	}

	public static ShaderObsolete createShaderFromPath(String vertexPath, String fragmentPath) throws IOException
	{
		ShaderObsolete shader = new ShaderObsolete();
		shader.program = GL30.glCreateProgram();
		shader.vertexShader = attachShaderFromPath(shader.program, GL30.GL_VERTEX_SHADER, vertexPath);
		shader.fragmentShader = attachShaderFromPath(shader.program, GL30.GL_FRAGMENT_SHADER, fragmentPath);

		GL30.glLinkProgram(shader.program);
		GL30.glValidateProgram(shader.program);

		int success = GL30.glGetProgrami(shader.program, GL30.GL_LINK_STATUS);
		if (success == GL11.GL_FALSE)
		{
			System.err.println("RuntimeException: failed to link program.");
			int logLength = GL30.glGetProgrami(shader.program, GL30.GL_INFO_LOG_LENGTH);
			if (logLength > 1)
			{
				String log = GL30.glGetProgramInfoLog(shader.program);
				System.err.println(log);
			}
		}
		return shader;
	}

	public static ShaderObsolete createShaderFromSource(String vertexSource, String fragmentSource)
	{
		ShaderObsolete shader = new ShaderObsolete();
		shader.program = GL30.glCreateProgram();
		shader.vertexShader = attachShaderFromSource(shader.program, GL30.GL_VERTEX_SHADER, vertexSource);
		shader.fragmentShader = attachShaderFromSource(shader.program, GL30.GL_FRAGMENT_SHADER, fragmentSource);

		GL30.glLinkProgram(shader.program);
		GL30.glValidateProgram(shader.program);

		int success = GL30.glGetProgrami(shader.program, GL30.GL_LINK_STATUS);
		if (success == GL11.GL_FALSE)
		{
			System.err.println("RuntimeException: failed to link program.");
			int logLength = GL30.glGetProgrami(shader.program, GL30.GL_INFO_LOG_LENGTH);
			if (logLength > 1)
			{
				String log = GL30.glGetProgramInfoLog(shader.program);
				System.err.println(log);
			}
		}
		return shader;
	}

	private ShaderObsolete()
	{
		this.locations = new HashMap<String, Integer>();
	}

	public void bind()
	{
		GL30.glUseProgram(program);
	}

	public static void unbind()
	{
		GL30.glUseProgram(0);
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
			Matrix4f mat = (Matrix4f) value;
			mat.get(buffer);
			GL20.glUniformMatrix4fv(location, false, buffer);
			MemoryUtil.memFree(buffer);
		} else if (value instanceof Color)
		{
			Color vec = (Color) value;
			GL20.glUniform4f(location, vec.getRed() / 255f, vec.getGreen() / 255f, vec.getBlue() / 255f, vec.getAlpha() / 255f);
		}
	}

	public void close()
	{
		unbind();
		GL30.glDeleteShader(vertexShader);
		GL30.glDeleteShader(fragmentShader);
		GL30.glDeleteProgram(program);
	}
}
