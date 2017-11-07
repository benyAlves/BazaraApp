package org.bazara.saudigitus.bazaraapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.bazara.saudigitus.bazaraapp.R;


public class PedidoRespondidoFragment extends Fragment {


    private View rootView;
    //private RequisicoesAdapter requisicaoAdapter;
    private RecyclerView recyclerView;
    //private List<Requisicao> listRequisicoes;
    private LinearLayout linearLayoutConectionMesg;
    private TextView textInfoo;
    private TextView conexaoTexto;
    private Button btnRecarregar;
//    private ArrayList<Requisicao> requisicoesArrayList;
//    private User user;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private ProgressBar progressBar;

    public PedidoRespondidoFragment() {
        // Required empty public constructor
    }



    public static PedidoRespondidoFragment newInstance(String param1, String param2) {
        PedidoRespondidoFragment fragment = new PedidoRespondidoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//
//        sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//
//        if (savedInstanceState != null) {
//            user = savedInstanceState.getParcelable("USER");
//        } else {
//            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getParcelable("USER") != null) {
//                user = getActivity().getIntent().getExtras().getParcelable("USER");
//            } else {
////                Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
////                finish();
//            }
//        }
        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//             // listRequisicoes = (ArrayList<Requisicao>)getArguments().getSerializable("REQUISICOES");
////            mParam2 = getArguments().getString(ARG_PARAM2);
//          //  Log.e("LIL", listRequisicoes.get(0)+" lista");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pedido_respondido, container, false);

//        progressBar = (ProgressBar) rootView.findViewById(R.id.app_progressbar);
//        linearLayoutConectionMesg = (LinearLayout) rootView.findViewById(R.id.conectionErrorMsg);
//        textInfoo = (TextView) rootView.findViewById(R.id.textInfo);
//        conexaoTexto = (TextView) rootView.findViewById(R.id.conexaoText);
//        btnRecarregar = (Button) rootView.findViewById(R.id.btnRecarregar);
//
//       // requisicaoAdapter = new RequisicoesAdapter(getContext(), listRequisicoes, "R");
//        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        //recyclerView.setAdapter(requisicaoAdapter);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setReverseLayout(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        carregarPedidos();

        return rootView;
    }

//    private void carregarPedidos() {
//        if (UtilWZ.verifyConnection(getContext())) {
//            progressBar.setVisibility(View.VISIBLE);
//            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//            Call<ArrayList<Requisicao>> call = apiService.getRequisicoesRespondidas(sharedpreferences.getInt("ID_ASSOCIACAO", 0));
//            call.enqueue(new Callback<ArrayList<Requisicao>>() {
//                @Override
//                public void onResponse(Call<ArrayList<Requisicao>> call, Response<ArrayList<Requisicao>> response) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    requisicoesArrayList = response.body();
//                    requisicaoAdapter = new RequisicoesAdapter(getContext(), requisicoesArrayList, "R", PedidoRespondidoFragment.this);
//                    recyclerView.setAdapter(requisicaoAdapter);
//                    if(requisicoesArrayList.size() == 0){
//                        textInfoo.setVisibility(View.VISIBLE);
//                    }
//                }
//                @Override
//                public void onFailure(Call<ArrayList<Requisicao>> call, Throwable t) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    conexaoTexto.setText("Ocorreu um erro ao carregar");
//                    linearLayoutConectionMesg.setVisibility(View.GONE);
//                }
//            });
//        }else{
//            progressBar.setVisibility(View.INVISIBLE);
//            linearLayoutConectionMesg.setVisibility(View.VISIBLE);
//        }
//    }
//
//
////    private List<Requisicao> listRequisicoes() {
////        List<Requisicao> listPub = new ArrayList<>();
////        listPub.add(new Requisicao(100, "Bernardo Alves", 30, "Cenoura", R.drawable.cenoura));
////        listPub.add(new Requisicao(780, "Matilde Bonifacio",100, "Hortalica", R.drawable.hortalica));
////        listPub.add(new Requisicao(450, "Zaira Nazareth", 100, "Couve Flor",R.drawable.couve));
////        listPub.add(new Requisicao(500, "Cristina de Jesus",230, "Repolho", R.drawable.repolho));
////        return listPub;
////    }
//
//    @Override
//    public void onSendMessageResult(String s, String numero, int id) {
//
//    }
//
//    @Override
//    public void onCallResult() {
//
//    }
}
