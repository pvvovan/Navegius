package com.pvvovan.Navegius.Geometry;

import java.util.LinkedList;

/**
 * Created by vpetrov on 7/20/2016.
 */
public class GpsMath {
    private double d; // parameter d in the plane definition
    private CartPoint n; // unit normal to the horizontal plane
    private CartPoint e; // unit easting
    private CartPoint s; // unit northing
    private CartPoint CoordinateCenter; // coordinate system center
    GeoPoint _Origin;

    public GpsMath(GeoPoint pGeo)
    {
        _Origin = pGeo;
        CoordinateCenter = Utilities.GeoToCartesian(pGeo);

        // Local plane definition

        double h1 = CoordinateCenter.Z + Math.sqrt(Math.pow(CoordinateCenter.X, 2) + Math.pow(CoordinateCenter.Y, 2)) / Math.tan(pGeo.Latitude * WGS84.pi / 180);
        s = new CartPoint(-CoordinateCenter.X, -CoordinateCenter.Y, h1 - CoordinateCenter.Z);
        s = s.Normalize(); // unit northing
        double e2 = 1; // any number
        double e3 = 0;
        double e1 = -e2 * CoordinateCenter.Y / CoordinateCenter.X;
        e = new CartPoint(e1, e2, e3);
        e = e.Normalize(); // unit easting
        double n1 = 1;
        double n2 = -n1 * e.X / e.Y;
        double n3 = -(n1 * s.X + n2 * s.Y) / s.Z;
        n = new CartPoint(n1, n2, n3);
        n = n.Normalize(); // unit normal to local horizontal plane
        //d = -(n * CoordinateCenter);
        d = CartPoint.Multiply((CartPoint.Multiply(-1, n)), CoordinateCenter);
    }

    public CartPoint ToLocalHorizontal(CartPoint p)
    {
        double Z = CartPoint.Multiply(n, p) + d;
        double X = CartPoint.Multiply(CartPoint.Subtract(p, CoordinateCenter), e);
        double Y = CartPoint.Multiply(CartPoint.Subtract(p, CoordinateCenter), s);
        return new CartPoint(X, Y, Z);
    }

    public double Azimuth(CartPoint vector)
    {
        double az = Math.atan2(vector.Y, vector.X) * 180.0 / WGS84.pi;
        az = -az + 90.0;
        if (az < 0)
            az += 360.0;
        return az;
    }

    LinkedList<CartPoint> vectorQueue = new LinkedList<CartPoint>();
    public double SmoothedAzimuth(CartPoint vector)
    {
        vectorQueue.addFirst(vector);

        CartPoint prevVec;
        CartPoint smoothed = new CartPoint(0, 0, 0);
        for (CartPoint vec : vectorQueue) {
            smoothed = CartPoint.Add(smoothed, vec.Normalize());
        }
        if (vectorQueue.size() == 15)
            prevVec = vectorQueue.removeLast();
        else
        {
            prevVec = vectorQueue.peekLast();
        }

        if ((CartPoint.AngleBetweenVectors(prevVec, vector) * 180 / WGS84.pi) > 10)
        {
            vectorQueue.clear();
            return Azimuth(vector);
        }
        else
            return Azimuth(smoothed);
    }

    public GeoPoint Origin() {
        return _Origin;
    }
}
