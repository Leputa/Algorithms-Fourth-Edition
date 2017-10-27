import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SET;

public class WordNet {
	private class Noun implements Comparable<Noun>{
		private String noun;
		private List<Integer>id;
		
		public Noun(String noun){
			id=new ArrayList<>();
			this.noun=noun;
		}
		
		public List<Integer>getId(){
			return this.id;
		}
		public void addId(int x) {
			this.id.add(x);
		}

		@Override
		public int compareTo(Noun o) {
			// TODO Auto-generated method stub
			return this.noun.compareTo(o.noun);
		}
		
	}
	
	private SAP sap;
	private Digraph G;
	private int rootCount;
	private SET<Noun>nounSet;
	private HashMap<Integer, String>map;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		In inSynset=new In(synsets);
		In inHypernyms=new In(hypernyms);
		
		int maxV=0;
		map=new HashMap<>();
		nounSet=new SET<Noun>();
		
		String line=inSynset.readLine();
		
		while(line!=null) {
			++maxV;
			String[]tmpline=line.split(",");
			int id=Integer.parseInt(tmpline[0]);
			String[]synonym=tmpline[1].split(" ");
			for(String nounName:synonym) {
				Noun noun=new Noun(nounName);
				if(nounSet.contains(noun)) {
					noun=nounSet.ceiling(noun);//return the noun that exists in nounSet,then update the idList
					noun.addId(id);
				}
				else {
					noun.addId(id);
					nounSet.add(noun);
				}
			}
			
			if(map.getOrDefault(id, null)==null)
				map.put(id, tmpline[1]);
			else
				throw new java.lang.IllegalArgumentException();
			
			line=inSynset.readLine();
		}
		G=new Digraph(maxV);
		Boolean[]isNotRoot=new Boolean[maxV];
		line=inHypernyms.readLine();
		while(line!=null) {
			String[]tmpLine=line.split(",");
			int v=Integer.parseInt(tmpLine[0]);
			isNotRoot[v]=true;
			for(int i=1;i<tmpLine.length;++i) {
				int w=Integer.parseInt(tmpLine[i]);
				G.addEdge(v, w);
			}
		}
		line=inHypernyms.readLine();
		
		//The wordnet digraph only has one root
		rootCount=0;
		for(int i=0;i<maxV;++i) {
			if(isNotRoot[i])
				++rootCount;
			if(rootCount>1)
				throw new java.lang.IllegalArgumentException();
		}
		
		//The wordnet digraph is a rooted DAG
		DirectedCycle cycle=new DirectedCycle(G);
		if (cycle.hasCycle())
			throw new java.lang.IllegalArgumentException();
		
		sap = new SAP(G); 	
	}

	// returns all WordNet nouns
	public Iterable<String> nouns(){
		Queue<String>it=new Queue<>();
		for (Noun noun:nounSet)
			it.enqueue(noun.noun);
		return it;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		Noun noun=new Noun(word);
		return nounSet.contains(noun);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
	    if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }
        Noun nodeA=nounSet.ceiling(new Noun(nounA));
        Noun nodeB=nounSet.ceiling(new Noun(nounA));
        return sap.length(nodeA.getId(), nodeB.getId());
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
        if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }
        Noun nodeA=nounSet.ceiling(new Noun(nounA));
        Noun nodeB=nounSet.ceiling(new Noun(nounA));
        
        return map.get(sap.ancestor(nodeA.getId(), nodeB.getId()));
		
	}

	// do unit testing of this class
	public static void main(String[] args) {
		
	}
}
