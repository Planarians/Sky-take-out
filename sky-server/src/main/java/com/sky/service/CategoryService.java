package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

public interface CategoryService {

    PageResult getPage(CategoryPageQueryDTO categoryPageQueryDTO);


    void save(CategoryDTO categoryDTO);

    void deleteById(Long id);

    void updateById(Category category);

    // 分类启用禁用
    void startOrStop(Integer status, Long id);
}


