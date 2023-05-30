package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sky.context.ThreadLocalUtil;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {


    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    // 查询购物车列表
    @Override
    public List<ShoppingCart> getList() {
        // 1.取出登录人
        Long userId = ThreadLocalUtil.getCurrentId();
        // 2.调用mapper查询
        return shoppingCartMapper.getByUserId(userId);
    }


    @Transactional
    @Override
    public void save(ShoppingCartDTO shoppingCartDTO) {

        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();

        ShoppingCart shoppingCart = null;

        if (shoppingCartDTO.getSetmealId() != null) {
            queryWrapper.eq("user_id", ThreadLocalUtil.getCurrentId());
        }
        if (shoppingCartDTO.getSetmealId() != null) {
            queryWrapper.eq("setmeal_id", shoppingCartDTO.getSetmealId());
        }
        if (shoppingCartDTO.getDishId() != null) {
            queryWrapper.eq("dish_id", shoppingCartDTO.getDishId());
        }
        if (shoppingCartDTO.getDishFlavor() != null) {
            queryWrapper.eq("dish_flavor", shoppingCartDTO.getDishFlavor());
        }

        queryWrapper.orderByAsc("setmeal_id", "dish_id", "dish_flavor");


        shoppingCart = shoppingCartMapper.selectOne(queryWrapper);


        //没有的情况 增加一条
        if (shoppingCart) {
            shoppingCartList.forEach(shoppingCart -> {
                shoppingCart.setUserId(ThreadLocalUtil.getCurrentId());
                shoppingCart.setNumber(1);
                shoppingCart.setCreateTime(LocalDateTime.now());
                shoppingCart.setAmount(dishMapper.getById(shoppingCart.getDishId()).getPrice());

                //套餐
                if(Objects.isNull(shoppingCart.getDishId())) {
                    shoppingCart.setImage(setmealMapper.getById(shoppingCart.getDishId()).getImage());
                }
                else{
                    shoppingCart.setImage(dishMapper.getById(shoppingCart.getDishId()).getImage());
                }
                shoppingCartMapper.insert(shoppingCart);


            });

            //shoppingCart->{shoppingCartMapper.insert(shoppingCart);});

        } else {

            //有的情况
            shoppingCartList.forEach(shoppingCart -> {
                        shoppingCart.setNumber(shoppingCart.getNumber()+1);
                        Long l = shoppingCart.getAmount().longValue();
                        shoppingCart.setAmount(BigDecimal.valueOf(l + (l / shoppingCart.getNumber())));
                        // shoppingCart.getAmount()/shoppingCart.getNumber());
                        shoppingCartMapper.update(shoppingCart, null);
                    }
            );

        }
    }

}
