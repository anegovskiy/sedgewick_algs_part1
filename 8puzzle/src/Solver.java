/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previousNode;
        private final int priority;

        private SearchNode(Board board, SearchNode previousNode, int numberOfMoves) {
            this.board = board;
            this.previousNode = previousNode;
            this.priority = numberOfMoves + board.manhattan();
        }

        public int compareTo(SearchNode searchNode) {
            return Integer.compare(priority, searchNode.priority);
        }
    }

    private final Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("board can't be null");
        }

        solution = getSolutionFor(initial);
    }

    private Stack<Board> getSolutionFor(Board board) {
        Stack<Board> solutionOfInitial = new Stack<>();
        if (board.isGoal()) {
            solutionOfInitial.push(board);
            return solutionOfInitial;
        }

        Board twin = board.twin();
        MinPQ<SearchNode> solvingQueueOfInitial = new MinPQ<>();
        MinPQ<SearchNode> solvingQueueOfTwin = new MinPQ<>();

        SearchNode searchNodeInitial = new SearchNode(board, null, 0);
        solvingQueueOfInitial.insert(searchNodeInitial);

        SearchNode searchNodeTwin = new SearchNode(twin, null, 0);
        solvingQueueOfTwin.insert(searchNodeTwin);

        while (!searchNodeInitial.board.isGoal() && !searchNodeTwin.board.isGoal()) {
            searchNodeInitial = solvingQueueOfInitial.delMin();
            enqueueNeighborsOf(searchNodeInitial, solvingQueueOfInitial);

            searchNodeTwin = solvingQueueOfTwin.delMin();
            enqueueNeighborsOf(searchNodeTwin, solvingQueueOfTwin);
        }

        if (!searchNodeInitial.board.isGoal()) {
            return null;
        }

        while (searchNodeInitial != null) {
            solutionOfInitial.push(searchNodeInitial.board);
            searchNodeInitial = searchNodeInitial.previousNode;
        }

        return solutionOfInitial;
    }

    private void enqueueNeighborsOf(SearchNode initialNode, MinPQ<SearchNode> queue) {
        Iterable<Board> neighbors = initialNode.board.neighbors();
        SearchNode previousNode = initialNode.previousNode;
        int movesDone = countMovesDoneFor(initialNode);

        neighbors.forEach(neighbor -> {
            SearchNode neighborSearchNode = new SearchNode(neighbor, initialNode, movesDone + 1);
            if (previousNode != null) {
                if (previousNode.board.equals(neighborSearchNode.board)) {
                    return;
                }
            }

            queue.insert(neighborSearchNode);
        });
    }

    private int countMovesDoneFor(SearchNode node) {
        int movesDone = 0;
        SearchNode parentNode = node;

        while (parentNode != null) {
            parentNode = parentNode.previousNode;
            movesDone += 1;
        }

        return movesDone;
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution == null ? -1 : solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

    }
}
