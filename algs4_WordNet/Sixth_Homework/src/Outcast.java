import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wordnet; 
	
	public Outcast(WordNet wordnet) {         // constructor takes a WordNet object
		this.wordnet = wordnet;
	}
	public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast
		int len=nouns.length;
		int distance[][]=new int[len][len];  // distance[i][j]==distance[j][i]
		for(int i=0;i<len;i++) {
			for(int j=i;j<len;j++) {
				distance[i][j]=wordnet.distance(nouns[i], nouns[j]);
				distance[j][i]=distance[i][j];
			}
		}
		
		int max=-1;
		int tag=0;
		for(int i=0;i<len;i++) {
			int dist=0;
			for(int j=0;j<len;j++) {
				dist+=distance[i][j];
			}
			if(dist>max) {
				max=dist;
				tag=i;
			}
		}
		return nouns[tag];
	}
	public static void main(String[] args) {
	    WordNet wordnet = new WordNet(args[0], args[1]);
	    Outcast outcast = new Outcast(wordnet);
	    for (int t = 2; t < args.length; t++) {
	        In in = new In(args[t]);
	        String[] nouns = in.readAllStrings();
	        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
	    }
	}
}
