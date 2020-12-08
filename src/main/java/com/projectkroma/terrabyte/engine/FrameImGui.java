package com.projectkroma.terrabyte.engine;

import imgui.ImGuiStyle;

public interface FrameImGui extends Frame
{
	void drawImGui();
	void customImGuiStyle(ImGuiStyle style);
}
