package com.revosystems.llms.reason;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@RestResource
public interface ReasonRepository extends PagingAndSortingRepository<Reason, String> {

}
