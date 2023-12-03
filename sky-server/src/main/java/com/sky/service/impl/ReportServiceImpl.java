package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;


    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
//        存放从begin到end的每一天日期
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end)){
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

//        存放每天营业额
        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
//            查询当日状态已完成的订单金额，当天时刻
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
//            select sum(amount) from orders where order_time>? and order_time<? and status=5
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover==null?0.0:turnover;
            turnoverList.add(turnover);
        }

        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        //        存放从begin到end的每一天日期
        List<LocalDate> dateList = new ArrayList<>();
        while(!begin.equals(end)){
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
            Integer newnum = userMapper.countByMap(map);
            newnum = newnum==null?0:newnum;
            newUserList.add(newnum);
            map.put("begin", beginTime);
            Integer totalnum = userMapper.countByMap(map);
            totalnum = totalnum==null?0:totalnum;
            totalUserList.add(totalnum);
        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }
}
