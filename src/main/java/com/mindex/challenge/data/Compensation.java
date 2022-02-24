package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    // In the event that compensation and employee have a one-to-one mapping, then
    // the primary key of compensation could be the employee ID.  Since there's an
    // effective date, I'm going to assume here that an employee can have many compensation records
    // and perhaps the current _effective_ compensation is the one with the greatest effectiveDate?|

    // Mongodb seems to handle creating a unique id in the underlying database anyway, so we are
    // not storing a publicly visible one here.

    private String employeeId;
    private double salary;
    private Date effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
