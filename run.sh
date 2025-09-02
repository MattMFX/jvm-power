#!/bin/bash

# Energy Efficiency Benchmark - Single Script Runner
# This script automatically sets up jRAPL (if needed) and runs the energy efficiency benchmarks
# Just run this script after cloning the repository - no other setup required!

set -e  # Exit on any error

echo "=== Energy Efficiency Benchmark ==="
echo "Projeto: Medição de Eficiência Energética na JVM"
echo "Algoritmos: Sorting Algorithms"
echo "=================================="

# Get the current project directory
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
echo "Diretório do projeto: $PROJECT_DIR"

# Function to check dependencies
check_dependencies() {
    echo "Verificando dependências..."
    
    # Check if Java is installed
    if ! command -v java &> /dev/null; then
        echo "ERROR: Java não encontrado."
        echo "Instale o Java 11+ primeiro:"
        echo "  Ubuntu/Debian: sudo apt install openjdk-11-jdk"
        echo "  CentOS/RHEL: sudo yum install java-11-openjdk-devel"
        exit 1
    fi
    
    # Check if git is installed (needed for jRAPL installation)
    if ! command -v git &> /dev/null; then
        echo "ERROR: Git não encontrado."
        echo "Instale o Git primeiro:"
        echo "  Ubuntu/Debian: sudo apt install git"
        echo "  CentOS/RHEL: sudo yum install git"
        exit 1
    fi
    
    echo "✓ Java: $(java -version 2>&1 | head -n 1)"
    echo "✓ Git: $(git --version)"
}

# Function to setup Maven wrapper if needed
setup_maven_wrapper() {
    if [ ! -f "./mvnw" ]; then
        echo "ERROR: Maven wrapper não encontrado."
        echo "Este script deve ser executado no diretório raiz do projeto."
        exit 1
    fi
    
    # Check if Maven wrapper files exist
    if [ ! -d ".mvn/wrapper" ] || [ ! -f ".mvn/wrapper/maven-wrapper.jar" ]; then
        echo "Configurando Maven wrapper..."
        mkdir -p .mvn/wrapper
        
        # Create maven-wrapper.properties
        cat > .mvn/wrapper/maven-wrapper.properties << 'EOF'
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.5/apache-maven-3.9.5-bin.zip
wrapperUrl=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
EOF
        
        # Download Maven wrapper JAR
        echo "Baixando Maven wrapper..."
        if command -v wget &> /dev/null; then
            wget -O .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
        elif command -v curl &> /dev/null; then
            curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar
        else
            echo "ERROR: Nem wget nem curl encontrados. Instale um deles:"
            echo "  Ubuntu/Debian: sudo apt install wget"
            exit 1
        fi
        
        echo "✓ Maven wrapper configurado"
    fi
    
    echo "✓ Maven: $(./mvnw --version | head -n 1)"
}

