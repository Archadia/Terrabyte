package com.projectkroma.terrabyte.engine;

import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

import com.projectkroma.terrabyte.engine.descriptors.KeyActionDescriptor;
import com.projectkroma.terrabyte.engine.descriptors.MouseActionDescriptor;
import com.projectkroma.terrabyte.engine.descriptors.MouseMoveDescriptor;
import com.projectkroma.terrabyte.engine.descriptors.WindowResizeDescriptor;
import com.projectkroma.terrabyte.imgui.ImGuiBridge;
import com.projectkroma.terrabyte.observer.Subject;
import com.projectkroma.terrabyte.opengl.Image;
import com.projectkroma.terrabyte.pixel.PixelRender;
import com.projectkroma.terrabyte.util.Hints;

import imgui.ImGui;

public class Display
{

	private Hints windowHints;
	public long window;
	private double ups;
	private double fps;
	private PixelRender pixelRender;

	public static final String DEFAULT_GLSL_VERSION = "440";
	public String glslVersion = DEFAULT_GLSL_VERSION;

	public Display(Hints windowHints)
	{
		if (windowHints == null)
			throw new NullPointerException("Display cannot be initialised without window hints.");
		this.windowHints = windowHints;
	}

	public void init()
	{
		GLFW.glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
		if (!GLFW.glfwInit())
		{
			System.exit(-1);
		}
		GLFW.glfwDefaultWindowHints();
		for (int hint : windowHints)
		{
			GLFW.glfwWindowHint(hint, windowHints.get(hint));
		}

		GLFW.glfwWindowHint(GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_COMPAT_PROFILE);
//		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
	}

	public void createWindow(int width, int height, String title, long monitor, long share, int pixelWidth, int pixelHeight)
	{
		window = GLFW.glfwCreateWindow(width, height, title, monitor, share);
		if (window == MemoryUtil.NULL)
		{
			GLFW.glfwTerminate();
			System.exit(-1);
		}
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();

		try
		{
			//TODO: provide better solution.
			Image icon = new Image("src/main/resources/icon_48.png");
			setProgramIcon(icon);
		} catch (IOException e)
		{
			System.err.println(TerrabyteEngine.LOG_PREFIX + " Error occurred while trying to import program icon.");
		}

		setupCallbacks();
		
		this.pixelRender = new PixelRender(this.getWindowSize().x, this.getWindowSize().y, pixelWidth, pixelHeight);
	}

	public void startRunLoop(Frame frame)
	{
		if (window == 0)
		{
			throw new NullPointerException("Window NULL - Not initialized.");
		}

		ImGuiBridge tgui = ImGuiBridge.newBridge(window, glslVersion);
		if(frame instanceof FrameImGui)
		{
			((FrameImGui) frame).customImGuiStyle(ImGui.getStyle());			
		}
		double t = 0.0;
		final double dt = 0.01;

		double currentTime = System.nanoTime() / 1000000000.0;
		double accumulator = 0.0;

		double ctFPS = System.nanoTime() / 1000000000.0;
		double ctUPS = System.nanoTime() / 1000000000.0;

		int countFPS = 0;
		int countUPS = 0;
		
		while (!GLFW.glfwWindowShouldClose(window))
		{
			double newTime = System.nanoTime() / 1000000000.0;
			double frameTime = newTime - currentTime;
			currentTime = newTime;

			accumulator += frameTime;

			while (accumulator >= dt)
			{
				if (frame != null)
					frame.update(t, dt);

				accumulator -= dt;
				countUPS += 1;
				double ntUPS = System.nanoTime() / 1000000000.0;
				if (ntUPS - ctUPS >= 1.0)
				{
					ups = countUPS;
					countUPS = 0;
					ctUPS = ntUPS;
				}
				t += 1;
			}
			if (frame != null)
			{
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				frame.draw(pixelRender);
				if(pixelRender != null)
					pixelRender.render();
				if(frame instanceof FrameImGui)
				{
					if (tgui != null)
					{
						tgui.newFrameGLFW();
						ImGui.newFrame();
	
						((FrameImGui) frame).drawImGui();
	
						ImGui.render();
						tgui.renderGL(ImGui.getDrawData());
					}
				}
			}
			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();

			countFPS += 1;
			double ntFPS = System.nanoTime() / 1000000000.0;
			if (ntFPS - ctFPS >= 1.0)
			{
				fps = countFPS;
				countFPS = 0;
				ctFPS = ntFPS;
			}
		}
		if (tgui != null)
		{
			tgui.dispose();
		}
		GLFW.glfwTerminate();
	}

	// glfw functions

