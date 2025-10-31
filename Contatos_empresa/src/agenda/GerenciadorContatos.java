package agenda;

import agenda.exceptions.ContatoExistenteException;
import agenda.exceptions.ContatoNaoEncontradoException;
import java.util.List;

/**
 * Interface que define o contrato para o gerenciamento de contatos.
 */
public interface GerenciadorContatos {

    /**
     * Adiciona um novo contato à agenda.
     *
     * @param contato O contato a ser adicionado.
     * @throws ContatoExistenteException Se um contato com o mesmo nome já existir.
     */
    void adicionarContato(Contato contato) throws ContatoExistenteException;

    /**
     * Busca um contato pelo nome.
     *
     * @param nome O nome do contato a buscar.
     * @return O objeto Contato encontrado.
     * @throws ContatoNaoEncontradoException Se o contato não for encontrado.
     */
    Contato buscarContato(String nome) throws ContatoNaoEncontradoException;

    /**
     * Remove um contato pelo nome.
     *
     * @param nome O nome do contato a remover.
     * @throws ContatoNaoEncontradoException Se o contato não for encontrado.
     */
    void removerContato(String nome) throws ContatoNaoEncontradoException;

    /**
     * Lista todos os contatos presentes na agenda.
     *
     * @return Uma lista de todos os contatos.
     */
    List<Contato> listarTodosContatos();
}