package org.bazara.saudigitus.bazaraapp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.claudiodegio.msv.MaterialSearchView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import org.bazara.saudigitus.bazaraapp.fragments.MainFragment;
import org.bazara.saudigitus.bazaraapp.notificacao.NotificationCountSetClass;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;
import org.bazara.saudigitus.bazaraapp.rest.InterfaceAPI;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main2Activity";
    public static int notificationCountCart = 0;
    private MaterialSearchView searchView;
    private TokenManager tokenManager;
    InterfaceAPI service;
    private Call<ResponseBody> call;
    private MaterialDialog dialog;
    private SearchBox search;
    private Toolbar  toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        search = (SearchBox) findViewById(R.id.searchbox);

       toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.w(TAG, "Token: " + token);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                openSearch();
//                return true;
//            }
//        });




        dialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_terminar)
                .content(R.string.please_wait_terminar)
                .progress(true, 0).build();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        service = ClientAPI.createServiceWithAuth(InterfaceAPI.class, tokenManager);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        TextView numeroPerfil = hView.findViewById(R.id.numero_profile);
        TextView nomePerfil = hView.findViewById(R.id.nome_profile);

        nomePerfil.setText(tokenManager.getNomeProfile());
        numeroPerfil.setText(tokenManager.getNumeroProfile());

        displaySelectedScreen(R.id.nav_produtos);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            openSearch();
            return true;
        }else
        if (id == R.id.action_cart) {

           /* NotificationCountSetClass.setAddToCart(MainActivity.this, item, notificationCount);
            invalidateOptionsMenu();*/
            startActivity(new Intent(Main2Activity.this, CestoActivity.class));

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
        NotificationCountSetClass.setAddToCart(Main2Activity.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }



    private void logout() {
        dialog.show();
        call = service.logOut();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.w(TAG, "onResponse: " + response);
                dialog.dismiss();
                if (response.isSuccessful()) {
                    tokenManager.logOut();
                    finish();
                    startActivity(new Intent(Main2Activity.this, LoginActivity.class));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                dialog.dismiss();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }


    private void displaySelectedScreen(int id) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (id) {
            case R.id.nav_produtos:
                fragment = new MainFragment();
                break;
            case R.id.nav_notificacao:
                startActivity(new Intent(Main2Activity.this, PedidosActivity.class));
                break;
            case R.id.nav_cesto:
                startActivity(new Intent(Main2Activity.this, CestoActivity.class));
                break;
            case R.id.nav_perfil:
                startActivity(new Intent(Main2Activity.this, SettingsActivity.class));
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void openSearch() {
        toolbar.setTitle("");
        search.revealFromMenuItem(R.id.action_search, this);
//        for (int x = 0; x < 10; x++) {
//            SearchResult option = new SearchResult("Result "
//                    + Integer.toString(x), getResources().getDrawable(
//                    R.drawable.ic_history));
//            search.addSearchable(option);
//        }
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                // Hamburger has been clicked
                Toast.makeText(Main2Activity.this, "Menu click",
                        Toast.LENGTH_LONG).show();
            }

        });
        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                // Use this to tint the screen

            }

            @Override
            public void onSearchClosed() {
                // Use this to un-tint the screen
                closeSearch();
            }

            @Override
            public void onSearchTermChanged(String term) {
                // React to the search term changing
                // Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(Main2Activity.this, searchTerm + " Searched",
                        Toast.LENGTH_LONG).show();
                toolbar.setTitle(searchTerm);

            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to result being clicked
            }

            @Override
            public void onSearchCleared() {

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            search.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void closeSearch() {
        search.hideCircularly(this);
        if(search.getSearchText().isEmpty())toolbar.setTitle("");
    }
}
