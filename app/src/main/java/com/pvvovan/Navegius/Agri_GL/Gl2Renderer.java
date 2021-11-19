package com.pvvovan.Navegius.Agri_GL;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.pvvovan.Navegius.Geometry.CartPoint;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by vovan on 07.01.16.
 */
public class Gl2Renderer implements GLSurfaceView.Renderer {

    private Camera m_camera;

    /**
     * Initialize the model data.
     */
    private Mesh triagMesh;
    private Mesh lineMesh;
    private Mesh line2Mesh;
    private Context m_Context;

    public Gl2Renderer(Context ctx)
    {
        m_Context = ctx;
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config)
    {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.0f, 0.5f, 0.0f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 10.0f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f; // 0.3, 5.0
        final float lookZ = -1.0f;

        // Set our up vector. This is where our head would be pointing were we holding the m_camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f; // 0.3, 5.0

        m_camera = new Camera(m_Context,
                new Vector3(eyeX, eyeY, eyeZ),
                new Vector3(lookX, lookY, lookZ),
                new Vector3(upX, upY, upZ));

        float[] triandVerticesData = {
                // X, Y, Z,
                // R, G, B, A
                0.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.8f, 1.0f,

                10.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.8f, 1.0f,

                10.0f, 10.0f, 0.0f,
                1.0f, 0.0f, 0.8f, 1.0f,

                0.0f, 10.0f, 0.0f,
                1.0f, 0.0f, 0.8f, 1.0f
        };
        short dsfrawOrder[] =
                {
                        0, 2, 1,
                        0, 3, 2
                };
        triagMesh = new Mesh(m_camera, triandVerticesData, dsfrawOrder);



        float[] liVerticesData = {
                // X, Y, Z,
                // R, G, B, A
                10000.0f, 10000.0f, 0.0f,
                1.0f, 1.0f, 0.8f, 1.0f,

                10020.0f, 10000.0f, 0.0f,
                1.0f, 1.0f, 0.8f, 1.0f,

                10010.0f, 10010.0f, 0.0f,
                1.0f, 1.0f, 0.8f, 1.0f,

                10000.0f, 10010.0f, 0.0f,
                1.0f, 1.0f, 0.8f, 1.0f
        };
        short lifrawOrder[] =
                {
                        0, 1,
                        2, 3
                };
        lineMesh = new Mesh(2.0f, m_camera, liVerticesData, lifrawOrder);




        float[] li2VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                10030.0f, 10015.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,

                10020.0f, 10010.0f, 0.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
        };
        short li2frawOrder[] =
                {
                        0, 1
                };
        line2Mesh = new Mesh(5.0f, m_camera, li2VerticesData, li2frawOrder);
    }

    private float m_zoom = 0.7f;
    void SetZoom(float zoom)
    {
        m_zoom = zoom;
        SetupCamera();
    }

    float GetZoom(){return m_zoom;}

    private int m_ViewPortWidth;
    private int m_ViewPortHeight;

    public void SetLines(ArrayList<Vector3> points)
    {
//        float[] liVerticesData = {
//                // X, Y, Z,
//                // R, G, B, A
//                0.0f, 0.0f, 0.0f,
//                1.0f, 1.0f, 0.8f, 1.0f,
//
//                20.0f, 0.0f, 0.0f,
//                1.0f, 1.0f, 0.8f, 1.0f,
//
//                10.0f, 10.0f, 0.0f,
//                1.0f, 1.0f, 0.8f, 1.0f,
//
//                0.0f, 10.0f, 0.0f,
//                1.0f, 1.0f, 0.8f, 1.0f
//        };
//
//        short lifrawOrder[] =
//                {
//                        0, 1,
//                        2, 3
//                };
//        lineMesh = new Mesh(2.0f, m_camera, liVerticesData, lifrawOrder);

        float[] verticesData = new float[points.size() * 7];
        int pos = 0;
        for(Vector3 p : points)
        {
            verticesData[0 + pos] = p.x;
            verticesData[1 + pos] = p.y;
            verticesData[2 + pos] = p.z;
            verticesData[3 + pos] = 1.0f;
            verticesData[4 + pos] = 1.0f;
            verticesData[5 + pos] = 0.8f;
            verticesData[6 + pos] = 1.0f;
            pos += 7;
        }
        short drawOrder[] = new short[points.size()];
        for(short i = 0; i < points.size(); i++)
        {
            drawOrder[i] = i;
        }

        lineMesh = new Mesh(2.0f, m_camera, verticesData, drawOrder);
    }

    public void SetRedLine(ArrayList<Vector3> points)
    {
        float[] verticesData = new float[points.size() * 7];
        int pos = 0;
        for(Vector3 p : points)
        {
            verticesData[0 + pos] = p.x;
            verticesData[1 + pos] = p.y;
            verticesData[2 + pos] = p.z;
            verticesData[3 + pos] = 1.0f;
            verticesData[4 + pos] = 0.0f;
            verticesData[5 + pos] = 0.0f;
            verticesData[6 + pos] = 1.0f;
            pos += 7;
        }
        short drawOrder[] = new short[points.size()];
        for(short i = 0; i < points.size(); i++)
        {
            drawOrder[i] = i;
        }

        line2Mesh = new Mesh(5.0f, m_camera, verticesData, drawOrder);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height)
    {
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);
        m_ViewPortWidth = width;
        m_ViewPortHeight = height;
        SetupCamera();
        SetPolygons();
    }

    private void SetupCamera() {
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = ((float)m_ViewPortWidth) / m_ViewPortHeight;
        final float left = -ratio * m_zoom;
        final float right = ratio * m_zoom;
        final float bottom = -m_zoom;
        final float top = m_zoom;
        final float near = 0.5f;
        final float far = 300.0f;

//        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        m_camera.SetCameraProjection(left, right, bottom, top, near, far);
        m_camera.UpdateCamera();
//        Matrix.setIdentityM(mModelMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 glUnused)
    {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        triagMesh.Draw();
        lineMesh.Draw();
        line2Mesh.Draw();
    }

    ArrayList<Vector3> vectors = new ArrayList<>();
    float dt = 10;
    public void SetPolygons()
    {
        if (vectors.isEmpty())
        {
            vectors.add(new Vector3(-5, 10000, 0));
            vectors.add(new Vector3(5, 10000, 0));
            vectors.add(new Vector3(-5, 10005, 0));
            vectors.add(new Vector3(5, 10005, 0));
        }
        else
        {
            vectors.add(new Vector3(-5, 5 + dt, 0));
            vectors.add(new Vector3(5, 5 + dt, 0));
            SetCameraPosition(new Vector3(0, 5 + dt, 0), 0.0f);
            dt += 10;
        }
        float[] vertices = new float[vectors.size() * 7];
        short[] order = new short[(vectors.size() - 2) * 3];
        int count = 0;
        for (Vector3 ve : vectors)
        {
            vertices[0 + count * 7] = ve.x;
            vertices[1 + count * 7] = ve.y;
            vertices[2 + count * 7] = ve.z;
            vertices[3 + count * 7] = 0;
            vertices[4 + count * 7] = 0.1f;
            vertices[5 + count * 7] = 0.8f;
            vertices[6 + count * 7] = 1;

            if (count > 0 && (count % 2 == 0)) {
                order[0 + (count - 2) * 3] = (short)(count - 2);
                order[1 + (count - 2) * 3] = (short)(count - 1);
                order[2 + (count - 2) * 3] = (short)(count - 0);
                order[3 + (count - 2) * 3] = (short)(count - 0);
                order[4 + (count - 2) * 3] = (short)(count + 1);
                order[5 + (count - 2) * 3] = (short)(count - 1);
            }

            count++;
        }

        triagMesh.SetVertices(vertices, order);
    }

    public void SetTriangles(ArrayList<Tetragon> tetragons)
    {
        if (tetragons.size() == 0)
        {
            return;
        }

        float[] vertices = new float[tetragons.size() * 4 * 7];
        short[] order = new short[tetragons.size() * 3 * 2];
        int vertexPos = 0, indexPos = 0, count = 0;
        for(Tetragon tetr : tetragons)
        {
            for (Vector3 vertex : tetr.Vertices) {
                vertices[0 + vertexPos] = vertex.x;
                vertices[1 + vertexPos] = vertex.y;
                vertices[2 + vertexPos] = vertex.z;
                vertices[3 + vertexPos] = 0;
                vertices[4 + vertexPos] = 0.1f;
                vertices[5 + vertexPos] = 0.8f;
                vertices[6 + vertexPos] = 1;
                vertexPos += 7;
            }

            order[0 + indexPos] = (short)(0 + count);
            order[1 + indexPos] = (short)(1 + count);
            order[2 + indexPos] = (short)(2 + count);

            order[3 + indexPos] = (short)(2 + count);
            order[4 + indexPos] = (short)(3 + count);
            order[5 + indexPos] = (short)(0 + count);

            count += 4;
            indexPos += 6;
        }
        triagMesh.SetVertices(vertices, order);
    }

    public void SetCameraPosition(Vector3 pos, float rotationAngle)
    {
        m_camera.SetCameraPosition(pos);
        m_camera.SetCameraRotation(rotationAngle);
        m_camera.UpdateCamera();
    }
}
