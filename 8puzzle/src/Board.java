/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

public class Board {
    private final char[] tiles;
    private int emptySpaceCoordinate = -1;

    private int hamming = 0;
    private int manhattan = 0;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        validateTiles(tiles);
        int n = tiles.length;
        this.tiles = new char[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                this.tiles[index] = (char) tiles[i][j];
            }
        }

        hamming = calculateHammingNumber();
        manhattan = calculateManhattanNumber();
    }

    private Board(char[] tiles) {
        validateTiles(tiles);
        this.tiles = new char[tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            this.tiles[i] = tiles[i];
        }

        hamming = calculateHammingNumber();
        manhattan = calculateManhattanNumber();
        findEmptySpaceCoordinates();
    }

    // string representation of this board
    public String toString() {
        String stringRep = String.format("%d", dimension());

        for (int i = 0; i < dimension() * dimension(); i++) {
            if (i % dimension() == 0) {
                stringRep = stringRep.concat("\n");
            }

            stringRep = stringRep.concat(String.format(" %d", (int) tiles[i]));
        }

        return stringRep;
    }

    // board dimension n
    public int dimension() {
        return (int) Math.sqrt((double) tiles.length);
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    private int calculateHammingNumber() {
        int hammingNumber = 0;
        int dimension = dimension();
        for (int i = 0; i < dimension * dimension; i++) {
            char tile = tiles[i];
            if ((int) tile == 0) continue;
            if (!isTileAtCorrectCoordinates(tile, i)) {
                hammingNumber += 1;
            }
        }
        return hammingNumber;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    private int calculateManhattanNumber() {
        int manhattanDistance = 0;
        int dimension = dimension();
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                char tile = tiles[row * dimension + col];
                manhattanDistance += manhattanDistanceFor(tile, row, col);
            }
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
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

        return Arrays.equals(tiles, ((Board) y).tiles);
    }

    private enum Neighbor {
        LEFT, TOP, RIGHT, BOTTOM;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        findEmptySpaceCoordinates();
        Stack<Board> neighbors = new Stack<>();

        for (Neighbor neighbor : Neighbor.values()) {
            char[] neighborTiles = createTilesFor(neighbor);
            if (neighborTiles != null) {
                Board board = new Board(neighborTiles);
                neighbors.push(board);
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        char[] tilesCopy = Arrays.copyOf(tiles, tiles.length);
        Board twin = new Board(tilesCopy);
        findEmptySpaceCoordinates();

        int firstCoordinate = 0;
        while (firstCoordinate == emptySpaceCoordinate) {
            firstCoordinate += 1;
        }

        int secondCoordinate = 0;
        while (secondCoordinate == firstCoordinate
                || secondCoordinate == emptySpaceCoordinate) {
            secondCoordinate += 1;
        }

        char firstTile = twin.tiles[firstCoordinate];
        char secondTile = twin.tiles[secondCoordinate];
        twin.tiles[firstCoordinate] = secondTile;
        twin.tiles[secondCoordinate] = firstTile;

        return twin;
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

    private void validateTiles(char[] tilesToValidate) {
        if (tilesToValidate == null || tilesToValidate.length < 1) {
            throw new IllegalArgumentException("tiles should be present");
        }
    }

    // Coordinate calculations
    private int correctRowFor(char tile) {
        if ((int) tile == 0) {
            return dimension();
        }
        return (int) Math.ceil((double) tile / dimension());
    }

    private int correctColFor(char tile) {
        if ((int) tile == 0) {
            return dimension();
        }

        int mod = (int) tile % dimension();
        return mod == 0 ? dimension() : mod;
    }

    private boolean isTileAtCorrectCoordinates(char tile, int coordinate) {
        if ((int) tile == 0) {
            return true;
        }

        return (int) tile - 1 == coordinate;
    }

    private int manhattanDistanceFor(char tile, int row, int col) {
        if ((int) tile == 0) return 0;

        int manhattanDistance = 0;
        int correctRow = correctRowFor(tile);
        int correctCol = correctColFor(tile);
        int manhattanRow = correctRow - (row + 1);
        int manhattanCol = correctCol - (col + 1);

        manhattanDistance += Math.abs(manhattanRow);
        manhattanDistance += Math.abs(manhattanCol);

        return manhattanDistance;
    }

    private void findEmptySpaceCoordinates() {
        if (emptySpaceCoordinate != -1) {
            return;
        }

        int dimension = dimension() * dimension();
        for (int i = 0; i < dimension; i++) {
            int tile = tiles[i];
            if (tile == 0) {
                emptySpaceCoordinate = i;
            }
        }
    }


    private char[] createTilesFor(Neighbor neighbor) {
        int neighborEmptySpaceCoordinate = 0;

        switch (neighbor) {
            case LEFT:
                if (emptySpaceCoordinate % dimension() == 0) {
                    return null;
                }
                neighborEmptySpaceCoordinate = emptySpaceCoordinate - 1;
                break;

            case TOP:
                if (emptySpaceCoordinate < dimension()) {
                    return null;
                }
                neighborEmptySpaceCoordinate = emptySpaceCoordinate - dimension();
                break;

            case RIGHT:
                if (emptySpaceCoordinate % dimension() == dimension() - 1) {
                    return null;
                }
                neighborEmptySpaceCoordinate = emptySpaceCoordinate + 1;
                break;

            case BOTTOM:
                if (emptySpaceCoordinate >= dimension() * dimension() - dimension()) {
                    return null;
                }
                neighborEmptySpaceCoordinate = emptySpaceCoordinate + dimension();
                break;
        }

        return createNeighborWithEmptySpaceAt(neighborEmptySpaceCoordinate);
    }

    private char[] createNeighborWithEmptySpaceAt(int neighborEmptySpaceCoordinate) {
        char[] neighborTiles = Arrays.copyOf(tiles, tiles.length);
        char backUpTile = neighborTiles[neighborEmptySpaceCoordinate];
        neighborTiles[emptySpaceCoordinate] = backUpTile;
        neighborTiles[neighborEmptySpaceCoordinate] = (char) 0;

        return neighborTiles;
    }
}
