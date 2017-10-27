import java.util.List;
import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private Digraph G;
	private boolean[] markedV;
	private boolean[] markedW;
	private int[] distToV;
	private int[] distToW;
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		this.G=G;
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		BFS bv=new BFS(G, v);
		BFS bw=new BFS(G, w);
		int[]ans=new int[2];
		ans=getShortest(bv,bw);
		return ans[0];
		
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		BFS bv=new BFS(G, v);
		BFS bw=new BFS(G, w);
		int[]ans=new int[2];
		ans=getShortest(bv,bw);
		return ans[1];
	}



	private int[] getShortest(BFS bv, BFS bw) {
		// TODO Auto-generated method stub
		markedV=bv.getMark();
		markedW=bw.getMark();
		distToV=bv.getDistTo();
		distToW=bw.getDistTo();
		int length=Integer.MAX_VALUE;
		int ancestor=-1;
		int tag=0;
		
		for (int i=0;i<markedV.length;++i) {
			if(markedV[i]) {
				if(markedW[i]) {
					tag=1;
					if(distToV[i]+distToW[i]<length) {
						length=distToV[i]+distToW[i];
						ancestor=i;
					}
				}
			}
		}
		int[]ans=new int[2];
		if(tag==0) {
			ans[0]=-1;
			ans[1]=-1;
			return ans;
		}
		ans[0]=length;
		ans[1]=ancestor;
		return ans;
		
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		BFS bv=new BFS(G, v);
		BFS bw=new BFS(G, w);
		int[]ans=new int[2];
		ans=getShortest(bv,bw);
		return ans[0];
		
		
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		BFS bv=new BFS(G, v);
		BFS bw=new BFS(G, w);
		int[]ans=new int[2];
		ans=getShortest(bv,bw);
		return ans[1];
	}

	// do unit testing of this class
	public static void main(String[] args) {
	    In in = new In("./wordnet/digraph1.txt");
	    Digraph G = new Digraph(in);
	    SAP sap = new SAP(G);
	    while (!StdIn.isEmpty()) {
	        int v = StdIn.readInt();
	        int w = StdIn.readInt();
	        int length   = sap.length(v, w);
	        int ancestor = sap.ancestor(v, w);
	        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
	    }
	}
}
