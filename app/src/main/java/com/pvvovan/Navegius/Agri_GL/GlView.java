package com.pvvovan.Navegius.Agri_GL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.pvvovan.Navegius.Geometry.CartPoint;

import java.util.ArrayList;

/**
 * Created by vovan on 07.01.16.
 */
public class GlView extends GLSurfaceView {
    public GlView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        gl2Renderer = new Gl2Renderer(context);
        setRenderer(gl2Renderer);
    }

    public GlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        // Set the Renderer for drawing on the GLSurfaceView
        gl2Renderer = new Gl2Renderer(context);
        setRenderer(gl2Renderer);
    }

    Gl2Renderer gl2Renderer;
    public float GetZoom()
    {
        return gl2Renderer.GetZoom();
    }

    public void SetZoom(float zoom)
    {
        gl2Renderer.SetZoom(zoom);
    }

    public void Set4()
    {
        gl2Renderer.SetPolygons();
    }

    public void SetPosition(Vector3 position, float rotationAngle)
    {
        gl2Renderer.SetCameraPosition(position, rotationAngle);
    }

    public void SetTriangles(ArrayList<Tetragon> tetragons)
    {
        gl2Renderer.SetTriangles(tetragons);
    }

    public void SetLines(ArrayList<Vector3> points)
    {
        gl2Renderer.SetLines(points);
    }

    public void SetRedLine(ArrayList<Vector3> points)
    {
        gl2Renderer.SetRedLine(points);
    }
}
