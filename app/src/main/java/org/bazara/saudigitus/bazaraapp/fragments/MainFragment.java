package org.bazara.saudigitus.bazaraapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.claudiodegio.msv.MaterialSearchView;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.CestoActivity;
import org.bazara.saudigitus.bazaraapp.LoginActivity;
import org.bazara.saudigitus.bazaraapp.MainActivity;
import org.bazara.saudigitus.bazaraapp.PedidosActivity;
import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.TokenManager;
import org.bazara.saudigitus.bazaraapp.Utils;
import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoResponse;
import org.bazara.saudigitus.bazaraapp.notificacao.NotificationCountSetClass;
import org.bazara.saudigitus.bazaraapp.recycler.ProdutoAdapter;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment implements OnFragmentInteractionListener,  SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private InterfaceAPI service;
    private TokenManager tokenManager;
    private Call<ProdutoResponse> call;
    private ProdutoResponse produtoResponse;
    private List<Produto> produtosLista;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProdutoAdapter produtoAdapter;
    private ProgressFrameLayout progressActivity;

    public MainFragment() {
        // Required empty public constructor
    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        tokenManager = TokenManager.getInstance(getContext().getSharedPreferences("prefs", MODE_PRIVATE));
        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);


        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);



        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(getContext());
        progressActivity = view.findViewById(R.id.progressActivity);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        produtoAdapter = new ProdutoAdapter(getContext(), produtosLista);
        recyclerView.setAdapter(produtoAdapter);


        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.verifyConnection(getContext())) {
                            getListProdutos();
                        }else {
                            Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                                    .colorRes(R.color.colorAccent);
                            progressActivity.showError(errorDrawable, "Sem Conexão",
                                    "Volte a tentar quando estiver conectado à internet.",
                                    "Tentar novamente", errorClickListener);
                        }
                    }
                }
        );
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getListProdutos();
        }
    };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.main, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_search) {
////            startActivity(new Intent(MainActivity.this, SearchResultActivity.class));
////            return true;
////        }else
//        if (id == R.id.action_cart) {
//
//           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
//            invalidateOptionsMenu();*/
//            startActivity(new Intent(getContext(), CestoActivity.class));
//
//           /* notificationCount=0;//clear notification count
//            invalidateOptionsMenu();*/
//            return true;
//        }else if(id == R.id.action_notifications){
//            startActivity(new Intent(getContext(), PedidosActivity.class));
//            return true;
//        }else if(id == R.id.action_terminar){
//            logout();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
/*    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        NotificationCountSetClass.setAddToCart(getContext(), item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        getActivity().invalidateOptionsMenu();

    }*/


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
                        produtoAdapter = new ProdutoAdapter(getContext(), produtosLista);
                        recyclerView.setAdapter(produtoAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }else{
                        Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_mood_bad)
                                .colorRes(R.color.colorAccent);
                        progressActivity.showEmpty(emptyDrawable, "Não produtos neste momento",
                                "Em breve teremos os produtos que procura.");
                    }
                }else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    swipeRefreshLayout.setRefreshing(false);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<ProdutoResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage() );
                swipeRefreshLayout.setRefreshing(false);
                Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                        .colorRes(R.color.colorAccent);
                progressActivity.showError(errorDrawable, "Erro ao conectar",
                        "Volte a tentar mais tarde.",
                        "Tentar novamente", errorClickListener);
            }
        });

    }


//    private void logout() {
//        tokenManager.logOut();
//        getActivity().finish();
//        startActivity(new Intent(getContext(), LoginActivity.class));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getActivity().invalidateOptionsMenu();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRefresh() {
        getListProdutos();
    }

}
