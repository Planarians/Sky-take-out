package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.ThreadLocalUtil;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.BusinessException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;




    // 员工登录
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        // 1.参数校验
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            throw new BusinessException(400, "非法参数");
        }
        // 2.根据用户名查询数据库
        Employee employee = employeeMapper.getByUsername(username);
        // 3.业务校验
        // 3-1 用户是否存在
        if (employee == null) {
            throw new BusinessException(400, "用户名不存在");
        }
        // 3-2 密码是否正确
        String md5 = SecureUtil.md5(password);
        if (!CharSequenceUtil.equals(md5, employee.getPassword())) {
            throw new BusinessException(400, "密码错误");
        }
        // 3-3 账号是否被禁用
        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new BusinessException(400, "此账号被禁用，请联系管理员");
        }

        // 4.登录成功，返回employee
        return employee;
    }

    // 员工分页
    @Override
    public PageResult getPage(EmployeePageQueryDTO employeePageQueryDTO) {
        // 1.开启分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 2 .查询list
        List<Employee> list = employeeMapper.getList(employeePageQueryDTO.getName());
        Page<Employee> page = (Page<Employee>) list;
        // 3.返回
        return new PageResult(page.getTotal(), page.getResult());
    }


    //新增员工
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //参数校验
        if (

                StrUtil.isBlank((employeeDTO.getUsername())) ||
                        StrUtil.isBlank((employeeDTO.getName())) ||
                        StrUtil.isBlank((employeeDTO.getPhone())) ||
                        StrUtil.isBlank((employeeDTO.getIdNumber()))
        ) {
            throw new BusinessException(400, "表单填写不完整，请确认 ");
        }
        //2。业务校验
        //2-1 账号唯一
        Employee byUsername = employeeMapper.getByUsername(employeeDTO.getUsername());
        if (byUsername != null) {
            throw new BusinessException(400, "此账号已存在");
        }
        // 2-2 手机号唯一
        Employee byPhone = employeeMapper.getByPhone(employeeDTO.getPhone());
        if (byPhone != null) {
            throw new BusinessException(400, "此手机号已存在");
        }
        // 2-3 身份证号唯一
        Employee byIdNumber = employeeMapper.getIdNumber(employeeDTO.getIdNumber());
        if (byIdNumber != null) {
            throw new BusinessException(400, "此身份证号已存在");
        }
        // 3. dto -> entity??
        Employee employee = BeanUtil.copyProperties(employeeDTO, Employee.class);
        // 3-1 补全信息
        String md5 = SecureUtil.md5(PasswordConstant.DEFAULT_PASSWORD);
        employee.setPassword(md5);
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setCreateUser(1L);
        employee.setUpdateUser(1L);

        // 4.调用mapper保存到数据库
        employeeMapper.insert(employee);
    }

    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }


    //修改
    @Override
    public void updateById(EmployeeDTO employeeDTO) {
        //1.非法参数校验
        if (employeeDTO.getId() == null) {
            throw new BusinessException(400, "修改id不能为空");
        }

        // 2. 加一个校验手机号

        // 3.dto->entity
        Employee employee = BeanUtil.copyProperties(employeeDTO, Employee.class);
        // 3-1 补充信息
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(ThreadLocalUtil.getCurrentId());

        // 4.调用mapper修改
        employeeMapper.updateById(employee);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        // 1.封装employee对象
        Employee employee = Employee.builder()
                .id(id)
                .status(status).build();
        // 2.补充信息
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(ThreadLocalUtil.getCurrentId());
        // 3.调用mapper修改
        employeeMapper.updateById(employee);
    }

}

