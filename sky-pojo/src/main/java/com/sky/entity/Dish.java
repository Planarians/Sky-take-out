package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GenerationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@javax.persistence.Table(name = "dish")
@javax.persistence.Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id", nullable = false)
    @javax.persistence.Id
    private Long id;

    //菜品名称
    @javax.persistence.Column(name = "name", nullable = false)
    private String name;

    //菜品分类id
    @javax.persistence.Column(name = "category_id", nullable = false)
    private Long categoryId;

    //菜品价格
    @javax.persistence.Column(name = "price")
    private BigDecimal price;

    //图片
    @javax.persistence.Column(name = "image")
    private String image;

    //描述信息
    @javax.persistence.Column(name = "description")
    private String description;

    //0 停售 1 起售
    @javax.persistence.Column(name = "status")
    private Integer status;

    @javax.persistence.Column(name = "create_time")
    private LocalDateTime createTime;

    @javax.persistence.Column(name = "update_time")
    private LocalDateTime updateTime;

    @javax.persistence.Column(name = "create_user")
    private Long createUser;

    @javax.persistence.Column(name = "update_user")
    private Long updateUser;

}
