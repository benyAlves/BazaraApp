package org.bazara.saudigitus.bazaraapp.recycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bazara.saudigitus.bazaraapp.CestoUtils;
import org.bazara.saudigitus.bazaraapp.DetalhesActivity;
import org.bazara.saudigitus.bazaraapp.Main2Activity;
import org.bazara.saudigitus.bazaraapp.MainActivity;
import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoCesto;
import org.bazara.saudigitus.bazaraapp.models.Publicacao;
import org.bazara.saudigitus.bazaraapp.notificacao.NotificationCountSetClass;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by dalves on 9/19/17.
 */

public class PubicacaoAdapter extends RecyclerView.Adapter<PublicacaoHolder> {

    private final List<Publicacao> produtosList;
    private Context context;

    public PubicacaoAdapter(List<Publicacao> produtosList, Context context) {
        this.produtosList = produtosList;
        this.context = context;
    }

    @Override
    public PublicacaoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PublicacaoHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.publi_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final PublicacaoHolder holder, final int position) {

        holder.tvItem.setText(produtosList.get(position).getNomeProduto());
        holder.tvPreco.setText(NumberFormat.getCurrencyInstance(new Locale("pt","MZ")).format(produtosList.get(position).getPreco())+"/"+produtosList.get(position).getUnidade());
        holder.nomeProdutor.setText(produtosList.get(position).getNome());

        Glide.with(context)
                .load(produtosList.get(position).getImagem())
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .apply(RequestOptions.fitCenterTransform()).into(holder.imageView);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetalhesActivity.class);
                intent.putExtra("PUBLICACAO", produtosList.get(position));
                intent.putExtra("URL", produtosList.get(position).getImagem());
                context.startActivity(intent);
            }
        });

        holder.btColocarCesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(position);

            }
        });
    }

    private void showInfoDialog(final int position) {
        new MaterialDialog.Builder(context)
                .title(R.string.input)
                .content(R.string.input_content)
                .inputRangeRes(1, 6, R.color.material_red_500)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(R.string.input_hint, 0, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        CestoUtils imageUrlUtils = new CestoUtils();
                        ProdutoCesto produtoCesto = new ProdutoCesto();
                        produtoCesto.setQuantidade(Double.parseDouble(input.toString()));
                        produtoCesto.setPublicacao(produtosList.get(position));
                        imageUrlUtils.addCartListProduto(produtoCesto);
                        Toast.makeText(context, "Produto foi adicionado no cesto",Toast.LENGTH_SHORT).show();
                        Main2Activity.notificationCountCart++;
                        NotificationCountSetClass.setNotifyCount(Main2Activity.notificationCountCart);
                    }
                }).show();
    }

    @Override
    public int getItemCount() {
        return produtosList != null ? produtosList.size() : 0;
    }
}
