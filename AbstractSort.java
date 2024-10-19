//Jake Shoemake
//4/1/24
//CMSC 451
//Project 1

//This is the superclass for the Merge and Shell sort methods.
abstract class AbstractSort {
	private int critical;
	private long nanoseconds, begin, end;
	
	public AbstractSort() {
		this.critical = 0;
		this.begin = 0;
		this.end = 0;
		this.nanoseconds = 0;
	}

	abstract public void sort() throws UnsortedException; //Performs the main sorting functions for both sort algorithms
	
	protected void startSort() {
		critical = 0;
		begin = System.nanoTime();
		try {
			sort();
		} catch (UnsortedException e) {
			e.printStackTrace();
		}
	}
	
	abstract protected void incrementCount();
	
	protected void endSort() {
		end = System.nanoTime();
		nanoseconds = end - begin;
	}
	
	public int getCount() {
		return critical;
	}
	
	public long getTime() {
		return nanoseconds;
	}
}
