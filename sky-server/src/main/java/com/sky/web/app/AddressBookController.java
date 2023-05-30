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
    public Result save(AddressBook addressBook){
        addressBookService.save(addressBook);
        return Result.success();
    }

}