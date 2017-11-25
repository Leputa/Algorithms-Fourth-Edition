import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	
	static private final int R=256;
	private static char[] a;
	
	
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	a=new char[R];
    	for(char i=0;i<R;i++)
    		a[i]=i;
    	String s=BinaryStdIn.readString();
    	char[]input=s.toCharArray();
    	char index=0;
    	for(int i=0;i<input.length;i++) {
    		while(a[index]!=input[i])
    			index++;
    		BinaryStdOut.write(index);
    		while(index>0) {
    			a[index]=a[index-1];
    			index--;
    		}
    		a[0]=input[i];
    	}
    	BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	a=new char[R];
    	for(char i=0;i<R;i++)
    		a[i]=i;
    	String s=BinaryStdIn.readString();
    	char[]input=s.toCharArray();
    	char index=0;
    	for(int i=0;i<input.length;i++) {
    		index=input[i];
    		BinaryStdOut.write(a[index]);
    		char tmp=a[index];
    		while(index>0) {
    			a[index]=a[index-1];
    			index--;
    		}
    		a[0]=tmp;
    	}
    	BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if
        	(args[0].equals("-")) encode();
        else if 
        	(args[0].equals("+")) decode();
        else 
        	throw new RuntimeException("Illegal command line argument");
    	
    }
}
