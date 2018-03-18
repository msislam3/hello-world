package elevatorsystem;

/**
 * <p>
 * choose the last element of the array is pivot.
 * </p>
 * 
 * @see <a href="http://www.java2novice.com/java-sorting-algorithms/quick-sort/">Java2Novice - Quick Sort</a>
 * @see <a href="https://www.geeksforgeeks.org/quick-sort/">Geeks for Geeks - Quick Sort</a>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Feb 13, 2018
 */
public class Quick{

	public static void sort( int[] array, final int size, boolean ascending){
		if( array == null || array.length < 2){
			return;
		}
		sortRecursive( array, 0, size - 1, ascending);
	}

	public static void sortRecursive( int[] array, final int low, final int high, boolean ascending){
		// base case all data has been sorted
		if( low > high){
			return;
		}

		int pivot = partition( array, low, high, ascending);

		//break array on pivot point
		sortRecursive( array, low, pivot - 1, ascending);
		sortRecursive( array, pivot + 1, high, ascending);
	}

	private static int partition( int[] array, final int low, final int high, boolean ascending){
		int pivot = array[high];// choose last element as pivot
		int i = (low - 1); // assume left most element is smallest
		for( int j = low; j < high; j++){// start at smallest index
			// If current element is smaller than or equal to pivot
			if( ascending && array[j] <= pivot){
				i++;// keep track of the last partitioned number smaller than pivot
				swap( array, i, j);// swap smaller than pivot number with index j
			}else if(!ascending && array[j] >= pivot) {
				i++;// keep track of the last partitioned number larger than pivot
				swap( array, i, j);// swap larger than pivot number with index j
			}
		}
		// bring pivot to correct position
		swap( array, i + 1, high);
		return i + 1;
	}

	private static void swap( int[] array, final int source, final int dest){
		int num = array[source];
		array[source] = array[dest];
		array[dest] = num;
	}
}

