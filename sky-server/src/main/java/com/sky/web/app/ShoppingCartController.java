package com.sky.web.app;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 查询购物车列表
    @GetMapping("/list")
    public Result getList() {
        List<ShoppingCart> list = shoppingCartService.getList();
        return Result.success(list);
    }

    //添加购物车
    @PostMapping("/add")
    public Result addCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        shoppingCartService.save(shoppingCartDTO);

        return Result.success();
    }


    @DeleteMapping("/clean")
    public Result cleanCart(){
        shoppingCartService.cleanCart();
        return Result.success();

    }





}
