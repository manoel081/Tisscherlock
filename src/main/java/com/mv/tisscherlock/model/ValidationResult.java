package com.mv.tisscherlock.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Modelo que representa o resultado da validação de um arquivo XML.
 * Este modelo encapsula o estado da validação, os erros encontrados e os resultados financeiros.
 */
@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {

    /**
     * Indica se o arquivo XML é válido ou não.
     */
    private boolean valid;

    /**
     * Lista de erros encontrados na validação do XML.
     * Usamos Set para garantir que erros duplicados não sejam retornados.
     */
    private Set<String> erros;

    /**
     * Mensagem de sucesso a ser retornada quando a validação for bem-sucedida.
     * Pode ser útil para exibir um feedback positivo.
     */
    private String sucessoMessage;

    /**
     * Código de status HTTP que pode ser utilizado pelo front-end
     * para compreender a resposta da API de maneira mais intuitiva.
     */
    private int statusCode;

    /**
     * Resultado financeiro calculado a partir da validação do XML.
     */
    private FinancialResults resultadosFinanceiros;

    // Novos campos para valores financeiros formatados
    private String totalLiberadoFormatted;
    private String totalGlosaFormatted;
    private String discrepanciaFormatted;

    /**
     * Método utilitário para verificar se o arquivo XML é válido.
     * @return Retorna 'true' se o XML for válido, caso contrário, 'false'.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Construtor para criar o objeto com os dados de erro.
     * Este construtor é útil para quando ocorre um erro e queremos
     * retornar um único erro com uma mensagem detalhada.
     *
     * @param erroMessage A mensagem de erro a ser exibida.
     */
    public ValidationResult(String erroMessage) {
        this.valid = false;
        this.erros = Set.of(erroMessage); // Usa Set para evitar duplicação de erros
        this.sucessoMessage = null; // Nenhuma mensagem de sucesso
        this.statusCode = 400; // Código de erro
        this.resultadosFinanceiros = null; // Sem resultados financeiros em caso de erro
        this.totalLiberadoFormatted = null;
        this.totalGlosaFormatted = null;
        this.discrepanciaFormatted = null;
    }

    /**
     * Construtor para criação de uma resposta de sucesso
     * que indica que o XML foi validado com sucesso.
     *
     * @param sucessoMessage A mensagem de sucesso a ser exibida.
     * @param erros Lista de erros encontrados durante a validação (caso existam).
     * @param resultadosFinanceiros Resultados financeiros calculados a partir do XML.
     * @param totalLiberadoFormatted Valor formatado do total liberado.
     * @param totalGlosaFormatted Valor formatado do total glosa.
     * @param discrepanciaFormatted Valor formatado da discrepância.
     */
    public ValidationResult(String sucessoMessage, Set<String> erros, FinancialResults resultadosFinanceiros,
                            String totalLiberadoFormatted, String totalGlosaFormatted, String discrepanciaFormatted) {
        this.valid = erros.isEmpty();
        this.erros = erros != null ? erros : Set.of(); // Se não houver erros, retorna um Set vazio
        this.sucessoMessage = sucessoMessage;
        this.statusCode = valid ? 200 : 400; // Código de sucesso ou erro
        this.resultadosFinanceiros = resultadosFinanceiros;
        this.totalLiberadoFormatted = totalLiberadoFormatted;
        this.totalGlosaFormatted = totalGlosaFormatted;
        this.discrepanciaFormatted = discrepanciaFormatted;
    }

    /**
     * Método utilitário para verificar se há erros e retornar um status apropriado.
     * @return Um código de status HTTP (200 para sucesso, 400 para erro).
     */
    public int getStatusCode() {
        return valid ? 200 : 400;
    }
}
