import java.awt.Color;

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	static final private double BORDERENERGY=1000;
	private Picture picture;
	private Color[][] colorMatrix;
	private double[] distTo;
	private int[] edgeTo;
	
	//Warning: this is the opposite of the standard mathematical notation used in linear algebra where (i, j) refers to row i and column j and with Cartesian coordinates where (0, 0) is at the lower left corner.
	public SeamCarver(Picture picture) {                // create a seam carver object based on the given picture
		if (picture==null)
			throw new java.lang.IllegalArgumentException();
		this.picture=picture;
		colorMatrix=new Color[picture.height()][picture.width()];
		for(int i=0;i<picture.height();i++)
			for(int j=0;j<picture.width();j++)
				colorMatrix[i][j]=picture.get(j, i);
		distTo=new double[width()*height()];
		edgeTo=new int[width()*height()];
	}
	



	public Picture picture() {                          // current picture
		int height=colorMatrix.length;
		int width=colorMatrix[0].length;
		System.out.println(height+"  "+width);
		Picture newPicture = new Picture(width,height);
	    for (int x = 0; x < height; x++) {
	    	for (int y = 0; y < width; y++) {
	    		newPicture.set(y, x, colorMatrix[x][y]);
	        }
	    }
	    this.picture=newPicture;
	    return newPicture;
	}
	
	public int width() {                            // width of current picture
		return picture.width();
	}
	
	public int height() {                           // height of current picture
		return picture.height();
	}
	public double energy(int x, int y) {               // energy of pixel at column x and row y
		if(x<0||x>=width()||y<0||y>=height())
			throw new java.lang.IllegalArgumentException("");
		if(x==0||x==colorMatrix[0].length-1||y==0||y==colorMatrix.length-1)
			return BORDERENERGY;
		final Color leftPixel=colorMatrix[y][x-1];
		final Color rightPixel=colorMatrix[y][x+1];
		final Color upPixel=colorMatrix[y-1][x];
		final Color downPixel=colorMatrix[y+1][x];
		double redX=leftPixel.getRed()-rightPixel.getRed();
		double greenX=leftPixel.getGreen()-rightPixel.getGreen();
		double blueX=leftPixel.getBlue()-rightPixel.getBlue();
		double redY=upPixel.getRed()-downPixel.getRed();
		double greenY=upPixel.getGreen()-downPixel.getGreen();
		double blueY=upPixel.getBlue()-downPixel.getBlue();
		return Math.sqrt(redX*redX+greenX*greenX+blueX*blueX+redY*redY+greenY*greenY+blueY*blueY);
	}
	
	private void DijkstraSP(boolean isVertical) {
		int V=width()*height();
		for(int v=0;v<V;v++) {
			distTo[v]=Double.POSITIVE_INFINITY;
			edgeTo[v]=0;
		}
		int Len=width();
		if(!isVertical)
			Len=height();
		IndexMinPQ<Double>pq=new IndexMinPQ<>(V);
		//the all pixels at top row are source
		for(int v=0;v<Len;v++) {
			distTo[v]=BORDERENERGY;
			pq.insert(v, distTo[v]);
		}
		boolean notFinStat=true;
		while(!pq.isEmpty()&&notFinStat) {
			int v=pq.delMin();
			relax(v,isVertical,pq);
			//ensure there is at least a path to bottom rather than left or right
			for(int i=V-1;i>=V-Len;--i) {
				if(distTo[i]!=Double.POSITIVE_INFINITY) {
					notFinStat=false;
					break;
				}
			}
		}
		
	}
	
	private void relax(int v, boolean isVertical,IndexMinPQ<Double> pq) {
		// TODO Auto-generated method stub
		int x,y,w;
		double weight;
		int width=width();
		int height=height();
		if(!isVertical) {
			width=height();
			height=width();
		}
		x=v%width;   //col
		y=v/width;   //row
		if(x==0||x==width-1||y==height-1)
			return;
		//choose the three pixels(w) below v
		for(int k=-1;k<2;k++){
			w=(y+1)*width+x+k;
			if(isVertical)
				weight=energy(x+k, y+1);
			else
				weight=energy(y+1, x+k);
			if(distTo[w]>distTo[v]+weight) {
				distTo[w]=distTo[v]+weight;
				edgeTo[w]=v;
				if(y+1==height-1)
					return;
				if(pq.contains(w))
					pq.change(w, distTo[w]);
				else
					pq.insert(w, distTo[w]);
			}
		}
	}
	private int[]findSeam(boolean isVertical){
		int width=width();
		int height=height();
		if(!isVertical) {
			width=height();
			height=width();
		}
		double minDist=Double.POSITIVE_INFINITY;
		int minIndex=0;
		int[]seamPath=new int[height];
		int start=width*height-width;
		for(int i=0;i<width;i++) {
			if(distTo[start+i]<minDist) {
				minDist=distTo[start+i];
				minIndex=start+i;
			}
		}
		//if i==0,edgeTo[minIndex] is not 
		for(int i=height-1;i>0;i--) {
			seamPath[i]=minIndex%width;   //i is the row,seamPath[i] is the col
			minIndex=edgeTo[minIndex];
		}
		//the first path is vertical
		if(seamPath.length>1)
			seamPath[0]=seamPath[1];
		return seamPath;
	}
	
	
	public int[] findHorizontalSeam() {               // sequence of indices for horizontal seam
		DijkstraSP(false);
		return findSeam(false);
	}
	public int[] findVerticalSeam() {                 // sequence of indices for vertical seam
		DijkstraSP(true);
		return findSeam(true);
	}
	
	public void removeHorizontalSeam(int[] seam) {   // remove horizontal seam from current picture
		int height=height();
		int width=width();
		if(seam==null)
			throw new java.lang.IllegalArgumentException();
		if(seam.length!=width||height<=1)
			throw new java.lang.IllegalArgumentException();
		Color[][]copy=new Color[height-1][width];
		for(int x=0;x<width;x++) {
			if(seam[x]<0||seam[x]>=height)
				throw new java.lang.IllegalArgumentException();
			for(int y=0;y<height;y++) {
				if(y<seam[x])
					copy[y][x]=colorMatrix[y][x];
				else if(y>seam[x])
					copy[y-1][x]=colorMatrix[y][x];
			}
		}
		colorMatrix=copy;
		picture();	
	}
	
	public void removeVerticalSeam(int[] seam) {     // remove vertical seam from current picture
		int height=height();
		int width=width();
		if(seam==null)
			throw new java.lang.IllegalArgumentException();
		if(seam.length!=height||width<=1)
			throw new java.lang.IllegalArgumentException();
		Color[][]copy=new Color[height][width-1];
		for(int y=0;y<height;y++) {
			if(seam[y]<0||seam[y]>=width)
				throw new java.lang.IllegalArgumentException();
			for(int x=0;x<width;x++) {
				if(x<seam[y])
					copy[y][x]=colorMatrix[y][x];
				else if(x>seam[y])
					copy[y][x-1]=colorMatrix[y][x];	
			}
		}
		colorMatrix=copy;
		picture();
	}
	
	public static void main(String[] args) {
		
	}
}
