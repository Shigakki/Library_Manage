package com.wen.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wen.common.JwtUtil;
import com.wen.common.Result;
import com.wen.entity.User;
import com.wen.mapper.UserMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Api(value = "user-controller")
public class UserController {
    @Resource
    UserMapper userMapper;

//    添加用户的
    @PostMapping
    @ApiOperation(value = "添加用户")
    public Result<?> save(@RequestBody User user){
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return Result.success();
    }
//    更新
    @PutMapping
    @ApiOperation(value = "更新用户")
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }
    //    删除
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);
        return Result.success();
    }
    //    根据id查询
    @GetMapping ("/{id}")
    @ApiOperation(value = "根据用户的id查询用户")
    public Result<?> getById(@PathVariable Long id){
        User user = userMapper.selectById(id);
        return Result.success(user);
    }
//    查询
    @GetMapping
    @ApiOperation(value = "进行分页查询")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)){
            wrapper.like(User::getUsername,search);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(userPage);
    }

    //添加JWT
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public Result<?> login(@RequestBody User user){//这只是传进来的user，并没有在数据库中进行查找
        System.out.println("<<<<<<consumer调用了！！" + "穿过来的对象是：" + user);
        System.out.println("<<<<<<consumer调用了！！" + "穿过来的对象是：" + user.getUsername());
        System.out.println("<<<<<<consumer调用了！！" + "穿过来的对象是：" + user.getPassword());
        // 最好别有重名的用户
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if (res==null){
            return Result.error("-1","用户名或密码错误");
        }
        Map<String,String> info = new HashMap<>();
        String token = JwtUtil.createJWT(UUID.randomUUID().toString(), user.getRole(), null);
        info.put("token",token);
        System.out.println(">>>>>>完成了登录，好耶！！");
        res.setToken(token);
        Result<?> result = Result.success(res);
        System.out.println("---->这次登录的token:"+res.getUsername()+token+"\n");

        // token写进数据库中
        userMapper.updateById(res);

        return result;
    }
    //注册
    @PostMapping("/register")
    @ApiOperation(value = "用户注册")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if (res!=null){
            return Result.error("-1","用户名重复");
        }
        if (user.getPassword()==null){
            user.setPassword("861200");
        }
        userMapper.insert(user);
        return Result.success();
    }
}
