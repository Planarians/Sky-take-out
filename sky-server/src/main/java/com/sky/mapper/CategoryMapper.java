package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> getList(CategoryPageQueryDTO categoryPageQueryDTO);


    @Select("select * from sky_take_out.category where name=#{name}")
    Category getByName(String name);


//    @AutoFill("insert")
//    void insert(Category category);


    // 根据id删除
    @Delete("delete from sky_take_out.category where id=#{id}")
    void deleteById(Long id);



    @Override
    @AutoFill("update")
    int updateById(Category category);
}
