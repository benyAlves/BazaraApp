package org.bazara.saudigitus.bazaraapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.florent37.materialviewpager.header.MaterialViewPagerHeaderDecorator;
import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.TokenManager;
import org.bazara.saudigitus.bazaraapp.models.Publicacao;
import org.bazara.saudigitus.bazaraapp.models.PublicacaoResponse;
import org.bazara.saudigitus.bazaraapp.recycler.PubicacaoAdapter;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class RecentesFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static final int ITEM_COUNT = 100;
    private static int idProduto;

    RecyclerView mRecyclerView;
    private View view;
    public TokenManager tokenManager;
    private InterfaceAPI service;
    private Call<PublicacaoResponse> call;
    private String TAG="Fragment";
    private List<Publicacao> publicacaoesLista;
    private ProgressBar progressBar;
    private LinearLayout linearVazio;
    private LinearLayout linearFallha;

    public static RecentesFragment newInstance(int id) {
        idProduto = id;
        return new RecentesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recente, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        progressBar =  view.findViewById(R.id.progress_load);
        linearVazio = view.findViewById(R.id.vazio);
        linearFallha = view.findViewById(R.id.falha_rede);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        final List<Object> items = new ArrayList<>();

        for (int i = 0; i < ITEM_COUNT; ++i) {
            items.add(new Object());
        }


        //setup materialviewpager

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);

        //Use this now
        mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        tokenManager = TokenManager.getInstance(getContext().getSharedPreferences("prefs", MODE_PRIVATE));
        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);
        getListPublicacoes(idProduto);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    public void getListPublicacoes(int idProduto){
        progressBar.setVisibility(View.VISIBLE);
        call = service.publicacoesRecentes();
        call.enqueue(new Callback<PublicacaoResponse>() {
            public PubicacaoAdapter publicacaoAdapter;

            @Override
            public void onResponse(Call<PublicacaoResponse> call, Response<PublicacaoResponse> response) {
                Log.w(TAG, "onResponse: " + response );
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){
                    publicacaoesLista = response.body().getData();
                    if(publicacaoesLista.size() != 0) {
                        publicacaoAdapter = new PubicacaoAdapter(publicacaoesLista, getContext());
                        mRecyclerView.setAdapter(publicacaoAdapter);
                        publicacaoAdapter.notifyDataSetChanged();

                    }else{
                        linearVazio.setVisibility(View.VISIBLE);
                    }
                }else {
                      linearFallha.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PublicacaoResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                linearFallha.setVisibility(View.VISIBLE);
                Log.w(TAG, "onFailure: " + t.getMessage() );
            }
        });

    }


}

