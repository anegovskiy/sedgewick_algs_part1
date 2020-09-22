/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class BoardTest {
    public static void main(String[] args) {
        // toStringTest();
        hammingTest();
        // manhattanTest();
        // testEquals();
        //testTwin();
        //testNeighbors();
    }

    private static void testTwin() {
        int[][] tiles = { { 1, 0 }, { 3, 2 } };
        Board board = new Board(tiles);
        Board twin = board.twin();
        System.out.println(board.toString());
        System.out.println(twin.toString());

        System.out.println(board.equals(twin));
    }

    private static void toStringTest() {
        int[][] tiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        System.out.println(board.toString());
        assert (board.toString().equals("3\n" + " 1 2 3\n" + " 4 5 6\n" + " 7 8 0"));
    }

    private static void hammingTest() {
        int[][] tiles = { { 1, 2, 3 }, { 0, 7, 6 }, { 5, 4, 8 } };
        Board board = new Board(tiles);
        System.out.println(board.hamming());
        assert (board.hamming() == 4);
    }

    private static void manhattanTest() {
        int[][] tiles = { { 1, 2, 3 }, { 0, 7, 6 }, { 5, 4, 8 } };
        Board board = new Board(tiles);
        System.out.println(board.manhattan());
        assert (board.manhattan() == 7);
    }

    private static void testEquals() {
        int[][] tilesOne = { { 1, 2, 4 }, { 3, 5, 6 }, { 7, 8, 0 } };
        Board boardOne = new Board(tilesOne);

        int[][] tilesTwo = { { 1, 2, 4 }, { 3, 5, 6 }, { 7, 8, 0 } };
        Board boardTwo = new Board(tilesTwo);

        int[][] tilesThree = { { 1, 2, 5 }, { 3, 4, 6 }, { 7, 8, 0 } };
        Board boardThree = new Board(tilesThree);

        assert (boardOne.equals(boardTwo));
        assert (!boardOne.equals(boardThree));
    }

    private static void testNeighbors() {
        int[][] tiles = { { 1, 2, 4 }, { 3, 0, 6 }, { 7, 5, 8 } };
        Board board = new Board(tiles);
        Iterable<Board> neighbors = board.neighbors();
        int count = 0;
        for (Board neighbor : neighbors) {
            System.out.println(neighbor.toString());
            count++;
        }

        assert count == 4;

        int[][] tiles2 = { { 2, 6, 3 }, { 0, 1, 7 }, { 8, 5, 4 } };
        Board board2 = new Board(tiles2);
        Iterable<Board> neighbors2 = board2.neighbors();
        count = 0;
        for (Board neighbor : neighbors2) {
            System.out.println(neighbor.toString());
            count++;
        }

        assert count == 3;
    }
}
