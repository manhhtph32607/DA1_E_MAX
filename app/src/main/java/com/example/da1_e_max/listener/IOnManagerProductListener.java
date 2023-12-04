package com.example.da1_e_max.listener;


import com.example.da1_e_max.model.Products;

public interface IOnManagerProductListener {
    void onClickUpdateProduct(Products products);
    void onClickDeleteProduct(Products products);
}
