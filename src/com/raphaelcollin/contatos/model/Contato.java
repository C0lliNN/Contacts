
package com.raphaelcollin.contatos.model;

public class Contato {
    
        // A descricao sera opcional

    private int id = -1;
    private String nome;
    private String numero;
    private String email;
    private String descricao;

        // Construtores

    public Contato(int id,String nome, String numero, String email, String descricao) {
        this.id = id;
        this.nome = nome;
        this.numero = numero;
        this.email = email;
        this.descricao = descricao;
    }
    public Contato(String nome, String numero, String email, String descricao) {
        this.nome = nome;
        this.numero = numero;
        this.email = email;
        this.descricao = descricao;
    }

        // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

        /* Metodo to String  Retornara apenas o nome pois a ListView sera de Objetos do tipo Contato
         * Entao para aparecer somente o nome do contato na Lista vamos apenas retornar o nome Do contato */

    @Override
    public String toString() {
        return nome;
    }
}
