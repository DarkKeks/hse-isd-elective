package ru.darkkeks.isdelectivebackend;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface ConversionOperationRepository extends CrudRepository<ConversionOperation, Integer> {
}
