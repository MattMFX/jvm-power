package br.edu.ufabc.energy.benchmark;

import br.edu.ufabc.energy.algorithms.SortingAlgorithms;
import br.edu.ufabc.energy.util.DataGenerator;
import br.edu.ufabc.energy.monitoring.EnergyMonitor;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Benchmark JMH para medir o consumo de energia de algoritmos de ordenação
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class SortingBenchmark {

    @Param({"1000", "5000", "10000", "25000"})
    private int arraySize;

    @Param({"RANDOM", "SORTED", "REVERSE_SORTED", "WITH_DUPLICATES"})
    private DataGenerator.DataType dataType;

    private int[] testData;

    @Setup(Level.Trial)
    public void setupTrial() {
        EnergyMonitor.initialize();
        System.out.println("=== Energy Monitoring Setup ===");
        System.out.println("JRAPL Available: " + EnergyMonitor.isJRAPLAvailable());
        System.out.println("Measurement Unit: " + EnergyMonitor.getMeasurementUnit());
        System.out.println("Array Size: " + arraySize);
        System.out.println("Data Type: " + dataType);
        System.out.println("==============================");
    }

    @Setup(Level.Invocation)
    public void setupInvocation() {
        // Gera novos dados para cada invocação para evitar cache effects
        testData = DataGenerator.generateArray(arraySize, dataType);
    }

    @Benchmark
    public void bubbleSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.bubbleSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("BubbleSort", energy);
    }

    @Benchmark
    public void quickSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.quickSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("QuickSort", energy);
    }

    @Benchmark
    public void mergeSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.mergeSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("MergeSort", energy);
    }

    @Benchmark
    public void insertionSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.insertionSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("InsertionSort", energy);
    }

    @Benchmark
    public void selectionSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.selectionSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("SelectionSort", energy);
    }

    @Benchmark
    public void heapSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.heapSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("HeapSort", energy);
    }

    @Benchmark
    public void javaSort() {
        int[] data = DataGenerator.copyArray(testData);
        EnergyMonitor.startMeasurement();
        SortingAlgorithms.javaSort(data);
        double energy = EnergyMonitor.stopMeasurement();
        recordEnergyConsumption("JavaSort", energy);
    }

    private void recordEnergyConsumption(String algorithm, double energy) {
        // Log energy consumption for later analysis
        // Em um cenário real, isso poderia ser salvo em arquivo ou banco de dados
        if (Boolean.getBoolean("energy.logging.enabled")) {
            System.out.printf("[ENERGY] %s,%s,%d,%.6f,%s%n", 
                algorithm, dataType, arraySize, energy, EnergyMonitor.getMeasurementUnit());
        }
    }

    /**
     * Método principal para executar os benchmarks
     */
    public static void main(String[] args) throws RunnerException {
        // Habilita logging de energia
        System.setProperty("energy.logging.enabled", "true");
        
        Options opt = new OptionsBuilder()
                .include(SortingBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5)
                .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.CSV)
                .result("benchmark_results.csv")
                .build();

        new Runner(opt).run();
    }
}

