package org.bazara.saudigitus.bazaraapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.vlonjatg.progressactivity.ProgressFrameLayout;
import com.vlonjatg.progressactivity.ProgressRelativeLayout;

import org.bazara.saudigitus.bazaraapp.ImageUrlUtils;
import org.bazara.saudigitus.bazaraapp.ListaProdutoActivity;
import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.models.Produto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


public class BazaraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String STRING_IMAGE_URI = "ImageUri";
    public static final String STRING_IMAGE_POSITION = "ImagePosition";


    private String mParam1;
    private String mParam2;
    String[] items=null;

    private OnFragmentInteractionListener mListener;
    private ProgressFrameLayout progressRelativeLayout;
    private RecyclerView recyclerView;
    private SectionedRecyclerViewAdapter sectionAdapter;

    public BazaraFragment() {
        // Required empty public constructor
    }


    public static BazaraFragment newInstance(String param1, String param2) {
        BazaraFragment fragment = new BazaraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static BazaraFragment newInstance() {
        BazaraFragment fragment = new BazaraFragment();
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

        View f = inflater.inflate(R.layout.fragment_bazara, container, false);

        recyclerView = (RecyclerView)f.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

//        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);


        items = ImageUrlUtils.getTop();

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new ProdutoSection("Top", getTopRatedMoviesList()));
        sectionAdapter.addSection(new ProdutoSection("Populares", getMostPopularMoviesList()));

        GridLayoutManager glm = new GridLayoutManager(getContext(), 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });


        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);


        progressRelativeLayout = (ProgressFrameLayout) f.findViewById(R.id.progress);
        progressRelativeLayout.showContent();

        Drawable emptyDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_shopping_basket)
                .colorRes(android.R.color.white);

        Drawable empty = ContextCompat.getDrawable(getContext(), R.drawable.ic_emoticon_sad_white_48dp);

        Drawable errorDrawable = new IconDrawable(getContext(), Iconify.IconValue.zmdi_wifi_off)
                .colorRes(android.R.color.white);



//        progressRelativeLayout.showEmpty(empty, "O Bazara está vazio",
//                "Olá,em breve teremos a mercadoria que precisa.");

        return f;
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


    private List<Produto> getTopRatedMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.top_rated_movies)));

        List<Produto> movieList = new ArrayList<>();

//        for (int i = 0; i < arrayList.size(); i++) {
//            String str = arrayList.get(i);
//            String[] array = str.split("\\|");
//            movieList.add(new Produto(array[0], array[1],  items[i]));
//        }

        return movieList;
    }

    private List<Produto> getMostPopularMoviesList() {
        List<String> arrayList = new ArrayList<>(Arrays.asList(getResources()
                .getStringArray(R.array.most_popular_movies)));

        List<Produto> movieList = new ArrayList<>();

//        for (int i = 0; i < arrayList.size(); i++) {
//            String str = arrayList.get(i);
//            String[] array = str.split("\\|");
//            movieList.add(new Produto(array[0], array[1], ImageUrlUtils.getImageUrls()[i]));
//        }

        return movieList;
    }


    private class ProdutoSection extends StatelessSection {

        String title;
        List<Produto> list;
        private Produto p;

        ProdutoSection(String title, List<Produto> list) {
            super(new SectionParameters.Builder(R.layout.section_item)
                    .headerResourceId(R.layout.section_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

          p = list.get(position);


            itemHolder.tvItem.setText(p.getNome());
            itemHolder.tvSubItem.setText(p.getDescricao());

                    Glide.with(getContext())
                    .load(p.getImagemUrl()).
                            apply(RequestOptions.centerCropTransform())
                            .apply(RequestOptions.fitCenterTransform()).into(itemHolder.imageView);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "URL "+position+" "+p.getImagemUrl(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ListaProdutoActivity.class);
                    intent.putExtra("NOME", list.get(position).getNome());
                    intent.putExtra("URL", list.get(position).getImagemUrl());
                    startActivity(intent);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);

            headerHolder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), String.format("Clicked on more button from the header of Section %s",
//                            title),
//                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ListaProdutoActivity.class);
                    intent.putExtra("TITULO", title);
                    startActivity(intent);
                }
            });
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;
        private final Button btnMore;

        HeaderViewHolder(View view) {
            super(view);

            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            btnMore = (Button) view.findViewById(R.id.btnMore);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView tvItem;
        private final TextView tvSubItem;
        private final ImageView imageView;

        ItemViewHolder(View view) {
            super(view);

            rootView = view;
            tvItem = (TextView) view.findViewById(R.id.tvItem);
            tvSubItem = (TextView) view.findViewById(R.id.tvSubItem);
            imageView = (ImageView) view.findViewById(R.id.imgItem);
        }
    }


}
