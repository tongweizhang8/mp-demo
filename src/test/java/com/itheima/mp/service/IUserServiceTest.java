package com.itheima.mp.service;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.po.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class IUserServiceTest {
    @Autowired
    private IUserService userService;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setId(5L);
        user.setUsername("Lucy");
        user.setPassword("123");
        user.setPhone("18688990011");
        user.setBalance(200);
        //user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
        user.setInfo(UserInfo.of(24, "英文老师", "female"));
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.save(user); //新增
    }

    @Test
    void testQuery() {
        List<User> users = userService.listByIds(List.of(1L, 2L, 4L));
        users.forEach(System.out::println);
    }

    @Test
    void testPageQuery() {
        int pageNo = 1,pageSize = 2;
        //1.准备分页条件
        //1.1分页条件
        Page<User> page = Page.of(pageNo, pageSize);
        //1.2排序条件
        page.addOrder(new OrderItem("balance", true));
        page.addOrder(new OrderItem("id", false));
        //2.分页查询
        Page<User> userPage = userService.page(page);
        //3.解析
        Long total = userPage.getTotal();
        System.out.println("total = " + total);
        Long pages = userPage.getPages();
        System.out.println("pages = " + pages);
        List<User> records = userPage.getRecords();
        records.forEach(System.out::println);
    }

}