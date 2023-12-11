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

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.AdminProductViewHolder> {

    private final List <Products> mListProducts;
    public final IOnManagerProductListener iOnManagerProductListener;

    public AdminProductAdapter(List<Products> mListProducts, IOnManagerProductListener listener) {
        this.mListProducts = mListProducts;
        this.iOnManagerProductListener = listener;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminProductBinding itemAdminProductBinding = ItemAdminProductBinding
                .inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AdminProductViewHolder(itemAdminProductBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        Products products = mListProducts.get(position);
        if (products == null) {
            return;
        }
        GlideUtils.loadUrl(products.getImage(), holder.mItemAdminProductBinding.imgFood);
        if (products.getSale() <= 0) {
            holder.mItemAdminProductBinding.tvSaleOff.setVisibility(View.GONE);
            holder.mItemAdminProductBinding.tvPrice.setVisibility(View.GONE);

            String strPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemAdminProductBinding.tvPriceSale.setText(strPrice);
        } else {
            holder.mItemAdminProductBinding.tvSaleOff.setVisibility(View.VISIBLE);
            holder.mItemAdminProductBinding.tvPrice.setVisibility(View.VISIBLE);

            String strSale = "Giảm " + products.getSale() + "%";
            holder.mItemAdminProductBinding.tvSaleOff.setText(strSale);

            String strOldPrice = products.getPrice() + Constant.CURRENCY;
            holder.mItemAdminProductBinding.tvPrice.setText(strOldPrice);
            holder.mItemAdminProductBinding.tvPrice.setPaintFlags(holder.mItemAdminProductBinding.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            String strRealPrice = products.getRealPrice() + Constant.CURRENCY;
            holder.mItemAdminProductBinding.tvPriceSale.setText(strRealPrice);
        }
        holder.mItemAdminProductBinding.tvFoodName.setText(products.getName());
        holder.mItemAdminProductBinding.tvFoodDescription.setText(products.getDescription());
        if (products.isPopular()) {
            holder.mItemAdminProductBinding.tvPopular.setText("Có");
        } else {
            holder.mItemAdminProductBinding.tvPopular.setText("Không");
        }

        holder.mItemAdminProductBinding.imgEdit.setOnClickListener(v -> iOnManagerProductListener.onClickUpdateProduct(products));
        holder.mItemAdminProductBinding.imgDelete.setOnClickListener(v -> iOnManagerProductListener.onClickDeleteProduct(products));
    }

    @Override
    public int getItemCount() {
        return null == mListProducts ? 0 : mListProducts.size();
    }

    public static class AdminProductViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminProductBinding mItemAdminProductBinding;

        public AdminProductViewHolder(ItemAdminProductBinding itemAdminProductBinding) {
            super(itemAdminProductBinding.getRoot());
            this.mItemAdminProductBinding = itemAdminProductBinding;
        }
    }
}
