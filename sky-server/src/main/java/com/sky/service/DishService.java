package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.ArrayList;
import java.util.List;

public interface DishService {
    void saveDishWithFlavor(DishDTO dishDTO);

    // 分页
    PageResult getPage(DishPageDTO dishPageDTO);

    DishVO getById(Long id);

    void updateByDishId(DishDTO dishVO);

    void deleteDish(List<Long> ids);

    void changeDishStatus(Integer status, Long id);

}
