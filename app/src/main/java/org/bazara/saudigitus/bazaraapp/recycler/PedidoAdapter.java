package org.bazara.saudigitus.bazaraapp.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.models.Pedido;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dalves on 10/10/17.
 */

public class PedidoAdapter  extends RecyclerView.Adapter<PedidoHolder> {

    private final List<Pedido> pedidosList;
    private Context context;


    public PedidoAdapter(List<Pedido> pedidosList, Context context) {
        this.pedidosList = pedidosList;
        this.context = context;
    }

    @Override
    public PedidoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PedidoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedido_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PedidoHolder holder, int position) {
        holder.nomeProd.setText(pedidosList.get(position).getNomeProduto());
        holder.dataPedido.setText(pedidosList.get(position).getDataRequisicao());
        holder.precoProduto.setText(NumberFormat.getCurrencyInstance(new Locale("pt","MZ"))
                .format(pedidosList.get(position)
                        .getPreco())+" X "+pedidosList.get(position)
                .getQuantidadeRequisitada()+ " "+pedidosList.get(position).getUnidade());
        holder.nomeAgricultor.setText(pedidosList.get(position).getNomeAgricultor());
        holder.precoTotal.setText(NumberFormat.getCurrencyInstance(new Locale("pt","MZ")).format(pedidosList.get(position).getPreco()*pedidosList.get(position).getQuantidadeRequisitada()));

        Glide.with(context)
                .load(pedidosList.get(position).getImagem())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .apply(RequestOptions.fitCenterTransform()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return  pedidosList != null ? pedidosList.size() : 0;
    }
}
