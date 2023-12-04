package com.example.da1_e_max.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.databinding.ItemAdminProductBinding;
import com.example.da1_e_max.listener.IOnManagerProductListener;
import com.example.da1_e_max.model.Products;
import com.example.da1_e_max.utils.GlideUtils;

import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.AdminFoodViewHolder> {

    private final List <Products> mListProducts;
    public final IOnManagerProductListener iOnManagerProductListener;

    public AdminProductAdapter(List<Products> mListProducts, IOnManagerProductListener listener) {
        this.mListProducts = mListProducts;
        this.iOnManagerProductListener = listener;
    }

    @NonNull
    @Override
    public AdminFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminProductBinding itemAdminFoodBinding = ItemAdminProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminFoodViewHolder(itemAdminFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFoodViewHolder holder, int position) {
        Products products = mListProducts.get(position);
        if (products == null) {
            return;
        }
        GlideUtils.loadUrl(products.getImage(), holder.mItemAdminFoodBinding.imgFood);
        if (products.getSale() <= 0) {
            holder.mItemAdminFoodBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemAdminFoodBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPriceSale.setText(strPrice);
        } else {
            holder.mItemAdminFoodBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemAdminFoodBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + products.getSale() + "%";
            holder.mItemAdminFoodBinding.tvSaleOff.setText(strSale);

            String strOldPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPrice.setText(strOldPrice);
            holder.mItemAdminFoodBinding.tvPrice.setPaintFlags(holder.mItemAdminFoodBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = products.getRealPrice() + Constant.CURRENCY;
            holder.mItemAdminFoodBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemAdminFoodBinding.tvFoodName.setText(products.getName());
        holder.mItemAdminFoodBinding.tvFoodDescription.setText(products.getDescription());
        if (products.isPopular()) {
            holder.mItemAdminFoodBinding.tvPopular.setText("Có");
        } else {
            holder.mItemAdminFoodBinding.tvPopular.setText("Không");
        }

        holder.mItemAdminFoodBinding.imgEdit.setOnClickListener(v -> iOnManagerProductListener.onClickUpdateProduct(products));
        holder.mItemAdminFoodBinding.imgDelete.setOnClickListener(v -> iOnManagerProductListener.onClickDeleteProduct(products));
    }

    @Override
    public int getItemCount() {
        return null == mListProducts ? 0 : mListProducts.size();
    }

    public static class AdminFoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminProductBinding mItemAdminFoodBinding;

        public AdminFoodViewHolder(ItemAdminProductBinding itemAdminFoodBinding) {
            super(itemAdminFoodBinding.getRoot());
            this.mItemAdminFoodBinding = itemAdminFoodBinding;
        }
    }
}
