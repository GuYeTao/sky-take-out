package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

public interface DishService {


    /*
    * 新增菜品和口味
    * */
    public void saveWithFlavor(DishDTO dishDTO);

    /*
    * 菜品分页查询
    * */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /*
    * 菜品批量删除
    * */
    void deleteBatch(List<Long> ids);


    /*
    * 根据菜品id查询菜品和口味
    * */
    DishVO getByIdWithFlavor(Long id);


    /*
    * 根据菜品id修改
    * */
    void updateWithFlavor(DishDTO dishDTO);
}
