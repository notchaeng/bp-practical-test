package com.nttdata.accounts.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.accounts.exception.ResourceNotFoundException;
import com.nttdata.accounts.model.Report;
import com.nttdata.accounts.repository.MovementRepository;
import com.nttdata.accounts.service.ReportService;
import com.nttdata.accounts.util.ResourseApplication;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MovementRepository movementRepository;

    @Override
    public List<Report> getReport(String startDate, String endDate) {
        List<Report> report = movementRepository.findReport(startDate, endDate);
        if (report.isEmpty()) {
            throw new ResourceNotFoundException(ResourseApplication.properties.getProperty("report.not.found"));
        }
        return report;
    }

}
