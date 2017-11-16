import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	// create a baseball division from given filename in format specified below
	private int num;
	private int w[];
	private int l[];
	private int r[];
	private int g[][];

	private final List<String>teams;
	private boolean[]isEliminateExec;
	private List<List<Integer>>R; 
	
	public BaseballElimination(String filename) {
		 In in = new In(filename);
	        
	     // initial variable
	     num = in.readShort();
	     teams = new ArrayList<>();
	     w = new int[num];
	     l = new int[num];
	     r = new int[num];
	     g = new int[num][num];
	     
	     isEliminateExec=new boolean[num];
	     R=new ArrayList<List<Integer>>();
	     for(int i=0;i<num;i++) {
	    	 List<Integer>tmpList=new ArrayList<>();
	    	 R.add(tmpList);
	     }
	     int i = 0;
	     while (!in.isEmpty()) {
	         teams.add(in.readString());
	         w[i] = in.readShort();
	         l[i] = in.readShort();
	         r[i] = in.readShort();
	         for (int j = 0; j < num; j++) 
	             g[i][j] = in.readShort();            
	         i++;
	     }
	}
	
	// number of teams
	public int numberOfTeams() {
		return num;
		
	}
	// all teams
	public Iterable<String> teams(){
		return teams;
	}

	private int getIndex(String team) {
		// TODO Auto-generated method stub
	if(!teams.contains(team))
			throw new java.lang.IllegalArgumentException();
		return teams.indexOf(team);
	}

	// number of wins for given team
	public int wins(String team) {
		int index=getIndex(team);
		return w[index];	
	}

	// number of losses for given team
	public int losses(String team) {
		int index=getIndex(team);
		return l[index];
	}
	// number of remaining games for given team
	public int remaining(String team) {
		int index=getIndex(team);
		return r[index];
	}
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		int i=getIndex(team1);
		int j=getIndex(team2);
		return g[i][j];
	}


	// is given team eliminated?
	public boolean isEliminated(String team) {
		int i=getIndex(team);
		if(!isEliminateExec[i]) {
			computeR(i);
			isEliminateExec[i]=true;
		}
		
		if(R.get(i).isEmpty())
			return false;
		else
			return true;
	}

	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team){
		int i=getIndex(team);
		if(!isEliminateExec[i]) {
			computeR(i);
			isEliminateExec[i]=true;
		}
		if(R.get(i).isEmpty())
			return null;
		List<String>res=new ArrayList<>();
		for(int x:R.get(i)) {
			res.add(teams.get(x));
		}
		return res;
	}  

	private void computeR(int index) {
		// TODO Auto-generated method stub
		List<Integer>RR=R.get(index);
		for(int i=0;i<num;i++) {
			if(i!=index&&w[index]+r[index]<w[i]) {
				RR.add(i);
				return;
			}		
		}
		int N=num*num+num+2;
		FlowNetwork fNetwork=buildFlowNetwork(index,N);
		FordFulkerson FF = new FordFulkerson(fNetwork, N-2, N-1);
		for(int i=num*num;i<num*num+num;i++) 
			if(i-num*num!=index&&FF.inCut(i)) {
				RR.add(i-num*num);
			}
	}
	private FlowNetwork buildFlowNetwork(int index,int N) {
		// TODO Auto-generated method stub
		boolean tag[][]=new boolean[num][num];
		FlowNetwork fNetwork=new FlowNetwork(N);
		for(int i=num*num;i<N-2;i++) {
			if(index!=i-num*num) {
				fNetwork.addEdge(new FlowEdge(i,N-1,w[index]+r[index]-w[i-num*num]));
			}
		}
		for(int i=0;i<num*num;i++) {
			int v=i/num;
			int w=i%num;
			if(v!=index&&w!=index&&v!=w&&!tag[w][v]) {
				fNetwork.addEdge(new FlowEdge(N-2,i,g[v][w]));
				fNetwork.addEdge(new FlowEdge(i,num*num+v,Integer.MAX_VALUE));
				fNetwork.addEdge(new FlowEdge(i,num*num+w,Integer.MAX_VALUE));
				tag[w][v]=true;
				tag[v][w]=true;
			}
		}
		return fNetwork;
	}

	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination("./baseball/teams5.txt");
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
}
