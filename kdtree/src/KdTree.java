/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class KdTree {
    private final TreeSet<Point2D> points;


    // construct an empty set of points
    public KdTree() {
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

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);

        Point2D minPoint = new Point2D(rect.xmin(), rect.ymin());
        Point2D maxPoint = new Point2D(rect.xmax(), rect.ymax());
        return points.subSet(minPoint, maxPoint);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);

        return points.first();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

    // Validation
    private void validatePoint(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point cant be null");
    }

    private void validateRect(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rectangle cant be null");
    }
}
