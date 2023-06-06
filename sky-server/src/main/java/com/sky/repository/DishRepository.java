package com.sky.repository;

import com.sky.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish,Long> {


    Dish findByName(String name);


    @Override
    Dish getById (Long id);
}
