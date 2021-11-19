package com.pvvovan.Navegius.Geometry;

/**
 * Created by Vovan on 18.05.2016.
 */
public class CartPoint {
    public double X;
    public double Y;
    public double Z;

    public CartPoint(double x, double y, double z)
    {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public CartPoint(CartPoint point)
    {
        this.X = point.X;
        this.Y = point.Y;
        this.Z = point.Z;
    }

    public CartPoint()
    {
        X = 0; Y = 0; Z = 0;
    }

    public double Norm()
    {
        return (Math.sqrt(X * X + Y * Y + Z * Z));
    }

    public CartPoint Normalize()
    {
        double N = this.Norm();
        CartPoint Vec = new CartPoint(X / N, Y / N, Z / N);
        return Vec;
    }

    public static double AngleBetweenVectors(CartPoint vector_A, CartPoint vector_B)
    {
        double dotProduct = vector_A.X * vector_B.X + vector_A.Y * vector_B.Y + vector_A.Z * vector_B.Z;
        double normProduct = vector_A.Norm() * vector_B.Norm();
        return Math.acos(dotProduct / normProduct);
    }

    public static CartPoint Add(CartPoint p1, CartPoint p2)
    {
        return new CartPoint(p1.X + p2.X, p1.Y + p2.Y, p1.Z + p2.Z);
    }
    public static CartPoint Subtract(CartPoint p1, CartPoint p2)
    {
        return new CartPoint(p1.X - p2.X, p1.Y - p2.Y, p1.Z - p2.Z);
    }
    public static double Multiply(CartPoint p1, CartPoint p2)
    {
        return ((p1.X * p2.X) + (p1.Y * p2.Y) + (p1.Z * p2.Z));
    }
    public static CartPoint Multiply(CartPoint p1, double p2)
    {
        return new CartPoint((p1.X * p2), (p1.Y * p2), (p1.Z * p2));
    }
    public static CartPoint Multiply(double p2, CartPoint p1)
    {
        return new CartPoint((p1.X * p2), (p1.Y * p2), (p1.Z * p2));
    }
    public static CartPoint Divide(CartPoint p1, double p2)
    {
        return new CartPoint((p1.X / p2), (p1.Y / p2), (p1.Z / p2));
    }
    public static CartPoint Divide(double p2, CartPoint p1)
    {
        return new CartPoint((p1.X / p2), (p1.Y / p2), (p1.Z / p2));
    }
//    public static implicit operator MyPoint3D(AgroGuide.Geometry.QuadTree.PointF point)
//    {
//        return new MyPoint3D(point.X, point.Y, 0);
//    }
//    public static explicit operator AgroGuide.Geometry.QuadTree.PointF(MyPoint3D point)
//    {
//        return new AgroGuide.Geometry.QuadTree.PointF((float)point.X, (float)point.Y);
//    }
}
