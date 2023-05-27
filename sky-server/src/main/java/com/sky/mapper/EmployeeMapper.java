package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {



    @Select("select  * from sky_take_out.employee where username=#{username} ")
    Employee getByUsername(String username);

    // 条件查询
    List<Employee> getList(String name);








    //根据手机号查询
    @Select("select * from sky_take_out.employee where phone =#{phone}")
    Employee getByPhone(String phone);


    //根据身份证号查询
    @Select("select * from sky_take_out.employee where id_number =#{id_number}")
    Employee getIdNumber(String idNumber);


    //新增员工
    @AutoFill("insert")
    void insert(Employee employee);


    //根据id查询
    @Select("select * from sky_take_out.employee where id =#{id}")
    Employee getById(Long id);

    @AutoFill("update")
    void updateById(Employee employee);


}




