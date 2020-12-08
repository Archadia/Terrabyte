package com.projectkroma.terrabyte.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.projectkroma.terrabyte.engine.Display;
import com.projectkroma.terrabyte.opengl.Texture;
import com.projectkroma.terrabyte.opengl.VAO;
import com.projectkroma.terrabyte.opengl.shader.Shader;
import com.projectkroma.terrabyte.renderer.IShaderPipeline;
import com.projectkroma.terrabyte.renderer.PipelineObject;
import com.projectkroma.terrabyte.renderer.StandardShaderPipeline;
import com.projectkroma.terrabyte.util.Hints;

public class FontRenderer
{
	private static String vertexShaderSource = "#version 440\n" + "\n" + "in layout(location=0) vec3 vertex;\n" + "in layout(location=1) vec2 texel;\n" + "\n" + "uniform mat4 projection;\n"
			+ "uniform mat4 transformation;\n" + "\n" + "out vec2 p_texel;\n" + "\n" + "void main()\n" + "{\n" + "	gl_Position = projection * transformation * vec4(vertex, 1);\n"
			+ "	p_texel = texel;\n" + "}";

	private static String fragmentShaderSource = "#version 440\n" + "\n" + "in vec2 p_texel;\n" + "\n" + "out vec4 gl_FragColor;\n" + "\n" + "uniform vec4 color;\n" + "uniform sampler2D map;\n" + "\n"
			+ "void main()\n" + "{\n" + "	gl_FragColor = texture(map, p_texel);\n" + "	if(gl_FragColor.a != 0)\n" + "	{\n" + "		gl_FragColor = vec4(color.rgb, gl_FragColor.a);\n" + "	}\n"
			+ "}";

	public static class FontFace
	{
		private Map<Character, FontChar> smallCharMap;
		private Map<Character, FontChar> mediumCharMap;
		private Map<Character, FontChar> largeCharMap;

		private FontFace()
		{
			this.smallCharMap = new HashMap<Character, FontChar>();
			this.mediumCharMap = new HashMap<Character, FontChar>();
			this.largeCharMap = new HashMap<Character, FontChar>();
		}
	}

	public static class FontChar
	{
		public Texture texture;
		public int width;
		public int height;
	}

	public static final String chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890[{]};:'@#~,<.>/?\\|`¬!\"£$%^&*()-_=+ ";

	private VAO vao;
	private IShaderPipeline pipeline;

	private Display display;

	public FontRenderer(Display display)
	{
		this.display = display;

		vao = new VAO();
		vao.bind();
		vao.buffer(0, 3, new float[] { 0, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0 });
		vao.buffer(1, 2, new float[] { 0, 0, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1 });
		VAO.unbind();

		this.pipeline = new StandardShaderPipeline(new Shader().attachShader(vertexShaderSource, GL30.GL_VERTEX_SHADER).attachShader(fragmentShaderSource, GL30.GL_FRAGMENT_SHADER).link());
		pipeline.setShaderUniform("projection", new Matrix4f().ortho(0, display.getWindowSize().x, display.getWindowSize().y, 0, 0.0f, 10f).scale(1f));
		pipeline.setAutoClear(true);
	}

	private static final int SMALL_FONT_SIZE = 64;
	private static final int MEDIUM_FONT_SIZE = 128;
	private static final int LARGE_FONT_SIZE = 256;

	public static FontFace loadFontFace(Font font)
	{
		FontFace fontFace = new FontFace();
		for (char c : chars.toCharArray())
		{
			fontFace.smallCharMap.put(c, loadChar(c, font, SMALL_FONT_SIZE));
			fontFace.mediumCharMap.put(c, loadChar(c, font, MEDIUM_FONT_SIZE));
			fontFace.largeCharMap.put(c, loadChar(c, font, LARGE_FONT_SIZE));
		}
		return fontFace;
	}

	private static FontChar loadChar(char c, Font baseFont, int fontSize)
	{
		BufferedImage temp = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		Font font = baseFont.deriveFont((float) fontSize);
		FontRenderContext frc = new FontRenderContext(null, true, true);
		TextLayout layout = new TextLayout("" + c, font, frc);

		FontMetrics metrics = temp.getGraphics().getFontMetrics(font);

		int width = metrics.charWidth(c);
		int height = metrics.getHeight();

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = bi.createGraphics();

		g2d.setFont(font);
		g2d.setColor(Color.black);
		layout.draw(g2d, 0, height - metrics.getDescent());
		g2d.dispose();

		byte[] pixels = new byte[width * height * 4];
		for (int i = 0; i < width * height; i++)
		{
			int argb = bi.getRGB(i % width, (int) Math.floor(i / width));

			int a = (argb & 0xff000000) >> 24;
			int r = (argb & 0xff0000) >> 16;
			int g = (argb & 0xff00) >> 8;
			int b = (argb & 0xff);
			pixels[(i * 4) + 0] = (byte) r;
			pixels[(i * 4) + 1] = (byte) g;
			pixels[(i * 4) + 2] = (byte) b;
			pixels[(i * 4) + 3] = (byte) a;
		}

		Texture texture = new Texture(pixels, width, height);
		texture.hint(Hints.with(GL15.GL_TEXTURE_MIN_FILTER, GL15.GL_LINEAR).and(GL15.GL_TEXTURE_MAG_FILTER, GL15.GL_LINEAR));

		FontChar ch = new FontChar();
		ch.texture = texture;
		ch.width = width;
		ch.height = height;

		return ch;
	}

	public void drawText(FontFace fontFace, String str, int x, int y, int targetHeight, Color color)
	{
		int caret = 0;

		for (int i = 0; i < str.length(); i++)
		{
			var charMap = fontFace.mediumCharMap;
			if (targetHeight <= 50)
			{
				charMap = fontFace.smallCharMap;
			} else if (targetHeight <= 150)
			{
				charMap = fontFace.mediumCharMap;
			} else
			{
				charMap = fontFace.largeCharMap;
			}
			FontChar c = charMap.get(str.charAt(i));

			if (c == null)
				continue;

			float ratio = (float) c.width / (float) c.height;

			PipelineObject po = new PipelineObject(vao, c.texture);
			po.setObjectUniform("color", color);
			po.setObjectUniform("projection", new Matrix4f().ortho(0, display.getWindowSize().x, display.getWindowSize().y, 0, 0.0f, 10f).scale(1f));
			po.setObjectUniform("transformation", new Matrix4f().translate(x + caret, y, 0).scale(targetHeight * ratio, targetHeight, 0));
			pipeline.addPipelineObject(po);
			caret += targetHeight * ratio;
		}
	}

	public void draw()
	{
		pipeline.draw();
	}
}
