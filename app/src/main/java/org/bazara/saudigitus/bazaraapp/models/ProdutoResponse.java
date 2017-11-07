package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dalves on 10/3/17.
 */

public class ProdutoResponse {


    @SerializedName("data")
    @Expose
    List<Produto> data;

    public void setData(List<Produto> data) {
        this.data = data;
    }

    public List<Produto> getData() {
        return data;
    }
}
