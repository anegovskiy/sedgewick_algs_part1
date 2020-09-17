/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class BoardTest {
    public static void main(String[] args) {
        toStringTest();
        hammingTest();
        manhattanTest();
        testEquals();
        testNeighbors();
    }

    private static void toStringTest() {
        int[][] tiles = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        System.out.println(board.toString());
        assert (board.toString().equals("3\n" + " 1 2 3\n" + " 4 5 6\n" + " 7 8 0"));
    }

    private static void hammingTest() {
        int[][] tiles = { { 1, 2, 4 }, { 3, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        System.out.println(board.hamming());
        assert (board.hamming() == 2);
    }

    private static void manhattanTest() {
        int[][] tiles = { { 1, 2, 4 }, { 3, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        System.out.println(board.manhattan());
        assert (board.manhattan() == 6);
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
        int[][] tiles = { { 1, 2, 4 }, { 3, 5, 6 }, { 7, 8, 0 } };
        Board board = new Board(tiles);
        Iterable<Board> neighbors = board.neighbors();

        for (Board neighbor : neighbors) {
            System.out.println(neighbor.toString());
        }
    }
}
