# Diretório de Bibliotecas

Este diretório é reservado para bibliotecas externas, especialmente o JRAPL.

## JRAPL (Java Runtime for Application Power and energy Library)

Para obter medições reais de energia, você precisa instalar o JRAPL:

### Instalação do JRAPL

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/kliu20/jRAPL.git
   cd jRAPL
   ```

2. **Compile a biblioteca:**
   ```bash
   make
   ```

3. **Copie o JAR para este diretório:**
   ```bash
   cp target/jRAPL-3.0.jar /path/to/projeto/lib/
   ```

### Requisitos

- Sistema Linux com suporte a Intel RAPL
- Acesso de root ou permissões adequadas para ler os registradores MSR
- Processadores Intel com suporte a RAPL (Sandy Bridge ou mais recente)

### Configuração

Após instalar o JRAPL, você pode:

1. Atualizar a classe `EnergyMonitor.java` para usar a biblioteca real
2. Configurar as permissões necessárias no sistema
3. Executar os benchmarks com medições reais de energia

### Fallback

Sem o JRAPL, o projeto usa tempo de CPU como proxy para consumo de energia, o que ainda fornece insights úteis sobre eficiência computacional.

