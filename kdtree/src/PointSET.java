/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;


    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validatePoint(p);
        if (!contains(p)) points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validatePoint(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            StdDraw.point(point.x(), point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);

        ArrayList<Point2D> containedPoints = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) containedPoints.add(point);
        }

        return containedPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);
        Point2D nearest = null;
        double distance = Double.MAX_VALUE;

        for (Point2D point : points) {
            double newDistance = Math.abs(p.distanceTo(point));
            if (Double.compare(newDistance, distance) == -1) {
                distance = newDistance;
                nearest = point;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        Point2D p1 = new Point2D(0.25, 0);
        Point2D p2 = new Point2D(0, 0.5);
        Point2D p3 = new Point2D(1.0, 1.0);
        Point2D p4 = new Point2D(0, 0.5);
        Point2D p6 = new Point2D(0.75, 0.25);
        Point2D p7 = new Point2D(0.25, 0.5);
        Point2D p8 = new Point2D(1.0, 0.0);
        Point2D p9 = new Point2D(1.0, 0.75);
        /*
         A  0.25 0.0
      B  0.0 0.5
      C  1.0 1.0
      D  0.0 0.5
      E  0.0 0.5
      F  1.0 1.0
      G  0.75 0.25
      H  0.25 0.5
      I  1.0 0.0
      J  1.0 0.75
         */

        PointSET pointSET = new PointSET();
        pointSET.insert(p1);
        pointSET.insert(p2);
        pointSET.insert(p3);
        pointSET.insert(p4);
        pointSET.insert(p6);
        pointSET.insert(p7);
        pointSET.insert(p8);
        pointSET.insert(p9);

        System.out.printf("size: %d", pointSET.size());
        System.out.println();
        System.out.printf("nearest: %s", pointSET.nearest(p1).toString());
        System.out.println();
    }

    // Validation
    private void validatePoint(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point cant be null");
    }

    private void validateRect(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rectangle cant be null");
    }
}
