package com.pvvovan.Navegius.Agri_GL;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by vovan on 30.01.16.
 */
public class Mesh
{
    enum MeshType { Line, Triangle }

    private MeshType m_MeshType;

    private float[] m_VerticesData;
    private short[] m_DrawOrder;

    private Camera m_camera;

    private FloatBuffer m_VerticesBuffer;
    private ShortBuffer m_DrawListBuffer 	= null;

    private int m_programHandle;

    /** This will be used to pass in the transformation matrix. */
    private int m_MVPMatrixHandle;

    /** This will be used to pass in model position information. */
    private int m_PositionHandle;

    /** This will be used to pass in model color information. */
    private int m_ColorHandle;

    /** How many bytes per float. */
    private final int m_BytesPerFloat = 4;

    /** How many elements per vertex. */
    private final int m_StrideBytes = 7 * m_BytesPerFloat;

    /** Offset of the position data. */
    private final int m_PositionOffset = 0;

    /** Size of the position data in elements. */
    private final int m_PositionDataSize = 3;

    /** Offset of the color data. */
    private final int m_ColorOffset = 3;

    /** Size of the color data in elements. */
    private final int m_ColorDataSize = 4;

    float m_LineWidth;

    public Mesh(Camera camera, float[] verticesData, short[] drawOrder)
    {
        m_MeshType = MeshType.Triangle;
        m_camera = camera;
        m_VerticesData = verticesData;
        m_DrawOrder = drawOrder;

        InitBuffers();
        InitShaders();
    }

    public Mesh(float lineWidth, Camera camera, float[] verticesData, short[] drawOrder)
    {
        m_MeshType = MeshType.Line;
        m_LineWidth = lineWidth;
        m_camera = camera;
        m_VerticesData = verticesData;
        m_DrawOrder = drawOrder;

        InitBuffers();
        InitShaders();
    }

    private void InitShaders() {
        final String vertexShader =
                "uniform mat4 u_MVPMatrix;      \n"		// A constant representing the combined model/view/projection matrix.

                        + "attribute vec4 a_Position;     \n"		// Per-vertex position information we will pass in.
                        + "attribute vec4 a_Color;        \n"		// Per-vertex color information we will pass in.

                        + "varying vec4 v_Color;          \n"		// This will be passed into the fragment shader.

                        + "void main()                    \n"		// The entry point for our vertex shader.
                        + "{                              \n"
                        + "   v_Color = a_Color;          \n"		// Pass the color through to the fragment shader.
                        // It will be interpolated across the triangle.
                        + "   gl_Position = u_MVPMatrix   \n" 	// gl_Position is a special variable used to store the final position.
                        + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                        + "}                              \n";    // normalized screen coordinates.

        final String fragmentShader =
                "precision mediump float;       \n"		// Set the default precision to medium. We don't need as high of a
                        // precision in the fragment shader.
                        + "varying vec4 v_Color;          \n"		// This is the color from the vertex shader interpolated across the
                        // triangle per fragment.
                        + "void main()                    \n"		// The entry point for our fragment shader.
                        + "{                              \n"
                        + "   gl_FragColor = v_Color;     \n"		// Pass the color directly through the pipeline.
                        + "}                              \n";

        // Load in the vertex shader.
        int vertexShaderHandle = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);

        if (vertexShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(vertexShaderHandle, vertexShader);

            // Compile the shader.
            GLES20.glCompileShader(vertexShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(vertexShaderHandle);
                vertexShaderHandle = 0;
            }
        }

        if (vertexShaderHandle == 0)
        {
            throw new RuntimeException("Error creating vertex shader.");
        }

        // Load in the fragment shader shader.
        int fragmentShaderHandle = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);

        if (fragmentShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(fragmentShaderHandle, fragmentShader);

            // Compile the shader.
            GLES20.glCompileShader(fragmentShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(fragmentShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(fragmentShaderHandle);
                fragmentShaderHandle = 0;
            }
        }

        if (fragmentShaderHandle == 0)
        {
            throw new RuntimeException("Error creating fragment shader.");
        }

        // Create a program object and store the handle to it.
        m_programHandle = GLES20.glCreateProgram();

        if (m_programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(m_programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(m_programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(m_programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(m_programHandle, 1, "a_Color");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(m_programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(m_programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(m_programHandle);
                m_programHandle = 0;
            }
        }

        if (m_programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        // Set program handles. These will later be used to pass in values to the program.
        m_MVPMatrixHandle = GLES20.glGetUniformLocation(m_programHandle, "u_MVPMatrix");
        m_PositionHandle = GLES20.glGetAttribLocation(m_programHandle, "a_Position");
        m_ColorHandle = GLES20.glGetAttribLocation(m_programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(m_programHandle);
    }

    private void InitBuffers() {
        // Initialize the buffers.
        m_DrawListBuffer = ShortBuffer.wrap(m_DrawOrder);

        m_VerticesBuffer = ByteBuffer.allocateDirect(m_VerticesData.length * m_BytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        m_VerticesBuffer.put(m_VerticesData).position(0);
    }

    public void SetVertices(float[] verticesData, short[] drawOrder)
    {
        m_VerticesData = verticesData;
        m_DrawOrder = drawOrder;
        InitBuffers();
    }

    public void Draw()
    {
        GLES20.glUseProgram(m_programHandle);
        // Pass in the position information
        m_VerticesBuffer.position(m_PositionOffset);
        GLES20.glVertexAttribPointer(m_PositionHandle, m_PositionDataSize, GLES20.GL_FLOAT, false,
                m_StrideBytes, m_VerticesBuffer);

        GLES20.glEnableVertexAttribArray(m_PositionHandle);

        // Pass in the color information
        m_VerticesBuffer.position(m_ColorOffset);
        GLES20.glVertexAttribPointer(m_ColorHandle, m_ColorDataSize, GLES20.GL_FLOAT, false,
                m_StrideBytes, m_VerticesBuffer);

        GLES20.glEnableVertexAttribArray(m_ColorHandle);


        GLES20.glUniformMatrix4fv(m_MVPMatrixHandle, 1, false, m_camera.GetMPVMatrix(), 0);

        switch (m_MeshType) {
            case Triangle:
                GLES20.glDrawElements(GLES20.GL_TRIANGLES,
                        m_DrawListBuffer.capacity(),
                        GLES20.GL_UNSIGNED_SHORT,
                        m_DrawListBuffer);
                break;
            case Line:
                GLES20.glLineWidth(m_LineWidth);
                GLES20.glDrawElements(GLES20.GL_LINES,
                        m_DrawListBuffer.capacity(),
                        GLES20.GL_UNSIGNED_SHORT,
                        m_DrawListBuffer);
                break;
        }
    }
}
