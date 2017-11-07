package org.bazara.saudigitus.bazaraapp.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.bazara.saudigitus.bazaraapp.ImageUrlUtils;
import org.bazara.saudigitus.bazaraapp.R;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressFrameLayout;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CestoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ProgressFrameLayout progressRelativeLayout;
    private Context mContext;
    private LinearLayout layoutCartItems;
    private LinearLayout layoutCartPayments;
    private TextView buttonTerminar;

    public CestoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CestoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CestoFragment newInstance(String param1, String param2) {
        CestoFragment fragment = new CestoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static CestoFragment newInstance() {
        CestoFragment fragment = new CestoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cesto, container, false);

        progressRelativeLayout = (ProgressFrameLayout) view.findViewById(R.id.progress);

        Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_shopping_basket)
                .colorRes(android.R.color.white);

        Drawable empty = ContextCompat.getDrawable(getContext(), R.drawable.ic_emoticon_sad_white_48dp);

        Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(android.R.color.white);

//        progressRelativeLayout.showEmpty(emptyDrawable, "O Cesto está vazio",
//
//          "Adicione produtos no cesto e faça um pedido.");



        mContext = getContext();

        ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
        ArrayList<String> cartlistImageUri =imageUrlUtils.getCartListImageUri();
        //Show cart layout based on items
        setCartLayout(view);

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager recylerViewLayoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(recylerViewLayoutManager);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(recyclerView, cartlistImageUri));


        buttonTerminar = view.findViewById(R.id.text_action_bottom2);

        buttonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Pedido Enviado!")
                        .setContentText("O seu pedido foi enviado, aguarde pela resposta!")
                        .show();
            }
        });

        return view;
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private ArrayList<String> mCartlistImageUri;
        private RecyclerView mRecyclerView;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImageView;
            public final LinearLayout mLayoutItem, mLayoutRemove , mLayoutEdit;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_cartlist);
                mLayoutItem = (LinearLayout) view.findViewById(R.id.layout_item_desc);
                mLayoutRemove = (LinearLayout) view.findViewById(R.id.layout_action1);
                mLayoutEdit = (LinearLayout) view.findViewById(R.id.layout_action2);
            }
        }

        public SimpleStringRecyclerViewAdapter(RecyclerView recyclerView, ArrayList<String> wishlistImageUri) {
            mCartlistImageUri = wishlistImageUri;
            mRecyclerView = recyclerView;
        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cartlist_item, parent, false);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onViewRecycled(SimpleStringRecyclerViewAdapter.ViewHolder holder) {
//            if (holder.mImageView.getController() != null) {
//                holder.mImageView.getController().onDetach();
//            }
//            if (holder.mImageView.getTopLevelDrawable() != null) {
//                holder.mImageView.getTopLevelDrawable().setCallback(null);
////                ((BitmapDrawable) holder.mImageView.getTopLevelDrawable()).getBitmap().recycle();
//            }
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            final Uri uri = Uri.parse(mCartlistImageUri.get(position));
            holder.mImageView.setImageURI(uri);
            holder.mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ItemDetailsActivity.class);
//                    intent.putExtra(STRING_IMAGE_URI,mCartlistImageUri.get(position));
//                    intent.putExtra(STRING_IMAGE_POSITION, position);
//                    mContext.startActivity(intent);
                }
            });

            //Set click action
            holder.mLayoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageUrlUtils imageUrlUtils = new ImageUrlUtils();
                    imageUrlUtils.removeCartListImageUri(position);
                    notifyDataSetChanged();
                    //Decrease notification count
                    //MainActivity.notificationCountCart--;

                }
            });

            //Set click action
            holder.mLayoutEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCartlistImageUri.size();
        }
    }

    protected void setCartLayout(View view){

        layoutCartItems = view.findViewById(R.id.layout_items);
        layoutCartPayments = view.findViewById(R.id.layout_payment);

        //LinearLayout layoutCartNoItems = (LinearLayout) findViewById(R.id.layout_cart_empty);

        //if(MainActivity.notificationCountCart >0){
        //layoutCartNoItems.setVisibility(View.GONE);
        layoutCartItems.setVisibility(View.VISIBLE);
        layoutCartPayments.setVisibility(View.VISIBLE);
//        }else {
//            layoutCartNoItems.setVisibility(View.VISIBLE);
//            layoutCartItems.setVisibility(View.GONE);
//            layoutCartPayments.setVisibility(View.GONE);
//
//            Button bStartShopping = (Button) findViewById(R.id.bAddNew);
//            bStartShopping.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    finish();
//                }
//            });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



}
