package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dalves on 10/10/17.
 */

public class RequisicaoResponse {

    @SerializedName("data")
    @Expose
    List<Pedido> data;

    public List<Pedido> getData() {
        return data;
    }

    public void setData(List<Pedido> data) {
        this.data = data;
    }
}
