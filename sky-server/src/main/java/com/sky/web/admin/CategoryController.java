package com.sky.web.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private JwtProperties jwtProperties;



    // 分类分页
    @GetMapping("/page")
    public Result getPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.getPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    // 根据类型查询分类
    @GetMapping("/list")
    public Result getList(Integer type) {
        List<Category> list = categoryService.getList(type);
        return Result.success(list);
    }


    // 新增分类
    @PostMapping
    public Result save(@RequestBody CategoryDTO categoryDTO){
        categoryService.save(categoryDTO);
        return Result.success();
    }

    // 删除分类
    @DeleteMapping
    public Result deleteById(@RequestParam Long id) {
        categoryService.deleteById(id);
         return Result.success();
    }

    // 修改分类
    @PutMapping
    public Result update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success();
    }


    // 分类启用禁用
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status,
                              @RequestParam Long id){
        categoryService.startOrStop(status,id);
        return Result.success();
    }
}



