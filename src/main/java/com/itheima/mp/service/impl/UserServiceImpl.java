package com.itheima.mp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.itheima.mp.domain.dto.PageDTO;
import com.itheima.mp.domain.po.Address;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.PageQuery;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.domain.vo.AddressVO;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.enums.UserStatus;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public void deductBalance(Long id, Integer money) {
        //1.查询用户
        User user = getById(id);
        //2.校验用户id
        if (user == null && user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在");
        }
        //3.校验余额是否充足
        if (user.getBalance() < money) {
            throw new RuntimeException("余额不足");
        }
        //4.扣减余额
        int remainBalance = user.getBalance() - money;
        lambdaUpdate()
                .set(User::getBalance, remainBalance)
                .set(remainBalance == 0,User::getStatus ,UserStatus.FROZEN)
                .eq(User::getId, id)
                .eq(User::getBalance, user.getBalance()) //乐观锁
                .update();
    }

    @Override
    public List<User> queryUsers(String name, Integer status, Integer minBalance, Integer maxBalance) {
        return lambdaQuery()
                .like(name != null, User::getUsername, name)
                .eq(status != null, User::getStatus, status)
                .eq(minBalance != null, User::getBalance, minBalance)
                .eq(maxBalance != null, User::getBalance, maxBalance)
                .list();
    }

    @Override
    public UserVO UserServiceAndAddressById(Long id) {
        //1.查询用户
        User user = getById(id);
        if (user == null && user.getStatus() == UserStatus.FROZEN) {
            throw new RuntimeException("用户不存在");
        }
        //2.查询地址
        List<Address> list = Db.lambdaQuery(Address.class)
                .eq(Address::getUserId, id)
                .list();
        //3.封装vo
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        if (CollUtil.isNotEmpty(list)) {
            userVO.setAddresses(BeanUtil.copyToList(list, AddressVO.class));
        }
        return userVO;
    }

    @Override
    public PageDTO<UserVO> queryUsersPage(UserQuery userQuery) {
        //1.构建查询对象
        String name = userQuery.getName();
        Integer status = userQuery.getStatus();
//        1.1分页条件
//        Page<User> page = new Page<>(userQuery.getPageNo(), userQuery.getPageSize());
//        1.2排序条件
//        if (StrUtil.isNotBlank(userQuery.getSortBy())) {
//            //不为空
//            page.addOrder(new OrderItem(userQuery.getSortBy(), userQuery.getIsAsc()));
//        } else {
//            page.addOrder(new OrderItem("update_time", false));
//        }
        Page<User> page = userQuery.toMpPageDefaultSortByUpdateTimeDesc();
        //2.分页查询
        Page<User> p = lambdaQuery()
                .like(name != null, User::getUsername, userQuery.getName())
                .eq(status != null, User::getStatus, userQuery.getStatus())
                .page(page);
        //3.封装VO返回结果
//        PageDTO<UserVO> dto = new PageDTO<>();
//        //3.1总条数
//        dto.setTotal(page.getTotal());
//        //3.2总页数
//        dto.setPages(page.getPages());
//        //3.3当前数据页
//        List<User> records = p.getRecords();
//        if (CollUtil.isEmpty(records)) {
//            dto.setList(Collections.emptyList());
//            return dto;
//        }
//        //3.4拷贝uservo
//        List<UserVO> userVOS = BeanUtil.copyToList(records, UserVO.class);
//        dto.setList(userVOS);
//        //4.返回
//        return dto;
        return PageDTO.of(p, UserVO.class);
    }
}
