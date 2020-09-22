/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class SolverTest {
    public static void main(String[] args) {
        testOverall(args);
        // testHamming();
        // testManhattan();
        // testMoves();
    }

    private static void testMoves() {
        int[][] tiles = { { 2, 4, 3 }, { 1, 5, 0 }, { 7, 8, 6 } };
        Board board = new Board(tiles);
        Solver solver = new Solver(board);

        int moves = solver.moves();
        assert (moves == 7);
    }

    private static void testOverall(String[] args) {
        // for each command-line argument
        for (String filename : args) {
            // create initial board from file
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    tiles[i][j] = in.readInt();
            Board initial = new Board(tiles);

            // solve the puzzle
            Solver solver = new Solver(initial);

            // print solution to standard output
            if (!solver.isSolvable())
                StdOut.println("No solution possible");
            else {
                StdOut.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                    StdOut.println(board);
            }


            int moves = solver.moves();
            String result = filename + ": " + moves;
            StdOut.println(result);

            switch (filename) {
                case "puzzle00.txt":
                    assert moves == 0;
                    break;

                case "puzzle07.txt":
                    assert moves == 7;
                    break;

                case "puzzle08.txt":
                    assert moves == 8;
                    break;

                case "puzzle2x2-unsolvable1.txt":
                    assert moves == -1;
                    break;

                case "puzzle10.txt:":
                    assert moves == 10;
                    break;
            }
        }
    }

    private static void testHamming() {
        In in = new In("puzzle00.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        assert initial.hamming() == 2;
    }

    private static void testManhattan() {
        In in = new In("puzzle00.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        for (int i = 0; i < 14; i++) {
            initial = initial.twin();
        }
        int manhattan = initial.manhattan();
        assert manhattan == 2;
    }
}
