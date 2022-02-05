package com.revosystems.llms.reason;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends PagingAndSortingRepository<Reason, Long> {

}
