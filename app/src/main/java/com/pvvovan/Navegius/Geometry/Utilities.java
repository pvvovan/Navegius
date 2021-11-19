package com.pvvovan.Navegius.Geometry;

/**
 * Created by vpetrov on 7/20/2016.
 */
public class Utilities {
    public static CartPoint GeoToCartesian(GeoPoint pGeo)
    {
        double radLatitude = pGeo.Latitude * WGS84.pi / 180;
        double radLongitude = pGeo.Longitude * WGS84.pi / 180;
        double N = WGS84.c / Math.sqrt(1 + WGS84.ex2 * Math.cos(radLatitude) * Math.cos(radLatitude));
        double X = (N + pGeo.Elevation) * Math.cos(radLatitude) * Math.cos(radLongitude);
        double Y = (N + pGeo.Elevation) * Math.cos(radLatitude) * Math.sin(radLongitude);
        double Z = ((1 - WGS84.f) * (1 - WGS84.f) * N + pGeo.Elevation) * Math.sin(radLatitude);
        return new CartPoint(X, Y, Z);
    }
}
