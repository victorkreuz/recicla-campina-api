document.addEventListener("DOMContentLoaded", () => {
    const API_BASE_URL = window.API_BASE_URL || "http://localhost:8090";
    const outroCampo = document.getElementById("outroCampo");
    const tipoRadios = document.querySelectorAll("input[name='tipoEmail']");
    const diaSelect = document.getElementById("dia");
    const horaSelect = document.getElementById("hora");
    const areaSelect = document.getElementById("area");

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

    // Preencher o select da área com os NOMES diretamente
    areaSelect.innerHTML = '<option value="">Selecione uma área</option>';
    enderecosFixos.forEach(endereco => {
        const option = document.createElement("option");
        option.value = endereco.nome; // ← agora envia o NOME
        option.textContent = endereco.nome;
        areaSelect.appendChild(option);
    });

    // Mostrar/ocultar campo de mensagem quando "Outro" for selecionado
    tipoRadios.forEach(radio => {
        radio.addEventListener("change", () => {
            outroCampo.classList.toggle("hidden", radio.value !== "outro" || !radio.checked);
        });
    });

    // Preencher dias (1-31)
    for (let d = 1; d <= 31; d++) {
        const option = document.createElement("option");
        option.value = d.toString().padStart(2, '0');
        option.textContent = d;
        diaSelect.appendChild(option);
    }

    // Preencher horas (00h - 23h)
    for (let h = 0; h <= 23; h++) {
        const option = document.createElement("option");
        option.value = h.toString().padStart(2, '0') + ":00";
        option.textContent = h.toString().padStart(2, '0') + ":00";
        horaSelect.appendChild(option);
    }

    // Enviar formulário
    document.getElementById("adminForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const tipoSelecionado = document.querySelector("input[name='tipoEmail']:checked")?.value;
        const area = document.getElementById("area").value;
        const dia = document.getElementById("dia").value;
        const mes = document.getElementById("mes").value;
        const ano = document.getElementById("ano").value;
        const hora = document.getElementById("hora").value;
        const mensagemOutro = document.getElementById("mensagemOutro").value;

        const dataEHora = `${ano}-${mes}-${dia}T${hora}`;

        const dados = {
            area: area,
            dataEHora: dataEHora,
            tipoLixo: tipoSelecionado,
            mensagem: mensagemOutro != null ? mensagemOutro:null

        };

        try {
            const response = await fetch(`${API_BASE_URL}/api/roteiros`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(dados)
            });

            if (!response.ok) throw new Error("Erro ao enviar roteiro.");

            alert("Roteiro enviado com sucesso!");
            e.target.reset();
        } catch (err) {
            console.error(err);
            alert("Erro ao enviar roteiro.");
        }
    });
});
