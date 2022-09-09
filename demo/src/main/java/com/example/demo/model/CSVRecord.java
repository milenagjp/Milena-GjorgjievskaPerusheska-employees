package com.example.demo.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CSVRecord {

    @CsvBindByName(column = "EmployeeID")
    private Long employeeId;

    @CsvBindByName(column = "ProjectID")
    private Long projectId;

    @CsvCustomBindByName(column = "DateFrom", converter = LocalDateConverter.class)
    private LocalDate dateFrom;

    @CsvCustomBindByName(column = "DateTo", converter = LocalDateConverter.class)
    private LocalDate dateTo;

}
