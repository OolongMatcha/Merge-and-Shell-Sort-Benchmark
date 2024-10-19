//Jake Shoemake
//4/1/24
//CMSC 451
//Project 1

public class MergeSort extends AbstractSort{
	private int[] array;
	private int left, right;
	private int critical;
	private long nanoseconds, begin, end;
	
	public MergeSort(int array[]) {
		this.array = array;
		left = 0;
		right = array.length - 1;
		begin = 0;
		end = 0;
		critical = 0;
	}
	
	public void sort() throws UnsortedException{
		begin = System.nanoTime();
		sortMerge(array, left, right);
		end = System.nanoTime();
		nanoseconds = end - begin;
		
		for(int c = 0; c < array.length - 2; c++) {
			if(array[c] > array[c + 1]) {
				throw new UnsortedException("ERROR: Unsorted array.");
			}
		}
	}
	
	void sortMerge(int array[], int left, int right) {
		//Splits array in half
		if(left < right) {
			incrementCount();
			int mid = (left + right) / 2;
			
			//Recursively split arrays
			sortMerge(array, left, mid);
			sortMerge(array, mid + 1, right);
			
			//Combine sorted arrays into one
			incrementCount();
			merge(array, left, mid, right);
		}
	}
	
	void merge(int array[], int p, int q, int r) { //Merge two arrays back into each other
		int n1 = q - p + 1;
		int n2 = r - q;
		
		int L[] = new int[n1];
		int M[] = new int[n2];
		
		//Fills both left and right arrays
		for(int i = 0; i < n1; i++) { //Left
			L[i] = array[p + i];
		}
		for(int i = 0; i < n2; i++) { //Right
			M[i] = array[q + 1 + i];
		}
		
		//For maintaining current array + main array index
		int i = 0, j = 0, k = p;
		
		while(i < n1 && j < n2) {
			incrementCount();
			if(L[i] <= M[j]) {
				array[k] = L[i];
				i++;
			} else {
				array[k] = M[j];
				j++;
			}
			k++;
		}
		
		//Remaining element pickup
		while(i < n1) {
			array[k] = L[i];
			i++;
			k++;
		}
		while(j < n2) {
			array[k] = M[j];
			j++;
			k++;
		}
	}
	
	/* MergeSort code reference
	 * Java program to implement merge sort algorithm. Programiz. (n.d.).
	 * https://www.programiz.com/java-programming/examples/merge-sort 
	 */

	public int getCount() {
		return critical;
	}

	public long getTime() {
		return nanoseconds;
	}

	protected void incrementCount() {
		critical++;
	}
}
