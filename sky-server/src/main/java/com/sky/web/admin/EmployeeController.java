package com.sky.web.admin;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;


    // 登录
    @ApiOperation("员工登录")
    @PostMapping("/login")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "employeeLoginDTO", value = "员工登录DTO", required = true, example = "username:admin,password=123456")
    )
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecret(),
                jwtProperties.getAdminTtl(),
                claims);

        //返回前端所需对象
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }


    // 员工退出
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout() {
        return Result.success();
    }


    // 员工分页
    @GetMapping("/page")
    public Result getPage(EmployeePageQueryDTO employeePageQueryDTO) {
        // 调用service
        PageResult pageResult = employeeService.getPage(employeePageQueryDTO);
        // 返回分页结果
        return Result.success(pageResult);
    }


    //新增员工
    @PostMapping
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        //调用service
        employeeService.save(employeeDTO);
        //返回success

        return Result.success();
    }

    //回显员工
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Long id) {
        //调用service
        Employee employee = employeeService.getById(id);
        //返回success

        return Result.success(employee);
    }


    //修改员工
    @PutMapping
    public Result updateById(@RequestBody EmployeeDTO employeeDTO) {
        //调用service
        employeeService.updateById(employeeDTO);
        //返回success

        return Result.success();
    }


    //更改状态
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable("status") Integer status,
            @RequestParam Long id) {
        //调用service
        employeeService.startOrStop(status,id);
        //返回success

        return Result.success();
    }
}
