package com.example.da1_e_max.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.da1_e_max.ControllerApplication;
import com.example.da1_e_max.R;
import com.example.da1_e_max.activity.AddProductActivity;
import com.example.da1_e_max.activity.AdminMainActivity;
import com.example.da1_e_max.adapter.AdminProductAdapter;
import com.example.da1_e_max.constant.Constant;
import com.example.da1_e_max.constant.GlobalFunction;
import com.example.da1_e_max.databinding.FragmentAdminHomeBinding;
import com.example.da1_e_max.fragment.BaseFragment;
import com.example.da1_e_max.listener.IOnManagerProductListener;
import com.example.da1_e_max.model.Products;
import com.example.da1_e_max.utils.StringUtil;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends BaseFragment {

    private FragmentAdminHomeBinding mFragmentAdminHomeBinding;
    private List<Products> mListProducts;
    private AdminProductAdapter mAdminProductAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false);

        initView();
        initListener();
        getListFood("");
        return mFragmentAdminHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.home));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminHomeBinding.rcvFood.setLayoutManager(linearLayoutManager);
        mListProducts = new ArrayList<>();
        mAdminProductAdapter = new AdminProductAdapter(mListProducts, new IOnManagerProductListener() {
            @Override
            public void onClickUpdateProduct(Products products) {
                onClickEditFood(products);
            }

            @Override
            public void onClickDeleteProduct(Products products) {
                deleteFoodItem(products);
            }
        });
        mFragmentAdminHomeBinding.rcvFood.setAdapter(mAdminProductAdapter);
    }

    private void initListener() {
        mFragmentAdminHomeBinding.btnAddFood.setOnClickListener(v -> onClickAddFood());

        mFragmentAdminHomeBinding.imgSearch.setOnClickListener(view1 -> searchFood());

        mFragmentAdminHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });

        mFragmentAdminHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchFood();
                }
            }
        });
    }

    private void onClickAddFood() {
        GlobalFunction.startActivity(getActivity(), AddProductActivity.class);
    }

    private void onClickEditFood(Products products) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_PRODUCT_OBJECT, products);
        GlobalFunction.startActivity(getActivity(), AddProductActivity.class, bundle);
    }

    private void deleteFoodItem(Products products) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                            .child(String.valueOf(products.getId())).removeValue((error, ref) ->
                            Toast.makeText(getActivity(),
                                    getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchFood() {
        String strKey = mFragmentAdminHomeBinding.edtSearchName.getText().toString().trim();
        if (mListProducts != null) {
            mListProducts.clear();
        } else {
            mListProducts = new ArrayList<>();
        }
        getListFood(strKey);
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListFood(String keyword) {
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        Products products = dataSnapshot.getValue(Products.class);
                        if (products == null || mListProducts == null || mAdminProductAdapter == null) {
                            return;
                        }
                        if (StringUtil.isEmpty(keyword)) {
                            mListProducts.add(0, products);
                        } else {
                            if (GlobalFunction.getTextSearch(products.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                mListProducts.add(0, products);
                            }
                        }
                        mAdminProductAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        Products products = dataSnapshot.getValue(Products.class);
                        if (products == null || mListProducts == null
                                || mListProducts.isEmpty() || mAdminProductAdapter == null) {
                            return;
                        }
                        for (int i = 0; i < mListProducts.size(); i++) {
                            if (products.getId() == mListProducts.get(i).getId()) {
                                mListProducts.set(i, products);
                                break;
                            }
                        }
                        mAdminProductAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Products products = dataSnapshot.getValue(Products.class);
                        if (products == null || mListProducts == null
                                || mListProducts.isEmpty() || mAdminProductAdapter == null) {
                            return;
                        }
                        for (Products productsObject : mListProducts) {
                            if (products.getId() == productsObject.getId()) {
                                mListProducts.remove(productsObject);
                                break;
                            }
                        }
                        mAdminProductAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
