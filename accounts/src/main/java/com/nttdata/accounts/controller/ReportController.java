package com.nttdata.accounts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.accounts.domain.model.Report;
import com.nttdata.accounts.domain.model.Response;
import com.nttdata.accounts.service.ReportService;
import com.nttdata.accounts.util.ResourseApplication;

@RestController
@RequestMapping("/")
public class ReportController {

    public static Logger logger = LogManager.getLogger();

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/reportes", params = {"fechaInicio", "fechaFin"})
    public ResponseEntity<Response> getReport(@RequestParam(value = "fechaInicio") String startDate,
            @RequestParam(value = "fechaFin") String endDate) {
        Response response = new Response();
        List<Report> report;
        try {
            report = reportService.getReport(startDate, endDate);
            response.setResponse(response, ResourseApplication.properties.getProperty("success.query"), report);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while executing getReport method.", e.getMessage());
            response.setResponse(response, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
