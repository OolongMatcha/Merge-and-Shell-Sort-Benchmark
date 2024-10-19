//Jake Shoemake
//4/1/24
//CMSC 451
//Project 1

public class ShellSort extends AbstractSort{
	private int array[];
	private int size;
	private int critical;
	private long nanotime, begin, end;
	
	public ShellSort(int[] array) {
		this.array = array;
		size = array.length;
		begin = 0;
		end = 0;
		critical = 0;
	}
	
	public void sort() throws UnsortedException{
		begin = System.nanoTime();
		for(int interval = size / 2; interval > 0; interval /= 2) {
			incrementCount();
			for(int i = interval; i < size; i++) {
				int temp = array[i];
				int j;
				
				incrementCount();
				for(j = i; j >= interval && array[j - interval] > temp; j -= interval) {
					incrementCount();
					array[j] = array[j - interval];
				}
				array[j] = temp;
			}
			end = System.nanoTime();
			nanotime = end - begin;
		}
		
		for(int c = 0; c < size - 2; c++) {
			if(array[c] > array[c + 1]) {
				throw new UnsortedException("ERROR: Unsorted array.");
			}
		}
	}
	
	//Reference for ShellSort code
	/*
	 * Shell sort algorithm. Programiz. (n.d.). https://www.programiz.com/dsa/shell-sort 
	 */

	public int getCount() {
		return critical;
	}

	public long getTime() {
		return nanotime;
	}

	protected void incrementCount() {
		critical++;
	}
}
