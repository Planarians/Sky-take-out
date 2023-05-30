package com.sky.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.ShoppingCart;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {


    @Select("select * from sky_take_out.shopping_cart where user_id=#{userId}")
    List<ShoppingCart> getByUserId(Long userId);

    @Override
    int insert(ShoppingCart entity);

    @Override
    int updateById(ShoppingCart entity);

    int updateNumberAndAmountById(@Param("number") Integer number, @Param("amount") BigDecimal amount, @Param("id") Long id);


    @Delete("delete from  sky_take_out.shopping_cart where user_id = #{userId}")
    void deleteByUserId(@Param("userId") Long userId);

    @Update("update sky_take_out.shopping_cart set number=#{number} where id = #{id}")
    void updateNumberAndAmountByIdMy(ShoppingCart shoppingCart);

    //   getBySetmealIdIsNotNullAndDishIdIsNotNullAndDishFlavorIsNotNull

}

