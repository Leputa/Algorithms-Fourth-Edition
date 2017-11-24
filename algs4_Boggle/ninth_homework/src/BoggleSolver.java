import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
	
	private int M;
	private int N;
	private TrieSET trieSET;
	private boolean marked[][];
	private SET<String>validWords;
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	trieSET=new TrieSET();
    	for(String string:dictionary) {
    		trieSET.add(string);
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
    	M=board.rows();
    	N=board.cols();
    	validWords=new SET<>();
    	for(int i=0;i<M;i++) {
    		for(int j=0;j<N;j++) {
    			marked=new boolean[M][N];
    			trieSET.resetSearch();
    			dfs(board,i,j,"");
    		}
    	}
    	return validWords;
    }

  
	private void dfs(BoggleBoard board,int i, int j, String word) {
		// TODO Auto-generated method stub
		marked[i][j]=true;
		char c=board.getLetter(i, j);
		if(trieSET.oneStepForward(c)) {
			String tmpString=new String();
			if(c=='Q') {
				if(!trieSET.oneStepForward('U')) {
					trieSET.oneStepBack();
					return;
				}
				else
					tmpString=word+c+'U';
			}
			else 
				tmpString=word+c;
			if(tmpString.length()>=3&&trieSET.contains(tmpString)) {
				validWords.add(tmpString);
			}
			for(int x=-1;x<=1;x++) {
				for(int y=-1;y<=1;y++) {
					if(i+x>=0&&i+x<M&&j+y>=0&&j+y<N&&!marked[i+x][j+y]) {
						dfs(board, i+x, j+y, tmpString);
						marked[i+x][j+y]=false;
					}	
				}
			}
			if(c=='Q')
				trieSET.oneStepBack();
			trieSET.oneStepBack();
		}
	}

	// Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (word.length() <= 2) return 0;
    	
        if (!trieSET.contains(word)) return 0;
        
        if (word.length() == 3 || word.length() == 4) return 1;
        if (word.length() == 5) return 2;
        if (word.length() == 6) return 3;
        if (word.length() == 7) return 5;
        return 11;
    }
    
    public static void main(String[] args) {
        In in = new In("./boggle/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("./boggle/board-q.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
