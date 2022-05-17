package com.revo.llms.reason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, Long> {

}
