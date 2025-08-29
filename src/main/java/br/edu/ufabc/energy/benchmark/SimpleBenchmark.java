package br.edu.ufabc.energy.benchmark;

import br.edu.ufabc.energy.algorithms.SortingAlgorithms;
import br.edu.ufabc.energy.util.DataGenerator;
import br.edu.ufabc.energy.monitoring.EnergyMonitor;

import java.util.Arrays;

/**
 * Benchmark simples para testes rápidos e validação
 */
public class SimpleBenchmark {
    
    public static void main(String[] args) {
        System.out.println("=== Simple Energy Benchmark ===");
        
        EnergyMonitor.initialize();
        System.out.println("JRAPL Available: " + EnergyMonitor.isJRAPLAvailable());
        System.out.println("Measurement Unit: " + EnergyMonitor.getMeasurementUnit());
        System.out.println();
        
        // Tamanhos de teste
        int[] sizes = {1000, 5000, 10000};
        
        // Tipos de dados
        DataGenerator.DataType[] dataTypes = {
            DataGenerator.DataType.RANDOM,
            DataGenerator.DataType.SORTED,
            DataGenerator.DataType.REVERSE_SORTED
        };
        
        System.out.println("Algorithm,DataType,Size,Energy,Unit,Time(ms)");
        
        for (int size : sizes) {
            for (DataGenerator.DataType dataType : dataTypes) {
                runBenchmarkSuite(size, dataType);
            }
        }
        
        System.out.println("\n=== Benchmark Complete ===");
    }
    
    private static void runBenchmarkSuite(int size, DataGenerator.DataType dataType) {
        // Gera dados base
        int[] baseData = DataGenerator.generateArray(size, dataType);
        
        // Executa cada algoritmo
        runAlgorithm("BubbleSort", baseData, dataType, SortingAlgorithms::bubbleSort);
        runAlgorithm("QuickSort", baseData, dataType, SortingAlgorithms::quickSort);
        runAlgorithm("MergeSort", baseData, dataType, SortingAlgorithms::mergeSort);
        runAlgorithm("InsertionSort", baseData, dataType, SortingAlgorithms::insertionSort);
        runAlgorithm("SelectionSort", baseData, dataType, SortingAlgorithms::selectionSort);
        runAlgorithm("HeapSort", baseData, dataType, SortingAlgorithms::heapSort);
        runAlgorithm("JavaSort", baseData, dataType, SortingAlgorithms::javaSort);
    }
    
    private static void runAlgorithm(String algorithmName, int[] baseData, 
                                   DataGenerator.DataType dataType, 
                                   SortingAlgorithm algorithm) {
        
        // Múltiplas execuções para média
        int iterations = 5;
        double totalEnergy = 0;
        long totalTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            // Copia dados para cada iteração
            int[] data = DataGenerator.copyArray(baseData);
            
            // Medição
            long startTime = System.currentTimeMillis();
            EnergyMonitor.startMeasurement();
            
            algorithm.sort(data);
            
            double energy = EnergyMonitor.stopMeasurement();
            long endTime = System.currentTimeMillis();
            
            totalEnergy += energy;
            totalTime += (endTime - startTime);
            
            // Verifica se está ordenado (validação)
            if (!isArraySorted(data)) {
                System.err.println("ERROR: Array not sorted by " + algorithmName);
                return;
            }
        }
        
        // Calcula médias
        double avgEnergy = totalEnergy / iterations;
        double avgTime = totalTime / (double) iterations;
        
        // Output
        System.out.printf("%s,%s,%d,%.6f,%s,%.2f%n", 
            algorithmName, dataType, baseData.length, 
            avgEnergy, EnergyMonitor.getMeasurementUnit(), avgTime);
    }
    
    private static boolean isArraySorted(int[] arr) {
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

