package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dalves on 10/3/17.
 */

public class PublicacaoResponse {

    @SerializedName("data")
    @Expose
    List<Publicacao> data;

    public List<Publicacao> getData() {
        return data;
    }

    public void setData(List<Publicacao> data) {
        this.data = data;
    }
}
