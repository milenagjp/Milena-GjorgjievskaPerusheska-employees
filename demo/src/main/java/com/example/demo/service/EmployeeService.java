package com.example.demo.service;

import com.example.demo.model.CSVRecord;
import com.example.demo.model.EmployeePair;

import java.util.List;

public interface EmployeeService {

    List<EmployeePair> calculateMaxEmployeePairPerProject(List<CSVRecord> csvRecordList);

}
