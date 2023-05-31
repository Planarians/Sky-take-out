package com.sky.web.app;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
public class AddressBookController {


    @Autowired
    private AddressBookService addressBookService;

    // 查询起售分类列表
    @GetMapping("/list")
    public Result getList() {
        //Long id = ThreadLocalUtil.getCurrentId();
        List<AddressBook> list = addressBookService.getList();
        // 3.返回
        return Result.success(list);

    }

    @PostMapping
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/default")
    public Result getDefault() {
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }


    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }


    //修改
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    //删除
    @DeleteMapping
    public Result deleteById(@RequestParam Long id){
        addressBookService.deleteById(id);
        return Result.success();
    }


}