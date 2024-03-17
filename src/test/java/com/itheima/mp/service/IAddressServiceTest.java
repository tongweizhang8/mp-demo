package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class IAddressServiceTest {

    @Autowired
    private IAddressService addressService;

    @Test
    void testLogicDelete() {
        //删除
        addressService.removeById(59L);
        //查询
        Address byId = addressService.getById(59L);
        System.out.println(byId);
    }
}
