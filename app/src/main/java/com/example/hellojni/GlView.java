package com.example.hellojni;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by Vovan on 14.05.2016.
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
}
