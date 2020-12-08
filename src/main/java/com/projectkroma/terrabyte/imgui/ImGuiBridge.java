package com.projectkroma.terrabyte.imgui;

import imgui.ImDrawData;
import imgui.ImGuiImplTerrabyte;

public interface ImGuiBridge
{
	void newFrameGLFW();
	void renderGL(ImDrawData drawData);
	void dispose();	
	
	public static ImGuiBridge newBridge(long window, String glslVersion)
	{
		return new ImGuiImplTerrabyte(window, glslVersion);
	}
}
