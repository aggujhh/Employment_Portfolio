package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Operations;
import org.example.server.mapper.ReportMapper;
import org.example.server.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportMapper reportMapper;

    @Autowired
    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Operations.ReportData fetchReportDataByCountDays(Integer countDays) {
        LocalDate endDay;
        LocalDate startDay;
        List<LocalDate> dateList = new ArrayList<>();
        List<Integer> salesList;
        List<Integer> customerCountList;
        List<Double> turnoverRateList;

        if (countDays == 3 || countDays == 5) {
            endDay = LocalDate.now();
            startDay = LocalDate.now().minusDays(countDays - 1);
            addDateRangeByDays(dateList, startDay, countDays, 1); // 每天增加 1 天
            Object[] result = fetchReportDataListByDates(dateList, 1);
            salesList = (List<Integer>) result[0];
            customerCountList = (List<Integer>) result[1];
            turnoverRateList = (List<Double>) result[2];

        } else if (countDays == 7) {
            startDay = LocalDate.now().with(DayOfWeek.MONDAY);
            endDay = LocalDate.now().with(DayOfWeek.SUNDAY);
            addDateRangeByDays(dateList, startDay, countDays, 1); // 每天增加 1 天
            Object[] result = fetchReportDataListByDates(dateList, 1);
            salesList = (List<Integer>) result[0];
            customerCountList = (List<Integer>) result[1];
            turnoverRateList = (List<Double>) result[2];
        } else if (countDays == 30) {
            YearMonth currentMonth = YearMonth.now();
            startDay = currentMonth.atDay(1);
            endDay = currentMonth.atEndOfMonth();
            addDateRangeByDays(dateList, startDay, 26, 5); // 每隔 5 天
            dateList.add(endDay);
            Object[] result = fetchReportDataListByDates(dateList, 5);
            salesList = (List<Integer>) result[0];
            customerCountList = (List<Integer>) result[1];
            turnoverRateList = (List<Double>) result[2];
        } else {
            // 获取全年每个月的最后一天
            Year currentYear = Year.now();
            addDateRangeByMonth(dateList, currentYear); // 按月添加每月的最后一天
            // 获取年的第一天
            startDay = currentYear.atDay(1);
            // 获取年的最后一天
            endDay = currentYear.atDay(currentYear.length());
            Object[] result = fetchMonthlySales(Year.now());
            salesList = (List<Integer>) result[0];
            customerCountList = (List<Integer>) result[1];
            turnoverRateList = (List<Double>) result[2];
        }

        LocalDateTime endDateTime = endDay.atTime(23, 59, 59);
        LocalDateTime startDateTime = startDay.atStartOfDay(); // 转为当天的 00:00:00

        List<String> topDishesNameList = new ArrayList<>();
        List<Integer> topDishesTotalCountList = new ArrayList<>();

        List<Operations.TopDishes> topDishes = reportMapper.fetchTopDishesByCountDays(startDateTime, endDateTime);
        topDishes.forEach(topDish -> {
            topDishesNameList.add(topDish.getName());
            topDishesTotalCountList.add(topDish.getTotalCount());
        });


        Operations.ReportData reportData = new Operations.ReportData();
        reportData.setTopDishesNames(topDishesNameList);
        reportData.setTopDishesTotalCounts(topDishesTotalCountList);
        reportData.setDate(dateList);
        reportData.setSales(salesList);
        reportData.setCustomerCount(customerCountList);
        reportData.setTurnoverRate(turnoverRateList);
        return reportData;
    }

    // 方法：按天数间隔添加日期
    private void addDateRangeByDays(List<LocalDate> dateList, LocalDate startDay, int range, int step) {
        for (int i = 0; i < range; i += step) {
            dateList.add(startDay.plusDays(i));
        }
    }

    // 方法：按月份添加每个月的最后一天
    private void addDateRangeByMonth(List<LocalDate> dateList, Year year) {
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year.getValue(), month);
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth(); // 获取每月最后一天
            dateList.add(lastDayOfMonth);
        }
    }

    private Object[] fetchReportDataListByDates(List<LocalDate> dateList, Integer step) {
        List<Integer> salesList = new ArrayList<>();
        List<Integer> customerCountList = new ArrayList<>();
        List<Double> turnoverRateList = new ArrayList<>();
        if (step == 1) {
            dateList.forEach(date -> {
                // 如果 fetchSalesByDate 返回 null，则设置为 0
                Integer sales = reportMapper.fetchSalesByDate(date);
                salesList.add(sales != null ? sales : 0);
                Integer customerCount = reportMapper.fetchCustomerCountByDate(date);
                customerCountList.add(customerCount != null ? customerCount : 0);
                Double turnoverRate = reportMapper.fetchTurnoverRateByDate(date);
                turnoverRateList.add(turnoverRate != null ? turnoverRate : 0);
            });
        } else {
            for (int i = 0; i < dateList.size(); i++) {
                LocalDate startDay = dateList.get(i);
                LocalDate endDay;

                if (i == dateList.size() - 1) {
                    // 最后一个日期，跳过（或按需求自定义逻辑）
                    break;
                } else if (i == dateList.size() - 2) {
                    // 倒数第二次，使用最后两个日期
                    endDay = dateList.get(i + 1);
                } else {
                    // 常规情况，按 step 范围查询
                    endDay = startDay.plusDays(4);
                }

                // 查询销售数据
                fetchMetricsByDateRange(salesList, customerCountList, turnoverRateList, startDay, endDay);
            }
        }
        return new Object[]{salesList, customerCountList, turnoverRateList};
    }

    private Object[] fetchMonthlySales(Year year) {
        List<Integer> salesList = new ArrayList<>();
        List<Integer> customerCountList = new ArrayList<>();
        List<Double> turnoverRateList = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(year.getValue(), month);
            LocalDate startDay = yearMonth.atDay(1); // 每月第一天
            LocalDate endDay = yearMonth.atEndOfMonth(); // 每月最后一天
            // 查询该月份的销售额
            fetchMetricsByDateRange(salesList, customerCountList, turnoverRateList, startDay, endDay);
        }
        return new Object[]{salesList, customerCountList, turnoverRateList};
    }

    private void fetchMetricsByDateRange(List<Integer> salesList, List<Integer> customerCountList, List<Double> turnoverRateList, LocalDate startDay, LocalDate endDay) {
        Integer sales = reportMapper.fetchSalesBetweenDate(startDay, endDay);
        salesList.add(sales != null ? sales : 0);
        Integer customerCount = reportMapper.fetchCustomerCountBetweenDate(startDay, endDay);
        customerCountList.add(customerCount != null ? customerCount : 0);
        Double turnoverRate = reportMapper.fetchTurnoverRateBetweenDate(startDay, endDay);
        turnoverRateList.add(turnoverRate != null ? turnoverRate : 0);
    }
}