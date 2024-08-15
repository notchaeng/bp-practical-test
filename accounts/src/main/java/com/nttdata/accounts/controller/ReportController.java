package com.nttdata.accounts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.accounts.domain.model.Report;
import com.nttdata.accounts.service.ReportService;

@RestController
@RequestMapping("/")
public class ReportController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/reportes", params = {"fechaInicio", "fechaFin", "identificacion"})
    public ResponseEntity<List<Report>> getReport(@RequestParam(value = "fechaInicio") String startDate,
            @RequestParam(value = "fechaFin") String endDate, @RequestParam(value = "identificacion") String identification) {
        return ResponseEntity.ok(reportService.getReport(startDate, endDate, identification));
    }
}
