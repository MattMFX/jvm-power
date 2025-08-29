package br.edu.ufabc.energy.util;

import java.util.Random;
import java.util.Arrays;

/**
 * Gerador de dados para os benchmarks de algoritmos de ordenação
 */
public class DataGenerator {
    
    private static final Random random = new Random(42); // Seed fixo para reprodutibilidade

    /**
     * Gera um array de inteiros aleatórios
     */
    public static int[] generateRandomArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size * 10); // Valores entre 0 e size*10
        }
        return array;
    }

    /**
     * Gera um array já ordenado (melhor caso para alguns algoritmos)
     */
    public static int[] generateSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    /**
     * Gera um array ordenado em ordem decrescente (pior caso para alguns algoritmos)
     */
    public static int[] generateReverseSortedArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = size - i - 1;
        }
        return array;
    }

    /**
     * Gera um array com muitos elementos duplicados
     */
    public static int[] generateArrayWithDuplicates(int size) {
        int[] array = new int[size];
        int numUniqueValues = Math.max(1, size / 10); // 10% de valores únicos
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(numUniqueValues);
        }
        return array;
    }

    /**
     * Gera um array quase ordenado (90% ordenado com alguns elementos fora de lugar)
     */
    public static int[] generateNearlySortedArray(int size) {
        int[] array = generateSortedArray(size);
        int swaps = Math.max(1, size / 10); // 10% de trocas
        
        for (int i = 0; i < swaps; i++) {
            int pos1 = random.nextInt(size);
            int pos2 = random.nextInt(size);
            
            int temp = array[pos1];
            array[pos1] = array[pos2];
            array[pos2] = temp;
        }
        return array;
    }

    /**
     * Cria uma cópia de um array
     */
    public static int[] copyArray(int[] original) {
        return Arrays.copyOf(original, original.length);
    }

    /**
     * Enum para tipos de dados de entrada
     */
    public enum DataType {
        RANDOM,
        SORTED,
        REVERSE_SORTED,
        WITH_DUPLICATES,
        NEARLY_SORTED
    }

    /**
     * Gera array baseado no tipo especificado
     */
    public static int[] generateArray(int size, DataType type) {
        switch (type) {
            case RANDOM:
                return generateRandomArray(size);
            case SORTED:
                return generateSortedArray(size);
            case REVERSE_SORTED:
                return generateReverseSortedArray(size);
            case WITH_DUPLICATES:
                return generateArrayWithDuplicates(size);
            case NEARLY_SORTED:
                return generateNearlySortedArray(size);
            default:
                return generateRandomArray(size);
        }
    }
}

