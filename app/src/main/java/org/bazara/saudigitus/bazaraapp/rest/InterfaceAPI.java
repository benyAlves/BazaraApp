package org.bazara.saudigitus.bazaraapp.rest;

import org.bazara.saudigitus.bazaraapp.models.AccessToken;
import org.bazara.saudigitus.bazaraapp.models.Cesto;
import org.bazara.saudigitus.bazaraapp.models.Produto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoCesto;
import org.bazara.saudigitus.bazaraapp.models.ProdutoResponse;
import org.bazara.saudigitus.bazaraapp.models.PublicacaoResponse;
import org.bazara.saudigitus.bazaraapp.models.RequisicaoResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dalves on 9/12/17.
 */

public interface InterfaceAPI {


     @POST("registo")
     @FormUrlEncoded
     Call<AccessToken> registarUser(@Field("nome") String nome,
                                    @Field("email") String email,
                                    @Field("telefone") String telefone,
                                    @Field("password") String password);


     @POST("login")
     @FormUrlEncoded
     Call<AccessToken> login(@Field("telefone") String telefone,
                             @Field("password") String password);

     @POST("refresh")
     @FormUrlEncoded
     Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);


    @POST("requisicao")
    Call<ResponseBody> efectuarRequisicao (@Body Cesto cesto);

    @GET("produtos")
    Call<ProdutoResponse> produtos();


    @POST("logout")
    Call<ResponseBody> logOut();

    @GET("recomendados/publicacoes")
    Call<PublicacaoResponse> publicacoes();

    @GET("recentes/publicacoes")
    Call<PublicacaoResponse> publicacoesRecentes();

    @GET("baratos/publicacoes")
    Call<PublicacaoResponse> publicacoesBaratas();

    @GET("requisicao/cliente/{id}")
    Call<RequisicaoResponse> requisicaoesCliente(@Path("id") int id);

//
//    @POST("/api/users")
//    Call<User> createUser(@Body User user);
//
   // @GET("apiGetDisponibilidadeHora/{id}")
//    @GET("/api/users?")
//    Call<UserList> doGetUserList(@Query("page") String page);
//
//    @FormUrlEncoded
//    @POST("/api/users?")
//    Call<UserList> doCreateUserWithField(@Field("name") String name, @Field("job") String job);
}
