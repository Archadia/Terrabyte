package com.projectkroma.terrabyte.engine;

import com.projectkroma.terrabyte.pixel.PixelRender;

public interface Frame
{
	void update(double time, double dt);
	void draw(PixelRender pixelRender);
}
