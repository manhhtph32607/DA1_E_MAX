package com.example.da1_e_max.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da1_e_max.databinding.ItemProductPopularBinding;
import com.example.da1_e_max.listener.IOnClickFoodItemListener;
import com.example.da1_e_max.model.Products;
import com.example.da1_e_max.utils.GlideUtils;

import java.util.List;


public class ProductPopularAdapter extends RecyclerView.Adapter<ProductPopularAdapter.FoodPopularViewHolder> {

    private final List <Products> mListProducts;
    public final IOnClickFoodItemListener iOnClickFoodItemListener;

    public ProductPopularAdapter(List<Products> mListProducts, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListProducts = mListProducts;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodPopularViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductPopularBinding itemFoodPopularBinding = ItemProductPopularBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodPopularViewHolder(itemFoodPopularBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodPopularViewHolder holder, int position) {
        Products products = mListProducts.get(position);
        if (products == null) {
            return;
        }
        GlideUtils.loadUrlBanner(products.getBanner(), holder.mItemFoodPopularBinding.imageFood);
        if (products.getSale() <= 0) {
            holder.mItemFoodPopularBinding.tvSaleOff.setVisibility(View.GONE);
        } else {
            holder.mItemFoodPopularBinding.tvSaleOff.setVisibility(View.VISIBLE);
            String strSale = "Giảm " + products.getSale() + "%";
            holder.mItemFoodPopularBinding.tvSaleOff.setText(strSale);
        }
        holder.mItemFoodPopularBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItemFood(products));
    }

    @Override
    public int getItemCount() {
        if (mListProducts != null) {
            return mListProducts.size();
        }
        return 0;
    }

    public static class FoodPopularViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductPopularBinding mItemFoodPopularBinding;

        public FoodPopularViewHolder(@NonNull ItemProductPopularBinding itemFoodPopularBinding) {
            super(itemFoodPopularBinding.getRoot());
            this.mItemFoodPopularBinding = itemFoodPopularBinding;
        }
    }
}
