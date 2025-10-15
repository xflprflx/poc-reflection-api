package com.xflprflx.table_poc.entities;


import com.xflprflx.table_poc.annotations.*;

@Entidade
@Tabela(name = "usuarios_sistema")
public class Usuario {

    @ChavePrimaria
    @ValorGerado
    @Coluna(name = "user_id")
    private Long id;

    @Coluna()
    private String nome;

    @Coluna(name = "email")
    private String email;

    @Coluna(name = "idade")
    private int idade;

    public Usuario() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", idade=" + idade +
                '}';
    }

}
