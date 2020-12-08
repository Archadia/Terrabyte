package com.projectkroma.terrabyte.renderer;

public interface IShaderPipeline
{
	public void setShaderUniform(String uniform, Object object);

	public void remShaderUniform(String uniform);

	public void setAutoClear(boolean clear);

	public boolean isAutoClear();

	public void addPipelineObject(PipelineObject pipelineObject);

	public void draw();
}
