package com.parking.nl.scheduler;

import com.opencsv.CSVWriter;
import com.parking.nl.common.CommonUtil;
import com.parking.nl.data.model.UnRegsiteredVehicles;
import com.parking.nl.service.ReportSchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.parking.nl.common.Constants.COMMA;
import static com.parking.nl.common.Constants.REPORTS_HEADER;

@Component
@Slf4j
public class ReportScheduler {
    private ReportSchedulerService service;
    private SchedulerConfig schedulerConfig;

    @Autowired
    public ReportScheduler(final ReportSchedulerService service,final SchedulerConfig schedulerConfig) {
        this.service = service;
        this.schedulerConfig = schedulerConfig;
    }

    @Scheduled(cron = "${report.cron}")
    public void perform() {
        log.info("Scheduled task run started successfully.");
        List<UnRegsiteredVehicles> unRegsiteredVehicles = service.findByIsNotified(Boolean.FALSE);
        List<UnRegsiteredVehicles> sortedList = unRegsiteredVehicles.stream().sorted(Comparator.comparing(UnRegsiteredVehicles::getObservationTime).reversed()).collect(Collectors.toList());
        generateReport(sortedList);
        service.save(sortedList);
        log.info("Scheduled task completed successfully.");
    }
    private void generateReport(List<UnRegsiteredVehicles> unRegsiteredVehicles){
        if(unRegsiteredVehicles.isEmpty()){
            log.info("No vehicle is unregistered for penalty process");
        }else {
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
            String csvFilePath = schedulerConfig.getReportLocation() + "/unregistered_vehicles_report_" + currentDateTime + ".csv";
            List<String[]> data = new ArrayList<String[]>();
           try{
               CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFilePath), COMMA.charAt(0), CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
               data.add(REPORTS_HEADER);
               for (UnRegsiteredVehicles response : unRegsiteredVehicles) {
                   String[] row = {response.getLicensePlateNumber(), CommonUtil.formateDate(response.getObservationTime()),response.getStreetName()};
                   data.add(row);
               }
               csvWriter.writeAll(data);
               csvWriter.flush();
               csvWriter.close();
           }catch (IOException e){
               log.error("Error occurred while generating report", e);
           }
        }
    }
}