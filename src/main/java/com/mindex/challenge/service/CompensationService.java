package com.mindex.challenge.service;

import java.util.List;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation create(Compensation compensation);
    // Because of a many to one compensation to employee mapping,
    // compensation is returned as a list.
    List<Compensation> read(String employeeId);
}
