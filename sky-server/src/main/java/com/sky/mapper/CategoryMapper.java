package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CategoryMapper {
    List<Category> getList(CategoryPageQueryDTO categoryPageQueryDTO);


    @Select("select * from sky_take_out.category where name=#{name}")
    Category getByName(String name);


    @AutoFill("insert")
    void insert(Category category);


    // 根据id删除
    @Delete("delete from sky_take_out.category where id=#{id}")
    void deleteById(Long id);


    @AutoFill("update")
    void updateById(Category category);
}
