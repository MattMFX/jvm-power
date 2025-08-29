#!/bin/bash

# Script para baixar e configurar JRAPL

echo "=== Configurando JRAPL ==="

# Criar diretório temporário
TEMP_DIR=$(mktemp -d)
cd "$TEMP_DIR"

echo "Baixando JRAPL do GitHub..."

# Clonar o repositório JRAPL
git clone https://github.com/kliu20/jRAPL.git
cd jRAPL

echo "Compilando JRAPL..."

# Compilar o projeto
make

# Verificar se o JAR foi criado
if [ -f "jRAPL.jar" ]; then
    echo "JRAPL compilado com sucesso!"
    
    # Copiar para o diretório lib do projeto
    PROJECT_DIR="/Users/mfmachado/UFABC/Projeto_PGC"
    cp jRAPL.jar "$PROJECT_DIR/lib/jRAPL-3.0.jar"
    
    echo "JAR copiado para $PROJECT_DIR/lib/jRAPL-3.0.jar"
    
    # Instalar no repositório Maven local
    cd "$PROJECT_DIR"
    ./mvnw install:install-file \
        -Dfile=lib/jRAPL-3.0.jar \
        -DgroupId=jRAPL \
        -DartifactId=jRAPL \
        -Dversion=3.0 \
        -Dpackaging=jar \
        -DgeneratePom=true
    
    echo "JRAPL instalado no repositório Maven local!"
    
else
    echo "Erro: Falha na compilação do JRAPL"
    echo "Criando JAR placeholder..."
    
    # Criar JAR placeholder se a compilação falhar
    PROJECT_DIR="/Users/mfmachado/UFABC/Projeto_PGC"
    cd "$PROJECT_DIR"
    
    # Criar estrutura de classes placeholder
    mkdir -p temp_jrapl/jRAPL
    
    cat > temp_jrapl/jRAPL/EnergyCheckUtils.java << 'EOF'
package jRAPL;

public class EnergyCheckUtils {
    public static native int ProfileInit();
    public static native int GetSocketNum();
    public static native String EnergyStatCheck();
    public static native void ProfileDealloc();
    public static native void SetPowerLimit(int socketId, int level, double limit);
}
EOF

    cat > temp_jrapl/jRAPL/ArchSpec.java << 'EOF'
package jRAPL;

public class ArchSpec {
    public static final int BROADWELL = 1;
    public static final int HASWELL = 2;
    public static final int SKYLAKE = 3;
    public static final int KABYLAKE = 4;
}
EOF

    # Compilar classes placeholder
    cd temp_jrapl
    javac jRAPL/*.java
    
    # Criar JAR
    jar cf ../lib/jRAPL-3.0.jar jRAPL/*.class
    
    # Limpar
    cd ..
    rm -rf temp_jrapl
    
    echo "JAR placeholder criado em lib/jRAPL-3.0.jar"
fi

# Limpar diretório temporário
rm -rf "$TEMP_DIR"

echo "=== Configuração do JRAPL concluída ==="
