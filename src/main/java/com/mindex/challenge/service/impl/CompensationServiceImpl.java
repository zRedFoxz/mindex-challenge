package com.mindex.challenge.service.impl;

import java.util.List;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        String employeeId = compensation.getEmployeeId();
        Employee employee;

        // In the event that an invalid employee ID is added to the compensation data
        // employeeService.read will throw a runtime exception. Catch that and add a little
        // more detail to the exception that occurred.
        try {
            employee = employeeService.read(employeeId);
        } catch(RuntimeException e){
            throw new RuntimeException("Cannot save compensation - invalid employeeId: " + employeeId);
        }

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public List<Compensation> read(String id) {
        LOG.debug("Reading compensations with id [{}]", id);

        List<Compensation> compensation = compensationRepository.findCompensationByEmployeeId(id);

        if (compensation.size() == 0) {
            throw new RuntimeException("Compensation not found for employeeId: " + id);
        }

        return compensation;
    }
}
