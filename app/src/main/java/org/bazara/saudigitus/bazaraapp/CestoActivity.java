package org.bazara.saudigitus.bazaraapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.models.Cesto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoCesto;
import org.bazara.saudigitus.bazaraapp.recycler.CestoVazioListener;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CestoActivity extends AppCompatActivity implements CestoVazioListener{

    private CestoActivity mContext;
    private TextView btnterminar;
    private ProgressRelativeLayout progressActivity;
    private TextView totalPagar;
    private Toolbar toolbar;
    private InterfaceAPI service;
    private TokenManager tokenManager;
    private String TAG = "CestoActivity";
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesto);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = CestoActivity.this;

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_pedir)
                .content(R.string.please_wait_pedir)
                .progress(true, 0).build();

        List<ProdutoCesto> cartlistImageUri = new CestoUtils().getCartListPublicacao();

        progressActivity = findViewById(R.id.progressActivity);
        btnterminar = findViewById(R.id.btn_terminar);
        totalPagar = findViewById(R.id.valor_total);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);

        btnterminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                efectuarPedido();
            }


        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.setAdapter(new CestoAdapter(cartlistImageUri, CestoActivity.this, this));

        setCartLayout();
        actualizarTotal();

    }

    private void efectuarPedido() {
        dialog.show();
        Cesto c = new Cesto(CestoUtils.cartListPublicacao);
        Call<ResponseBody> cestoService = service.efectuarRequisicao(c);
        cestoService.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    dialog.dismiss();
                    Main2Activity.notificationCountCart = 0;
                    Toast.makeText(getApplicationContext(),
                            "Pedido foi enviado com sucesso", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Erro ao enviar pedido, tente mais tarde", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void cestoVazio() {
        setCartLayout();
        actualizarTotal();
    }

    private void actualizarTotal() {
        CestoUtils cestoUtils = new CestoUtils();
        totalPagar.setText(NumberFormat.getCurrencyInstance(
                new Locale("pt","MZ"))
                .format(cestoUtils.totalPagar()));
        toolbar.setSubtitle(Main2Activity.notificationCountCart+" Itens");
    }


    public static class CestoAdapter
            extends RecyclerView.Adapter<CestoAdapter.ViewHolder> {

        private final Context context;
        private final CestoVazioListener cestoVazioListener;
        private List<ProdutoCesto> mCartlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            private final ImageView mImageView;
            private final TextView nomeProduto;
            private final TextView quantidadePedida;
            private final TextView precoTotal;
            private final View btnRemover;
            private final View btnActualizar;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = view.findViewById(R.id.imagem_prod);
                nomeProduto =  view.findViewById(R.id.nome_produto);
                quantidadePedida = view.findViewById(R.id.quantidade_pedida);
                precoTotal =  view.findViewById(R.id.preco_total);
                btnRemover = view.findViewById(R.id.btn_remover);
                btnActualizar = view.findViewById(R.id.btn_actualizar);
            }
        }

        public CestoAdapter(List<ProdutoCesto> wishlistImageUri, Context context, CestoVazioListener listener) {
            mCartlistImageUri = wishlistImageUri;
            this.context = context;
            this.cestoVazioListener = listener;
        }

        @Override
        public CestoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cesto, parent, false);
            return new CestoAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(CestoAdapter.ViewHolder holder) {
//            if (holder.mImageView.getController() != null) {
//                holder.mImageView.getController().onDetach();
//            }
//            if (holder.mImageView.getTopLevelDrawable() != null) {
//                holder.mImageView.getTopLevelDrawable().setCallback(null);
////                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
//            }
        }

        @Override
        public void onBindViewHolder(final CestoAdapter.ViewHolder holder, final int position) {

            Glide.with(context)
                    .load(mCartlistImageUri.get(position).getPublicacao().getImagem())
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                    .apply(RequestOptions.fitCenterTransform()).into(holder.mImageView);
            holder.nomeProduto.setText(mCartlistImageUri.get(position).getPublicacao().getNomeProduto());
            holder.precoTotal.setText(
                    NumberFormat.getCurrencyInstance(
                            new Locale("pt","MZ"))
                            .format(mCartlistImageUri.get(position).getQuantidade() * mCartlistImageUri.get(position).getPublicacao().getPreco()));
            holder.quantidadePedida.setText("Qtd.: "+ mCartlistImageUri.get(position).getQuantidade()+" "+mCartlistImageUri.get(position).getPublicacao().getUnidade());

            holder.btnActualizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actualizarQuantidade(mCartlistImageUri.get(position).getQuantidade(), position);
                }
            });

            //Set click action
            holder.btnRemover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CestoUtils cestoUtils = new CestoUtils();
                    cestoUtils.removeCartListPublicacao(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    Main2Activity.notificationCountCart--;
                    cestoVazioListener.cestoVazio();

                }
            });

        }

        private void actualizarQuantidade(double quantidade, final int position) {
           MaterialDialog materialDialog =  new MaterialDialog.Builder(context)
                    .title(R.string.input)
                    .content(R.string.input_content)
                    .inputRangeRes(1, 6, R.color.material_red_500)
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .input(R.string.input_hint, 0, new MaterialDialog.InputCallback() {

                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            CestoUtils cestoUtils = new CestoUtils();
                            cestoUtils.updateCartListPublicacao(position, Double.parseDouble(input.toString()));
                            notifyDataSetChanged();
                        }
                    }).show();
            materialDialog.getInputEditText().setText(""+quantidade);
        }

        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }

    public void setCartLayout(){
        LinearLayout layoutCartItems =  findViewById(R.id.layout_items);
        LinearLayout layoutCartPayments = findViewById(R.id.layout_payment);

        if(Main2Activity.notificationCountCart >0){
            progressActivity.showContent();
            layoutCartPayments.setVisibility(View.VISIBLE);
        }else {
            layoutCartPayments.setVisibility(View.GONE);
            Drawable emptyDrawable = new IconDrawable(CestoActivity.this, Iconify.IconValue.zmdi_shopping_basket)
                    .colorRes(R.color.colorAccent);
            progressActivity.showEmpty(emptyDrawable, "Cesto esta vazio",
                    "Adicione produtos ao cesto e faça o seu pedido!");

        }
    }

}
