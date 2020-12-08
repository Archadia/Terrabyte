# Terrabyte
A library for making Java games, with OpenGL.

## Starting the Engine 
```java
  // Creates the engine instance.
  TerrabyteEngine engine = new TerrabyteEngine(ExampleFrame.class, "Example Title", 800, 600);
  // Begins the game loop.
  engine.start();
```
Where `ExampleFrame.class` is an implementation of the `Frame` interface. The framework also provides a `FrameImGui` interface, which allows the use of ImGui.

## Frame

Your `Frame` implementation requires a public constructor with no parameters, as such:
```java
public ExampleFrame()
{
  // Called just before the game loop is started.
}
```
The `Frame` implements an `update(time, dt)` function, and a `draw(pixelRender)` function. The double `dt` represents the time passed between updates (in seconds), and the double `time` represents the elapsed time since the program has begun (in seconds).
```java
void update(double time, double dt)
{
  // Update game world
}

void draw(PixelRender pixelRender)
{
  // Draw game world
}
```
#### PixelRender
The pixel render instance allows you to draw a primative pixelated image straight to the display.
