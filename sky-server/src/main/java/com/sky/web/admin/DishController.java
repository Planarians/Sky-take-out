package com.sky.web.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    // 菜品新增
    @PostMapping
    public Result saveDishWithFlavor(@RequestBody DishDTO dishDTO) {
        dishService.saveDishWithFlavor(dishDTO);
        return Result.success();
    }

    // 分页
    @GetMapping("/page")
    public Result getPage(DishPageDTO dishPageDTO) {
        PageResult pageResult = dishService.getPage(dishPageDTO);
        return Result.success(pageResult);
    }


    // 回显菜品
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    //修改菜品
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO){
        dishService.updateByDishId(dishDTO);

        return Result.success();
    }


    //批量删除菜品
    @DeleteMapping
    public  Result deleteDish(@RequestParam List<Long> ids){
        dishService.deleteDish(ids);
        return Result.success();
    }


    //启售或停售菜品
    @PostMapping("/status/{status}")
    public  Result changeDishStatus(@PathVariable("status") Integer status,
                                   @RequestParam Long id){
        dishService.changeDishStatus(status,id);
        return Result.success();
    }





}
