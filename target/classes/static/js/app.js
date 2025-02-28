document.getElementById("btnUpload").addEventListener("click", function () {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Por favor, selecione um arquivo XML.");
        return;
    }

    // Validação para garantir que o arquivo tenha a extensão .xml
    if (!file.name.endsWith('.xml')) {
        alert("Por favor, selecione um arquivo XML válido.");
        return;
    }

    // Validação do MIME type para garantir que o arquivo seja XML
    if (file.type !== 'application/xml' && file.type !== 'text/xml') {
        alert("O arquivo selecionado não é um arquivo XML válido.");
        return;
    }

    const formData = new FormData();
    formData.append("file", file);

    fetch("http://localhost:8080/api/validar-xml", {
        method: "POST",
        body: formData,
        headers: {
            // Adicionando headers personalizados caso necessário
            "Content-Type": "multipart/form-data"
        },
        credentials: "include"  // Envia cookies e credenciais com a requisição
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Erro na validação do arquivo.');
        }
        return response.json(); // Converte a resposta em JSON
    })
    .then(data => {
        displayValidationResults(data.erros);
        displayFinancialResults(data.resultadosFinanceiros);
    })
    .catch(error => {
        console.error("Erro ao enviar o arquivo:", error);
        alert("Ocorreu um erro ao validar o arquivo.");
    });
});

function displayValidationResults(errors) {
    const errorLog = document.getElementById("errorLog");
    if (errors && errors.length > 0) {
        errorLog.innerHTML = errors.join('<br>');
    } else {
        errorLog.innerHTML = "Nenhum erro encontrado.";
    }
}

function displayFinancialResults(results) {
    const financialLog = document.getElementById("financialLog");

    if (results) {
        financialLog.innerHTML = `
            <strong>Total Liberado:</strong> R$ ${results.totalLiberado.toFixed(2)}<br>
            <strong>Total Glosa:</strong> R$ ${results.totalGlosa.toFixed(2)}<br>
            <strong>Discrepância:</strong> R$ ${results.discrepancia.toFixed(2)}
        `;
    } else {
        financialLog.innerHTML = "Nenhum dado financeiro disponível.";
    }
}
