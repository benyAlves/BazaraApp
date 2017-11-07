package org.bazara.saudigitus.bazaraapp;

import android.content.SharedPreferences;

import org.bazara.saudigitus.bazaraapp.models.AccessToken;

/**
 * Created by dalves on 9/30/17.
 */

public class TokenManager    {
    private static TokenManager INSTANCE = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private AccessToken token;


    private TokenManager(SharedPreferences prefs){
        this.prefs = prefs;
        this.editor = prefs.edit();
    }


    public static synchronized  TokenManager getInstance(SharedPreferences prefs){
        if (INSTANCE == null){
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }


    public void  saveToken(AccessToken token){
        editor.putString("ACCESS_TOKEN", token.getAccessToken()).commit();
        editor.putString("REFRESH_TOKEN", token.getRefreshToken()).commit();
    }


    public void  saveProfile(String nome, String numero){
        editor.putString("NOME", nome).commit();
        editor.putString("NUMERO", numero).commit();
    }


    public void deleteToken(){
        editor.remove("ACCESS_TOKEN").commit();
        editor.remove("REFRESH_TOKEN").commit();
        token = null;
    }


    public AccessToken getToken(){
        token = new AccessToken();
        token.setAccessToken(prefs.getString("ACCESS_TOKEN", null));
        token.setRefreshToken(prefs.getString("REFRESH_TOKEN", null));
        return  token;
    }


    public String getNomeProfile(){
        return prefs.getString("NOME", null);
    }

    public String getNumeroProfile(){
        return prefs.getString("NUMERO", null);
    }

    public void logOut(){
        deleteToken();
    }
}
