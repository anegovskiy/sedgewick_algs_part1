/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;

    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        this.points = points.clone();
        Arrays.sort(this.points);
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {
        return oneMoreTry();
    }

    private LineSegment[] oneMoreTry() {
        int pointsCount = points.length;
        int segmentCount = 0;
        LineSegment[] segments = new LineSegment[pointsCount];

        int i = 0;
        while (i < pointsCount - 1) {

            Point[] equalSlopes = new Point[pointsCount];

            Point p = points[i];
            Point q0 = points[i + 1];

            equalSlopes[0] = p;
            equalSlopes[1] = q0;

            int slopeCount = 2;

            double slopeToQ0 = p.slopeTo(q0);
            for (int j = 0; j < points.length; j++) {
                Point q = points[j];
                if (p.compareTo(q) == 0 || q0.compareTo(q) == 0) {
                    continue;
                }

                double slopeToQ = p.slopeTo(q);

                if (slopeToQ0 == slopeToQ) {
                    equalSlopes[slopeCount++] = q;
                }
            }

            if (slopeCount > 2) {
                Point[] pointsForSegment = new Point[slopeCount];
                for (int j = 0; j < slopeCount; j++) {
                    pointsForSegment[j] = equalSlopes[j];
                }

                Arrays.sort(pointsForSegment);
                LineSegment segment = new LineSegment(pointsForSegment[0],
                                                      pointsForSegment[slopeCount - 1]);
                segments[segmentCount++] = segment;

                i += slopeCount - 1;
            }

            i += 1;
        }

        LineSegment[] smallerArray = new LineSegment[segmentCount];
        for (int j = 0; j < segmentCount; j++) {
            smallerArray[j] = segments[j];
        }

        return smallerArray;
    }

    // Validation

    private void validatePoints(Point[] receivedPoints) {
        if (receivedPoints == null) {
            throw new IllegalArgumentException("points cannot be null");
        }

        int pointsCount = receivedPoints.length;
        for (int i = 0; i < pointsCount; i++) {
            Point firstPoint = receivedPoints[i];
            if (firstPoint == null) {
                throw new IllegalArgumentException("point cannot be null");
            }

            for (int j = i + 1; j < pointsCount; j++) {
                Point secondPoint = receivedPoints[j];
                if (firstPoint.equals(secondPoint)) {
                    throw new IllegalArgumentException("duplicated points");
                }
            }
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
