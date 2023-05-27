package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.BusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl<DishFlavorMapper> implements DishService {


    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private com.sky.mapper.DishFlavorMapper dishFlavorMapper;


    @Autowired
    SetmealDishMapper setmealDishMapper;

    @Autowired
    SetmealMapper setmealMapper;


    // 菜品新增
    @Transactional
    @Override
    public void saveDishWithFlavor(DishDTO dishDTO) {
        // 1.参数校验
        // 2.业务校验
        Dish oldDish = dishMapper.getByName(dishDTO.getName());
        if (oldDish != null) {
            throw new BusinessException("菜品已存在");
        }
        // 3.菜品dto->菜品entity
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);
        // 4.补充状态
        dish.setStatus(StatusConstant.DISABLE); // 禁用
        // 5.调用dishMapper新增 （主键返回）
        log.info("菜品新增前id：{}", dish.getId());
        dishMapper.insert(dish);
        log.info("菜品新增后id：{}", dish.getId());

        // 6. 取出口味列表
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        // 7.遍历（非空判断）
        /*if (flavorList!=null && flavorList.size()>0) {}*/
        if (ArrayUtil.isNotEmpty(flavorList)) {
            for (DishFlavor dishFlavor : flavorList) {
                // 关联菜品id
                dishFlavor.setDishId(dish.getId());
                // 保存口味
                dishFlavorMapper.insert(dishFlavor);
            }
        }
    }


    // 分页
    @Override
    public PageResult getPage(DishPageDTO dishPageDTO) {
        // 1.开启分页
        PageHelper.startPage(dishPageDTO.getPage(), dishPageDTO.getPageSize());
        // 2.查询list
        List<DishVO> voList = dishMapper.getList(dishPageDTO);
        Page<DishVO> page = (Page<DishVO>) voList;
        // 3.返回分页
        return new PageResult(page.getTotal(), page.getResult());
    }

    // 回显菜品
    @Override
    public DishVO getById(Long id) {
        // 1.先查菜品
        Dish dish = dishMapper.getById(id);
        // 2.再根据菜品id查询口味列表
        List<DishFlavor> flavorList = dishFlavorMapper.getListByDishId(id);
        // 3.封装vo
        DishVO dishVO = BeanUtil.copyProperties(dish, DishVO.class);
        dishVO.setFlavors(flavorList);
        // 4.返回vo
        return dishVO;
    }


    //修改菜品
    @Override
    public void updateByDishId(DishDTO dishDTO) {
        Dish dish = BeanUtil.copyProperties(dishDTO, Dish.class);

        dishMapper.updateByDishId(dish);
        Long dishId = dish.getId();
        dishFlavorMapper.deleteByDishId(dishId);
//        ??
//        dish.setUpdateTime(LocalDateTime.now());
//        dish.setUpdateUser(ThreadLocalUtil.getCurrentId());
        // 6. 取出口味列表
        List<DishFlavor> flavorList = dishDTO.getFlavors();
        // 7.遍历（非空判断）
        /*if (flavorList!=null && flavorList.size()>0) {}*/
        if (ArrayUtil.isNotEmpty(flavorList)) {
            for (DishFlavor dishFlavor : flavorList) {
                // 关联菜品id
                dishFlavor.setDishId(dish.getId());
                // 保存口味
                dishFlavorMapper.insert(dishFlavor);
            }
        }

    }


    //删除菜品
    @Transactional
    @Override
    public void deleteDish(List<Long> ids) {
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            Integer dishStatus = dish.getStatus();

            if (dishStatus.equals(1)) {
                throw new BusinessException(400, "有启用的菜品 无法删除");
            }


        }


        for (Long dishId : ids) {

            Long k = 0l;
            k = setmealDishMapper.getDishCountById(dishId);
            if (k >= 1l) {
                throw new BusinessException(400, "有菜品在套餐中 无法删除");
            }

        }

        for (Long id : ids) {
            dishMapper.deleteByDishId(id);
            dishFlavorMapper.deleteByDishId(id);
        }

    }




    @Transactional
    @Override
    public void changeDishStatus(Integer status, Long id) {
       ArrayList<Long> setmealIds =setmealDishMapper.findSetmealIdByDishId(id);

        for (Long setmealId : setmealIds) {
            Setmeal setmeal =setmealMapper.getById(setmealId);
            setmeal.setStatus(Math.abs(setmeal.getStatus()));
            setmealMapper.updateBySetmeal(setmeal);
        }



        Dish dish = dishMapper.getById(id);
        dish.setStatus(Math.abs(dish.getStatus() - 1));
        dishMapper.updateByDishId(dish);
    }
}
