package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import elevatorsystem.QuickSort;

class TestQuickSort {

	@Test
	void testSortAscending() {
		QuickSort sort = new QuickSort();
		int[] sortedData = new int[] {1,2,3,4,5};
		
		int[] data = new int[] {5,4,3,2,1};
		
		sort.sort(data, true);
		assertArrayEquals(sortedData, data);
		
		data = new int[] {5,3,1,2,4};
		
		sort.sort(data, true);
		assertArrayEquals(sortedData, data);
		
		data = new int[] {1,2,3,4,5};
		
		sort.sort(data, true);
		assertArrayEquals(sortedData, data);
	}
	
	@Test
	void testSortDescending() {
		QuickSort sort = new QuickSort();
		int[] sortedData = new int[] {5,4,3,2,1};
		
		int[] data = new int[] {5,4,3,2,1};
		
		sort.sort(data, false);
		assertArrayEquals(sortedData, data);
		
		data = new int[] {5,3,1,2,4};
		
		sort.sort(data, false);
		assertArrayEquals(sortedData, data);
		
		data = new int[] {1,2,3,4,5};
		
		sort.sort(data, false);
		assertArrayEquals(sortedData, data);
	}

}
