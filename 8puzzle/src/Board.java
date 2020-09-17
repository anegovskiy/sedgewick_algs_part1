/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private int emptySpaceRow = -1;
    private int emptySpaceCol = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        validateTiles(tiles);
        int n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            this.tiles[i] = Arrays.copyOf(tiles[i], tiles.length);
        }
    }

    // string representation of this board
    public String toString() {
        String stringRep = String.format("%d", dimension());

        for (int[] row : tiles) {
            stringRep = stringRep.concat("\n");
            for (int tile : row) {
                stringRep = stringRep.concat(String.format(" %d", tile));
            }
        }

        return stringRep;
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingNumber = 0;
        int dimension = dimension();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int tile = tiles[i][j];
                if (tile == 0) continue;
                if (!isTileAtCorrectCoordinates(tile, i, j)) {
                    hammingNumber += 1;
                }
            }
        }

        return hammingNumber;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        int dimension = dimension();
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int tile = tiles[row][col];
                manhattanDistance += manhattanDistanceFor(tile, row, col);
            }
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        if (dimension() != ((Board) y).dimension()) {
            return false;
        }

        return Arrays.deepEquals(tiles, ((Board) y).tiles);
    }

    private enum Neighbor {
        LEFT, TOP, RIGHT, BOTTOM;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        findEmptySpaceCoordinates();
        Stack<Board> neighbors = new Stack<>();

        for (Neighbor neighbor : Neighbor.values()) {
            int[][] neighborTiles = createTilesFor(neighbor);
            if (neighborTiles != null) {
                Board board = new Board(neighborTiles);
                neighbors.push(board);
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }

    // Validation
    private void validateTiles(int[][] tilesToValidate) {
        if (tilesToValidate == null || tilesToValidate.length < 1) {
            throw new IllegalArgumentException("tiles should be present");
        }
    }

    // Coordinate calculations
    private int correctRowFor(int tile) {
        if (tile == 0) {
            return dimension();
        }
        return (int) Math.ceil((double) tile / dimension());
    }

    private int correctColFor(int tile) {
        if (tile == 0) {
            return dimension();
        }

        int mod = tile % dimension();
        return mod == 0 ? dimension() : mod;
    }

    private boolean isTileAtCorrectCoordinates(int tile, int row, int col) {
        int correctRow = correctRowFor(tile);
        int correctCol = correctColFor(tile);

        return row + 1 == correctRow && col + 1 == correctCol;
    }

    private int manhattanDistanceFor(int tile, int row, int col) {
        if (tile == 0) return 0;

        int manhattanDistance = 0;
        int correctRow = correctRowFor(tile);
        int correctCol = correctColFor(tile);
        int manhattanRow = correctRow - (row + 1);
        int manhattanCol = correctCol - (col + 1);

        if (manhattanRow != 0) {
            manhattanDistance += Math.abs(manhattanRow);
        }

        if (manhattanCol != 0) {
            manhattanDistance += Math.abs(manhattanCol);
        }

        return manhattanDistance;
    }

    private void findEmptySpaceCoordinates() {
        if (emptySpaceRow != -1 && emptySpaceCol != -1) {
            return;
        }

        int dimension = dimension();
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int tile = tiles[row][col];
                if (tile == 0) {
                    emptySpaceRow = row + 1;
                    emptySpaceCol = col + 1;
                }
            }
        }
    }

    private int[][] createTilesFor(Neighbor neighbor) {
        int neighborEmptySpaceRow = 0;
        int neighborEmptySpaceCol = 0;

        switch (neighbor) {
            case LEFT:
                neighborEmptySpaceRow = emptySpaceRow;
                neighborEmptySpaceCol = emptySpaceCol - 1;
                break;

            case TOP:
                neighborEmptySpaceRow = emptySpaceRow - 1;
                neighborEmptySpaceCol = emptySpaceCol;
                break;

            case RIGHT:
                neighborEmptySpaceRow = emptySpaceRow;
                neighborEmptySpaceCol = emptySpaceCol + 1;
                break;

            case BOTTOM:
                neighborEmptySpaceRow = emptySpaceRow + 1;
                neighborEmptySpaceCol = emptySpaceCol;
                break;
        }

        boolean isCoordinatesValid =
                isCoordinateValid(neighborEmptySpaceRow) && isCoordinateValid(
                        neighborEmptySpaceCol);
        if (!isCoordinatesValid) {
            return null;
        }

        return createNeighborWithEmptySpaceAt(neighborEmptySpaceRow, neighborEmptySpaceCol);
    }

    private boolean isCoordinateValid(int coordinace) {
        return coordinace > 0 && coordinace <= dimension();
    }

    private int[][] createNeighborWithEmptySpaceAt(int row, int col) {
        int n = tiles.length;
        int[][] neighborTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            neighborTiles[i] = Arrays.copyOf(tiles[i], tiles.length);
        }

        int backUpTile = neighborTiles[row - 1][col - 1];
        neighborTiles[emptySpaceRow - 1][emptySpaceCol - 1] = backUpTile;
        neighborTiles[row - 1][col - 1] = 0;

        return neighborTiles;
    }
}
