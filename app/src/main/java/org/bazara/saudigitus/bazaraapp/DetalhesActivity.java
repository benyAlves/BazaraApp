package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.bazara.saudigitus.bazaraapp.models.ProdutoCesto;
import org.bazara.saudigitus.bazaraapp.models.Publicacao;
import org.bazara.saudigitus.bazaraapp.notificacao.NotificationCountSetClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class DetalhesActivity extends AppCompatActivity {

    private ImageView mImageView;
    private TextView textViewBuyNow;
    private TextView textViewAddToCart;
    private String imagemUrl;
    private String nomeProduto;
    private TextView txtNomeProduto;
    private Publicacao publicacao;
    private TextView txtPreco;
    private LinearLayout btnPartilhar;
    private TextView txtOutrosDetalhes;
    private TextView txtAgricultor;
    private TextView txtDetalhe;
    private LinearLayout btnGosto;
    private LinearLayout btnChamada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView = findViewById(R.id.image1);
        textViewAddToCart = findViewById(R.id.text_action_bottom1);
        textViewBuyNow = findViewById(R.id.text_action_bottom2);
        txtNomeProduto = findViewById(R.id.nomeProduto);
        txtPreco = findViewById(R.id.preco_detalhe);
        btnPartilhar = findViewById(R.id.btn_partilhar);
        btnChamada = findViewById(R.id.btn_chamada);
        btnGosto = findViewById(R.id.btn_gosto);
        txtDetalhe = findViewById(R.id.detalhe_produto);
        txtAgricultor = findViewById(R.id.detalhe_agricultor);
        txtOutrosDetalhes = findViewById(R.id.outros_detalhes);


        //Getting image uri from previous screen
        if (getIntent() != null) {
            imagemUrl = getIntent().getStringExtra("URL");
            publicacao = getIntent().getParcelableExtra("PUBLICACAO");
        }


        textViewAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog(publicacao);
            }
        });

        btnPartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Encontre os produtos mais frescos no Bazara. Adira Já!";

                Uri bmpUri = getLocalBitmapUri(mImageView);
                if (bmpUri != null) {


                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(shareIntent, "Partilha Produto Bazara."));
                }
            }
        });


        Glide.with(this)
                .load(imagemUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.placeholder))
                .apply(RequestOptions.fitCenterTransform()).into(mImageView);
        txtNomeProduto.setText(publicacao.getNomeProduto());
        txtAgricultor.setText(getString(R.string.associacao_agricultor)+publicacao.getNome());
        txtDetalhe.setText(R.string.detalhes_produto);
        txtPreco.setText(NumberFormat.getCurrencyInstance(new Locale("pt","MZ")).format(publicacao.getPreco())+"/"+publicacao.getUnidade());
        //txtOutrosDetalhes.setText();
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


    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void showInfoDialog(final Publicacao p) {
        new MaterialDialog.Builder(this)
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
                        produtoCesto.setPublicacao(p);
                        imageUrlUtils.addCartListProduto(produtoCesto);
                        Toast.makeText(getApplicationContext(), "Produto foi adicionado no cesto",Toast.LENGTH_SHORT).show();
                        Main2Activity.notificationCountCart++;
                        NotificationCountSetClass.setNotifyCount(Main2Activity.notificationCountCart);
                    }
                }).show();
    }

}
