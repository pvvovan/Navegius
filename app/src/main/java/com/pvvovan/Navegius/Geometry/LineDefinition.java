package com.pvvovan.Navegius.Geometry;

/**
 * Created by vpetrov on 7/20/2016.
 */
public class LineDefinition {
    public double k;
    public double b;
    public boolean IsVertical;
    public double X;

    public double GetY(double x)
    {
        return k * x + b;
    }

    public static LineDefinition GetLineDefinition(CartPoint p1, CartPoint p2)
    {
        LineDefinition def = new LineDefinition();
        if (p2.X == p1.X)
        {
            def.IsVertical = true;
            def.X = p1.X;
            if (p2.Y > p1.Y)
                def.k = Double.POSITIVE_INFINITY;
            else
                def.k = Double.NEGATIVE_INFINITY;
            return def;
        }
        def.k = (p2.Y - p1.Y) / (p2.X - p1.X);
        def.b = p1.Y - def.k * p1.X;
        return def;
    }

    public static CartPoint GetLineIntersection(LineDefinition l1, LineDefinition l2)
    {
        CartPoint point = new CartPoint(0, 0, 0);
        if (l1.IsVertical)
        {
            point.X = l1.X;
            point.Y = l2.k * point.X + l2.b;
            return point;
        }
        if (l2.IsVertical)
        {
            point.X = l2.X;
            point.Y = l1.k * point.X + l1.b;
            return point;
        }
        point.X = (l2.b - l1.b) / (l1.k - l2.k);
        point.Y = point.X * l1.k + l1.b;
        return point;
    }

    public static LineDefinition GetParallelLine(LineDefinition line, double distance)
    {
        if (line.IsVertical)
        {
            if (line.k == Double.POSITIVE_INFINITY) {
                LineDefinition nLine = new LineDefinition();
                nLine.IsVertical = true;
                nLine.X = line.X - distance;
                nLine.k = Double.POSITIVE_INFINITY;
                return nLine;
            }
            else {
                LineDefinition nLine = new LineDefinition();
                nLine.IsVertical = true;
                nLine.X = line.X + distance;
                nLine.k = Double.NEGATIVE_INFINITY;
                return nLine;
            }
        }
        LineDefinition parallelLine = new LineDefinition();
        parallelLine.k = line.k;
        parallelLine.b = line.b;
        double alpha = Math.atan(line.k);
        parallelLine.b += distance / Math.cos(alpha);
        return parallelLine;
    }

    public static CartPoint GetVectorBetweenParallelLines(LineDefinition line1, LineDefinition line2)
    {
        CartPoint vec = new CartPoint();

        if (line1.IsVertical)
        {
            vec.X = line2.X - line1.X;
            return vec;
        }

        double alpha = Math.atan(line1.k);
        double d = (line1.b - line2.b) * Math.cos(alpha);
        vec.X = d * Math.sin(alpha);
        vec.Y = -d * Math.cos(alpha);

        return vec;
    }

    public static double GetDistance(LineDefinition line, CartPoint point)
    {
        if (line.IsVertical)
            return Math.abs(line.X - point.X);
        if (line.k == 0)
            return Math.abs(line.b - point.Y);

        CartPoint p1 = new CartPoint(point.X, line.k * point.X + line.b, point.Z);
        CartPoint p2 = new CartPoint((point.Y - line.b) / line.k, point.Y, point.Z);
        double c = CartPoint.Subtract(p1, p2).Norm();
        double a = CartPoint.Subtract(p1, point).Norm();
        double b = CartPoint.Subtract(p2, point).Norm();
        if (c == a + b)
            return 0.0;
        return a * Math.sqrt(1 - (a * a / c / c));
    }

    public static double GetCrossTrackError(LineDefinition line, CartPoint point, double azimuth)
    {
        double alpha = Math.atan(line.k) / WGS84.pi * 180;
        double gamma = azimuth - 90 + alpha;
        double sign = 1;
        boolean isHigher = false;
        double y = line.k * point.X + line.b;
        if (point.Y > y)
            isHigher = true;
        if ((gamma < 90) || (gamma > 270))
        {
            if (isHigher)
                sign = -1;
            else
                sign = 1;
        }
        else
        {
            if (isHigher)
                sign = 1;
            else
                sign = -1;
        }
        return sign * GetDistance(line, point);
    }

    public static CartPoint GetNudge(LineDefinition line, CartPoint point)
    {
        CartPoint nudge;// = new MyPoint3D();

        if (line.IsVertical)
            nudge = new CartPoint(line.X - point.X, 0, 0);
        else if (line.k == 0)
            nudge = new CartPoint(0, line.b - point.Y, 0);
        else
        {
            double k_0 = -1.0 / line.k;
            double d = LineDefinition.GetDistance(line, point);
            double x = d / Math.sqrt(k_0 * k_0 + 1);
            double y = k_0 * x;
            double py = line.k * point.X + line.b;
            if (py > point.Y)
                nudge = new CartPoint(-x, -y, 0);
            else
                nudge = new CartPoint(x, y, 0);
        }

        return nudge;
    }
}
