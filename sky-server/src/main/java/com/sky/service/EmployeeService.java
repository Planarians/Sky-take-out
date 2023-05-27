package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    Employee login(EmployeeLoginDTO employeeLoginDTO);

    // 员工分页
    PageResult getPage(EmployeePageQueryDTO employeePageQueryDTO);


    //新增员工
    void save(EmployeeDTO employeeDTO);

    Employee getById(Long id);

    //修改员工
    void updateById(EmployeeDTO employeeDTO);


    //禁用/解除禁用员工
    void startOrStop(Integer status, Long id);

}
