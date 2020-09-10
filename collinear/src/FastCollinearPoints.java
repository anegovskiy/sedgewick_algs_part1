/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        validatePoints(points);

        this.points = Arrays.copyOf(points, points.length);
        Arrays.sort(this.points);
        segments = findAllSegments();
    }

    public int numberOfSegments() {
        return segments().length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }

    private LineSegment[] findAllSegments() {
        int pointsCount = points.length;
        int segmentCount = 0;
        LineSegment[] segmentsTemporary = new LineSegment[pointsCount];

        for (int i = 0; i < pointsCount; i++) {
            Point[] equalSlopes = new Point[pointsCount];

            Point p = points[i];
            equalSlopes[0] = p;

            for (int j = i + 1; j < pointsCount; j++) {
                Point q0 = points[j];
                double slopeToQ0 = p.slopeTo(q0);

                equalSlopes[1] = q0;
                int slopeCount = 2;

                for (int k = j + 1; k < points.length; k++) {
                    Point q = points[k];
                    double slopeToQ = p.slopeTo(q);

                    if (slopeToQ0 == slopeToQ) {
                        equalSlopes[slopeCount++] = q;
                    }
                }

                if (slopeCount > 3) {
                    Point[] pointsForSegment = new Point[slopeCount];
                    for (int n = 0; n < slopeCount; n++) {
                        pointsForSegment[n] = equalSlopes[n];
                    }

                    Arrays.sort(pointsForSegment);
                    LineSegment segment = new LineSegment(pointsForSegment[0],
                                                          pointsForSegment[slopeCount - 1]);
                    segmentsTemporary[segmentCount++] = segment;
                }
            }
        }

        LineSegment[] smallerArray = new LineSegment[segmentCount];
        for (int j = 0; j < segmentCount; j++) {
            smallerArray[j] = segmentsTemporary[j];
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
        StdDraw.setPenRadius(0.001);
        StdDraw.setPenColor(Color.RED);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
