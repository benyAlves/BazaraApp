package org.bazara.saudigitus.bazaraapp.models;

/**
 * Created by dalves on 9/12/17.
 */

public class Categoria {

    private int id;
    private String nome;


    public Categoria(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
