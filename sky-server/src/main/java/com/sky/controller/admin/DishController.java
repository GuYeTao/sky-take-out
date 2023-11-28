package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
//        新增菜品默认禁售，可以不用清楚缓存
//        清理redis缓存
//        String key = "dish_"+dishDTO.getCategoryId();
//        clearCache(key);
        return Result.success();
    }


    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}", dishPageQueryDTO);

        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

//    RequestParam让mvc框架解析前端传来的1，2，3
    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品删除：{}", ids);
        dishService.deleteBatch(ids);
//        有可能会涉及多个菜品分类，到底清除哪个还需要查询，反而降低效率
//        直接删除所有dish_
        clearCache("dish_*");
        return Result.success();
    }


//    单一的dish不含有口味信息，所以要dishvo
    @GetMapping("/{id}")
    @ApiOperation("查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("查询菜品：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);

        return Result.success(dishVO);
    }


    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
//        修改名称等没有大影响，但有可能修改分类，比较麻烦
//        直接删除所有dish_
        clearCache("dish_*");
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启售禁售菜品")
    public Result startOrStop(@PathVariable("status") Integer status, Long id){
        log.info("启售禁售菜品：{},{}", status, id);
        dishService.startOrStop(status, id);

//        直接删除所有dish_
        clearCache("dish_*");
        return Result.success();
    }


    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        log.info("根据分类id查询菜品：{}",categoryId);
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void clearCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
