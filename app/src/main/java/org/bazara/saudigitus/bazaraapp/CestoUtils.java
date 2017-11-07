package org.bazara.saudigitus.bazaraapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoCesto;
import org.bazara.saudigitus.bazaraapp.models.Publicacao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dalves on 10/5/17.
 */

public class CestoUtils {


    static List<ProdutoCesto> cartListPublicacao = new ArrayList<>();

    public void addCartListProduto(ProdutoCesto wishlistImageUri) {
        this.cartListPublicacao.add(wishlistImageUri);
    }

    public void removeCartListPublicacao(int position) {
        this.cartListPublicacao.remove(position);
    }

    public void updateCartListPublicacao(int position, double quant) {
        ProdutoCesto produtoCesto = this.cartListPublicacao.get(position);
        produtoCesto.setQuantidade(quant);
        this.cartListPublicacao.remove(position);
        this.cartListPublicacao.add(position, produtoCesto);
    }

    public List<ProdutoCesto> getCartListPublicacao(){ return this.cartListPublicacao; }

    public double totalPagar(){
        double total = 0;
        for (int i =0; i < this.cartListPublicacao.size(); i++){
            total = total + (this.cartListPublicacao.get(i).getQuantidade() * this.cartListPublicacao.get(i).getPublicacao().getPreco());
        }
        return total;
    }
}
