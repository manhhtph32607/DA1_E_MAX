package com.example.da1_e_max.listener;


import com.example.da1_e_max.model.Food;

public interface IOnManagerFoodListener {
    void onClickUpdateFood(Food food);
    void onClickDeleteFood(Food food);
}
