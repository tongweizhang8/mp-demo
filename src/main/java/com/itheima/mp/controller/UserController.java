package com.itheima.mp.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
//@AllArgsConstructor
@RequiredArgsConstructor // 使用lombok注解，自动生成构造方法
public class UserController {

    private final IUserService userService;

    @ApiOperation("查询用户列表")
    @PostMapping
    public void saveUser(@RequestBody/*将请求体中的JSON数据转换为相应的Java对象，*/ UserFormDTO userFormDTO) {
        //1.把dto拷贝到to
        User user = BeanUtil.copyProperties(userFormDTO, User.class);
        //2.新增
        userService.save(user);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUser(@ApiParam("用户id")/*描述接口参数，提供参数的说明*/ @PathVariable("id") /*从url中提取路径变量并绑定到方法参数上*/Long id) {
        userService.removeById(id);
    }

    @ApiOperation("根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryUserById(@ApiParam("用户id") @PathVariable("id") Long id) {
        //1.查询用户po
        //User user = userService.getById(id);
        //2.把po拷贝到vo
        //return BeanUtil.copyProperties(user, UserVO.class);
        return userService.UserServiceAndAddressById(id);
    }

    @ApiOperation("根据id查询用户接口")
    @GetMapping
    public List<UserVO> queryUserByIds(@ApiParam("用户id") @RequestParam("ids") List<Long> id) {
        //1.查询用户po
        List<User> users = userService.listByIds(id);
        //2.把po拷贝到vo
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @ApiOperation("扣减用户余额接口")
    @PutMapping("/{id}/deduct/{money}")
    public void deductBalance(
            @ApiParam("用户id") @PathVariable("id") Long id,
            @ApiParam("扣减金额") @PathVariable("money") Integer money) {
        userService.deductBalance(id, money);
    }

    @ApiOperation("根据复杂条件查询用户接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery userQuery) {
        //1.查询用户po
        List<User> users = userService.queryUsers(userQuery.getName(), userQuery.getStatus(),userQuery.getMinBalance(),userQuery.getMaxBalance());
        //2.把po拷贝到vo
        return BeanUtil.copyToList(users, UserVO.class);
    }

    @ApiOperation("根据条件分页查询用户接口")
    @GetMapping("/list")
    public Page<UserVO> queryUsersPage(UserQuery userQuery) {
        return userService.queryUsersPage(userQuery);
    }
}
