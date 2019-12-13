package com.raphaelcollin.contacts.model;

import com.raphaelcollin.contacts.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ContatoDAO {

            /* Nessa classe, será utilizado o recurso de Singleton que permite apenas uma única instancia de uma
             * classe */

        // Conexão
    
    private Connection connection;

    private static ContatoDAO instance = new ContatoDAO();

    // Constantes relativas a estrutura do Banco de Dados

    private static final String TABELA_CONTATOS_DB = "contatos";
    private static final String COLUNA_IDCONTATO_DB = "idContato";
    private static final String COLUNA_NOME_DB = "nome";

        // Obetendo Conexão
    
    private ContatoDAO(){
        connection = ConnectionFactory.getConnection();
    }

        /* Metodo que irá adicionar um Contato ao Banco de Dados
         * Este método receberá um Objeto da Classe Contato e retornará true no caso de sucesso
          * e false no caso de erro */
        
    public int adicionarContato(Contato contato){
        
        String query = "insert into " + TABELA_CONTATOS_DB + " values(null,?,?,?,?,?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, contato.getNome());
            statement.setString(2,contato.getSexo());
            statement.setString(3, contato.getNumero());
            statement.setString(4, contato.getEmail());
            statement.setString(5, contato.getDescricao());

            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();

            resultSet.next();

            int idGerado = resultSet.getInt(1);

            resultSet.close();

            return idGerado;

        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            return -1;
        }
    }

    /* Este método recebe como parâmetro o Id do contato e retorna um objeto do tipo Contato contendo os dados do
       respectivo contato */

    public Contato recuperarContato(int id){

        String query = "select * from " + TABELA_CONTATOS_DB +" where " + COLUNA_IDCONTATO_DB + " = ?";

        try (PreparedStatement statement = connection.prepareStatement(query
            , PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            Contato contato = new Contato(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                , resultSet.getString(4),resultSet.getString(5),resultSet.getString(5));

            resultSet.close();

            return contato;


        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }


        /* Este método será executado toda vez que a pagina principal da Aplicação for carregada.
         * Será retornada a lista de todos os contatos cadastrados no banco ordenados em ordem alfabética */

    public ObservableList<Contato> recuperarTodosContatos(){

        String sql = "select * from " + TABELA_CONTATOS_DB + " order by " + COLUNA_NOME_DB;

        ObservableList<Contato> contatos = FXCollections.observableArrayList();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Contato contato = new Contato(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                    ,resultSet.getString(4),resultSet.getString(5),resultSet.getString(6));
                contatos.add(contato);
            }
            return contatos;

        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

        /* Este método será utilizado para atualizar um contato no banco de dados.
         * Será recebida a query que será executada, os atributos dos campos que serão atualizados e o id
          * do contato que será atualizado. Será retornado true no caso de sucesso e false no caso de erro */

    public boolean atualizarContato(String query, List<String> campos, int id) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int c = 0; c < campos.size(); c++){
                preparedStatement.setString(c+1,campos.get(c));
            }

            preparedStatement.setInt(campos.size() + 1,id);

            preparedStatement.execute();
            return true;
        } catch (Exception e){
            System.err.println("Erro: " + e.getMessage());
            return false;
        }
    }

        /*  Este método irá excluir um contato do banco de dados. Será recebido o id do contato e será
            retornado true no caso de sucesso e false no caso de erro */

    public boolean excluirContato(int id){

        String query = "delete from " + TABELA_CONTATOS_DB + " where " + COLUNA_IDCONTATO_DB + " = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1,id);
            statement.execute();
            return true;
        } catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

        /* Fechando a Conexão com o Banco de Dados */

    public void closeConnection(){
        ConnectionFactory.closeConnection(connection);
    }

        // Getter

    public static ContatoDAO getInstance() {
        return instance;
    }
}
