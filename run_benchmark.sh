#!/bin/bash

# Script para executar benchmarks de eficiência energética
# Energy Efficiency Benchmark Runner

echo "=== Energy Efficiency Benchmark ==="
echo "Projeto: Medição de Eficiência Energética na JVM"
echo "Algoritmos: Sorting Algorithms"
echo "=================================="

if [ ! -f "./mvnw" ]; then
    echo "ERROR: Maven Wrapper não encontrado."
    exit 1
fi

# Verificar se Java está instalado
if ! command -v java &> /dev/null; then
    echo "ERROR: Java não encontrado. Instale o Java 11+ primeiro."
    exit 1
fi

echo "Maven: $(./mvnw --version | head -n 1)"
echo "Java: $(java -version 2>&1 | head -n 1)"
echo ""

# Função para executar benchmark simples
run_simple_benchmark() {
    echo "=== Executando Benchmark Simples ==="
    ./mvnw exec:java -Dexec.mainClass="br.edu.ufabc.energy.benchmark.SimpleBenchmark" -q
}

# Função para executar benchmark JMH completo
run_jmh_benchmark() {
    echo "=== Executando Benchmark JMH Completo ==="
    
    # Compilar se necessário
    if [ ! -f "target/benchmarks.jar" ]; then
        echo "Compilando projeto..."
        ./mvnw clean package -q
        if [ $? -ne 0 ]; then
            echo "ERROR: Falha na compilação"
            exit 1
        fi
    fi
    
    # Executar benchmark
    echo "Iniciando benchmarks JMH..."
    java -jar target/benchmarks.jar -rf csv -rff benchmark_results.csv
}

# Função para compilar apenas
compile_project() {
    echo "=== Compilando Projeto ==="
    ./mvnw clean compile
}

# Função para executar testes
run_tests() {
    echo "=== Executando Testes ==="
    ./mvnw test
}

# Função para limpar projeto
clean_project() {
    echo "=== Limpando Projeto ==="
    ./mvnw clean
    rm -f benchmark_results.csv
    rm -f energy_measurements.log
}

# Menu principal
show_menu() {
    echo ""
    echo "Escolha uma opção:"
    echo "1) Compilar projeto"
    echo "2) Executar benchmark simples"
    echo "3) Executar benchmark JMH completo"
    echo "4) Executar testes"
    echo "5) Limpar projeto"
    echo "6) Sair"
    echo ""
}

# Loop principal
while true; do
    show_menu
    read -p "Opção [1-6]: " choice
    
    case $choice in
        1)
            compile_project
            ;;
        2)
            run_simple_benchmark
            ;;
        3)
            run_jmh_benchmark
            ;;
        4)
            run_tests
            ;;
        5)
            clean_project
            ;;
        6)
            echo "Saindo..."
            exit 0
            ;;
        *)
            echo "Opção inválida. Escolha entre 1-6."
            ;;
    esac
    
    echo ""
    read -p "Pressione Enter para continuar..."
done
