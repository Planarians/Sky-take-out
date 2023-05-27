package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface SetmealDishMapper {

    @Select("select count(*) from sky_take_out.setmeal_dish where dish_id = #{dishId} ")
 Long getDishCountById(Long dishId) ;



    @Select("select * from sky_take_out.setmeal_dish where dish_id = #{dishId}")
    Setmeal findByDishId(Long dishId);


    @Select("select setmeal_id from sky_take_out.setmeal_dish where dish_id = #{dishId}")
    Long findSetmealIdByDishId(Long id);

    //getDishCountById(dishId);


//    static Integer getDishCountByIds() {
//    }
}
