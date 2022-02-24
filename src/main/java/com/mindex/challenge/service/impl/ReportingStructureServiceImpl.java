package com.mindex.challenge.service.impl;

import java.util.List;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.impl.EmployeeServiceImpl;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeService employeeService;

    @Override
    public ReportingStructure read(String id) {
        LOG.debug("Finding reporting structure with employee id [{}]", id);

        // This could be incorrect.
        // My thought process is that we need to get an employee given an ID, which
        // the employeeService already knows how to do.  Instead of rewriting code, lets leverage
        // that knowledge here.
        ReportingStructure reportingStructure = new ReportingStructure();
        Employee employee = employeeService.read(id);

        reportingStructure.setEmployee(employee);
        reportingStructure.setNumberOfReports(countDirectReports(employee));

        return reportingStructure;
    }

    private int countDirectReports(Employee employee) {
        List<Employee> directReports = employee.getDirectReports();
        int numDirectReports = 0;
        
        if(directReports == null) {
            return 0;
        }

        // We are essentially dealing with a nested-tree problem. The easiest
        // way to count every entry is to recursively count each direct report's
        // direct reports until there are no more direct reports to count.
        for(int i = 0; i < directReports.size(); i++) {
            Employee temp = directReports.get(i);
            // Since the directReports field only holds the employeeIds,
            // we must query the other data from the database
            temp = employeeService.read(temp.getEmployeeId());

            numDirectReports += 1 + countDirectReports(temp);
        }
        return numDirectReports;
    }
}
