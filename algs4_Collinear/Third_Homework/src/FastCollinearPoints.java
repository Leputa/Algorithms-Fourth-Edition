import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;


public class FastCollinearPoints  {
	private Point origin;
	private int  numofSeg=0;
	private ArrayList<LineSegment> segment = new ArrayList<LineSegment>();


	public FastCollinearPoints(Point[] points){    // finds all line segments containing 4 points
		if(points==null)
			throw new java.lang.NullPointerException("there is no point");
		int Length=points.length;
		Point[] copy = new Point[Length];
		for (int i = 0; i < Length; i++) {
	          copy[i] = points[i];
		}
		Arrays.sort(copy);
		Point[] Other=new Point[Length];//except the original point
		for(int i=0;i<Length-1;i++){
			if(copy[i]==null)
				throw new java.lang.NullPointerException("the point is no existence");
			if(copy[i].equals(copy[i+1]))
				throw new java.lang.IllegalArgumentException("There is repeated point");
		}
		
		for(int i=0;i<Length;i++){
			for(int j=i+1;j<Length;j++)
				Other[j]=copy[j];
			origin=copy[i];
			Arrays.sort(Other,i+1,Length,origin.slopeOrder());
			Arrays.sort(copy,0,i,origin.slopeOrder());
			double slope1=0;
			int j = i + 1;
			while(j < Length)
			{
				int l = j;
				j++;
				slope1=origin.slopeTo(Other[l]);
				boolean flag = false;
				int lc = 0, rc = i-1;
				while(lc <= rc)
				{
					int k = (lc+rc)/2;
					if(slope1 == copy[k].slopeTo(origin))
					{
						flag = true;
						break;
					}
					else
					{
						if(slope1 < copy[k].slopeTo(origin))
						{
							rc = k - 1;
						}
						else
						{
							lc = k + 1;
						}
					}
				}
				if(flag)continue;
				while(j < Length && slope1==origin.slopeTo(Other[j]))j++;
				if(j - l > 2)
				{
						segment.add(new LineSegment(origin, Other[j-1]));
						numofSeg++;
				}
			}
		}
	}
	public int numberOfSegments(){	      // the number of line segments
		return numofSeg;
	}
	public LineSegment[] segments() {     // the line segments
		LineSegment[] results = new LineSegment[numofSeg];
		for (int i = 0; i < numofSeg; i++) {
			results[i] = segment.get(i);
		}
		return results;
	} 
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        // read the n points from a file
        In in = new In("./collinear/input100.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

//*******but I can't understand why the method as following exist some bugs**********//
/*
for(int i=0;i<Length-3;i++){
	for(int j=0;j<Length;j++)
		Other[j]=copy[j];
	origin=copy[i];
	Arrays.sort(Other,i+1,Length,origin.slopeOrder());
	Arrays.sort(Other,0,i,origin.slopeOrder());
	int head=i+1;
	boolean tag=false;
	double slope1=0;
	double slope2=0;
	while(head<Length-2){
		slope1=origin.slopeTo(Other[head]);
		if(slope1==origin.slopeTo(Other[head+1])&&slope1==origin.slopeTo(Other[head+2])){
			tag=true;
			break;
		}
		head++;
	}
	if(tag){
		int phead=0;
		while(phead<i){
			slope2=origin.slopeTo(Other[phead]);
			if(slope2>=slope1)
				break;
			phead++;
		}
		if(slope1!=slope2){
			int tail=head+2;
			while(tail<Length&&slope1==origin.slopeTo(Other[tail]))
				tail++;
			segment.add(new LineSegment(origin, Other[tail-1]));
			numofSeg++;
		}	
	}
}
*/
