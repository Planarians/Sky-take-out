package com.sky.web.app;

import com.sky.dto.DishPageDTO;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.List;

@RestController
@RequestMapping("/user/dish")
@Slf4j
public class AppDishController {


    @Autowired
    DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;


    // 根据分类id查询菜品列表（小程序段）
    @GetMapping("/list")
    public Result getList(@RequestParam Long categoryId) {

        List<DishVO> voList = null;

        // ---------------------start
        // 先查询redis中是否有该分类的菜品缓存
        String dishKey = "DISH:" + categoryId;
        if (redisTemplate.hasKey(dishKey)) {
            log.info("查询缓存...");
            voList = (List<DishVO>) redisTemplate.opsForValue().get(dishKey);
            return Result.success(voList);
        }
        // ---
        // 封装dto条件
        DishPageDTO dishPageDTO = DishPageDTO.builder()
                .categoryId(categoryId)
                .status(1)
                .build();

        // 调用service查询,返回结果
        log.info("查询数据库...");
        voList = dishService.getParamList(dishPageDTO);
        // --------------------------  start
        // 将数据库数据同步到缓存
        redisTemplate.opsForValue().set(dishKey, voList);
        // --------------------------  end

        // 调用service查询,返回结果
        //return Result.success(dishService.getParamList(dishPageDTO));

        return Result.success(voList);
    }
}
