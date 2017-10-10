import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] Rqueue;
	private int size;
	
	public RandomizedQueue() { 		// construct an empty randomized queue
		Rqueue=(Item[]) new Object[1];
		size=0;
	}
	             
   public boolean isEmpty(){      // is the queue empty?
	   return size==0;
   }
   
   public int size(){           // return the number of items on the queue
	   return size;
   }
   
   private void resize(int capacity){
		Item[]copy=(Item[])new Object[capacity];
		for(int i=0;i<size;i++)
			copy[i]=Rqueue[i];
		Rqueue=copy;
	}
   
   public void enqueue(Item item){	// add the item
	   if(item==null)
		   throw new java.lang.NullPointerException("shurubuhefa");
	   if(size==Rqueue.length)
		   resize(2*Rqueue.length);
	   Rqueue[size]=item;
	   size++;	   
   }
   
   public Item dequeue(){// remove and return a random item
	   if(isEmpty())
		   throw new java.util.NoSuchElementException("randomized queue underflow");
	   int rn=StdRandom.uniform(size);     //Math.random include 0 but don't include 1
	   Item item=Rqueue[rn];
	   Rqueue[rn]=Rqueue[size-1];
	   Rqueue[size-1]=null;
	   size--;
	   if(size>0&&size==Rqueue.length/4)
			resize(Rqueue.length/2);
	   return item;
   }
   
   public Item sample(){// return (but do not remove) a random item
	   if(isEmpty())
		   throw new java.util.NoSuchElementException("randomized queue underflow");
	   int rn=StdRandom.uniform(size);      
	   Item item=Rqueue[rn];
	   return item;
   }

   public Iterator<Item> iterator() {// return an independent iterator over items in random order
	   return new RQIterator();
   }
   
   private class RQIterator implements Iterator<Item>{
	   private int tag=0;
	   private Item[] rq;
	   public RQIterator(){
		   rq=(Item[]) new Object[size];
		   for(int i=0;i<size;i++)
			   rq[i]=Rqueue[i];
		   StdRandom.shuffle(rq);
	   }
	   public boolean hasNext(){
		   return tag<size;
	   }
	   public void remove(){
		   throw new java.lang.UnsupportedOperationException();
	   }
	   public Item next(){
		   if(!hasNext())
			   throw new java.util.NoSuchElementException("randomized queue underflow");
		   Item item=rq[tag];
		   tag++;
		   return item;   
	   } 
   }
   
   public static void main(String[] args){   // unit testing (optional)
	   RandomizedQueue<String>TestQueue=new RandomizedQueue<String>();
	   TestQueue.enqueue("a SB");
	   TestQueue.enqueue("hear about ");
	   TestQueue.enqueue("you are");
	   Iterator<String>it=TestQueue.iterator();
	   while(it.hasNext()){
		   StdOut.println(it.next());
	   }
   }
}