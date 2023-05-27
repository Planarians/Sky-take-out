package com.sky.web.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

//    // 菜品新增
//    @PostMapping
//    public Result saveSetmealWithFlavor(@RequestBody SetmealDTO setmealDTO) {
//        setmealService.saveSetmealWithFlavor(setmealDTO);
//        return Result.success();
//    }

    // 分页
    @GetMapping("/page")
    public Result getPage(SetmealPageDTO setmealPageDTO) {
        PageResult pageResult = setmealService.getPage(setmealPageDTO);
        return Result.success(pageResult);
    }

    // 回显套餐
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }
//
    //修改套餐
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.updateBySetmealId(setmealDTO);

        return Result.success();
    }

//    //批量删除菜品
//    @DeleteMapping
//    public  Result deleteSetmeal(@RequestParam ArrayList<Long> idslist){
//        setmealService.deleteSetmeal(idslist);
//        return Result.success();
//    }

//    //批量删除菜品
//    @DeleteMapping
//    public  Result deleteSetmeal(@RequestParam List<Long> ids){
//        setmealService.deleteSetmeal(ids);
//        return Result.success();
//    }
}
