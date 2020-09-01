/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int gridSideSize;
    private final int gridSize;
    private int openSitesCount;
    private boolean[][] grid;

    private final WeightedQuickUnionUF unionFind;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        validateSize(n);

        gridSideSize = n;
        gridSize = gridSideSize * gridSideSize;
        openSitesCount = 0;

        // Fill grid
        grid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            // Filling sites row by row
            boolean[] row = grid[i];
            for (int j = 0; j < n; j++) {
                row[j] = false;
            }

            grid[i] = row;
        }

        unionFind = new WeightedQuickUnionUF(gridSize + 2);

        // Create virtual first site
        for (int i = 0; i < gridSideSize; i++) {
            unionFind.union(0, i + 1);
        }

        // Create virtual last site
        for (int i = gridSize; i >= gridSize - gridSideSize; i--) {
            unionFind.union(gridSize + 1, i + 1);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateLocation(row, col);

        boolean isAlreadyOpen = isOpen(row, col);
        if (isAlreadyOpen) {
            return;
        }

        openSitesCount += 1;
        grid[row - 1][col - 1] = true;
        connectToAllNeighbours(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateLocation(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateLocation(row, col);

        if (!isOpen(row, col)) {
            return false;
        }

        int currentIndex = translateCoordinatesToIndex(row - 1, col - 1);
        return unionFind.find(currentIndex) == unionFind.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesCount;
    }

    // does the system percolate?
    public boolean percolates() {
        // Corner case
        if (gridSize == 1) {
            return isOpen(1, 1);
        }
        return unionFind.find(gridSize + 1) == unionFind.find(0);
    }

    // MARK: - Validation

    private void validateSize(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid can't be of size 0 or lower ))");
        }
    }

    private void validateLocation(int row, int col) {
        boolean isRowValid = row > 0 && row <= gridSideSize;
        boolean isColValid = col > 0 && col <= gridSideSize;

        if (!(isRowValid && isColValid)) {
            throw new IllegalArgumentException();
        }
    }

    // MARK: - Neighbours

    private void connectToAllNeighbours(int row, int col) {
        // Check left neighbour
        connectToNeighbour(row - 1, col - 1, row - 1, col - 2);
        // Check top neighbour
        connectToNeighbour(row - 1, col - 1, row - 2, col - 1);
        // Check right neighbour
        connectToNeighbour(row - 1, col - 1, row - 1, col);
        // Check bottom neighbour
        connectToNeighbour(row - 1, col - 1, row, col - 1);
    }

    private void connectToNeighbour(int currentRow, int currentCol, int neighbourRow, int neighbourCol) {
        if ((neighbourRow >= 0 && neighbourRow < gridSideSize) && (neighbourCol >= 0 && neighbourCol < gridSideSize)) {
            int currentIndex = translateCoordinatesToIndex(currentRow, currentCol);
            if (grid[neighbourRow][neighbourCol]) {
                int neighbourIndex = translateCoordinatesToIndex(neighbourRow, neighbourCol);
                unionFind.union(currentIndex, neighbourIndex);
            }
        }
    }

    // MARK: - Utils

    private int translateCoordinatesToIndex(int row, int col) {
        return row * gridSideSize + col + 1;
    }

    // MARK: - Test client (optional)
    public static void main(String[] args) {
        // This method is left empty for testing purposes
    }
}
