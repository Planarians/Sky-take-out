package com.sky.mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {



    @Select("select * from sky_take_out.shopping_cart where user_id=#{userId}")
    List<ShoppingCart> getByUserId(Long userId);

    @Override
    int insert(ShoppingCart entity);


    //   getBySetmealIdIsNotNullAndDishIdIsNotNullAndDishFlavorIsNotNull

}

