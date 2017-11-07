package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dalves on 9/12/17.
 */

public class Produto {

    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("descricao")
    @Expose
    private String descricao;

    @SerializedName("categoria_id")
    @Expose
    private String categoria_id;

    private String imagemUrl;




    public Produto(int id, String nome, String descricao, String categoria_id) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria_id = categoria_id;

    }

    public Produto(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getCategoria_id() {
        return categoria_id;
    }

    public void setCategoria_id(String categoria_id) {
        this.categoria_id = categoria_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }
}
