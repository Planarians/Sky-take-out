package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {


    // 根据分类id统计菜品数量
    @Select("select count(*) from sky_take_out.dish where category_id=#{categoryId}")
    Integer countByCategoryId(Long categoryId);


    // 根据菜品名称查询
    @Select("select * from sky_take_out.dish where name=#{name}")
    Dish getByName(String name);

//    // 新增菜品
//    @AutoFill("insert")
//    void insert(Dish dish);
    // 条件查询菜品列表
    List<DishVO> getList(DishPageDTO dishPageDTO);

    // 根据id查询
    @Select("select * from sky_take_out.dish where id=#{id}")
    Dish getById(Long id);


    @AutoFill("update")
    void updateByDishId(Dish dish);


    @Delete("delete from sky_take_out.dish where id=#{id}")
    void deleteByDishId(Long id);


    @Select("select count(*) from sky_take_out.dish where status = #{status}")
    Integer countByStatus(Integer status);
}

