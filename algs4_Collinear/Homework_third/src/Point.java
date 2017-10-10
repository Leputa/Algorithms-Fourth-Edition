/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point
    
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
    	if(compareTo(that)==0)
    		return Double.NEGATIVE_INFINITY;//treat the slope of a degenerate line segment (between a point and itself) as negative infinity.
    	else if(that.x==this.x)
    		return Double.POSITIVE_INFINITY;//treat the slope of a vertical line segment as positive infinity
    	else if(that.y==this.y)
    		return +0.00;                   //Treat the slope of a horizontal line segment as positive zero
    	else 
    		return (double)(that.y-this.y)/(double)(that.x-this.x);
        /* YOUR CODE HERE */
    }

    public int compareTo(Point that) {   //Just as comp() in C++
        /* YOUR CODE HERE */
    	if(this.y<that.y)
    		return -1;
    	else if(this.y>that.y)
    		return 1;
    	else{
    		if(this.x<that.x)
    			return -1;
    		else if(this.x>that.x)
    			return 1;
    		else return 0;
    	}
    }
    
    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    
    private class SlopeOrder implements Comparator<Point> {
        /* YOUR CODE HERE */
    	 public int compare(Point p1,Point p2){
    		 double Slope1=slopeTo(p1);
    		 double Slope2=slopeTo(p2);
    		 if(Slope1<Slope2)
    			 return -1;
    		 else if(Slope1>Slope2) 
    			 return 1;
    		 else
    			 return 0;
    	}
    }
    
    public Comparator<Point> slopeOrder() {
    	return new SlopeOrder();
    }

    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

}