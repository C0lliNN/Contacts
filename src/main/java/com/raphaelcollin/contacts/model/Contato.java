
package com.raphaelcollin.contatos.model;

public class Contato {
    
        // A descrição será opcional

    private int id = -1;
    private String nome;
    private String sexo;
    private String numero;
    private String email;
    private String descricao;

        // Construtores

    public Contato(int id,String nome, String sexo, String numero, String email, String descricao) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.numero = numero;
        this.email = email;
        this.descricao = descricao;
    }
    public Contato(String nome, String sexo, String numero, String email, String descricao) {
        this.nome = nome;
        this.sexo = sexo;
        this.numero = numero;
        this.email = email;
        this.descricao = descricao;
    }

        // Getters e Setters

    public String getNome() {
        return nome;
    }

    public String getSexo() {
        return sexo;
    }

    public String getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
