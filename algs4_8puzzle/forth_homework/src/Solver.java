import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
	private SearchNode current;
	private boolean isSolvable;
	
	private class SearchNode implements Comparable<SearchNode>{
		private Board board;
		private SearchNode previous;
		private int moves;
		private int priority;     //priority==moves+num_manhattan
		private boolean isInitParity;
		
		//the initial node
		public SearchNode(Board board,boolean isInitParity){
			if(board==null)
				throw new java.lang.NullPointerException();
			this.board=board;
			this.moves=0;
			this.priority=this.moves+this.board.manhattan();
			this.isInitParity=isInitParity;
		}
		//the later nodes
		public SearchNode(Board board,SearchNode previous){
			if(board==null)
				throw new java.lang.NullPointerException();
			if(previous==null)
				throw new java.lang.NullPointerException();
			this.board=board;
			this.previous = previous;
			this.moves=1+previous.moves;
			this.priority=this.moves+this.board.manhattan();
			this.isInitParity=previous.isInitParity;
		}
		public int compareTo(SearchNode that){
			if(this.priority==that.priority)
				return 0;
			else
				return this.priority-that.priority;
		}
	}
	
    public Solver(Board initial){           // find a solution to the initial board (using the A* algorithm)
    	if(initial==null)
    		throw new java.lang.NullPointerException();
    	MinPQ<SearchNode> originPQ=new MinPQ<SearchNode>();
    	originPQ.insert(new SearchNode(initial, true));
    	originPQ.insert(new SearchNode(initial.twin(), false));
    	while(true){
    		current=originPQ.delMin();
    		if(current.board.isGoal())
    			break;
    		for(Board newboard:current.board.neighbors()){
    			if(current.previous==null||!newboard.equals(current.previous.board))
    				originPQ.insert(new SearchNode(newboard, current));
    		}
    	}
    	isSolvable=current.isInitParity;
    }
    public boolean isSolvable(){            // is the initial board solvable?
    	return isSolvable;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
    	if(isSolvable)
    		return current.moves;
    	else 
    		return -1;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
    	if(!isSolvable)
    		return null;
    	Stack<Board>boards=new Stack<Board>();
    	SearchNode node=current;
    	while(node!=null){
    		boards.push(node.board);
    		node=node.previous;
    	}
    	return boards;
    }
    public static void main(String[] args) {

        // create initial board from file
        In in = new In("./8puzzle/puzzle3x3-04.txt"); 
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
    }
}