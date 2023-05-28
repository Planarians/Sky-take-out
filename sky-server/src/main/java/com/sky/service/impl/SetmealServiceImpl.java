package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.entity.*;
import com.sky.exception.BusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMPMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl<SetmealFlavorMapper> implements SetmealService {


    @Autowired
    private SetmealMPMapper setmealMPMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private com.sky.mapper.DishFlavorMapper dishFlavorMapper;


    @Autowired
    private SetmealDishMapper setmealDishMapper;


    @Autowired
    private com.sky.mapper.DishMapper dishMapper;


    // 套餐新增
    @Transactional
    @Override
    public void saveSetmealWithDish(SetmealDTO setmealDTO) {
        // 1.参数校验
        // 2.业务校验
        Setmeal oldSetmeal = setmealMapper.getByName(setmealDTO.getName());
        if (oldSetmeal != null) {
            throw new BusinessException(400, "套餐已存在");
        }
        // 3.菜品dto->菜品entity
        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);
        // 4.补充状态
        setmeal.setStatus(StatusConstant.DISABLE); // 禁用
        // 5.调用setmealMapper新增 （主键返回）
        log.info("套餐新增前id：{}", setmeal.getId());
        setmealMPMapper.insert(setmeal);
        log.info("菜品新增后id：{}", setmeal.getId());

        // 6. 取出口味列表
        List<Long> dishIds = setmealDishMapper.getDishIdsBySetmealId(setmealDTO.getId());
        // 7.遍历（非空判断）
        /*if (flavorList!=null && flavorList.size()>0) {}*/
        if (ArrayUtil.isNotEmpty(dishIds)) {
            for (Long dishId : dishIds) {
//                // 关联菜品id
//                setmealFlavor.setSetmealId(setmeal.getId());
//                // 保存口味
                dishMapper.insert(dishMapper.getById(dishId));
//                setmealFlavorMapper.insert(setmealFlavor);
            }
        }
    }


    // 分页
    @Override
    public PageResult getPage(SetmealPageDTO setmealPageDTO) {
        QueryWrapper<Setmeal> queryWrapper = new QueryWrapper<>();
        // 1.开启分页
        PageHelper.startPage(setmealPageDTO.getPage(), setmealPageDTO.getPageSize());
        // 2.查询list
        if (StringUtils.isNotBlank(setmealPageDTO.getName())) {
            queryWrapper.like("name", setmealPageDTO.getName());
        }

        if (setmealPageDTO.getCategoryId() != null) {
            queryWrapper.eq("category_id", setmealPageDTO.getCategoryId());
        }

        if (setmealPageDTO.getStatus() != null) {
            queryWrapper.eq("status", setmealPageDTO.getStatus());
        }

        queryWrapper.orderByAsc("name", "category_id", "status");

        List<Setmeal> list = setmealMapper.selectList(queryWrapper);
        // 修改后的代码
        PageInfo<SetmealVO> pageInfo = new PageInfo<>(list.stream().map(setmeal -> {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);
            return setmealVO;
        }).collect(Collectors.toList()));

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }


    // 回显套餐
    @Override
    public SetmealVO getById(Long id) {
        // 1.先查套餐
        Setmeal setmeal = setmealMapper.getById(id);
        // 2.再根据套餐id查询菜品列表
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishBySetmealId(id);


//        List<Dish> dishes = null;
//        for (Long dishId : dishIds) {
//            dishes.add(dishMapper.getById(dishId));
//
//        }


        // 3.封装vo
        SetmealVO setmealVO = BeanUtil.copyProperties(setmeal, SetmealVO.class);
        setmealVO.setSetmealDishes(setmealDishes);
        //   setmealVO.dishFlavors(flavorList);
        // 4.返回vo
        return setmealVO;
    }


    //修改套餐
    @Transactional
    @Override
    public void updateBySetmealId(SetmealDTO setmealDTO) {
        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);

        setmealMPMapper.updateById(setmeal);
    }


//        setmealMapper.updateBySetmealId(setmeal);
//        Long setmealId = setmeal.getId();
//        setmealFlavorMapper.deleteBySetmealId(setmealId);
//        ??
//        setmeal.setUpdateTime(LocalDateTime.now());
//        setmeal.setUpdateUser(ThreadLocalUtil.getCurrentId());
//        // 6. 取出口味列表
//        List<SetmealFlavor> flavorList = setmealDTO.getFlavors();
//        // 7.遍历（非空判断）
//        /*if (flavorList!=null && flavorList.size()>0) {}*/
//        if (ArrayUtil.isNotEmpty(flavorList)) {
//            for (SetmealFlavor setmealFlavor : flavorList) {
//                // 关联菜品id
//                setmealFlavor.setSetmealId(setmeal.getId());
//                // 保存口味
//                setmealFlavorMapper.insert(setmealFlavor);
//            }
//        }
//
//    }

    //    @Override
//    public void deleteSetmeal(ArrayList<Long> idslist) {
//
//    }


    @Transactional
    @Override
    public void deleteSetmeal(ArrayList<Long> ids) {
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            Integer setmealStatus = setmeal.getStatus();

            if (setmealStatus.equals(1)) {
                throw new BusinessException(400, "启售中的菜品 无法删除");
            }


        }
        for (Long id : ids) {
            setmealMapper.deleteById(id);
        }

    }

    @Transactional
    @Override
    public void updateSetmealStatus(Integer status,Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        setmeal.setStatus(Math.abs(setmeal.getStatus()- 1));
        setmealMapper.updateById(setmeal);
    }
}
