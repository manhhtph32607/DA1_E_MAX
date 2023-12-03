package com.example.da1_e_max.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.da1_e_max.model.Products;

import java.util.List;


@Dao
public interface ProductDAO {

    @Insert
    void insertProduct(Products products);

    @Query("SELECT * FROM Products")
    List <Products> getListProductCart();


    @Query("SELECT * FROM Products WHERE id=:id")
    List<Products> checkProductInCart(long id);

    @Delete
    void deleteProduct(Products products);

    @Update
    void updateProduct(Products products);

    @Query("DELETE from Products")
    void deleteAllProduct();
}
