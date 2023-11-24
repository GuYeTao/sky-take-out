package com.sky.service;

import com.sky.dto.DishDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;

public interface DishService {


    /*
    * 新增菜品和口味
    * */
    public void saveWithFlavor(DishDTO dishDTO);
}
