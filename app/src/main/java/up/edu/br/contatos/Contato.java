package up.edu.br.contatos;

import java.io.Serializable;

public class Contato implements Serializable{

    private Integer id;
    private String nome;
    private String tipo;
    private String numero;
    private String email;

    private String cidade;
    private String estado;

    private boolean ativo;

    @Override
    public boolean equals(Object o){
        if (id == null || ((Contato)o).getId() == null){
            return false;
        }
        return id.equals(((Contato)o).getId());
    }


    public String toString(){
        return nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
