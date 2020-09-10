/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointTest {
    public static void main(String[] args) {
        // testSlopeTo();
        // testCompareTo();
        testSlopeOrder();
    }

    private static void testCompareTo() {
        // Points are equal
        Point first = new Point(0, 0);
        Point second = new Point(0, 0);
        assertEquals(0, first.slopeTo(second));

        // First is less, because of y
        first = new Point(5, 2);
        second = new Point(1, 5);
        assertTrue(first.compareTo(second) < 0);

        // First is less, because of x
        first = new Point(5, 5);
        second = new Point(11, 5);
        assertTrue(first.compareTo(second) > 0);

        // First is greater, because of y
        first = new Point(5, 7);
        second = new Point(1, 5);
        assertTrue(first.compareTo(second) > 0);
    }

    private static void testSlopeTo() {

        Point first = new Point(339, 468);
        Point second = new Point(230, 468);
        assertEquals(0, first.slopeTo(second));

        first = new Point(361, 454);
        second = new Point(361, 163);
        assertEquals(Double.POSITIVE_INFINITY, first.slopeTo(second));

        first = new Point(62, 60);
        second = new Point(62, 116);
        assertEquals(Double.POSITIVE_INFINITY, first.slopeTo(second));
    }

    private static void testSlopeOrder() {
        // Ascending order of point's slopes
        Point that = new Point(0, 500);
        Point first = new Point(0, 500);
        Point second = new Point(0, 500);

        Point[] points = { first, second };
        Arrays.sort(points, that.slopeOrder());
        assertEquals(points[0], first);

        that.slopeOrder().compare(null, second);
    }
}
