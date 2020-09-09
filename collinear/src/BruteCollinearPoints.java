/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;

    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);

        this.points = points.clone();
    }

    public int numberOfSegments() {
        return segments().length;
    }


    //  The method segments() should include each line segment containing 4 points exactly once.
    //  If 4 points appear on a line segment in the order p→q→r→s, then you should include either
    //  the line segment p→s or s→p (but not both) and you should not include subsegments
    //  such as p→r or q→r. For simplicity, we will not supply any input to BruteCollinearPoints
    //  that has 5 or more collinear points.

    public LineSegment[] segments() {
        return findAllSegments();
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

    // Segment search

    private LineSegment[] findAllSegments() {
        int pointsCount = points.length;
        int segmentCount = 0;
        LineSegment[] segments = new LineSegment[pointsCount / 4];

        for (int i = 0; i < pointsCount; i++) {
            Point p = points[i];

            for (int j = i + 1; j < pointsCount; j++) {
                Point q = points[j];

                for (int k = j + 1; k < pointsCount; k++) {
                    Point r = points[k];

                    for (int n = k + 1; n < pointsCount; n++) {
                        Point s = points[n];

                        if (p.equals(q) || p.equals(r) || p.equals(s)) {
                            continue;
                        }

                        if (q.equals(p) || q.equals(r) || q.equals(s)) {
                            continue;
                        }

                        if (s.equals(q) || s.equals(r) || s.equals(p)) {
                            continue;
                        }

                        double slopeQ = p.slopeTo(q);
                        double slopeR = p.slopeTo(r);
                        double slopeS = p.slopeTo(s);

                        if (slopeQ == slopeR && slopeR == slopeS) {
                            Point[] linePoints = { p, q, r, s };

                            Arrays.sort(linePoints);
                            LineSegment segment = new LineSegment(linePoints[0], linePoints[3]);
                            segments[segmentCount++] = segment;
                        }

                    }
                }
            }
        }

        LineSegment[] smallerArray = new LineSegment[segmentCount];
        for (int i = 0; i < segmentCount; i++) {
            smallerArray[i] = segments[i];
        }

        return smallerArray;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