# Function to setup jRAPL
setup_jrapl() {
    echo "Verificando jRAPL..."
    
    # Check if jRAPL is already installed in local Maven repository
    if ./mvnw dependency:resolve &> /dev/null; then
        echo "✓ jRAPL já está instalado e funcionando"
        return 0
    fi
    
    echo "jRAPL não encontrado. Instalando automaticamente..."
    
    # Create lib directory if it doesn't exist
    mkdir -p lib
    
    # Check if jRAPL JAR already exists
    if [ -f "lib/jRAPL-3.0.jar" ]; then
        echo "JAR jRAPL encontrado em lib/, instalando no repositório Maven..."
        ./mvnw install:install-file \
            -Dfile=lib/jRAPL-3.0.jar \
            -DgroupId=jrapl \
            -DartifactId=jRAPL \
            -Dversion=3.0 \
            -Dpackaging=jar \
            -DgeneratePom=true
        echo "✓ jRAPL instalado no repositório Maven local"
        return 0
    fi
    
    echo "Baixando e compilando jRAPL do GitHub..."
    
    # Create temporary directory
    TEMP_DIR=$(mktemp -d)
    cd "$TEMP_DIR"
    
    # Clone jRAPL repository
    if ! git clone https://github.com/kliu20/jRAPL.git; then
        echo "ERROR: Falha ao clonar o repositório jRAPL"
        cd "$PROJECT_DIR"
        rm -rf "$TEMP_DIR"
        echo "Criando JAR placeholder..."
        create_placeholder_jar
        return 0
    fi
    
    cd jRAPL
    
    # Check if make is available
    if ! command -v make &> /dev/null; then
        echo "WARNING: 'make' não encontrado. Criando JAR placeholder..."
        cd "$PROJECT_DIR"
        rm -rf "$TEMP_DIR"
        create_placeholder_jar
        return 0
    fi
    
    # Try to compile
    if make; then
        echo "✓ jRAPL compilado com sucesso!"
        
        if [ -f "jRAPL.jar" ]; then
            # Copy to project lib directory
            cp jRAPL.jar "$PROJECT_DIR/lib/jRAPL-3.0.jar"
            echo "✓ JAR copiado para lib/jRAPL-3.0.jar"
        else
            echo "WARNING: JAR não encontrado após compilação. Criando placeholder..."
            cd "$PROJECT_DIR"
            rm -rf "$TEMP_DIR"
            create_placeholder_jar
            return 0
        fi
    else
        echo "WARNING: Falha na compilação do jRAPL. Criando JAR placeholder..."
        cd "$PROJECT_DIR"
        rm -rf "$TEMP_DIR"
        create_placeholder_jar
        return 0
    fi
    
    # Clean up temporary directory
    cd "$PROJECT_DIR"
    rm -rf "$TEMP_DIR"
    
    # Install in local Maven repository
    echo "Instalando jRAPL no repositório Maven local..."
    ./mvnw install:install-file \
        -Dfile=lib/jRAPL-3.0.jar \
        -DgroupId=jrapl \
        -DartifactId=jRAPL \
        -Dversion=3.0 \
        -Dpackaging=jar \
        -DgeneratePom=true
    
    echo "✓ jRAPL instalado com sucesso!"
}

