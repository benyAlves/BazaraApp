package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dalves on 9/12/17.
 */

public class Pedido {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("cliente_id")
    @Expose
    private int cliente_id;
    @SerializedName("quantidadeRequisitada")
    @Expose
    private double quantidadeRequisitada;
    @SerializedName("preco")
    @Expose
    private double preco;
    @SerializedName("dataRequisicao")
    @Expose
    private String dataRequisicao;
    @SerializedName("estado")
    @Expose
    private String estado;
    @SerializedName("nomeProduto")
    @Expose
    private String nomeProduto;
    @SerializedName("nomeAssociacao")
    @Expose
    private String nomeAssociacao;
    @SerializedName("dataPublicacao")
    @Expose
    private String dataPublicacao;
    @SerializedName("imagem")
    @Expose
    private String imagem;
    @SerializedName("nomeAgricultor")
    @Expose
    private String nomeAgricultor;

    @SerializedName("unidade")
    @Expose
    private String unidade;


    public Pedido(int id, int cliente_id, String unidade, double quantidadeRequisitada, double preco, String dataRequisicao, String estado, String nomeProduto, String nomeAssociacao, String dataPublicacao, String imagem, String nomeAgricultor) {
        this.id = id;
        this.cliente_id = cliente_id;
        this.quantidadeRequisitada = quantidadeRequisitada;
        this.preco = preco;
        this.dataRequisicao = dataRequisicao;
        this.estado = estado;
        this.nomeProduto = nomeProduto;
        this.nomeAssociacao = nomeAssociacao;
        this.dataPublicacao = dataPublicacao;
        this.imagem = imagem;
        this.nomeAgricultor = nomeAgricultor;
        this.unidade = unidade;
    }


    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(int cliente_id) {
        this.cliente_id = cliente_id;
    }

    public double getQuantidadeRequisitada() {
        return quantidadeRequisitada;
    }

    public void setQuantidadeRequisitada(double quantidadeRequisitada) {
        this.quantidadeRequisitada = quantidadeRequisitada;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(String dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNomeAssociacao() {
        return nomeAssociacao;
    }

    public void setNomeAssociacao(String nomeAssociacao) {
        this.nomeAssociacao = nomeAssociacao;
    }

    public String getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(String dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNomeAgricultor() {
        return nomeAgricultor;
    }

    public void setNomeAgricultor(String nomeAgricultor) {
        this.nomeAgricultor = nomeAgricultor;
    }
}
