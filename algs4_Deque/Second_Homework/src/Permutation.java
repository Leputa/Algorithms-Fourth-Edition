
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> Test= new RandomizedQueue<String>();
        int n = 1;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (n <= k)
                Test.enqueue(item);
            else if (StdRandom.uniform(n) < k) {
                String s = Test.dequeue();
                Test.enqueue(item);
            }
            n++;
        }
        for (String s : Test)
            StdOut.println(s);
    }
}
