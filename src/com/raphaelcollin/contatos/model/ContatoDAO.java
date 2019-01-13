package com.raphaelcollin.contatos.model;

import com.raphaelcollin.contatos.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContatoDAO {

        // Conexao
    
    private Connection connection;

        // Obetendo conexao
    
    public ContatoDAO(){
        connection = ConnectionFactory.getConnection();
    }

        /* Metodo que ira adicionar um Contato ao Banco de Dados
         * Esse metodo recebera um Objeto da Classe Contato e retornara true no caso de sucesso
          * e false no caso de erro*/
        
    public boolean insertContato(Contato contato){
        PreparedStatement statement = null;
        
        String sql = "insert into Contatos values(null,?,?,?,?)";
        
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, contato.getNome());
            statement.setString(2, contato.getNumero());
            statement.setString(3, contato.getEmail());
            statement.setString(4, contato.getDescricao());
            
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro: " + ex.getMessage());
            return false;
        } finally {
            ConnectionFactory.closeConnection(connection, statement);
        }       
    }

        /* Esse metodo sera executado toda vez que a pagina principal da Aplicacao for carregada
         * Esse metodo vai retorar a lista de todos os contatos cadastrados no banco ordenados em ordem alfabetica */

    public ObservableList<Contato> selectContatos(){
        PreparedStatement statement = null;

        String sql = "select * from Contatos order by nome";

        ResultSet resultSet = null;

        ObservableList<Contato> contatos = FXCollections.observableArrayList();

        try {
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            while (resultSet.next()){
                Contato contato = new Contato(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3)
                    ,resultSet.getString(4),resultSet.getString(5));
                contatos.add(contato);
            }
            return contatos;

        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        } finally {
            ConnectionFactory.closeConnection(connection,statement,resultSet);
        }
    }

        /* Esse metodo vai atualizar um contato no banco
         * Esse metodo vai receber uma string que sera executada e retornara true no caso de sucesso e false no caso de erro */

    public boolean updateContato(String sql) {

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.err.println("Erro: " + e.getMessage());
            return false;
        }
    }

        /*  Esse metodo vai excluir um contato do banco de dados
         *  Esse metodo vai receber o id do contato e retornara true no caso de sucesso e false no caso de erro */

    public boolean deleteContato(int id){
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("delete from Contatos where idContato = " + id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.out.println("Erro: " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.closeConnection(connection,statement);
        }
    }

        /* Metodo para fechar a conexao que sera util em alguns casos */

    public void closeConnection(){
        ConnectionFactory.closeConnection(connection);
    }
}
