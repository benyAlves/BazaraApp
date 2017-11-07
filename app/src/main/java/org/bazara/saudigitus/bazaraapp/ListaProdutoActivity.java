package org.bazara.saudigitus.bazaraapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import org.bazara.saudigitus.bazaraapp.fragments.BaratosFragment;
import org.bazara.saudigitus.bazaraapp.fragments.RecentesFragment;
import org.bazara.saudigitus.bazaraapp.fragments.RecomendadoFragment;
import org.bazara.saudigitus.bazaraapp.recycler.PubicacaoAdapter;

import butterknife.ButterKnife;

public class ListaProdutoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PubicacaoAdapter adapterProduto;
    private String url;
    private String nomeProduto;

    MaterialViewPager mViewPager;
    private int id;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_produto);
        mViewPager = findViewById(R.id.materialViewPager);
        url = getIntent().getStringExtra("URL");
        id = getIntent().getIntExtra("ID", 0);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


        setTitle("");
        ButterKnife.bind(this);

        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 4) {
                    case 0:
                        return RecentesFragment.newInstance(id);
                    case 1:
                        return RecomendadoFragment.newInstance(id);
                    case 2:
                        return BaratosFragment.newInstance(id);
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 3) {
                    case 0:
                        return "Recentes";
                    case 1:
                        return "Recomendados";
                    case 2:
                        return "Baratos";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.brown,
                                "https://static.pexels.com/photos/36740/vegetables-vegetable-basket-harvest-garden-medium.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://static.pexels.com/photos/89267/pexels-photo-89267-medium.jpeg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.orange,
                                "https://static.pexels.com/photos/208453/pexels-photo-208453-medium.jpeg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Bazara o seu mercado digital", Toast.LENGTH_SHORT).show();
                }
            });
        }


//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//
//        adapterProduto = new PubicacaoAdapter(getMostPopularMoviesList(), this);
//        recyclerView.setAdapter(adapterProduto);

//        // Configurando um dividr entre linhas, para uma melhor visualização.
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));



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



}
