package com.mindex.challenge.dao;

import java.util.List;
import com.mindex.challenge.data.Compensation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    // Because of the assumption that one employee can have "many" compensation
    // entries (assuming older ones are historical records), the compensation
    // query will return a list containing all of those compensations.
    List<Compensation> findCompensationByEmployeeId(String employeeId);
}
