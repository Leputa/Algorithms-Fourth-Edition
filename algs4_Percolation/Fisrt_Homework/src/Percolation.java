import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private WeightedQuickUnionUF uf;
	private WeightedQuickUnionUF uf_backwash;
	private int N;
	private int numberOfOpenSites=0;//record the number of open site;
	private boolean[] ifopen;//an array record if the site is open; 
	
	public Percolation(int N) {
		if (N<=0) throw new IllegalArgumentException("N is<=0");
			this.N = N;
		int i;
		uf=new WeightedQuickUnionUF((N+1)*(N+1));   //a bit bigger than n by n
		uf_backwash=new WeightedQuickUnionUF(N*N+N+1);
		ifopen=new boolean[(N+1)*(N+1)];
		for(i=1;i<=N;i++){
			uf.union(0*N+1, 0*N+i);
			uf_backwash.union(0*N+1, 0*N+i);
			ifopen[0*N+i]=true;
			uf.union((N+1)*N+1, (N+1)*N+i);  
		    ifopen[(N+1)*N+i] = true;  
			}
		}// create N-by-N grid, with all sites blocked
		   public void open(int i, int j){
			   if (i<1||i>N) 
				   throw new IndexOutOfBoundsException("row index i out of bounds");
			   if(j<1||j>N)  
				   throw new IndexOutOfBoundsException("column index j out of bounds");
			   if(ifopen[i*N+j]) 
				   return;
	           ifopen[i*N+j]=true;
			   numberOfOpenSites++;
	            if (ifopen[(i-1)*N+j]){  
	                   uf.union(i*N+j, (i-1)*N+j);  
	                   uf_backwash.union(i*N+j, (i-1)*N+j);  
	               }  
	           if (ifopen[(i+1)*N+j]){  
	                uf.union(i*N+j, (i+1)*N+j);  
	                 if (i!=N){  
	                       uf_backwash.union(i*N+j, (i+1)*N+j);  
	                   }  
	               }  
	               if (j!=1 && ifopen[i*N+j-1]){  
	                   uf.union(i*N+j, i*N+j-1);  
	                   uf_backwash.union(i*N+j, i*N+j-1);  
	               }  
	               if (j!=N && ifopen[i*N+j+1]){  
	                   uf.union(i*N+j, i*N+j+1);  
	                   uf_backwash.union(i*N+j, i*N+j+1);  
	               }  
	               
			   
		   }// open site (row i, column j) if it is not already
		   public boolean isOpen(int i, int j){
			   if (i<1||i>N) throw new IndexOutOfBoundsException("row index i out of bounds");
			   if(j<1||j>N)  throw new IndexOutOfBoundsException("column index j out of bounds");
				   
			   return ifopen[i*N+j];
		   }// is site (row i, column j) open?
		   public boolean isFull(int i, int j){
			   if (i<1||i>N) throw new IndexOutOfBoundsException("row index i out of bounds");
			   if(j<1||j>N)  throw new IndexOutOfBoundsException("column index j out of bounds");
		        return uf_backwash.connected(i*N+j, 0*N+1) && ifopen[i*N+j]; 
		   } // is site (row i, column j) full?
		   public boolean percolates() {
			   return uf.connected(0*N+1, (N+1)*N+1);
		   }// does the system percolate?
		   public int numberOfOpenSites(){
			   return numberOfOpenSites;
		}

		   public static void main(String[] args){
			   Percolation per = new Percolation(4);
			   per.open(1, 1);
			   per.open(1, 2);
			   per.open(2, 1);
			   per.open(3, 2);
			   per.open(4, 3);
			   per.open(4, 4);
			   per.open(3, 3);
			   per.open(3, 1);
			   System.out.println(per.percolates());
			   System.out.println("Is (4,3) full? "+per.isFull(4, 3));
		   }// test client, optional
		
}
