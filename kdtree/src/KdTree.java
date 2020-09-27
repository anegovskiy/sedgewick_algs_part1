/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private PointNode root;
    private int count;

    private class PointNode {
        private Point2D point;
        private RectHV rect;
        private PointNode left;
        private PointNode right;

        public PointNode(Point2D point, RectHV rect, PointNode left, PointNode right) {
            this.point = point;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }

        public int compareTo(PointNode pointNode, boolean horizontal) {
            if (pointNode == null) return -1;

            int result;
            if (horizontal) {
                result = Double.compare(point.x(), pointNode.point.x());
                if (result == 0 && Double.compare(point.y(), pointNode.point.y()) != 0) {
                    result = 1;
                }
            }
            else {
                result = Double.compare(point.y(), pointNode.point.y());
                if (result == 0 && Double.compare(point.x(), pointNode.point.x()) != 0) {
                    result = 1;
                }
            }

            return result;
        }
    }

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return count;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validatePoint(p);
        PointNode nodeToInsert = new PointNode(p, null, null, null);

        if (root == null) {
            nodeToInsert.rect = new RectHV(nodeToInsert.point.x(), 0, nodeToInsert.point.x(), 1);
            root = nodeToInsert;
            count += 1;
        }
        else insertNode(root, nodeToInsert, true);
    }

    private void insertNode(PointNode rootNode, PointNode pointNode, boolean horizontal) {
        int compareResult = pointNode.compareTo(rootNode, horizontal);
        if (compareResult == -1) {
            if (rootNode.left == null) {
                if (horizontal) {
                    pointNode.rect = new RectHV(0.0, pointNode.point.y(),
                                                rootNode.point.x(), pointNode.point.y());
                }
                else {
                    pointNode.rect = new RectHV(pointNode.point.x(), 0.0,
                                                pointNode.point.x(), rootNode.point.y());
                }

                rootNode.left = pointNode;
                count += 1;
            }
            else insertNode(rootNode.left, pointNode, !horizontal);
        }
        else if (compareResult == 1) {
            if (rootNode.right == null) {
                if (horizontal) {
                    pointNode.rect = new RectHV(rootNode.point.x(), pointNode.point.y(),
                                                1.0, pointNode.point.y());
                }
                else {
                    pointNode.rect = new RectHV(pointNode.point.x(), rootNode.point.y(),
                                                pointNode.point.x(), 1.0);
                }

                rootNode.right = pointNode;
                count += 1;
            }
            else insertNode(rootNode.right, pointNode, !horizontal);
        }
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validatePoint(p);
        PointNode node = new PointNode(p, null, null, null);
        return findNode(root, node, true) != null;
    }

    private PointNode findNode(PointNode rootNode, PointNode pointNode, boolean horizontal) {
        if (rootNode == null) return null;
        
        int compareResult = pointNode.compareTo(rootNode, horizontal);
        if (compareResult == -1) {
            if (rootNode.left == null) {
                return null;
            }
            else return findNode(rootNode.left, pointNode, !horizontal);
        }
        else if (compareResult == 1) {
            if (rootNode.right == null) {
                return null;
            }
            else return findNode(rootNode.right, pointNode, !horizontal);
        }
        else {
            return rootNode;
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawNode(root, false);
    }

    private void drawNode(PointNode node, boolean horizontal) {
        if (node.rect != null) {
            if (horizontal) StdDraw.setPenColor(StdDraw.BLUE);
            else StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            node.rect.draw();
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.03);
        StdDraw.point(node.point.x(), node.point.y());

        if (node.left != null) drawNode(node.left, !horizontal);
        if (node.right != null) drawNode(node.right, !horizontal);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validateRect(rect);

        ArrayList<Point2D> containedPoints = new ArrayList<>();
        findContainedPoints(rect, root, containedPoints);

        return containedPoints;
    }

    private void findContainedPoints(RectHV rect, PointNode node, ArrayList<Point2D> points) {
        if (node == null) return;

        if (node.rect.intersects(rect)) {
            // add to the points
            Point2D point = node.point;
            if (rect.contains(point)) points.add(point);
            if (node.left != null) findContainedPoints(rect, node.left, points);
            if (node.right != null) findContainedPoints(rect, node.right, points);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validatePoint(p);

        Point2D point = findNearest(p, root, root).point;
        return point;
    }

    private PointNode findNearest(Point2D p, PointNode fromNode, PointNode nearest) {
        double distance = calculateDistanceFromPoint(nearest, p);

        if (fromNode.left != null) {
            double distanceFromRect = calculateDistanceFromRect(fromNode.left, p);
            if (distanceFromRect < distance) {
                double distanceFromPoint = calculateDistanceFromPoint(fromNode.left, p);
                if (distanceFromPoint < distance) {
                    nearest = fromNode.left;
                }

                return findNearest(p, fromNode.left, nearest);
            }
        }

        if (fromNode.right != null) {
            double distanceFromRect = calculateDistanceFromRect(fromNode.right, p);
            if (distanceFromRect < distance) {
                double distanceFromPoint = calculateDistanceFromPoint(fromNode.right, p);
                if (distanceFromPoint < distance) {
                    nearest = fromNode.right;
                }

                return findNearest(p, fromNode.right, nearest);
            }
        }


        return nearest;
    }

    private double calculateDistanceFromRect(PointNode node, Point2D p) {
        return Math.abs(node.rect.distanceTo(p));
    }

    private double calculateDistanceFromPoint(PointNode node, Point2D p) {
        return Math.abs(node.point.distanceTo(p));
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        //testInsertion();
        testDraw();
    }

    private static void testInsertion() {
        KdTree tree = new KdTree();
        Point2D firstP = new Point2D(0.7, 0.2);
        Point2D secondP = new Point2D(0.5, 0.4);
        Point2D thirdP = new Point2D(0.2, 0.3);
        Point2D fourthP = new Point2D(0.4, 0.7);
        Point2D fifthP = new Point2D(0.9, 0.6);

        tree.insert(firstP);
        tree.insert(secondP);
        tree.insert(thirdP);
        tree.insert(fourthP);
        tree.insert(fifthP);

        System.out.println(tree.contains(firstP));
        System.out.println(tree.contains(secondP));
        System.out.println(tree.contains(thirdP));
        System.out.println(tree.contains(fourthP));
        System.out.println(tree.contains(fifthP));
    }

    private static void testDraw() {
        In in = new In("/Users/codemnky/Developer/algorithms_part1/kdtree/resources/circle10.txt");
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            System.out.println();
            System.out.println(p.toString());
            System.out.println();
        }

        kdtree.draw();

        Point2D point = new Point2D(0.81, 0.3);
        System.out.println(kdtree.nearest(point));
    }

    // Validation
    private void validatePoint(Point2D p) {
        if (p == null) throw new IllegalArgumentException("point cant be null");
    }

    private void validateRect(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("rectangle cant be null");
    }
}
