package com.mindex.challenge.service.impl;

import java.util.List;
import java.util.ArrayList;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/reportingstructure/{id}";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
    }

    @Test
    public void testReadReportingStructure() {
        Employee j1 = new Employee();
        Employee j2 = new Employee();
        Employee j3 = new Employee();
        Employee j4 = new Employee();
        List<Employee> dr;
        // Test tree will look as follows:
        //          j1
        //         /  \
        //       j2   j3
        //             \
        //             j4
        //
        // So the resullts should be
        // numberOfReports(j1) == 3,
        // numberOfReports(j2) == 0,
        // numberOfReports(j3) == 1,
        // numberOfReports(j4) == 0,
        setEmployeeDetails(j1, "John I");
        setEmployeeDetails(j2, "John II");
        setEmployeeDetails(j3, "John III");
        setEmployeeDetails(j4, "John IV");

        j4 = restTemplate.postForEntity(employeeUrl, j4, Employee.class).getBody();
        j2 = restTemplate.postForEntity(employeeUrl, j2, Employee.class).getBody();
        assertNotNull(j4.getEmployeeId());
        assertNotNull(j2.getEmployeeId());

        // Create J3 with J4 as his underling
        dr = new ArrayList<Employee>();
        dr.add(j4);
        j3.setDirectReports(dr);
        j3 = restTemplate.postForEntity(employeeUrl, j3, Employee.class).getBody();
        assertNotNull(j3.getEmployeeId());

        // Create J1 with J2 & J3 as his underlings
        dr = new ArrayList<Employee>();
        dr.add(j2);
        dr.add(j3);
        j1.setDirectReports(dr);
        j1 = restTemplate.postForEntity(employeeUrl, j1, Employee.class).getBody();
        assertNotNull(j1.getEmployeeId());

        ReportingStructure result = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, j1.getEmployeeId()).getBody();
        assertEmployeeEquivalence(j1, result.getEmployee());
        assertEquals(result.getNumberOfReports(), 3);

        result = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, j2.getEmployeeId()).getBody();
        assertEmployeeEquivalence(j2, result.getEmployee());
        assertEquals(result.getNumberOfReports(), 0);

        result = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, j3.getEmployeeId()).getBody();
        assertEmployeeEquivalence(j3, result.getEmployee());
        assertEquals(result.getNumberOfReports(), 1);

        result = restTemplate.getForEntity(reportingStructureUrl, ReportingStructure.class, j4.getEmployeeId()).getBody();
        assertEmployeeEquivalence(j4, result.getEmployee());
        assertEquals(result.getNumberOfReports(), 0);
    }
    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void setEmployeeDetails(Employee employee, String name) {
        employee.setFirstName(name);
        employee.setLastName("Doe");
        employee.setDepartment("Engineering");
        employee.setPosition("Developer");
    }
}

