# Energy Efficiency Benchmark - MVP

Este projeto é um MVP (Minimum Viable Product) para medir a eficiência energética de algoritmos de ordenação na JVM usando Java Microbenchmark Harness (JMH) e JRAPL.

## Estrutura do Projeto

```
src/main/java/br/edu/ufabc/energy/
├── algorithms/
│   └── SortingAlgorithms.java     # Implementações dos algoritmos de ordenação
├── benchmark/
│   ├── SortingBenchmark.java      # Benchmarks JMH completos
│   └── SimpleBenchmark.java       # Benchmark simples para testes rápidos
├── monitoring/
│   └── EnergyMonitor.java         # Wrapper para medição de energia (JRAPL)
└── util/
    └── DataGenerator.java         # Gerador de dados de teste
```

## Algoritmos Implementados

1. **Bubble Sort** - O(n²) - Algoritmo simples para comparação
2. **Quick Sort** - O(n log n) - Algoritmo divide-e-conquista popular
3. **Merge Sort** - O(n log n) - Algoritmo estável com garantia de performance
4. **Insertion Sort** - O(n²) - Eficiente para arrays pequenos
5. **Selection Sort** - O(n²) - Algoritmo de seleção simples
6. **Heap Sort** - O(n log n) - Baseado em heap binário
7. **Java Sort** - O(n log n) - Implementação otimizada do Java (Dual-Pivot Quicksort)

## Tipos de Dados de Teste

- **RANDOM**: Arrays com elementos aleatórios
- **SORTED**: Arrays já ordenados (melhor caso)
- **REVERSE_SORTED**: Arrays ordenados em ordem decrescente (pior caso)
- **WITH_DUPLICATES**: Arrays com muitos elementos duplicados
- **NEARLY_SORTED**: Arrays quase ordenados (90% ordenado)

## Pré-requisitos

- Java 11 ou superior
- Maven 3.6+
- JRAPL (opcional, para medições reais de energia)

## Como Executar

### 1. Compilar o projeto

```bash
mvn clean compile
```

### 2. Executar benchmark simples

```bash
mvn exec:java -Dexec.mainClass="br.edu.ufabc.energy.benchmark.SimpleBenchmark"
```

### 3. Executar benchmarks JMH completos

```bash
# Compilar JAR executável
mvn clean package

# Executar benchmarks
java -jar target/benchmarks.jar
```

### 4. Executar com parâmetros específicos

```bash
# Executar apenas QuickSort com arrays de 10000 elementos
java -jar target/benchmarks.jar -p arraySize=10000 ".*quickSort.*"

# Executar com dados aleatórios apenas
java -jar target/benchmarks.jar -p dataType=RANDOM
```

## Configuração do JRAPL

⚠️ **Nota**: Este MVP inclui um wrapper para JRAPL, mas a integração real requer:

1. Instalação do JRAPL no sistema
2. Configuração das bibliotecas nativas
3. Permissões adequadas para leitura dos registradores de energia

### Instalação do JRAPL (Linux)

```bash
# Clonar repositório JRAPL
git clone https://github.com/kliu20/jRAPL.git

# Compilar biblioteca nativa
cd jRAPL
make

# Copiar JAR para o projeto
cp target/jRAPL-*.jar /path/to/projeto/lib/
```

Para sistemas sem JRAPL, o projeto usa tempo de CPU como proxy para consumo de energia.

## Resultados

Os benchmarks geram resultados em formato CSV contendo:

- Nome do algoritmo
- Tipo de dados
- Tamanho do array
- Energia consumida (Joules ou CPU nanoseconds)
- Tempo de execução
- Estatísticas de performance

### Exemplo de saída:

```
Algorithm,DataType,Size,Energy,Unit,Time(ms)
BubbleSort,RANDOM,1000,1234567.89,CPU nanoseconds,15.50
QuickSort,RANDOM,1000,234567.12,CPU nanoseconds,2.30
MergeSort,RANDOM,1000,345678.23,CPU nanoseconds,3.10
```

## Customização

### Adicionando novos algoritmos

1. Implementar o algoritmo em `SortingAlgorithms.java`
2. Adicionar benchmark correspondente em `SortingBenchmark.java`
3. Incluir no `SimpleBenchmark.java` se necessário

### Modificando tamanhos de teste

Editar a anotação `@Param` em `SortingBenchmark.java`:

```java
@Param({"500", "1000", "5000", "10000", "50000"})
private int arraySize;
```

### Alterando tipos de dados

Modificar o enum `DataType` em `DataGenerator.java` e adicionar novos geradores.

## Próximos Passos

1. **Integração completa com JRAPL**: Implementar medições reais de energia
2. **Análise estatística**: Adicionar testes de significância e intervalos de confiança
3. **Visualização**: Gerar gráficos dos resultados
4. **Mais algoritmos**: Incluir algoritmos híbridos e especializados
5. **Diferentes linguagens**: Expandir para outras linguagens JVM (Kotlin, Scala)
6. **Otimizações JVM**: Testar diferentes configurações de GC e JIT

## Contribuição

Este é um projeto acadêmico. Contribuições são bem-vindas através de:

- Issues para bugs ou sugestões
- Pull requests com melhorias
- Documentação adicional
- Novos algoritmos ou tipos de dados

## Licença

Projeto acadêmico - UFABC

