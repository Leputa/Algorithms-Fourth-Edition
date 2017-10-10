import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
	private static class Node{
		private Node lChild;
		private Node rChild;
		private final RectHV rect;
		private Point2D point;
		private boolean isVertical;
		public Node(Point2D p,RectHV rect,boolean isVertical){
			this.point=p;
			this.rect=rect;
			this.isVertical=isVertical;
		}
	}
	
	private Node root;
	private int size;
	
	public  KdTree() {
	// construct an empty set of points 
		this.size=0;
		this.root=null;
	}
	
   public boolean isEmpty(){
	   // is the set empty? 
	   return this.size==0;
   }
   public int size(){
	   // number of points in the set 
	   return this.size;
   }
   public void insert(Point2D p){
	   // add the point to the set (if it is not already in the set)
	   if(p==null)
		   throw new java.lang.NullPointerException();
	   root=insert(root,null,p,0);
   }
   private Node insert(Node current,Node parent,Point2D p,int direction){
	   if(current==null){
		   if(size==0){
			   size++;
			   return new Node(p, new RectHV(0, 0, 1, 1), true);
		   }
		   size++;
		   RectHV rectOfCurrent=parent.rect;
		   if(direction<0){ //go left or down   
			   if(parent.isVertical)  //go left
				   	rectOfCurrent=new RectHV(parent.rect.xmin(),parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
			   else                   //go down
				   	rectOfCurrent=new RectHV(parent.rect.xmin(),parent.rect.ymin(), parent.rect.xmax(), parent.point.y());  
		   }
		   else if(direction>0) //go right or up      
			   if(parent.isVertical)  // go right
				   rectOfCurrent=new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
			   else                   // go up
				   rectOfCurrent=new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
		   return new Node(p, rectOfCurrent, !parent.isVertical);
	   }
	   int cmp=compare(p,current.point,current.isVertical);
	   if (cmp>0)  //go up or right
		   current.rChild=insert(current.rChild, current, p, cmp);
	   else if(cmp<0)
		   current.lChild=insert(current.lChild, current, p, cmp);
	   return current;
   }
   
   private int compare(Point2D p,Point2D q,boolean isVertical){
	   if(p==null||q==null)
		   throw new java.lang.NullPointerException();
	   if(p.equals(q))
		   return 0;
	   if(isVertical)
		   return p.x()<q.x()?-1:1; //q is current
	   else
		   return p.y()<q.y()?-1:1;
   }
   
   public boolean contains(Point2D p) {
	   // does the set contain point p? 
	   if(p==null)
		   throw new java.lang.NullPointerException();
	   return contains(root,p);
   }
   private boolean contains(Node x,Point2D p){
	   if(x==null)
		   return false;
	   int cmp=compare(p,x.point,x.isVertical);
	   if(cmp<0)
		   return contains(x.lChild,p);
	   else if(cmp>0)
		   return contains(x.rChild, p);
	   else return true;	   
   }
   
   public void draw(){
	   // draw all points to standard draw 
	   draw(root);
   }
   private void draw(Node x)
   {
       if (x == null) return; 
       draw(x.lChild);
       draw(x.rChild);
       StdDraw.setPenColor(StdDraw.BLACK);
       StdDraw.setPenRadius(0.01);
       x.point.draw();
       StdDraw.setPenRadius();
       // draw the splitting line segment
       if (x.isVertical) 
       {
           StdDraw.setPenColor(StdDraw.RED);
           StdDraw.line(x.point.x(), x.rect.ymin(), x.point.x(), x.rect.ymax());   
       }
       else
       {
           StdDraw.setPenColor(StdDraw.BLUE);
           StdDraw.line(x.rect.xmin(), x.point.y(), x.rect.xmax(), x.point.y());   
       }
   } 

   public Iterable<Point2D> range(RectHV rect){
	   // all points that are inside the rectangle 
	   if(rect==null)
		   throw new java.lang.NullPointerException();
	   Queue<Point2D> rangeQueue=new Queue<Point2D>();
	   range(root,rangeQueue,rect);
	   return rangeQueue;
   }
   private void range(Node x,Queue<Point2D>rangeQueue,RectHV rect){
	   if(x==null)
		   return;
	   if(rect.contains(x.point))
		   rangeQueue.enqueue(x.point);
	   if(x.lChild!=null&&rect.intersects(x.lChild.rect))
		   range(x.lChild,rangeQueue,rect);
	   if(x.rChild!=null&&rect.intersects(x.rChild.rect))
		   range(x.rChild,rangeQueue,rect);
   }
   
   public Point2D nearest(Point2D p){
	   // a nearest neighbor in the set to point p; null if the set is empty 
	   if(p==null)
		   throw new java.lang.NullPointerException();
	   if(root==null)
		   return null;
	   return nearest(root,root.point,p);
   }
   private Point2D nearest(Node x,Point2D ntPoint,Point2D p){
	   if(x==null)
		   return ntPoint;
	   int cmp=compare(p, x.point, x.isVertical);
	   if(p.distanceSquaredTo(x.point)<p.distanceSquaredTo(ntPoint))
		   ntPoint=x.point;
	   if(cmp<0){
		   ntPoint=nearest(x.lChild,ntPoint,p);
		   if (x.rChild!= null)
               if (ntPoint.distanceSquaredTo(p) > x.rChild.rect.distanceSquaredTo(p))
                   ntPoint = nearest(x.rChild, ntPoint, p);
	   }
	   else if(cmp>0){
		   ntPoint=nearest(x.rChild,ntPoint,p);
		   if (x.lChild!= null)
               if (ntPoint.distanceSquaredTo(p) > x.lChild.rect.distanceSquaredTo(p))
                   ntPoint = nearest(x.lChild, ntPoint, p);
	   }
	   return ntPoint;
   }

   public static void main(String[] args) {
       RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
       StdDraw.enableDoubleBuffering();
       KdTree kdtree = new KdTree();
       while (true) {
           if (StdDraw.mousePressed()) {
               double x = StdDraw.mouseX();
               double y = StdDraw.mouseY();
               StdOut.printf("%8.6f %8.6f\n", x, y);
               Point2D p = new Point2D(x, y);
               if (rect.contains(p)) {
                   StdOut.printf("%8.6f %8.6f\n", x, y);
                   kdtree.insert(p);
                   StdDraw.clear();
                   kdtree.draw();
                   StdDraw.show();
               }
           }
           StdDraw.pause(50);
       }
   }
}