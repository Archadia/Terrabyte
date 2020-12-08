package com.projectkroma.terrabyte.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.projectkroma.terrabyte.opengl.Texture;
import com.projectkroma.terrabyte.opengl.VAO;
import com.projectkroma.terrabyte.opengl.shader.Shader;

public class BatchedShaderPipeline implements IShaderPipeline
{
	public Shader shader;

	private boolean autoClear;

	private Map<VAO, List<PipelineObject>> batch;

	private Map<String, Object> shaderUniforms;

	public BatchedShaderPipeline(Shader shader)
	{
		this.shader = shader;
		this.batch = new HashMap<VAO, List<PipelineObject>>();
		this.shaderUniforms = new HashMap<String, Object>();
	}

	@Override
	public void setShaderUniform(String uniform, Object object)
	{
		this.shaderUniforms.put(uniform, object);
	}

	@Override
	public void remShaderUniform(String uniform)
	{
		this.shaderUniforms.remove(uniform);
	}

	@Override
	public void addPipelineObject(PipelineObject pipelineObject)
	{
		if (batch.containsKey(pipelineObject.vao))
		{
			batch.get(pipelineObject.vao).add(pipelineObject);
		} else
		{
			List<PipelineObject> list = new ArrayList<PipelineObject>();
			list.add(pipelineObject);
			batch.put(pipelineObject.vao, list);
		}
	}

	@Override
	public void draw()
	{
		shader.bind();
		for (String uniform : shaderUniforms.keySet())
		{
			shader.load(uniform, shaderUniforms.get(uniform));
		}

		for (VAO vao : batch.keySet())
		{
			List<PipelineObject> list = batch.get(vao);
			vao.bind();
			for (PipelineObject po : list)
			{
				if (po.texture != null)
				{
					po.texture.bind();
				}

				for (String uniform : po)
				{
					shader.load(uniform, po.getObjectUniform(uniform));
				}

				po.vao.drawArrays(GL11.GL_TRIANGLES);
			}
		}

		if (autoClear)
			batch.clear();

		VAO.unbind();
		Texture.unbind();
		Shader.unbind();
	}

	@Override
	public void setAutoClear(boolean clear)
	{
		this.autoClear = clear;
	}

	@Override
	public boolean isAutoClear()
	{
		return autoClear;
	}
}
