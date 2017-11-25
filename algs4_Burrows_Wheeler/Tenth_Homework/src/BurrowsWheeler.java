import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {
    	String s=BinaryStdIn.readString();
    	CircularSuffixArray csa=new CircularSuffixArray(s);
    	int length=csa.length();
    	int index=0;
    	StringBuilder sb=new StringBuilder(length);
    	for(int i=0;i<length;i++) {
    		index=csa.index(i);
    		if(index==0) {
    			BinaryStdOut.write(i);
    			sb.append(s.charAt(length-1));
    		}
    		else
    			sb.append(s.charAt(index-1));
    	}
    	BinaryStdOut.write(sb.toString());
    	BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {
        int first = BinaryStdIn.readInt();
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) 
            sb.append(BinaryStdIn.readChar());
        char[] t = sb.toString().toCharArray();
        int N = t.length;
        /*char[] head=new char[N];
        head=t.clone();
        Arrays.sort(head);*/
        int[] next = new int[N];
        
       int[] count = new int[257];
        for (int i = 0; i < N; i++)
            count[t[i]+1]++;
        for (int i = 0; i < 256; i++)
            count[i+1] += count[i];
        // The trickiest part
        for (int i = 0; i < N; i++)
            next[count[t[i]]++] = i;
        
        int nx = next[first];
        for (int i = 0; i < N; i++) {
            BinaryStdOut.write(t[nx]);
            nx = next[nx];
        }
        BinaryStdOut.close();
        
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();	
    }
}