# Function to create placeholder JAR if compilation fails
create_placeholder_jar() {
    echo "Criando JAR placeholder para jRAPL..."
    
    # Create placeholder class structure
    mkdir -p temp_jrapl/jRAPL
    
    cat > temp_jrapl/jRAPL/EnergyCheckUtils.java << 'EOF'
package jRAPL;

public class EnergyCheckUtils {
    public static native int ProfileInit();
    public static native int GetSocketNum();
    public static native String EnergyStatCheck();
    public static native void ProfileDealloc();
    public static native void SetPowerLimit(int socketId, int level, double limit);
    
    // Add static initializer to load native library
    static {
        try {
            System.loadLibrary("jrapl");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Warning: jRAPL native library not found. Energy measurements will be disabled.");
        }
    }
}
EOF

    cat > temp_jrapl/jRAPL/ArchSpec.java << 'EOF'
package jRAPL;

public class ArchSpec {
    public static final int BROADWELL = 1;
    public static final int HASWELL = 2;
    public static final int SKYLAKE = 3;
    public static final int KABYLAKE = 4;
    public static final int COFFEELAKE = 5;
    public static final int ICELAKE = 6;
    public static final int TIGERLAKE = 7;
    public static final int ALDERLAKE = 8;
}
EOF

    # Compile placeholder classes
    cd temp_jrapl
    if ! javac jRAPL/*.java; then
        echo "ERROR: Falha ao compilar classes placeholder"
        cd ..
        rm -rf temp_jrapl
        exit 1
    fi
    
    # Create JAR
    if ! jar cf ../lib/jRAPL-3.0.jar jRAPL/*.class; then
        echo "ERROR: Falha ao criar JAR placeholder"
        cd ..
        rm -rf temp_jrapl
        exit 1
    fi
    
    # Clean up
    cd ..
    rm -rf temp_jrapl
    
    echo "✓ JAR placeholder criado em lib/jRAPL-3.0.jar"
    
    # Install in local Maven repository
    echo "Instalando JAR placeholder no repositório Maven local..."
    ./mvnw install:install-file \
        -Dfile=lib/jRAPL-3.0.jar \
        -DgroupId=jrapl \
        -DartifactId=jRAPL \
        -Dversion=3.0 \
        -Dpackaging=jar \
        -DgeneratePom=true
    
    echo "✓ JAR placeholder instalado (medições de energia desabilitadas)"
}

# Function to show menu and get choice
show_menu_and_run() {
    echo ""
    echo "=== Benchmarks Disponíveis ==="
    echo "1) Executar benchmark simples"
    echo "2) Executar benchmark JMH completo"
    echo "3) Executar testes"
    echo "4) Sair"
    echo ""
    
    while true; do
        read -p "Escolha uma opção [1-4]: " choice
        
        case $choice in
            1)
                echo "=== Executando Benchmark Simples ==="
                if ./mvnw exec:java -Dexec.mainClass="br.edu.ufabc.energy.benchmark.SimpleBenchmark" -q; then
                    echo "✓ Benchmark simples executado com sucesso!"
                else
                    echo "✗ Falha ao executar benchmark simples"
                fi
                ;;
            2)
                echo "=== Executando Benchmark JMH Completo ==="
                
                # Compile if necessary
                if [ ! -f "target/benchmarks.jar" ]; then
                    echo "Compilando JAR de benchmarks..."
                    if ! ./mvnw package -q; then
                        echo "✗ Falha na compilação do JAR"
                        continue
                    fi
                fi
                
                # Run benchmark
                echo "Iniciando benchmarks JMH..."
                if java -jar target/benchmarks.jar -rf csv -rff benchmark_results.csv; then
                    echo "✓ Benchmark JMH executado com sucesso!"
                    echo "✓ Resultados salvos em benchmark_results.csv"
                else
                    echo "✗ Falha ao executar benchmark JMH"
                fi
                ;;
            3)
                echo "=== Executando Testes ==="
                if ./mvnw test; then
                    echo "✓ Testes executados com sucesso!"
                else
                    echo "✗ Falha nos testes"
                fi
                ;;
            4)
                echo "Saindo..."
                exit 0
                ;;
            *)
                echo "Opção inválida. Escolha entre 1-4."
                continue
                ;;
        esac
        
        echo ""
        read -p "Pressione Enter para continuar ou Ctrl+C para sair..."
        echo ""
        echo "=== Benchmarks Disponíveis ==="
        echo "1) Executar benchmark simples"
        echo "2) Executar benchmark JMH completo" 
        echo "3) Executar testes"
        echo "4) Sair"
        echo ""
    done
}

# Function to cleanup on exit
cleanup() {
    echo ""
    echo "🧹 Limpando projeto..."
    ./mvnw clean &> /dev/null
    rm -f benchmark_results.csv
    rm -f energy_measurements.log
    echo "✓ Limpeza concluída!"
    echo "👋 Obrigado por usar o Energy Efficiency Benchmark!"
}

# Main execution flow
main() {
    # Set up cleanup trap to run when script exits
    trap cleanup EXIT
    
    check_dependencies
    setup_maven_wrapper
    setup_jrapl
    
    echo ""
    echo "🎉 Setup completo! Projeto pronto para uso."
    
    # Always compile the project fresh at startup
    echo ""
    echo "🔨 Compilando projeto (build limpo)..."
    if ./mvnw clean compile; then
        echo "✓ Projeto compilado com sucesso!"
    else
        echo "✗ Falha na compilação do projeto"
        exit 1
    fi
    
    show_menu_and_run
}

# Run main function
main "$@"
