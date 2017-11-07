package org.bazara.saudigitus.bazaraapp.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.bazara.saudigitus.bazaraapp.R;

/**
 * Created by dalves on 9/21/17.
 */

public class ProdutoHolder extends RecyclerView.ViewHolder {

    public final View rootView;
    public final TextView nomeProd;
    public final TextView descricaoProd;
    public  final ImageView imageView;

    public ProdutoHolder(View view) {
        super(view);
        rootView = view;
        nomeProd =  view.findViewById(R.id.nome_produto);
        descricaoProd = view.findViewById(R.id.desc_prod);
        imageView = view.findViewById(R.id.imagem_prod);
    }
}
