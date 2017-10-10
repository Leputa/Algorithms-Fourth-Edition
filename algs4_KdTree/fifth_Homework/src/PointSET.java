import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
	private SET<Point2D> pointSet;
	
	public  PointSET() {
		// construct an empty set of points 
		this.pointSet=new SET<Point2D>();
	}
	public  boolean isEmpty(){
		// is the set empty?
		return pointSet.size()==0;
	}
	public  int size(){
		// number of points in the set 
		return pointSet.size();
	}
	public  void insert(Point2D p) {
		// add the point to the set (if it is not already in the set)
		if(p==null)
			throw new java.lang.NullPointerException();
		pointSet.add(p);
	}
	public  boolean contains(Point2D p){
		// does the set contain point p?
		if(p==null)
			throw new java.lang.NullPointerException();
		return pointSet.contains(p);
	}
	public  void draw(){
		// draw all points to standard draw
		StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
		for(Point2D p:pointSet)
			StdDraw.point(p.x(), p.y());
	}
	public 	Iterable<Point2D> range(RectHV rect) {
		// all points that are inside the rectangle
		if(rect==null)
			throw new java.lang.NullPointerException();
		SET<Point2D>rangeSet=new SET<Point2D>();
		for(Point2D p:pointSet)
			if(rect.contains(p))
					rangeSet.add(p);
		return rangeSet;
		
	}
	public  Point2D nearest(Point2D p){               	// a nearest neighbor in the set to point p; null if the set is empty 
		Point2D nearestPoint=null;
		double dis=Double.MAX_VALUE;
		if(this.pointSet.isEmpty())
			return nearestPoint;
		for(Point2D point:pointSet){
			if(point.distanceSquaredTo(p)<dis){
				dis=point.distanceSquaredTo(p);
				nearestPoint=point;
			}
		}
		return nearestPoint;
	}
	
	 public static void main(String[] args) {

	        String filename ="./kdtree/circle4.txt";
	        In in = new In(filename);

	        StdDraw.enableDoubleBuffering();

	        // initialize the data structures with N points from standard input
	        PointSET brute = new PointSET();
	        //KdTree kdtree = new KdTree();
	        while (!in.isEmpty()) {
	            double x = in.readDouble();
	            double y = in.readDouble();
	            Point2D p = new Point2D(x, y);
	            //kdtree.insert(p);
	            brute.insert(p);
	        }

	        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
	        double x1 = 0.0, y1 = 0.0;      // current location of mouse
	        boolean isDragging = false;     // is the user dragging a rectangle

	        // draw the points
	        StdDraw.clear();
	        StdDraw.setPenColor(StdDraw.BLACK);
	        StdDraw.setPenRadius(0.01);
	        brute.draw();
	        StdDraw.show();

	        while (true) {

	            // user starts to drag a rectangle
	            if (StdDraw.mousePressed() && !isDragging) {
	                x0 = StdDraw.mouseX();
	                y0 = StdDraw.mouseY();
	                isDragging = true;
	                continue;
	            }

	            // user is dragging a rectangle
	            else if (StdDraw.mousePressed() && isDragging) {
	                x1 = StdDraw.mouseX();
	                y1 = StdDraw.mouseY();
	                continue;
	            }

	            // mouse no longer pressed
	            else if (!StdDraw.mousePressed() && isDragging) {
	                isDragging = false;
	            }


	            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
	                                     Math.max(x0, x1), Math.max(y0, y1));
	            // draw the points
	            StdDraw.clear();
	            StdDraw.setPenColor(StdDraw.BLACK);
	            StdDraw.setPenRadius(0.01);
	            brute.draw();

	            // draw the rectangle
	            StdDraw.setPenColor(StdDraw.BLACK);
	            StdDraw.setPenRadius();
	            rect.draw();

	            // draw the range search results for brute-force data structure in red
	            StdDraw.setPenRadius(0.03);
	            StdDraw.setPenColor(StdDraw.RED);
	            for (Point2D p : brute.range(rect))
	                p.draw();
	            
	            StdDraw.show();
	            StdDraw.pause(40);
	        }
	    }
}
