package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.itheima.mp.domain.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testInsert() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        //user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    void testSelectById() {
        User user = userMapper.selectById(5L);
        System.out.println("user = " + user);
    }


    @Test
    void testQueryByIds() {
        List<User> users = userMapper.selectBatchIds(List.of(1L, 2L, 3L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateById() {
        User user = new User();
        user.setId(5L);
        user.setBalance(20000);
        userMapper.updateById(user);
    }

    @Test
    void testDeleteUser() {
        userMapper.deleteById(5L);
    }

    @Test
    void testQueryWapper() {
        //1.构建查询条件
        QueryWrapper<User> wapper = new QueryWrapper<User>()
                .select("id", "username", "info", "balance")
                .like("username", "o")
                .ge("balance", 1000);
        //2.执行查询
        List<User> users = userMapper.selectList(wapper);
        users.forEach(System.out::println);
    }

    @Test
    void testUpdateByQueryWapper() {
        //1.要更新的数据
        User user = new User();
        user.setBalance(10000);
        //2.更新的条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("username", "jack");
        //3.执行更新
        userMapper.update(user, wrapper);
    }

    @Test
    void testUpdateWapper() {
        List<Long> ids = List.of(1L, 2L, 3L, 4L);
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .setSql("balance = balance - 100")
                .in("id", ids);
        userMapper.update(null, wrapper);
    }

    @Test
    void testLambdaQueryWapper() {
        //1.构建查询条件
        LambdaQueryWrapper<User> wapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
                .like(User::getUsername, "o")
                .ge(User::getBalance, 1000);
        //2.执行查询
        List<User> users = userMapper.selectList(wapper);
        users.forEach(System.out::println);
    }

    @Test
    void testCustomSqlUpdate() {
        //1.更新条件
        List<Long> list = List.of(1L, 2L, 4L);
        int amount = 200;
        //2.定义条件
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .in("id", list);
        //3.调用自定义方法
        userMapper.updateBalanceByIds(wrapper, amount);
    }
}