package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="User")
@Table
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
   // @SequenceGenerator(name= "user_squence",sequenceName = "user_squence",allocationSize = 1)
    @Column(name="id")
    private Long id;

    //微信用户唯一标识
    @Column(name="openid")
    private String openid;

    //姓名
    @Column(name="name")
    private String name;

    //手机号
    @Column(name="phone")
    private String phone  ;

    //性别 0 女 1 男
    @Column(name="sex")
    private String sex;

    //身份证号
    @Column(name="id_number")
    private String idNumber;

    //头像
    @Column(name="avatar")
    private String avatar;

    //注册时间
    @Column(name="create_time")
    private LocalDateTime createTime;
}
