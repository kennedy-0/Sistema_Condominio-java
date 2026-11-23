package br.edu.poo.m5678.view;

import br.edu.poo.m5678.dao.ApartamentoDAO;
import br.edu.poo.m5678.dao.MoradorDAO;
import br.edu.poo.m5678.model.Apartamento;
import br.edu.poo.m5678.model.Morador;
import br.edu.poo.m5678.service.ViaCepService;
import br.edu.poo.m5678.util.Validador;
import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class
Menu {

    private final Scanner sc = new Scanner(System.in);
    private final ApartamentoDAO apartamentoDAO = new ApartamentoDAO();
    private final MoradorDAO moradorDAO = new MoradorDAO();

    public void iniciar() {
        try {
            String op;
            do {
                System.out.println("\n=== CONDOMÍNIO ===");
                System.out.println("1 - Apartamentos");
                System.out.println("2 - Moradores");
                System.out.println("0 - Sair");
                op = ler("Escolha: ");
                switch (op) {
                    case "1" -> menuApartamentos();
                    case "2" -> menuMoradores();
                    case "0" -> System.out.println("Até logo!");
                    default -> System.out.println("Opção inválida.");
                }
            } while (!op.equals("0"));
        } catch (NoSuchElementException e) {
            System.out.println("\nEntrada encerrada. Saindo...");
        }
    }

    // ======================= APARTAMENTOS =======================

    private void menuApartamentos() {
        String op;
        do {
            System.out.println("\n--- APARTAMENTOS ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por ID (com moradores)");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Deletar");
            System.out.println("0 - Voltar");
            op = ler("Escolha: ");
            try {
                switch (op) {
                    case "1" -> cadastrarApartamento();
                    case "2" -> listarApartamentos();
                    case "3" -> buscarApartamento();
                    case "4" -> atualizarApartamento();
                    case "5" -> deletarApartamento();
                    case "0" -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                tratarErroBanco(e);
            }
        } while (!op.equals("0"));
    }

    private void cadastrarApartamento() throws SQLException {
        System.out.println("\n> Novo apartamento");
        String bloco = lerObrigatorio("Bloco: ", 10);
        String numero = lerObrigatorio("Número do apartamento: ", 10);
        int andar = lerInteiro("Andar: ", 0, 200);

        Apartamento a = new Apartamento(numero, bloco, andar);
        apartamentoDAO.inserir(a);
        System.out.println("Apartamento cadastrado com ID " + a.getId() + ".");
    }

    private void listarApartamentos() throws SQLException {
        List<Apartamento> lista = apartamentoDAO.listar();
        if (lista.isEmpty()) {
            System.out.println("Nenhum apartamento cadastrado.");
            return;
        }
        System.out.println();
        lista.forEach(System.out::println);
    }

    private void buscarApartamento() throws SQLException {
        int id = lerInteiro("ID do apartamento: ", 1, Integer.MAX_VALUE);
        Apartamento a = apartamentoDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("Apartamento não encontrado.");
            return;
        }
        System.out.println("\n" + a);
        List<Morador> moradores = moradorDAO.listarPorApartamento(id);
        if (moradores.isEmpty()) {
            System.out.println("Sem moradores cadastrados.");
        } else {
            System.out.println("Moradores:");
            moradores.forEach(m -> System.out.println(" - " + m.getNome() + " (CPF " + m.getCpf() + ")"));
        }
    }

    private void atualizarApartamento() throws SQLException {
        int id = lerInteiro("ID do apartamento a atualizar: ", 1, Integer.MAX_VALUE);
        Apartamento a = apartamentoDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("Apartamento não encontrado.");
            return;
        }
        System.out.println("Atual: " + a);
        System.out.println("(Deixe em branco para manter o valor atual)");

        String bloco = ler("Bloco [" + a.getBloco() + "]: ");
        if (!bloco.isEmpty()) {
            if (bloco.length() > 10) {
                System.out.println("Bloco muito longo (máx. 10). Operação cancelada.");
                return;
            }
            a.setBloco(bloco);
        }
        String numero = ler("Número [" + a.getNumero() + "]: ");
        if (!numero.isEmpty()) {
            if (numero.length() > 10) {
                System.out.println("Número muito longo (máx. 10). Operação cancelada.");
                return;
            }
            a.setNumero(numero);
        }
        String andar = ler("Andar [" + a.getAndar() + "]: ");
        if (!andar.isEmpty()) {
            try {
                int v = Integer.parseInt(andar);
                if (v < 0 || v > 200) {
                    throw new NumberFormatException();
                }
                a.setAndar(v);
            } catch (NumberFormatException e) {
                System.out.println("Andar inválido. Operação cancelada.");
                return;
            }
        }
        apartamentoDAO.atualizar(a);
        System.out.println("Apartamento atualizado.");
    }

    private void deletarApartamento() throws SQLException {
        int id = lerInteiro("ID do apartamento a deletar: ", 1, Integer.MAX_VALUE);
        Apartamento a = apartamentoDAO.buscarPorId(id);
        if (a == null) {
            System.out.println("Apartamento não encontrado.");
            return;
        }
        int qtd = moradorDAO.contarPorApartamento(id);
        if (qtd > 0) {
            System.out.println("Não é possível deletar: o apartamento possui " + qtd
                    + " morador(es). Remova ou mude os moradores de apartamento antes.");
            return;
        }
        if (confirmar("Deletar \"" + a + "\"? (s/n): ")) {
            apartamentoDAO.deletar(id);
            System.out.println("Apartamento deletado.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    // ======================= MORADORES =======================

    private void menuMoradores() {
        String op;
        do {
            System.out.println("\n--- MORADORES ---");
            System.out.println("1 - Cadastrar");
            System.out.println("2 - Listar");
            System.out.println("3 - Buscar por ID");
            System.out.println("4 - Atualizar");
            System.out.println("5 - Deletar");
            System.out.println("0 - Voltar");
            op = ler("Escolha: ");
            try {
                switch (op) {
                    case "1" -> cadastrarMorador();
                    case "2" -> listarMoradores();
                    case "3" -> buscarMorador();
                    case "4" -> atualizarMorador();
                    case "5" -> deletarMorador();
                    case "0" -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                tratarErroBanco(e);
            }
        } while (!op.equals("0"));
    }

    private void cadastrarMorador() throws SQLException {
        List<Apartamento> aptos = apartamentoDAO.listar();
        if (aptos.isEmpty()) {
            System.out.println("Cadastre um apartamento antes de cadastrar moradores.");
            return;
        }
        System.out.println("\n> Novo morador");
        Morador m = new Morador();
        m.setNome(lerObrigatorio("Nome: ", 100));
        m.setCpf(lerCpf("CPF: "));
        m.setTelefone(lerTelefoneOpcional("Telefone (opcional): "));
        m.setEmail(lerEmailOpcional("E-mail (opcional): "));
        preencherEndereco(m);

        System.out.println("\nApartamentos disponíveis:");
        aptos.forEach(System.out::println);
        m.setApartamentoId(lerApartamentoExistente("ID do apartamento: "));

        moradorDAO.inserir(m);
        System.out.println("Morador cadastrado com ID " + m.getId() + ".");
    }

    private void listarMoradores() throws SQLException {
        List<Morador> lista = moradorDAO.listar();
        if (lista.isEmpty()) {
            System.out.println("Nenhum morador cadastrado.");
            return;
        }
        System.out.println();
        lista.forEach(System.out::println);
    }

    private void buscarMorador() throws SQLException {
        int id = lerInteiro("ID do morador: ", 1, Integer.MAX_VALUE);
        Morador m = moradorDAO.buscarPorId(id);
        System.out.println(m == null ? "Morador não encontrado." : "\n" + m);
    }

    private void atualizarMorador() throws SQLException {
        int id = lerInteiro("ID do morador a atualizar: ", 1, Integer.MAX_VALUE);
        Morador m = moradorDAO.buscarPorId(id);
        if (m == null) {
            System.out.println("Morador não encontrado.");
            return;
        }
        System.out.println("Atual:\n" + m);
        System.out.println("(Deixe em branco para manter o valor atual)");

        String nome = ler("Nome [" + m.getNome() + "]: ");
        if (!nome.isEmpty()) {
            if (nome.length() > 100) {
                System.out.println("Nome muito longo. Operação cancelada.");
                return;
            }
            m.setNome(nome);
        }

        while (true) {
            String cpf = ler("CPF [" + m.getCpf() + "]: ");
            if (cpf.isEmpty()) {
                break;
            }
            if (Validador.cpfValido(cpf)) {
                m.setCpf(Validador.somenteDigitos(cpf));
                break;
            }
            System.out.println("CPF inválido. Tente novamente.");
        }

        while (true) {
            String tel = ler("Telefone [" + valor(m.getTelefone()) + "]: ");
            if (tel.isEmpty()) {
                break;
            }
            if (Validador.telefoneValido(tel)) {
                m.setTelefone(Validador.somenteDigitos(tel));
                break;
            }
            System.out.println("Telefone inválido (use DDD + número).");
        }

        while (true) {
            String email = ler("E-mail [" + valor(m.getEmail()) + "]: ");
            if (email.isEmpty()) {
                break;
            }
            if (Validador.emailValido(email) && email.length() <= 100) {
                m.setEmail(email);
                break;
            }
            System.out.println("E-mail inválido.");
        }

        if (confirmar("Alterar o CEP/endereço? (s/n): ")) {
            preencherEndereco(m);
        }

        while (true) {
            String apto = ler("ID do apartamento [" + m.getApartamentoId() + "]: ");
            if (apto.isEmpty()) {
                break;
            }
            try {
                int aptoId = Integer.parseInt(apto);
                if (apartamentoDAO.buscarPorId(aptoId) != null) {
                    m.setApartamentoId(aptoId);
                    break;
                }
                System.out.println("Apartamento não existe.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro.");
            }
        }

        moradorDAO.atualizar(m);
        System.out.println("Morador atualizado.");
    }

    private void deletarMorador() throws SQLException {
        int id = lerInteiro("ID do morador a deletar: ", 1, Integer.MAX_VALUE);
        Morador m = moradorDAO.buscarPorId(id);
        if (m == null) {
            System.out.println("Morador não encontrado.");
            return;
        }
        if (confirmar("Deletar o morador \"" + m.getNome() + "\"? (s/n): ")) {
            moradorDAO.deletar(id);
            System.out.println("Morador deletado.");
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    // ======================= API EXTERNA (ViaCEP) =======================

    private void preencherEndereco(Morador m) {
        while (true) {
            String cep = ler("CEP (8 dígitos, ou Enter para pular): ");
            if (cep.isEmpty()) {
                return;
            }
            if (!Validador.cepValido(cep)) {
                System.out.println("CEP inválido. Digite 8 números.");
                continue;
            }
            cep = Validador.somenteDigitos(cep);
            try {
                System.out.println("Consultando ViaCEP...");
                JsonObject json = ViaCepService.buscar(cep);
                if (json == null) {
                    System.out.println("CEP não encontrado. Tente outro.");
                    continue;
                }
                m.setCep(cep);
                m.setLogradouro(texto(json, "logradouro"));
                m.setBairro(texto(json, "bairro"));
                m.setCidade(texto(json, "localidade"));
                m.setUf(texto(json, "uf"));
                System.out.println("Endereço encontrado: " + m.getLogradouro() + ", " + m.getBairro()
                        + " - " + m.getCidade() + "/" + m.getUf());
                return;
            } catch (Exception e) {
                System.out.println("Não foi possível consultar o ViaCEP (" + e.getMessage() + ").");
                if (confirmar("Digitar o endereço manualmente? (s/n): ")) {
                    m.setCep(cep);
                    m.setLogradouro(lerOpcional("Logradouro: ", 150));
                    m.setBairro(lerOpcional("Bairro: ", 100));
                    m.setCidade(lerOpcional("Cidade: ", 100));
                    while (true) {
                        String uf = ler("UF (2 letras): ").toUpperCase();
                        if (uf.matches("[A-Z]{2}")) {
                            m.setUf(uf);
                            break;
                        }
                        System.out.println("UF inválida.");
                    }
                }
                return;
            }
        }
    }

    private String texto(JsonObject json, String campo) {
        return json.has(campo) && !json.get(campo).isJsonNull() ? json.get(campo).getAsString() : "";
    }

    // ======================= LEITURA E VALIDAÇÃO =======================

    private String ler(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private String lerObrigatorio(String prompt, int max) {
        while (true) {
            String s = ler(prompt);
            if (Validador.vazio(s)) {
                System.out.println("Campo obrigatório.");
            } else if (s.length() > max) {
                System.out.println("Máximo de " + max + " caracteres.");
            } else {
                return s;
            }
        }
    }

    private String lerOpcional(String prompt, int max) {
        while (true) {
            String s = ler(prompt);
            if (s.length() <= max) {
                return s;
            }
            System.out.println("Máximo de " + max + " caracteres.");
        }
    }

    private int lerInteiro(String prompt, int min, int max) {
        while (true) {
            String s = ler(prompt);
            try {
                int v = Integer.parseInt(s);
                if (v >= min && v <= max) {
                    return v;
                }
                System.out.println("Valor fora do intervalo permitido (" + min + " a " + max + ").");
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private String lerCpf(String prompt) {
        while (true) {
            String s = ler(prompt);
            if (Validador.cpfValido(s)) {
                return Validador.somenteDigitos(s);
            }
            System.out.println("CPF inválido.");
        }
    }

    private String lerTelefoneOpcional(String prompt) {
        while (true) {
            String s = ler(prompt);
            if (s.isEmpty()) {
                return null;
            }
            if (Validador.telefoneValido(s)) {
                return Validador.somenteDigitos(s);
            }
            System.out.println("Telefone inválido (use DDD + número).");
        }
    }

    private String lerEmailOpcional(String prompt) {
        while (true) {
            String s = ler(prompt);
            if (s.isEmpty()) {
                return null;
            }
            if (Validador.emailValido(s) && s.length() <= 100) {
                return s;
            }
            System.out.println("E-mail inválido.");
        }
    }

    private int lerApartamentoExistente(String prompt) throws SQLException {
        while (true) {
            int id = lerInteiro(prompt, 1, Integer.MAX_VALUE);
            if (apartamentoDAO.buscarPorId(id) != null) {
                return id;
            }
            System.out.println("Apartamento não existe.");
        }
    }

    private boolean confirmar(String prompt) {
        while (true) {
            String s = ler(prompt).toLowerCase();
            if (s.equals("s") || s.equals("sim")) {
                return true;
            }
            if (s.equals("n") || s.equals("nao") || s.equals("não")) {
                return false;
            }
            System.out.println("Responda com s ou n.");
        }
    }

    private String valor(String s) {
        return s == null || s.isBlank() ? "-" : s;
    }

    private void tratarErroBanco(SQLException e) {
        switch (e.getErrorCode()) {
            case 1062 -> System.out.println("Erro: já existe um registro com esses dados (CPF ou bloco/apartamento duplicado).");
            case 1451 -> System.out.println("Erro: este registro está sendo usado em outra tabela e não pode ser removido.");
            case 1452 -> System.out.println("Erro: apartamento informado não existe.");
            default -> System.out.println("Erro no banco de dados: " + e.getMessage());
        }
    }
}
