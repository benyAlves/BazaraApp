package org.bazara.saudigitus.bazaraapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.bazara.saudigitus.bazaraapp.R;

/**
 * Created by dalves on 10/10/17.
 */

public class PedidoHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView nomeProd;
    public final TextView nomeAgricultor;
    public final TextView precoProduto;
    public final TextView precoTotal;
    public final TextView dataPedido;
    public  final ImageView imageView;

    public PedidoHolder(View view) {
        super(view);
        rootView = view;
        nomeProd =  view.findViewById(R.id.nomeProduto);
        nomeAgricultor = view.findViewById(R.id.nome_produtor);
        precoProduto = view.findViewById(R.id.quant_total_pedida);
        precoTotal = view.findViewById(R.id.preco_total_produto);
        dataPedido = view.findViewById(R.id.data_pedido);
        imageView = view.findViewById(R.id.imagem_prod);
    }
}
