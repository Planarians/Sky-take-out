package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageDTO;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {


//    @Select("select * from sky_take_out.setmeal where  ")
//    static Setmeal getBysetmealName(String name) {
//    }

    // 根据分类id统计套餐数量
    @Select("select count(*) from sky_take_out.setmeal where category_id=#{categoryId}")
    Integer countByCategoryId(Long categoryId);




    // 根据菜品名称查询
    @Select("select * from sky_take_out.setmeal where name=#{name}")
    Setmeal getByName(String name);

//    // 新增菜品
//    @AutoFill("insert")
//    void insert(Setmeal setmeal);

    // 条件查询套餐列表

    List<SetmealVO> getList(SetmealPageDTO setmealPageDTO);


    // 根据id查询
    @Select("select * from sky_take_out.setmeal where id=#{id}")
    Setmeal getById(Long id);





//
//    @AutoFill("update")
//    void updateBySetmeal(Setmeal setmeal);



//
//    @Delete("delete from sky_take_out.setmeal where id=#{id}")
//    void deleteBySetmealId(Long id);


}
