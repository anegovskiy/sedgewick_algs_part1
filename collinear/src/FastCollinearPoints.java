/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;
import java.util.Arrays;

public class FastCollinearPoints {

    private final Point[] points;
    private final LineSegment[] segments;

    private LineSegment[] temporarySegments;
    private int temporarySegmentCount;

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
        temporarySegmentCount = 0;
        temporarySegments = new LineSegment[pointsCount * pointsCount + 1];

        for (int i = 0; i < pointsCount; i++) {
            Point[] equalSlopes = new Point[pointsCount + 1];
            Point p = points[i];
            equalSlopes[0] = p;

            Point[] sortedBySlope = Arrays.copyOfRange(points, i, pointsCount);
            int sortedBySlopeCount = sortedBySlope.length;
            Arrays.sort(sortedBySlope, p.slopeOrder());

            Double previousSlope = null;
            int slopeCount = 1;

            for (int j = 0; j < sortedBySlopeCount; j++) {
                Point q = sortedBySlope[j];
                double slopeToQ = p.slopeTo(q);

                if (previousSlope == null) {
                    previousSlope = slopeToQ;
                    equalSlopes[slopeCount++] = q;
                }
                else if (slopeToQ == previousSlope) {
                    equalSlopes[slopeCount++] = q;
                }
                else {
                    saveSegments(slopeCount, equalSlopes);

                    // Cleanup for next slope
                    slopeCount = 2;
                    equalSlopes = new Point[pointsCount];
                    equalSlopes[0] = p;
                    equalSlopes[1] = q;
                    previousSlope = slopeToQ;
                }
            }

            saveSegments(slopeCount, equalSlopes);
        }

        LineSegment[] smallerArray = new LineSegment[temporarySegmentCount];
        for (int j = 0; j < temporarySegmentCount; j++) {
            smallerArray[j] = temporarySegments[j];
        }

        return smallerArray;
    }

    private void saveSegments(int slopeCount, Point[] equalSlopes) {
        if (slopeCount > 3) {
            Point[] pointsForSegment = new Point[slopeCount];
            for (int n = 0; n < slopeCount; n++) {
                pointsForSegment[n] = equalSlopes[n];
            }

            Arrays.sort(pointsForSegment);
            LineSegment segment = new LineSegment(pointsForSegment[0],
                                                  pointsForSegment[slopeCount - 1]);
            temporarySegments[temporarySegmentCount++] = segment;
        }
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

        Stopwatch stopwatch = new Stopwatch();
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(stopwatch.elapsedTime());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
