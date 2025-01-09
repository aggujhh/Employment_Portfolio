package org.example.server.service;

import org.example.pojo.entity.Operations;

public interface ReportService {
    Operations.ReportData fetchReportDataByCountDays(Integer countDays);
}
