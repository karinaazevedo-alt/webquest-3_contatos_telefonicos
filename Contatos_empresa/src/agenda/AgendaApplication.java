package agenda;

import agenda.exceptions.ContatoExistenteException;
import agenda.exceptions.ContatoNaoEncontradoException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que executa a aplicação de agenda via console.
 */
public class AgendaApplication {

    // Declara o Scanner e o Manager como estáticos para fácil acesso no menu
    private static final Scanner scanner = new Scanner(System.in);
    private static final AgendaManager agenda = new AgendaManager();
    private static final String NOME_ARQUIVO_CSV = "agenda_contatos.csv";

    public static void main(String[] args) {
        boolean executando = true;

        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    adicionarContato();
                    break;
                case 2:
                    buscarContato();
                    break;
                case 3:
                    removerContato();
                    break;
                case 4:
                    listarTodosContatos();
                    break;
                case 5:
                    listarContatosOrdenados();
                    break;
                case 6:
                    buscarPorDominioEmail();
                    break;
                case 7:
                    salvarEmCSV();
                    break;
                case 8:
                    carregarDeCSV();
                    break;
                case 9:
                    executando = false;
                    System.out.println("Obrigado por usar a agenda. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }

            if (executando) {
                pressionarEnterParaContinuar();
            }
        }
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n--- Agenda Eletrônica ---");
        System.out.println("1. Adicionar Contato");
        System.out.println("2. Buscar Contato");
        System.out.println("3. Remover Contato");
        System.out.println("4. Listar Todos os Contatos");
        System.out.println("5. Listar Contatos Ordenados (por nome)");
        System.out.println("6. Buscar por Domínio de Email");
        System.out.println("7. Salvar em CSV");
        System.out.println("8. Carregar de CSV");
        System.out.println("9. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcao() {
        try {

            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void pressionarEnterParaContinuar() {
        System.out.println("\nPressione [Enter] para continuar...");
        scanner.nextLine();
    }

    private static void adicionarContato() {
        System.out.println("\n-- Adicionar Contato --");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        if (nome.isEmpty() || telefone.isEmpty() || email.isEmpty()) {
            System.out.println("Erro: Todos os campos são obrigatórios.");
            return;
        }

        try {
            Contato novoContato = new Contato(nome, telefone, email);
            agenda.adicionarContato(novoContato);
            System.out.println("Contato adicionado com sucesso!");
        } catch (ContatoExistenteException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void buscarContato() {
        System.out.println("\n-- Buscar Contato --");
        System.out.print("Digite o nome a buscar: ");
        String nome = scanner.nextLine();

        try {
            Contato c = agenda.buscarContato(nome);
            System.out.println("Contato encontrado:");
            System.out.println(c);
        } catch (ContatoNaoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void removerContato() {
        System.out.println("\n-- Remover Contato --");
        System.out.print("Digite o nome a remover: ");
        String nome = scanner.nextLine();

        try {
            agenda.removerContato(nome);
            System.out.println("Contato removido com sucesso!");
        } catch (ContatoNaoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void listarContatos(List<Contato> lista, String titulo) {
        System.out.println(titulo);
        if (lista.isEmpty()) {
            System.out.println("Nenhum contato encontrado.");
            return;
        }

        for (Contato c : lista) {
            System.out.println(c);
        }
        System.out.println("Total: " + lista.size() + " contato(s).");
    }

    private static void listarTodosContatos() {
        listarContatos(agenda.listarTodosContatos(), "\n-- Lista de Todos os Contatos --");
    }

    private static void listarContatosOrdenados() {
        listarContatos(agenda.listarContatosOrdenados(), "\n-- Contatos Ordenados por Nome --");
    }

    private static void buscarPorDominioEmail() {
        System.out.println("\n-- Buscar por Domínio de Email --");
        System.out.print("Digite o domínio (ex: gmail.com): ");
        String dominio = scanner.nextLine();

        if (dominio.isEmpty()) {
            System.out.println("Domínio não pode ser vazio.");
            return;
        }

        List<Contato> resultados = agenda.buscarPorDominioEmail(dominio);
        listarContatos(resultados, "\n-- Contatos do domínio '" + dominio + "' --");
    }

    private static void salvarEmCSV() {
        System.out.println("\n-- Salvar em CSV --");
        try {
            agenda.salvarContatosCSV(NOME_ARQUIVO_CSV);
            System.out.println("Contatos salvos com sucesso em '" + NOME_ARQUIVO_CSV + "'");
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo CSV: " + e.getMessage());
        }
    }

    private static void carregarDeCSV() {
        System.out.println("\n-- Carregar de CSV --");
        try {
            agenda.carregarContatosCSV(NOME_ARQUIVO_CSV);
            System.out.println("Contatos carregados com sucesso de '" + NOME_ARQUIVO_CSV + "'");
            listarTodosContatos();
        } catch (IOException e) {

            System.err.println("Erro ao carregar arquivo CSV: " + e.getMessage());
        }
    }
}