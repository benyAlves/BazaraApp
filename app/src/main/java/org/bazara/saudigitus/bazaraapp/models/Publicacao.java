package org.bazara.saudigitus.bazaraapp.models;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dalves on 10/3/17.
 */

public class Publicacao implements Parcelable{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("associacao_id")
    @Expose
    private int associacao_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("imagem")
    @Expose
    private String imagem;

    @SerializedName("quantidade")
    @Expose
    private double quantidade;

    @SerializedName("preco")
    @Expose
    private double preco;

    @SerializedName("nomeProduto")
    @Expose
    private String nomeProduto;

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("unidade")
    @Expose
    private String unidade;


    public Publicacao(int id, int user_id, int associacao_id, String created_at, String imagem, double quantidade, double preco, String nomeProduto, String nome, String unidade) {
        this.id = id;
        this.user_id = user_id;
        this.associacao_id = associacao_id;
        this.created_at = created_at;
        this.imagem = imagem;
        this.quantidade = quantidade;
        this.preco = preco;
        this.nomeProduto = nomeProduto;
        this.nome = nome;
        this.unidade = unidade;
    }


    protected Publicacao(Parcel in) {
        id = in.readInt();
        user_id = in.readInt();
        associacao_id = in.readInt();
        created_at = in.readString();
        imagem = in.readString();
        quantidade = in.readDouble();
        preco = in.readDouble();
        nomeProduto = in.readString();
        nome = in.readString();
        unidade = in.readString();
    }

    public static final Creator<Publicacao> CREATOR = new Creator<Publicacao>() {
        @Override
        public Publicacao createFromParcel(Parcel in) {
            return new Publicacao(in);
        }

        @Override
        public Publicacao[] newArray(int size) {
            return new Publicacao[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAssociacao_id() {
        return associacao_id;
    }

    public void setAssociacao_id(int associacao_id) {
        this.associacao_id = associacao_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(user_id);
        parcel.writeInt(associacao_id);
        parcel.writeString(created_at);
        parcel.writeString(imagem);
        parcel.writeDouble(quantidade);
        parcel.writeDouble(preco);
        parcel.writeString(nomeProduto);
        parcel.writeString(nome);
        parcel.writeString(unidade);
    }
}