	public void maximize()
	{
		GLFW.glfwMaximizeWindow(window);
	}

	public void minimize()
	{
		GLFW.glfwIconifyWindow(window);
	}

	public void restore()
	{
		GLFW.glfwRestoreWindow(window);
	}

	public void hide()
	{
		GLFW.glfwHideWindow(window);
	}

	public void show()
	{
		GLFW.glfwShowWindow(window);
	}

	public void focus()
	{
		GLFW.glfwFocusWindow(window);
	}

	public void requestAttention()
	{
		GLFW.glfwRequestWindowAttention(window);
	}

	// glfw setters

	public void setVSYNC(boolean b)
	{
		GLFW.glfwSwapInterval(b ? 1 : 0);
	}

	public void setForcedAspectRatio(int numerator, int denominator)
	{
		GLFW.glfwSetWindowAspectRatio(window, numerator, denominator);
	}

	public void setWindowSize(int width, int height)
	{
		GLFW.glfwSetWindowSize(window, width, height);
	}

	public void setWindowSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight)
	{
		GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
	}

	public void setWindowPosition(int x, int y)
	{
		GLFW.glfwSetWindowPos(window, x, y);
	}

	public void setWindowTitle(String title)
	{
		GLFW.glfwSetWindowTitle(window, title);
	}

	public void setInputMode(int mode, int value)
	{
		GLFW.glfwSetInputMode(window, mode, value);
	}

	public void setProgramIcon(Image icon)
	{
		ByteBuffer pixelBuffer = MemoryUtil.memAlloc(icon.width * icon.height * 4);
		pixelBuffer.put(icon.pixels);
		pixelBuffer.flip();

		GLFWImage.Buffer buffer = GLFWImage.malloc(icon.width * icon.height * 4);

		ByteBuffer imageBuffer = MemoryUtil.memAlloc(icon.width * icon.height * 4);
		GLFWImage iconImage = new GLFWImage(imageBuffer);
		iconImage.set(icon.width, icon.height, pixelBuffer);

		buffer.put(iconImage);
		buffer.flip();
		GLFW.glfwSetWindowIcon(window, buffer);

		iconImage.free();
		buffer.free();
		MemoryUtil.memFree(pixelBuffer);
	}

	public Vector2d getMousePosition()
	{
		double[] xpos = new double[1];
		double[] ypos = new double[1];
		GLFW.glfwGetCursorPos(window, xpos, ypos);
		return new Vector2d(xpos[0], ypos[0]);
	}

	public Vector2f getWindowContentScale()
	{
		float[] xscale = new float[1];
		float[] yscale = new float[1];
		GLFW.glfwGetWindowContentScale(window, xscale, yscale);
		return new Vector2f(xscale[0], yscale[0]);
	}

	public Vector2i getWindowSize()
	{
		int[] xscale = new int[1];
		int[] yscale = new int[1];
		GLFW.glfwGetWindowSize(window, xscale, yscale);
		return new Vector2i(xscale[0], yscale[0]);
	}

	public Vector2i getFramebufferSize()
	{
		int[] xscale = new int[1];
		int[] yscale = new int[1];
		GLFW.glfwGetFramebufferSize(window, xscale, yscale);
		return new Vector2i(xscale[0], yscale[0]);
	}

	public float getWindowAspectRatio()
	{
		Vector2i size = getWindowSize();
		return (float) size.x / (float) size.y;
	}

	// callbacks

	public Subject<KeyActionDescriptor> keyActionListener;
	public Subject<MouseActionDescriptor> mouseActionListener;
	public Subject<MouseMoveDescriptor> mouseMoveListener;
	public Subject<WindowResizeDescriptor> windowResizeListener;

	private void setupCallbacks()
	{
		keyActionListener = new Subject<KeyActionDescriptor>();
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->
		{
			keyActionListener.tell(new KeyActionDescriptor(key, action));
		});

		mouseActionListener = new Subject<MouseActionDescriptor>();
		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
		{
			mouseActionListener.tell(new MouseActionDescriptor(button, action));
		});

		mouseMoveListener = new Subject<MouseMoveDescriptor>();
		GLFW.glfwSetCursorPosCallback(window, (window, xpos, ypos) ->
		{
			mouseMoveListener.tell(new MouseMoveDescriptor(xpos, ypos));
		});

		windowResizeListener = new Subject<WindowResizeDescriptor>();
		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) ->
		{
			pixelRender.updateSize(width, height);
			windowResizeListener.tell(new WindowResizeDescriptor(width, height));
		});
	}
	
	public double getFPS()
	{
		return fps;
	}
	
	public double getUPS()
	{
		return ups;
	}
}
