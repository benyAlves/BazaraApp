package org.bazara.saudigitus.bazaraapp.models;

/**
 * Created by dalves on 10/5/17.
 */

public class ProdutoCesto {

    private double quantidade;
    Publicacao publicacao;

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }
}
