package com.projectkroma.terrabyte.renderer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.projectkroma.terrabyte.opengl.ITexture;
import com.projectkroma.terrabyte.opengl.VAO;

// TODO: Add multiple textures.

public class PipelineObject implements Iterable<String>
{
	public VAO vao;
	public ITexture texture;

	private Map<String, Object> objectUniforms = new HashMap<String, Object>();

	public boolean visable = true;

	public PipelineObject()
	{

	}

	public PipelineObject(VAO vao, ITexture texture)
	{
		this.vao = vao;
		this.texture = texture;
	}

	public void setObjectUniform(String uniform, Object object)
	{
		this.objectUniforms.put(uniform, object);
	}

	public Object getObjectUniform(String uniform)
	{
		return this.objectUniforms.get(uniform);
	}

	@Override
	public Iterator<String> iterator()
	{
		return objectUniforms.keySet().iterator();
	}

	public static void setArrayObjectUniform(PipelineObject[] pipelineObjects, String uniform, Object object)
	{
		for (PipelineObject po : pipelineObjects)
			po.setObjectUniform(uniform, object);
	}
}
