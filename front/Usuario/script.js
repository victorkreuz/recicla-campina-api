document.addEventListener("DOMContentLoaded", () => {
    const API_BASE_URL = window.API_BASE_URL || "http://localhost:8090";
    const enderecoSelect = document.getElementById("endereco");

    // Lista de endereços fixos
    const enderecosFixos = [
        { id: "rua1", nome: "Linha Tereza" },
        { id: "rua2", nome: "Linha Amadeu" },
        { id: "rua3", nome: "Linha Buriti" },
        { id: "rua4", nome: "Linha Níquel" },
        { id: "rua5", nome: "Linha Palmeiras" },
        { id: "rua6", nome: "Linha Paca" },
        { id: "rua7", nome: "Bairro Floresta" },
        { id: "rua8", nome: "Bairro Alvorada" },
        { id: "rua9", nome: "Bairro Centro" },
        { id: "rua10", nome: "Rua dos Mensageiros" },
    ];

    // Preencher o select com os endereços
    enderecoSelect.innerHTML = '<option value="">Selecione um endereço</option>';
    enderecosFixos.forEach((endereco) => {
        const option = document.createElement("option");
        option.value = endereco.nome;
        option.textContent = endereco.nome;
        enderecoSelect.appendChild(option);
    });

    // Lidar com o envio do formulário
    document.getElementById("inscricaoForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const dados = {
            nome: document.getElementById("nome").value,
            email: document.getElementById("email").value,
            endereco: document.getElementById("endereco").value,
        };

        try {
            const response = await fetch(`${API_BASE_URL}/api/usuarios`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(dados),
            });

            if (!response.ok) {
                throw new Error("Erro ao enviar os dados.");
            }

            alert("Inscrição registrada com sucesso!");
            document.getElementById("inscricaoForm").reset();
            window.location.href='../NovaTela/nova.html'
        } catch (error) {
            console.error("Erro:", error);
            alert("Erro ao registrar inscrição.");
        }
    });
});
