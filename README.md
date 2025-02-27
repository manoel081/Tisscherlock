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


2. Instalar as dependências

Certifique-se de ter o Java 21 e o Maven instalados no seu sistema.

Execute o comando abaixo para instalar as dependências do projeto:

mvn clean install


3. Executar o projeto

Após a instalação das dependências, você pode executar o projeto localmente com o comando:

mvn spring-boot:run

O back-end será executado em http://localhost:8080 por padrão.

4. Front-end

Se você estiver desenvolvendo o front-end, pode rodá-lo na porta 4200 (ou a porta configurada em seu projeto). A comunicação entre o front-end e o back-end será feita via API REST.
Como Usar

    Faça uma requisição POST para o endpoint /api/validar-xml com um arquivo XML.
    A resposta será um ValidationResult com a validação do XML, incluindo quaisquer erros encontrados nas tags e valores financeiros.
    Caso o XML seja válido, você receberá uma resposta com um status 200 OK. Se houver erros, um 400 Bad Request será retornado com os detalhes dos erros.

Exemplos de Uso
Requisição

Endpoint: POST /api/validar-xml

curl -X POST -F "file=@seuarquivo.xml" http://localhost:8080/api/validar-xml

Resposta de Sucesso

{
  "valid": true,
  "erros": [],
  "sucessoMessage": "O XML foi validado com sucesso.",
  "statusCode": 200,
  "resultadosFinanceiros": {
    "totalLiberado": "R$ 1400.00",
    "totalGlosa": "R$ 200.00",
    "discrepancia": "R$ 10.00"
  }
}

Resposta de Erro

{
  "valid": false,
  "erros": [
    "❌ A tag <ans:prestador> está ausente.",
    "❌ O valor de <ans:valorLiberado> está incorreto."
  ],
  "sucessoMessage": null,
  "statusCode": 400,
  "resultadosFinanceiros": null
}

Contribuindo

Contribuições são bem-vindas! Para contribuir com este projeto, siga os seguintes passos:

    Fork este repositório.
    Crie uma nova branch para sua feature (git checkout -b feature/nova-feature).
    Faça as alterações necessárias e commit suas mudanças (git commit -am 'Adiciona nova feature').
    Envie para o repositório remoto (git push origin feature/nova-feature).
    Abra um pull request.

Licença

Distribuído sob a licença MIT. Veja o arquivo LICENSE para mais informações.
