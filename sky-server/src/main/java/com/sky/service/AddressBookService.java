package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {


    List<AddressBook> getList();


    void save(AddressBook addressBook);

    AddressBook getDefault();

    AddressBook getById(Long id);

    AddressBook update(AddressBook addressBook);

}