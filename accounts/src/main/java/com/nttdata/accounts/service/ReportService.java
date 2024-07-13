package com.nttdata.accounts.service;

import java.util.List;

import com.nttdata.accounts.model.Report;

public interface ReportService {

    List<Report> getReport(String startDate, String endDate);

}
