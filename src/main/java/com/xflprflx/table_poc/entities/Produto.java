package com.xflprflx.table_poc.entities;

import com.xflprflx.table_poc.annotations.*;

@Entidade
@Tabela(name = "produtos")
public class Produto {

    @ChavePrimaria
    @ValorGerado
    @Coluna(name = "produto_id")
    private Long id;

    @Coluna(name = "descricao")
    private String nome;

    @Coluna(name = "preco_unitario")
    private Double preco;

    public Produto() {
    }

    public Produto(Long id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}
