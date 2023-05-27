package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {


    void insert(DishFlavor dishFlavor);

    // 根据菜品id查询口味列表
    @Select("select * from sky_take_out.dish_flavor where dish_id=#{dishId}")
    List<DishFlavor> getListByDishId(Long dishId);


    @Delete("delete  from sky_take_out.dish_flavor where dish_id=#{dishId} ")
    void deleteByDishId(Long dishId);


}
