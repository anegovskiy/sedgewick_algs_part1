/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;

    private final int gridSideSize;
    private final double[] thresholds;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        validate(n, trials);

        // Stopwatch stopwatch = new Stopwatch();

        gridSideSize = n;
        thresholds = new double[trials];

        for (int i = 0; i < trials; i++) {
            double threshold = runMonteCarloSimulation(gridSideSize);
            thresholds[i] = threshold;
        }

        // System.out.println(String.format("Elapsed time: %f", stopwatch.elapsedTime()));
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(thresholds.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(thresholds.length);
    }

    // MARK: - Validation

    private void validate(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n or trials is incorrect");
        }
    }


    // test client (see below)
    public static void main(String[] args) {
        int argumentsCount = args.length;
        if (argumentsCount != 2) {
            throw new IllegalArgumentException("Arguments count is incorrect");
        }

        int sideSize = Integer.parseInt(args[0]);
        int trilasCount = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(sideSize, trilasCount);

        double mean = stats.mean();
        System.out.println(String.format("mean() = %f", mean));

        double stddev = stats.stddev();
        System.out.println(String.format("stddev() = %f", stddev));

        System.out.println(String.format("95%% confidence interval = [%f, %f]", stats.confidenceLo(), stats.confidenceHi()));
    }

    private double runMonteCarloSimulation(int sideSize) {
        Percolation perc = new Percolation(sideSize);
        boolean percolates = false;
        int gridSize = sideSize * sideSize;

        // if (shouldVisualize) {
        //     StdDraw.enableDoubleBuffering();
        //     visualizePercolation(perc);
        // }

        while (!percolates) {
            chooseAndOpenRandomBlockedSiteIn(perc);
            percolates = perc.percolates();

            // if (shouldVisualize) {
            //     visualizePercolation(perc);
            //     StdDraw.pause(50);
            // }
        }

        return (double) perc.numberOfOpenSites() / gridSize;
    }

    private void chooseAndOpenRandomBlockedSiteIn(Percolation perc) {
        int randomRow = StdRandom.uniform(1, gridSideSize + 1);
        int randomCol = StdRandom.uniform(1, gridSideSize + 1);

        boolean isOpened = perc.isOpen(randomRow, randomCol);
        if (isOpened) {
            chooseAndOpenRandomBlockedSiteIn(perc);
        } else {
            perc.open(randomRow, randomCol);
        }
    }

    // private static void visualizePercolation(Percolation perc) {
    //     PercolationVisualizer.draw(perc, perc.gridSideSize);
    //     StdDraw.show();
    // }

}
