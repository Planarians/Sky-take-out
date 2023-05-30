package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {


    // 查询购物车列表
    List<ShoppingCart> getList();

    void save(ShoppingCartDTO shoppingCartDTO);

}
