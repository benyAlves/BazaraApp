package org.bazara.saudigitus.bazaraapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cesto{

  @SerializedName("produtoCestos")
  public List<ProdutoCesto> produtoCestos;

  public Cesto(List<ProdutoCesto> produtoCestos) {
    this.produtoCestos = produtoCestos;
  }

  public List<ProdutoCesto> getProdutoCestos() {
    return produtoCestos;
  }

  public void setProdutoCestos(List<ProdutoCesto> produtoCestos) {
    this.produtoCestos = produtoCestos;
  }
}