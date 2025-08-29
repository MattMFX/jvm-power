package br.edu.ufabc.energy.algorithms;

import br.edu.ufabc.energy.util.DataGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para os algoritmos de ordenação
 */
class SortingAlgorithmsTest {

    @Test
    @DisplayName("Bubble Sort - Array básico")
    void testBubbleSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.bubbleSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Quick Sort - Array básico")
    void testQuickSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.quickSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Merge Sort - Array básico")
    void testMergeSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.mergeSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Insertion Sort - Array básico")
    void testInsertionSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.insertionSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Selection Sort - Array básico")
    void testSelectionSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.selectionSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Heap Sort - Array básico")
    void testHeapSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.heapSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Java Sort - Array básico")
    void testJavaSort() {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = {11, 12, 22, 25, 34, 64, 90};
        
        SortingAlgorithms.javaSort(arr);
        
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Array vazio")
    void testEmptyArray() {
        int[] arr = {};
        
        assertDoesNotThrow(() -> {
            SortingAlgorithms.bubbleSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.quickSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.mergeSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.insertionSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.selectionSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.heapSort(DataGenerator.copyArray(arr));
            SortingAlgorithms.javaSort(DataGenerator.copyArray(arr));
        });
    }

    @Test
    @DisplayName("Array com um elemento")
    void testSingleElementArray() {
        int[] arr = {42};
        int[] expected = {42};
        
        SortingAlgorithms.bubbleSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
        
        SortingAlgorithms.quickSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
        
        SortingAlgorithms.mergeSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Array já ordenado")
    void testAlreadySortedArray() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        SortingAlgorithms.bubbleSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
        
        SortingAlgorithms.quickSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
        
        SortingAlgorithms.mergeSort(DataGenerator.copyArray(arr));
        assertArrayEquals(expected, arr);
    }

    @Test
    @DisplayName("Array com elementos duplicados")
    void testArrayWithDuplicates() {
        int[] arr = {5, 2, 8, 2, 9, 1, 5, 5};
        int[] expected = {1, 2, 2, 5, 5, 5, 8, 9};
        
        int[] testArr = DataGenerator.copyArray(arr);
        SortingAlgorithms.mergeSort(testArr);
        assertArrayEquals(expected, testArr);
        
        testArr = DataGenerator.copyArray(arr);
        SortingAlgorithms.quickSort(testArr);
        assertArrayEquals(expected, testArr);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500})
    @DisplayName("Teste com diferentes tamanhos de array aleatório")
    void testRandomArrays(int size) {
        Random rand = new Random(42); // Seed fixo para reprodutibilidade
        int[] arr = new int[size];
        
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000);
        }
        
        // Testa todos os algoritmos
        testSortingAlgorithm(arr, SortingAlgorithms::bubbleSort);
        testSortingAlgorithm(arr, SortingAlgorithms::quickSort);
        testSortingAlgorithm(arr, SortingAlgorithms::mergeSort);
        testSortingAlgorithm(arr, SortingAlgorithms::insertionSort);
        testSortingAlgorithm(arr, SortingAlgorithms::selectionSort);
        testSortingAlgorithm(arr, SortingAlgorithms::heapSort);
        testSortingAlgorithm(arr, SortingAlgorithms::javaSort);
    }

    @Test
    @DisplayName("Teste com diferentes tipos de dados do DataGenerator")
    void testDataGeneratorTypes() {
        int size = 100;
        
        for (DataGenerator.DataType type : DataGenerator.DataType.values()) {
            int[] arr = DataGenerator.generateArray(size, type);
            
            // Testa que o array foi gerado corretamente
            assertNotNull(arr);
            assertEquals(size, arr.length);
            
            // Testa ordenação
            int[] testArr = DataGenerator.copyArray(arr);
            SortingAlgorithms.quickSort(testArr);
            assertTrue(isArraySorted(testArr), 
                "Array should be sorted after QuickSort with " + type);
        }
    }

    private void testSortingAlgorithm(int[] originalArray, SortingAlgorithm algorithm) {
        int[] arr = DataGenerator.copyArray(originalArray);
        algorithm.sort(arr);
        assertTrue(isArraySorted(arr), "Array should be sorted");
        
        // Verifica se contém os mesmos elementos
        int[] expected = DataGenerator.copyArray(originalArray);
        Arrays.sort(expected);
        assertArrayEquals(expected, arr, "Sorted array should contain same elements");
    }

    private boolean isArraySorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i-1]) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    private interface SortingAlgorithm {
        void sort(int[] array);
    }
}

