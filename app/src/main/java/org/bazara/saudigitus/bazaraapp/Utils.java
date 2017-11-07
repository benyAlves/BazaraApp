package org.bazara.saudigitus.bazaraapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.afollestad.materialdialogs.MaterialDialog;

import org.bazara.saudigitus.bazaraapp.models.ApiError;
import org.bazara.saudigitus.bazaraapp.rest.ClientAPI;

import java.io.IOException;
import java.lang.annotation.Annotation;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by dalves on 9/29/17.
 */

public class Utils {
    private static MaterialDialog dialog;
    private static SweetAlertDialog pDialog;

    public static ApiError convertErrors(ResponseBody response){
        Converter<ResponseBody, ApiError> converter = ClientAPI.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);
        ApiError apiError = null;
        try {
            apiError = converter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  apiError;
    }

    public static boolean verifyConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnected()){
            return true;
        }
        else{
            return false;
        }
    }


    public static void showDialogProcess(String conteudo, Context context){
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("A Processar");
        pDialog.setContentText(conteudo);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void dismiss(){
        pDialog.dismiss();
    }

}
