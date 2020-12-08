package com.projectkroma.terrabyte.renderer;

import java.util.HashMap;
import java.util.Map;

public class RendererManager
{
	private Map<String, IShaderPipeline> pipelines;

	public RendererManager()
	{
		this.pipelines = new HashMap<String, IShaderPipeline>();
	}

	public void registerPipeline(String identifier, IShaderPipeline pipeline)
	{
		this.pipelines.put(identifier, pipeline);
	}

	public IShaderPipeline getPipeline(String identifier)
	{
		return this.pipelines.get(identifier);
	}

	public void addPipelineObject(String shaderKey, PipelineObject... pipelineObjects)
	{
		for (PipelineObject po : pipelineObjects)
			this.pipelines.get(shaderKey).addPipelineObject(po);
	}

	public void draw()
	{
		for (String shaderPipeline : pipelines.keySet())
		{
			pipelines.get(shaderPipeline).draw();
		}
	}
}
