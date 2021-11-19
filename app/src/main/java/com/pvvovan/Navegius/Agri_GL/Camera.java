package com.pvvovan.Navegius.Agri_GL;

import android.content.Context;
import android.opengl.Matrix;

/**
 * Created by vovan on 11.01.16.
 */
public class Camera {
    private float[] m_ProjectionMatrix 	= new float[16];
    private float[] m_ViewMatrix 		= new float[16];
//    private float[] m_ModelMatrix       = new float[16];
    private float[] m_MVPMatrix         = new float[16];

    // Viewing Frustrum
    private float m_Projleft 	=	0;
    private float m_Projright	=	0;
    private float m_Projbottom	=	0;
    private float m_Projtop		=	0;
    private float m_Projnear	=	0;
    private	float m_Projfar		=	0;

    // Camera Location and Orientation
    private Vector3 m_Eye 	 = new Vector3(0,0,0);
    private Vector3 m_Center = new Vector3(0,0,0);
    private Vector3 m_Up 	 = new Vector3(0,0,0);
    private Orientation m_Orientation;


    Camera(Context ctx, Vector3 Eye, Vector3 Center, Vector3 Up)
    {
        SetCameraView(Eye, Center, Up);
        m_Orientation = new Orientation(ctx);
        m_Orientation.GetForward().Set(Center.x, Center.y, Center.z);
        m_Orientation.GetUp().Set(Up.x, Up.y, Up.z);
        m_Orientation.GetPosition().Set(Eye.x, Eye.y, Eye.z);

        // Calculate Right Local Vector
        Vector3 CameraRight = Vector3.CrossProduct(Center, Up);
        CameraRight.Normalize();
        m_Orientation.SetRight(CameraRight);

//        Matrix.setIdentityM(m_ModelMatrix, 0);
    }

    void SetCameraProjection(float Projleft,
                             float Projright,
                             float Projbottom,
                             float Projtop,
                             float Projnear,
                             float Projfar)
    {
        m_Projleft	=	Projleft;
        m_Projright	=	Projright;
        m_Projbottom=	Projbottom;
        m_Projtop	=	Projtop;
        m_Projnear	=	Projnear;
        m_Projfar	=	Projfar;
        Matrix.frustumM(m_ProjectionMatrix, 0,
                m_Projleft, m_Projright,
                m_Projbottom, m_Projtop,
                m_Projnear, m_Projfar);
    }

    void SetCameraView(Vector3 Eye,
                       Vector3 Center,
                       Vector3 Up)
    {
        // public static void setLookAtM (float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
        // Create Matrix
        Matrix.setLookAtM(m_ViewMatrix, 0,
                Eye.x, Eye.y, Eye.z,
                Center.x, Center.y, Center.z,
                Up.x, Up.y, Up.z);
    }

    public void SetCameraPosition(Vector3 pos)
    {
        m_Position = pos;
    }

    public void SetCameraRotation(float rotationAngle)
    {
        m_RotationAngle = rotationAngle;
    }

    Vector3 GetCameraEye()
    {
        return m_Eye;
    }

    Vector3 GetCameraLookAtCenter()
    {
        return m_Center;
    }

    Vector3 GetCameraUp()
    {
        return m_Up;
    }

    float[] GetProjectionMatrix()
    {
        return m_ProjectionMatrix;
    }

    float[] GetViewMatrix()
    {
        return m_ViewMatrix;
    }

    private Vector3 m_RotationAxis = new Vector3(0, 0, 1);
    private Vector3 m_Position = new Vector3(0.0f, 0.0f, 0.0f);
    private Vector3 m_Scale = new Vector3(1.0f, 1.0f, 1.0f);
    private float m_RotationAngle = 0.0f;

    void UpdateCamera()
    {
        m_Orientation.SetPosition(m_Position);
//        m_Orientation.SetRotationAxis(m_RotationAxis);
        m_Orientation.SetRotationAngle(m_RotationAngle);
//        m_Orientation.SetScale(m_Scale);
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(m_MVPMatrix, 0, m_ViewMatrix, 0, m_Orientation.UpdateOrientation(), 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(m_MVPMatrix, 0, m_ProjectionMatrix, 0, m_MVPMatrix, 0);
    }

    float[] GetMPVMatrix() {
        return m_MVPMatrix;
    }
}
