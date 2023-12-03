package com.example.da1_e_max.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.da1_e_max.ControllerApplication;
import com.example.da1_e_max.R;
import com.example.da1_e_max.activity.MainActivity;
import com.example.da1_e_max.adapter.CartAdapter;
import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.constant.GlobalFunction;
import com.example.da1_e_max.database.ProductDatabase;
import com.example.da1_e_max.databinding.FragmentCartBinding;
import com.example.da1_e_max.event.ReloadListCartEvent;
import com.example.da1_e_max.model.Products;
import com.example.da1_e_max.model.Order;
import com.example.da1_e_max.prefs.DataStoreManager;
import com.example.da1_e_max.utils.StringUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends BaseFragment {

    private FragmentCartBinding mFragmentCartBinding;
    private CartAdapter mCartAdapter;
    private List<Products> mListProductsCart;
    private int mAmount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        displayListFoodInCart();
        mFragmentCartBinding.tvOrderCart.setOnClickListener(v -> onClickOrderCart());

        return mFragmentCartBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.cart));
        }
    }

    private void displayListFoodInCart() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentCartBinding.rcvFoodCart.setLayoutManager(linearLayoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mFragmentCartBinding.rcvFoodCart.addItemDecoration(itemDecoration);

        initDataFoodCart();
    }

    private void initDataFoodCart() {
        mListProductsCart = new ArrayList<>();
        mListProductsCart = ProductDatabase.getInstance(getActivity()).productDAO().getListFoodCart();
        if (mListProductsCart == null || mListProductsCart.isEmpty()) {
            return;
        }

        mCartAdapter = new CartAdapter(mListProductsCart, new CartAdapter.IClickListener() {
            @Override
            public void clickDeteteFood(Products products, int position) {
                deleteFoodFromCart(products, position);
            }

            @Override
            public void updateItemFood(Products products, int position) {
                ProductDatabase.getInstance(getActivity()).productDAO().updateProduct(products);
                mCartAdapter.notifyItemChanged(position);

                calculateTotalPrice();
            }
        });
        mFragmentCartBinding.rcvFoodCart.setAdapter(mCartAdapter);

        calculateTotalPrice();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void clearCart() {
        if (mListProductsCart != null) {
            mListProductsCart.clear();
        }
        mCartAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        List<Products> listProductsCart = ProductDatabase.getInstance(getActivity()).productDAO().getListFoodCart();
        if (listProductsCart == null || listProductsCart.isEmpty()) {
            String strZero = 0 + Constant.CURRENCY;
            mFragmentCartBinding.tvTotalPrice.setText(strZero);
            mAmount = 0;
            return;
        }

        int totalPrice = 0;
        for (Products products : listProductsCart) {
            totalPrice = totalPrice + products.getTotalPrice();
        }

        String strTotalPrice = totalPrice + Constant.CURRENCY;
        mFragmentCartBinding.tvTotalPrice.setText(strTotalPrice);
        mAmount = totalPrice;
    }

    private void deleteFoodFromCart(Products products, int position) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.confirm_delete_food))
                .setMessage(getString(R.string.message_delete_food))
                .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                    ProductDatabase.getInstance(getActivity()).productDAO().deleteProduct(products);
                    mListProductsCart.remove(position);
                    mCartAdapter.notifyItemRemoved(position);

                    calculateTotalPrice();
                })
                .setNegativeButton(getString(R.string.dialog_cancel), (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void onClickOrderCart() {
        if (getActivity() == null) {
            return;
        }

        if (mListProductsCart == null || mListProductsCart.isEmpty()) {
            return;
        }

        @SuppressLint("InflateParams") View viewDialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet_order, null);

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        // init ui
        TextView tvFoodsOrder = viewDialog.findViewById(R.id.tv_foods_order);
        TextView tvPriceOrder = viewDialog.findViewById(R.id.tv_price_order);
        TextView edtNameOrder = viewDialog.findViewById(R.id.edt_name_order);
        TextView edtPhoneOrder = viewDialog.findViewById(R.id.edt_phone_order);
        TextView edtAddressOrder = viewDialog.findViewById(R.id.edt_address_order);
        TextView tvCancelOrder = viewDialog.findViewById(R.id.tv_cancel_order);
        TextView tvCreateOrder = viewDialog.findViewById(R.id.tv_create_order);

        // Set data
        tvFoodsOrder.setText(getStringListFoodsOrder());
        tvPriceOrder.setText(mFragmentCartBinding.tvTotalPrice.getText().toString());

        // Set listener
        tvCancelOrder.setOnClickListener(v -> bottomSheetDialog.dismiss());

        tvCreateOrder.setOnClickListener(v -> {
            String strName = edtNameOrder.getText().toString().trim();
            String strPhone = edtPhoneOrder.getText().toString().trim();
            String strAddress = edtAddressOrder.getText().toString().trim();


            if (StringUtil.isEmpty(strName.trim().matches("[a-zA-Z ]+")+"") || (StringUtil.isEmpty(strPhone.matches("\\d{10}")+""))|| StringUtil.isEmpty(strAddress)) {

                GlobalFunction.showToastMessage(getActivity(), getString(R.string.message_enter_infor_order));
            } else {
                long id = System.currentTimeMillis();
                String strEmail = DataStoreManager.getUser().getEmail();

                Order order = new Order(id, strName, strEmail, strPhone, strAddress,
                        mAmount, getStringListFoodsOrder(), Constant.TYPE_PAYMENT_CASH, false);
                ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                        .child(String.valueOf(id))
                        .setValue(order, (error1, ref1) -> {
                            GlobalFunction.showToastMessage(getActivity(), getString(R.string.msg_order_success));
                            GlobalFunction.hideSoftKeyboard(getActivity());
                            bottomSheetDialog.dismiss();

                            ProductDatabase.getInstance(getActivity()).productDAO().deleteAllProduct();
                            clearCart();
                        });
            }
        });

        bottomSheetDialog.show();
    }

    private String getStringListFoodsOrder() {
        if (mListProductsCart == null || mListProductsCart.isEmpty()) {
            return "";
        }
        String result = "";
        for (Products products : mListProductsCart) {
            if (StringUtil.isEmpty(result)) {
                result = "- " + products.getName() + " (" + products.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + products.getCount();
            } else {
                result = result + "\n" + "- " + products.getName() + " (" + products.getRealPrice() + Constant.CURRENCY + ") "
                        + "- " + getString(R.string.quantity) + " " + products.getCount();
            }
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReloadListCartEvent event) {
        displayListFoodInCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
