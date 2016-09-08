package br.com.sovrau.dto;

import java.io.Serializable;

/**
 * Created by Lucas on 26/04/2016.
 */
public class UsuarioDTO implements Serializable{
    private String idUSuario;
    private String email;
    private String senha;
    private String nome;

    public UsuarioDTO(){}

    public String getIdUSuario() {
        return idUSuario;
    }

    public void setIdUSuario(String idUSuario) {
        this.idUSuario = idUSuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioDTO that = (UsuarioDTO) o;

        if (idUSuario != null ? !idUSuario.equals(that.idUSuario) : that.idUSuario != null)
            return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (senha != null ? !senha.equals(that.senha) : that.senha != null) return false;
        return nome != null ? nome.equals(that.nome) : that.nome == null;

    }

    @Override
    public int hashCode() {
        int result = idUSuario != null ? idUSuario.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (senha != null ? senha.hashCode() : 0);
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        return result;
    }
}
