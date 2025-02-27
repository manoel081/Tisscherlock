# Tisscherlock

**Tisscherlock** é um sistema desenvolvido para validar arquivos XML do TISS (Troca de Informações na Saúde Suplementar) com foco na análise de conformidade, estrutura e dados financeiros. Este sistema ajuda na verificação de arquivos XML gerados pelo TISS, garantindo que os mesmos sigam os padrões esperados e não contenham erros estruturais ou financeiros.

## Funcionalidades

- **Validação Estrutural**: Verifica a presença de tags obrigatórias, tags não fechadas corretamente e erros de digitação nas tags.
- **Validação de Valores**: Compara valores financeiros presentes no XML, como **`valorLiberado`** e **`valorGlosa`**, e verifica se a tag **`relacaoGlosa`** está presente quando necessário.
- **Verificação de Quantidade de Números de Carteira**: Valida se a quantidade de números de carteira no XML está correta, identificando se há números a mais ou a menos.
- **Cálculos Financeiros**: Realiza cálculos de discrepância entre valores **`totalLiberado`** e **`totalGlosa`**.
- **Integração com XSD da ANS**: Baixa automaticamente o XSD correspondente à versão do XML fornecido e valida o XML contra esse XSD.

## Tecnologias Utilizadas

- **Spring Boot**: Framework utilizado para o desenvolvimento do back-end.
- **Java 21**: Linguagem de programação utilizada.
- **Maven**: Gerenciador de dependências e construção.
- **CORS**: Habilitação de CORS para permitir integração com o front-end.
- **JAXB**: Para manipulação de arquivos XML.
- **XSD**: Integração com XSD da ANS para validação.

## Instalação

### 1. Clonar o repositório

Clone este repositório para sua máquina local:

```bash
git clone https://github.com/SEU_USUARIO/tisscherlock.git
