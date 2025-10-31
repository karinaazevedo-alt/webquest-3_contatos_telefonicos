package agenda;

import agenda.exceptions.ContatoExistenteException;
import agenda.exceptions.ContatoNaoEncontradoException;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaManager implements GerenciadorContatos {

    private List<Contato> contatos;

    public AgendaManager() {
        this.contatos = new ArrayList<>();
    }

    @Override
    public void adicionarContato(Contato contato) throws ContatoExistenteException {
        for (Contato c : contatos) {
            if (c.getNome().equalsIgnoreCase(contato.getNome())) {
                throw new ContatoExistenteException("Erro: Contato com o nome '" + contato.getNome() + "' já existe.");
            }
        }
        this.contatos.add(contato);
    }

    @Override
    public Contato buscarContato(String nome) throws ContatoNaoEncontradoException {
        for (Contato c : contatos) {
            if (c.getNome().equalsIgnoreCase(nome)) {
                return c;
            }
        }
        throw new ContatoNaoEncontradoException("Erro: Contato com o nome '" + nome + "' não encontrado.");
    }

    @Override
    public void removerContato(String nome) throws ContatoNaoEncontradoException {
        Contato contatoParaRemover = buscarContato(nome);
        this.contatos.remove(contatoParaRemover);
    }

    @Override
    public List<Contato> listarTodosContatos() {
        return new ArrayList<>(this.contatos);
    }

    public void salvarContatosCSV(String nomeArquivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            for (Contato c : contatos) {
                String linha = c.getNome() + ";" + c.getTelefone() + ";" + c.getEmail();
                bw.write(linha);
                bw.newLine();
            }
        }
    }

    public void carregarContatosCSV(String nomeArquivo) throws IOException {
        List<Contato> contatosCarregados = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(";");
                if (campos.length == 3) {
                    Contato c = new Contato(campos[0], campos[1], campos[2]);
                    contatosCarregados.add(c);
                } else {
                    System.err.println("Aviso: Linha mal formatada no CSV será ignorada: " + linha);
                }
            }

            this.contatos.clear();
            this.contatos.addAll(contatosCarregados);

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Erro: O arquivo " + nomeArquivo + " não foi encontrado.");
        }
    }

    public List<Contato> listarContatosOrdenados() {
        List<Contato> contatosOrdenados = new ArrayList<>(this.contatos);

        contatosOrdenados.sort(Comparator.comparing(Contato::getNome, String.CASE_INSENSITIVE_ORDER));

        return contatosOrdenados;
    }


    public List<Contato> buscarPorDominioEmail(String dominio) {
        String sufixoEmail = "@" + dominio.toLowerCase();

        return this.contatos.stream()
                .filter(c -> c.getEmail().toLowerCase().endsWith(sufixoEmail))
                .collect(Collectors.toList());
    }
}