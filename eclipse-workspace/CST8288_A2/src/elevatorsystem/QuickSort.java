package elevatorsystem;

import java.util.ArrayList;

//https://www.geeksforgeeks.org/quick-sort/
public class QuickSort {
	private Integer[] numbers;
	private Integer number;

	public void sort(ArrayList<Integer> values) {
		if (values == null || values.size() == 0)
			return;

		numbers = (Integer[]) values.toArray();
		number = values.size();
		sort(0, number - 1);
	}

	public void sort(Integer[] values) {
		if (values == null || values.length == 0)
			return;

		numbers = values;
		number = values.length;
		sort(0, number - 1);
	}

	/*
	 * The main function that implements QuickSort() arr[] --> Array to be sorted,
	 * low --> Starting index, high --> Ending index
	 */
	private void sort(int low, int high) {
		if (low < high) {
			/*
			 * pi is partitioning index, arr[pi] is now at right place
			 */
			int pi = partition(low, high);

			// Recursively sort elements before
			// partition and after partition
			sort(low, pi - 1);
			sort(pi + 1, high);
		}
	}

	/*
	 * This function takes last element as pivot, places the pivot element at its
	 * correct position in sorted array, and places all smaller (smaller than pivot)
	 * to left of pivot and all greater elements to right of pivot
	 */
	private int partition(int low, int high) {
		int pivot = numbers[high];
		int i = (low - 1); // index of smaller element
		for (int j = low; j < high; j++) {
			// If current element is smaller than or
			// equal to pivot
			if (numbers[j] <= pivot) {
				i++;
				// swap arr[i] and arr[j]
				swap(i, j);
			}
		}

		// swap arr[i+1] and arr[high] (or pivot)
		swap(i+1, high);
		return i + 1;
	}
	
	private void swap(int i, int j) {
		int temp = numbers[j];
		numbers[j] = numbers[i];
		numbers[i] = temp;
	}
}