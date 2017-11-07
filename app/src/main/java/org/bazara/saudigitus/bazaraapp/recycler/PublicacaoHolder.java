package org.bazara.saudigitus.bazaraapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.bazara.saudigitus.bazaraapp.R;

/**
 * Created by dalves on 9/19/17.
 */

public class PublicacaoHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView tvItem;
    public final TextView tvSubItem;
    public  final ImageView imageView;
    public final Button btColocarCesto;
    public final TextView tvPreco;
    public final TextView nomeProdutor;

    public PublicacaoHolder(View view) {
        super(view);
        rootView = view;
        tvItem = view.findViewById(R.id.nomeProduto);
        tvSubItem =  view.findViewById(R.id.agricultor);
        imageView = view.findViewById(R.id.imagem_prod);
        tvPreco =  view.findViewById(R.id.preco_produto);
        nomeProdutor = view.findViewById(R.id.nome_produtor);
        btColocarCesto = view.findViewById(R.id.btColocarNoCesto);
    }
}
