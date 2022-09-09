package com.example.demo.service;

import com.example.demo.model.CSVRecord;
import com.example.demo.model.EmployeePair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public List<EmployeePair> calculateMaxEmployeePairPerProject(List<CSVRecord> csvRecordList) {

        List<EmployeePair> employeePairList = new ArrayList<>();


        for (int i = 0; i < csvRecordList.size(); i++) {
            for (int j = i + 1; j < csvRecordList.size(); j++) {
                CSVRecord firstRecord = csvRecordList.get(i);
                CSVRecord nextRecord = csvRecordList.get(j);

                //has same project
                if (firstRecord.getProjectId().equals(nextRecord.getProjectId())) {

                    //have same between period (start1 <= end2) and (end1 >= start2)
                    if ((firstRecord.getDateFrom().isBefore(nextRecord.getDateTo())
                            || firstRecord.getDateFrom().isEqual(nextRecord.getDateTo()))
                            && (firstRecord.getDateTo().isAfter(nextRecord.getDateFrom())
                            || firstRecord.getDateTo().isEqual(nextRecord.getDateFrom()))) {

                        //getting total start and end for both employees
                        LocalDate start = firstRecord.getDateFrom().isBefore(nextRecord.getDateFrom()) ? nextRecord.getDateFrom() : firstRecord.getDateFrom();
                        LocalDate end = firstRecord.getDateTo().isBefore(nextRecord.getDateTo()) ? firstRecord.getDateTo() : nextRecord.getDateTo();

                        long period = ChronoUnit.DAYS.between(start, end);

                        if (period > 0) {
                            insertIntoEmployeeList(employeePairList, firstRecord, nextRecord, period);

                        }

                    }

                }

            }
        }

        return employeePairList.stream().sorted(Comparator.comparing(EmployeePair::getProjectID).reversed())
                .collect(Collectors.toList());
    }

    private void insertIntoEmployeeList(List<EmployeePair> employeePairList, CSVRecord firstRecord, CSVRecord nextRecord, long period) {
        EmployeePair newEmployeePair = new EmployeePair();

        if (employeePairList.size() != 0) {
            for (EmployeePair employeePair : employeePairList) {
                if (isRecordPresent(employeePair, firstRecord.getEmployeeId(), nextRecord.getEmployeeId())) {
                    if (employeePair.getDaysWorked() < period) {
                        employeePair.setDaysWorked(period);
                    }
                } else {
                    newEmployeePair = createNewEmployee(firstRecord, nextRecord, period);
                    employeePairList.add(newEmployeePair);
                }
            }
        } else {
            newEmployeePair = createNewEmployee(firstRecord, nextRecord, period);

        }
        employeePairList.add(newEmployeePair);
    }

    private EmployeePair createNewEmployee(CSVRecord firstRecord, CSVRecord nextRecord, long period) {

        EmployeePair newEmployeePair = new EmployeePair();

        newEmployeePair.setEmployeeOne(firstRecord.getEmployeeId());
        newEmployeePair.setEmployeeTwo(nextRecord.getEmployeeId());
        newEmployeePair.setProjectID(firstRecord.getProjectId());
        newEmployeePair.setDaysWorked(period);

        return newEmployeePair;
    }

    private boolean isRecordPresent(EmployeePair employeePair, Long employeeId1, Long employeeId2) {
        return (employeePair.getEmployeeOne() == employeeId1
                && employeePair.getEmployeeTwo() == employeeId2)
                || (employeePair.getEmployeeOne() == employeeId2
                && employeePair.getEmployeeTwo() == employeeId1);
    }
}
