package com.raphaelcollin.contatos.model;

import com.raphaelcollin.contatos.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ContatoDAO {

            /* Nessa classe, estamos utilizando o metodo de Singleton que permite apenas uma unica instancia de uma
             * classe */

        // Conexao
    
    private Connection connection;

    private static ContatoDAO instance = new ContatoDAO();

        // Obetendo conexao
    
    private ContatoDAO(){
        connection = ConnectionFactory.getConnection();
    }

        /* Metodo que ira adicionar um Contato ao Banco de Dados
         * Esse metodo recebera um Objeto da Classe Contato e retornara true no caso de sucesso
          * e false no caso de erro*/
        
    public int insertContato(Contato contato){
        
        String sql = "insert into Contatos values(null,?,?,?,?,?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS)) {
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

        } catch (SQLException ex) {
            System.err.println("Erro: " + ex.getMessage());
            return -1;
        }
    }

    public Contato selectContato(int id){
        try (PreparedStatement statement = connection.prepareStatement("select * from contatos where idContato = ?"
            , PreparedStatement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,id);

            ResultSet resultSet = statement.executeQuery();

            resultSet.next();

            Contato contato = new Contato(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                , resultSet.getString(4),resultSet.getString(5),resultSet.getString(5));

            resultSet.close();

            return contato;


        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }


        /* Esse metodo sera executado toda vez que a pagina principal da Aplicacao for carregada
         * Esse metodo vai retorar a lista de todos os contatos cadastrados no banco ordenados em ordem alfabetica */

    public ObservableList<Contato> selectContatos(){

        String sql = "select * from Contatos order by nome";

        ObservableList<Contato> contatos = FXCollections.observableArrayList();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()){
                Contato contato = new Contato(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                    ,resultSet.getString(4),resultSet.getString(5),resultSet.getString(6));
                contatos.add(contato);
            }
            return contatos;

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

        /* Esse metodo vai atualizar um contato no banco
         * Esse metodo vai receber uma string que sera executada, os atributos dos campos que serao atualizados, o id
          * do contato e retornara true no caso de sucesso e false no caso de erro */

    public boolean updateContato(String sql,List<String> campos, int id) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int c = 0; c < campos.size(); c++){
                preparedStatement.setString(c+1,campos.get(c));
            }

            preparedStatement.setInt(campos.size() + 1,id);

            preparedStatement.execute();
            return true;
        } catch (SQLException e){
            System.err.println("Erro: " + e.getMessage());
            return false;
        }
    }

        /*  Esse metodo vai excluir um contato do banco de dados
         *  Esse metodo vai receber o id do contato e retornara true no caso de sucesso e false no caso de erro */

    public boolean deleteContato(int id){
        try (PreparedStatement statement = connection.prepareStatement("delete from Contatos where idContato = ?")) {
            statement.setInt(1,id);
            statement.execute();
            return true;
        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return false;
        }
    }

        /* Fechando a Conexa com o Banco de Dados */

    public void closeConnection(){
        ConnectionFactory.closeConnection(connection);
    }

        // Getter

    public static ContatoDAO getInstance() {
        return instance;
    }
}
