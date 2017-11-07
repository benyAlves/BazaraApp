package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.claudiodegio.msv.MaterialSearchView;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.fragments.BazaraFragment;
import org.bazara.saudigitus.bazaraapp.fragments.CestoFragment;
import org.bazara.saudigitus.bazaraapp.fragments.OnFragmentInteractionListener;
import org.bazara.saudigitus.bazaraapp.fragments.PedidosFragment;
import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoResponse;
import org.bazara.saudigitus.bazaraapp.notificacao.NotificationCountSetClass;
import org.bazara.saudigitus.bazaraapp.recycler.ProdutoAdapter;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener,  SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    public static int notificationCountCart = 0;
    private MaterialSearchView searchView;
    private InterfaceAPI service;
    private TokenManager tokenManager;
    private Call<ProdutoResponse> call;
    private ProdutoResponse produtoResponse;
    private List<Produto> produtosLista;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProdutoAdapter produtoAdapter;
    private ProgressRelativeLayout progressActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Produtos");

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);

        searchView = findViewById(R.id.sv);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);



        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(this);
        progressActivity = (ProgressRelativeLayout)findViewById(R.id.progressActivity);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        produtoAdapter = new ProdutoAdapter(this, produtosLista);
        recyclerView.setAdapter(produtoAdapter);


        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.verifyConnection(MainActivity.this)) {
                            getListProdutos();
                        }else {
                            Drawable errorDrawable = new IconDrawable(MainActivity.this, Iconify.IconValue.zmdi_wifi_off)
                                    .colorRes(R.color.colorAccent);
                            progressActivity.showError(errorDrawable, "Sem Conexão",
                                    "Volte a tentar quando estiver conectado à internet.",
                                    "Tentar novamente", errorClickListener);
                        }
                    }
                }
        );

    }


    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getListProdutos();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
//            return true;
//        }else
        if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(MainActivity.this, CestoActivity.class));

           /* notificationCount=0;//clear notification count
            invalidateOptionsMenu();*/
            return true;
        }else if(id == R.id.action_terminar){
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(MainActivity.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }


    public void getListProdutos(){
        swipeRefreshLayout.setRefreshing(true);
        call = service.produtos();
        call.enqueue(new Callback<ProdutoResponse>() {


            @Override
            public void onResponse(Call<ProdutoResponse> call, Response<ProdutoResponse> response) {
                Log.w(TAG, "onResponse: " + response );

                if(response.isSuccessful()){
                    produtosLista = response.body().getData();
                    if(produtosLista.size() != 0) {
                        progressActivity.showContent();
                        produtoAdapter = new ProdutoAdapter(MainActivity.this, produtosLista);
                        recyclerView.setAdapter(produtoAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }else{
                        Drawable emptyDrawable = new IconDrawable(MainActivity.this, Iconify.IconValue.zmdi_mood_bad)
                                .colorRes(R.color.colorAccent);
                        progressActivity.showEmpty(emptyDrawable, "Não produtos neste momento",
                                "Em breve teremos os produtos que procura.");
                    }
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    swipeRefreshLayout.setRefreshing(false);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ProdutoResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void logout() {
        tokenManager.logOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRefresh() {
        getListProdutos();
    }
}
