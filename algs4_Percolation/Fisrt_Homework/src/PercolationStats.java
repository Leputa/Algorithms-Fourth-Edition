import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double staT[];  
    private double sta_mean;  
    private double sta_stddev;  
    private int N;
    public PercolationStats(int N, int T){
    	staT=new double[T];
    	this.N=N;
    	int times=0;
    	if(N<=0) throw new IllegalArgumentException();
    	if(T<=0) throw new IllegalArgumentException();
    	while(times<T){
    		Percolation pe=new Percolation(N);
    		int count=0;
    		while(true){
    			count++;
    			while(true){
    				int x = StdRandom.uniform(N) + 1;  
    				int y = StdRandom.uniform(N) + 1;
    				if(pe.isOpen(x, y)){
    					continue;
    				}else{
    					pe.open(x, y);
    					break;
    				}
    			}
    			if(pe.percolates()){
    				staT[times]=(double)count/((double)N*(double)N);
    				break;
    			}
			 }
    		times++;
    	}
    	this.sta_mean = StdStats.mean(staT);  
    	this.sta_stddev = StdStats.stddev(staT);
   }// perform T independent computational experiments on an N-by-N grid
   public double mean(){
	   return this.sta_mean;
   }// sample mean of percolation threshold
   public double stddev(){
	   return this.sta_stddev;
   } // sample standard deviation of percolation threshold
   public double confidenceLo(){
	   return this.sta_mean-1.96*this.sta_stddev/Math.sqrt(N);
   }// returns lower bound of the 95% confidence interval
   public double confidenceHi(){
	   return this.sta_mean+1.96*this.sta_stddev/Math.sqrt(N);
   }             // returns upper bound of the 95% confidence interval
   public static void main(String[] args){
	  
	   		int N = Integer.parseInt(args[0]);
	   		int T = Integer.parseInt(args[1]);
	        PercolationStats percolationStats = new PercolationStats(N, T);  
	        System.out.println("mean = " + percolationStats.mean());
	        System.out.println("stddev = " + percolationStats.stddev());  
	        System.out.println("95% confidence interval " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi());  
   } // test client, described below
}