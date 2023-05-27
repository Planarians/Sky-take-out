package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
//    void saveSetmealWithFlavor(SetmealDTO setmealDTO);

    // 分页
    PageResult getPage(SetmealPageDTO setmealPageDTO);

    SetmealVO getById(Long id);

//    void updateBySetmealId(SetmealDTO setmealVO);
//
//    void deleteSetmeal(List<Long> ids);

}
