package com.example.da1_e_max.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.databinding.ItemProductGridBinding;
import com.example.da1_e_max.listener.IOnClickFoodItemListener;
import com.example.da1_e_max.model.Products;
import com.example.da1_e_max.utils.GlideUtils;

import java.util.List;


public class ProductGridAdapter extends RecyclerView.Adapter<ProductGridAdapter.FoodGridViewHolder> {

    private final List <Products> mListProducts;
    public final IOnClickFoodItemListener iOnClickFoodItemListener;

    public ProductGridAdapter(List<Products> mListProducts, IOnClickFoodItemListener iOnClickFoodItemListener) {
        this.mListProducts = mListProducts;
        this.iOnClickFoodItemListener = iOnClickFoodItemListener;
    }

    @NonNull
    @Override
    public FoodGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductGridBinding itemFoodGridBinding = ItemProductGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FoodGridViewHolder(itemFoodGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodGridViewHolder holder, int position) {
        Products products = mListProducts.get(position);
        if (products == null) {
            return;
        }
        GlideUtils.loadUrl(products.getImage(), holder.mItemFoodGridBinding.imgFood);
        if (products.getSale() <= 0) {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strPrice);
        } else {
            holder.mItemFoodGridBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemFoodGridBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + products.getSale() + "%";
            holder.mItemFoodGridBinding.tvSaleOff.setText(strSale);

            String strOldPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPrice.setText(strOldPrice);
            holder.mItemFoodGridBinding.tvPrice.setPaintFlags(holder.mItemFoodGridBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = products.getRealPrice() + Constant.CURRENCY;
            holder.mItemFoodGridBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemFoodGridBinding.tvFoodName.setText(products.getName());

        holder.mItemFoodGridBinding.layoutItem.setOnClickListener(v -> iOnClickFoodItemListener.onClickItemFood(products));
    }

    @Override
    public int getItemCount() {
        return null == mListProducts ? 0 : mListProducts.size();
    }

    public static class FoodGridViewHolder extends RecyclerView.ViewHolder {

        private final ItemProductGridBinding mItemFoodGridBinding;

        public FoodGridViewHolder(ItemProductGridBinding itemFoodGridBinding) {
            super(itemFoodGridBinding.getRoot());
            this.mItemFoodGridBinding = itemFoodGridBinding;
        }
    }
}
