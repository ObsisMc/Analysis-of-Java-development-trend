package com.sustech.cs209a_project;

import com.sustech.cs209a_project.mapper.UserMapper;
import com.sustech.cs209a_project.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class mybatisPlusTest {
    @Resource
    private UserMapper userMapper;

    @Test
    public void test(){
        List<User> list = userMapper.selectList(null);
        list.forEach(System.out::println);
    }

}
