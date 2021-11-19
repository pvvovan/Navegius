package com.example.hellojni;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Vovan on 14.05.2016.
 */
public class Gl2Renderer implements GLSurfaceView.Renderer  {

    public Gl2Renderer(Context ctx)
    {
        //m_Context = ctx;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.0f, 0.5f, 0.0f, 0.5f);

    }
    private int m_ViewPortWidth;
    private int m_ViewPortHeight;
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        m_ViewPortWidth = width;
        m_ViewPortHeight = height;


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

    }
}
