package org.bazara.saudigitus.bazaraapp.recycler;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import org.bazara.saudigitus.bazaraapp.DetalhesActivity;
import org.bazara.saudigitus.bazaraapp.ListaProdutoActivity;
import org.bazara.saudigitus.bazaraapp.R;
import org.bazara.saudigitus.bazaraapp.models.Produto;

import java.util.List;

import static org.bazara.saudigitus.bazaraapp.R.id.container;

/**
 * Created by dalves on 9/21/17.
 */

public class ProdutoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public final int TYPE_PRODUTO = 0;
    public final int TYPE_LOAD = 1;
    List<Produto> dataList;
    String letter;
    Context context;
    ColorGenerator generator = ColorGenerator.MATERIAL;
   // OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public ProdutoAdapter(Context context, List<Produto> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       // if(viewType==TYPE_PRODUTO){
            return new ProdutoHolder(inflater
                    .inflate(R.layout.product_item, parent, false));
//        }else{
//            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
//        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        if(dataList.get(position) instanceof Produto){
//            return TYPE_PRODUTO;
//        }else{
//            return TYPE_LOAD;
//        }
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
//            isLoading = true;
//            loadMoreListener.onLoadMore();
//        }

       // if(getItemViewType(position)==TYPE_PRODUTO) {
            ProdutoHolder holderProduto = ((ProdutoHolder) holder);

            holderProduto.nomeProd.setText(dataList.get(position).getNome());
            holderProduto.descricaoProd.setText(dataList.get(position).getDescricao());
            letter = String.valueOf(dataList.get(position).getNome().charAt(0));
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            holderProduto.imageView.setImageDrawable(drawable);
            holderProduto.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "URL "+position+" "+p.getImagemUrl(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, ListaProdutoActivity.class);
                    intent.putExtra("ID", dataList.get(position).getId());
                    context.startActivity(intent);
                }
            });
       // }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }


//    static class LoadHolder extends RecyclerView.ViewHolder{
//        public LoadHolder(View itemView) {
//            super(itemView);
//        }
//    }
//
//    public void setMoreDataAvailable(boolean moreDataAvailable) {
//        isMoreDataAvailable = moreDataAvailable;
//    }
//
//    /* notifyDataSetChanged is final method so we can't override it
//         call adapter.notifyDataChanged(); after update the list
//         */
//    public void notifyDataChanged(){
//        notifyDataSetChanged();
//        isLoading = false;
//    }
//
//    public interface OnLoadMoreListener{
//        void onLoadMore();
//    }
//
//    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
//        this.loadMoreListener = loadMoreListener;
//    }
}
