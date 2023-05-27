package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.BusinessException;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl<SetmealFlavorMapper> implements SetmealService {


    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private com.sky.mapper.DishFlavorMapper dishFlavorMapper;

//    // 菜品新增
//    @Transactional
//    @Override
//    public void saveSetmealWithFlavor(SetmealDTO setmealDTO) {
//        // 1.参数校验
//        // 2.业务校验
//        Setmeal oldSetmeal = setmealMapper.getByName(setmealDTO.getName());
//        if (oldSetmeal != null) {
//            throw new BusinessException("菜品已存在");
//        }
//        // 3.菜品dto->菜品entity
//        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);
//        // 4.补充状态
//        setmeal.setStatus(StatusConstant.DISABLE); // 禁用
//        // 5.调用setmealMapper新增 （主键返回）
//        log.info("菜品新增前id：{}", setmeal.getId());
//        setmealMapper.insert(setmeal);
//        log.info("菜品新增后id：{}", setmeal.getId());
//
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
//    }


    // 分页
    @Override
    public PageResult getPage(SetmealPageDTO setmealPageDTO) {
        // 1.开启分页
        PageHelper.startPage(setmealPageDTO.getPage(), setmealPageDTO.getPageSize());
        // 2.查询list
        List<SetmealVO> voList = setmealMapper.getList(setmealPageDTO);
        Page<SetmealVO> page = (Page<SetmealVO>) voList;
        // 3.返回分页
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public SetmealVO getById(Long id) {
        return null;
    }

//    // 回显菜品
//    @Override
//    public SetmealVO getById(Long id) {
//        // 1.先查菜品
//        Setmeal setmeal = setmealMapper.getById(id);
//        // 2.再根据菜品id查询口味列表
//        List<DishFlavor> flavorList = diahFlavorMapper.getListBySetmealId(id);
//        // 3.封装vo
//        SetmealVO setmealVO = BeanUtil.copyProperties(setmeal, SetmealVO.class);
//        setmealVO.dishFlavors(flavorList);
//        // 4.返回vo
//        return setmealVO;
//    }

//
//    //修改菜品
//    @Override
//    public void updateBySetmealId(SetmealDTO setmealDTO) {
//        Setmeal setmeal = BeanUtil.copyProperties(setmealDTO, Setmeal.class);
//
//        setmealMapper.updateBySetmealId(setmeal);
//        Long setmealId = setmeal.getId();
//        setmealFlavorMapper.deleteBySetmealId(setmealId);
////        ??
////        setmeal.setUpdateTime(LocalDateTime.now());
////        setmeal.setUpdateUser(ThreadLocalUtil.getCurrentId());
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


//    @Transactional
//    @Override
//    public void deleteSetmeal(List<Long> ids) {
//        for (Long id : ids) {
//            Setmeal setmeal = setmealMapper.getById(id);
//            Integer setmealStatus = setmeal.getStatus();
//
//            if (setmealStatus.equals(1)) {
//                throw new BusinessException(400, "有启用的菜品 无法删除");
//            }
//
//
//        }
//
//
//        for (Long setmealId : ids) {
//
//            Long k =0l;
//            k =SetmealSetmealMapper.getSetmealCountById(setmealid);
//            if () {
//                throw new BusinessException(400, "有菜品在套餐中 无法删除");
//            }
//
//        }
//
//        for (Long id : ids) {
//            setmealMapper.deleteBySetmealId(id);
//            setmealFlavorMapper.deleteBySetmealId(id);
//        }
//
//    }
}
