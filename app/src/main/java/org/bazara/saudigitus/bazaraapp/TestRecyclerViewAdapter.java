package org.bazara.saudigitus.bazaraapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.recycler.PublicacaoHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<PublicacaoHolder> {

    List<Produto> produtosList;
    private Context context;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public TestRecyclerViewAdapter(List<Produto> produtosList, Context context) {
        this.produtosList = produtosList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public int getItemCount() {
        return produtosList != null ? produtosList.size() : 0;
    }

    @Override
    public PublicacaoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_big, parent, false);
                return new PublicacaoHolder(view) ;
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.publi_item, parent, false);
                return new PublicacaoHolder(view);
            }
        }
        return null;
    }


    @Override
    public void onBindViewHolder(PublicacaoHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:{
                holder.tvItem.setText(produtosList.get(0).getNome());
                holder.tvSubItem.setText(produtosList.get(0).getDescricao());
                Glide.with(context)
                        .load(produtosList.get(0).getImagemUrl()). apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.fitCenterTransform()).into(holder.imageView);

                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(), "URL "+position+" "+p.getImagemUrl(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DetalhesActivity.class);
                        intent.putExtra("NOME", produtosList.get(0).getNome());
                        intent.putExtra("URL", produtosList.get(0).getImagemUrl());
                        context.startActivity(intent);
                    }
                });

                holder.btColocarCesto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showInfoDialog();
                    }
                });
            }
                break;
            case TYPE_CELL:{
                holder.tvItem.setText(produtosList.get(position).getNome());
                holder.tvSubItem.setText(produtosList.get(position).getDescricao());
                Glide.with(context)
                        .load(produtosList.get(position).getImagemUrl()). apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions.fitCenterTransform()).into(holder.imageView);

                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getContext(), "URL "+position+" "+p.getImagemUrl(),Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DetalhesActivity.class);
                        intent.putExtra("NOME", produtosList.get(position).getNome());
                        intent.putExtra("URL", produtosList.get(position).getImagemUrl());
                        context.startActivity(intent);
                    }
                });

                holder.btColocarCesto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showInfoDialog();
                    }
                });
//        holder.moreButton.setOnClickListener(view -> updateItem(position));
//        holder.deleteButton.setOnClickListener(view -> removerItem(position));
            }
                break;
        }
    }

    private void showInfoDialog() {
        new MaterialDialog.Builder(context)
                .title(R.string.input)
                .content(R.string.input_content)
                .inputRangeRes(1, 6, R.color.material_red_500)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(R.string.input_hint, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                }).show();
    }
}

