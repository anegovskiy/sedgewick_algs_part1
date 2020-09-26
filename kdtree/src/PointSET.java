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
        points.add(p);
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
        double distance = 0.0;

        for (Point2D point : points) {
            double newDistance = p.distanceTo(point);
            if (distance == 0.0 || newDistance < distance) {
                distance = newDistance;
                nearest = point;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        Point2D firstP = new Point2D(0, 0);
        Point2D secondP = new Point2D(1, 1);
        Point2D thirdP = new Point2D(2, 2);
        Point2D fourthP = new Point2D(3, 1);

        PointSET pointSET = new PointSET();
        pointSET.insert(firstP);
        pointSET.insert(secondP);
        pointSET.insert(thirdP);
        pointSET.insert(fourthP);

        System.out.printf("size: %d", pointSET.size());
        System.out.println();
        System.out.printf("nearest: %s", pointSET.nearest(fourthP).toString());
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
