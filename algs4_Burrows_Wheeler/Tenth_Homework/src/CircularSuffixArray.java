import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircularSuffixArray {
	private String str;
	private List<Integer>indices;
	private int length;
	
	// circular suffix array of s
	public CircularSuffixArray(String s) {
		if(s==null)
			throw new java.lang.IllegalArgumentException();
		this.str=s;
		this.length=s.length();
		indices=new ArrayList<Integer>();
		
		for(int i=0;i<length;i++)
			indices.add(i);
		
		// Your data type should use space proportional to N. 
		// The constructor should take time proportional to N log N
        Collections.sort(indices, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                for (int i = 0; i < length; i++) {
                    int result = Character.compare(
                            str.charAt((o1 + i) % length),
                            str.charAt((o2 + i) % length));
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        });
	}
    public int length(){
    	// length of s
    	return length;
    }
    public int index(int i) {
    	// returns index of ith sorted suffix
    	if(i<0||i>=length)
    		throw new java.lang.IllegalArgumentException();
    	return indices.get(i);
    }
}
