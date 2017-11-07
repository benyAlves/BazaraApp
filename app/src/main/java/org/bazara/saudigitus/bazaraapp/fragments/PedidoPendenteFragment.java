package org.bazara.saudigitus.bazaraapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.CestoActivity;
import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.TokenManager;
import org.bazara.saudigitus.bazaraapp.Utils;
import org.bazara.saudigitus.bazaraapp.models.Pedido;
import org.bazara.saudigitus.bazaraapp.models.RequisicaoResponse;
import org.bazara.saudigitus.bazaraapp.recycler.PedidoAdapter;
import org.bazara.saudigitus.bazaraapp.recycler.ProdutoAdapter;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PedidoPendenteFragment extends Fragment{
    private static final String TAG = "PedidoPendenteFragment";
    private View rootView;
    private TokenManager tokenManager;
    private InterfaceAPI service;
    private List<Pedido> listaPedidos;
    private ProgressRelativeLayout progressActivity;
    private Call<RequisicaoResponse> call;
    private RecyclerView recyclerView;


    public PedidoPendenteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PedidoPendenteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PedidoPendenteFragment newInstance(String param1, String param2) {
        PedidoPendenteFragment fragment = new PedidoPendenteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pedido_pendente, container, false);


        progressActivity = rootView.findViewById(R.id.progressActivity);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);

        listaPedidos = new ArrayList<>();

       recyclerView = rootView.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(recylerViewLayoutManager);

         carregarPedidos(2);

        return rootView;
    }



    private void carregarPedidos(int id) {
        if (Utils.verifyConnection(getContext())) {
            progressActivity.showLoading();
            call = service.requisicaoesCliente(id);
            call.enqueue(new Callback<RequisicaoResponse>() {
                @Override
                public void onResponse(Call<RequisicaoResponse> call, Response<RequisicaoResponse> response) {
                    if(response.isSuccessful()){
                        listaPedidos = response.body().getData();
                        if(listaPedidos.size() != 0) {
                            progressActivity.showContent();
                            recyclerView.setAdapter(new PedidoAdapter(listaPedidos, getContext()));
                        }else{
                            Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_mood_bad)
                                    .colorRes(R.color.colorAccent);
                            progressActivity.showEmpty(emptyDrawable, "Sem Pedidos Pendentes",
                                    "Não possui pedidos pendentes neste momento...");
                        }
                    }
                }

                @Override
                public void onFailure(Call<RequisicaoResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                            .colorRes(R.color.colorAccent);
                    progressActivity.showError(errorDrawable, "Erro ao conectar",
                            "Volte a tentar mais tarde.",
                            "Tentar novamente", errorClickListener);
                }
            });
        }else{
            Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                    .colorRes(R.color.colorAccent);
            progressActivity.showError(errorDrawable, "Sem Conexão",
                    "Volte a tentar quando estiver conectado à internet.",
                    "Tentar novamente", errorClickListener);
        }
    }

    private View.OnClickListener errorClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            carregarPedidos(2);
        }
    };
//
//    public ArrayList<Requisicao> getRequisicoesList() {
//        return requisicoesArrayList;
//    }
//
//    public void setRequisicoesList(ArrayList<Requisicao> requisicoesArrayList) {
//        this.requisicoesArrayList = requisicoesArrayList;
//    }
//
//    @Override
//    public void onSendMessageResult(String s, String numero, int id) {
//        Intent intent = new Intent(getContext(), SendSMSActivity.class);
//        intent.putExtra("CLIENTE_NOME", s);
//        intent.putExtra("CLIENTE_NUMERO", numero);
//        intent.putExtra("ID_REQUISICAO", id);
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            carregarPedidos();
//        }
//    }
//
//    @Override
//    public void onCallResult() {
//
//    }
}
