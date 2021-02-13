package com.revosystems.cbms.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.revosystems.cbms.domain.model.ChannelConfiguration;

@Repository
public interface ChannelConfigurationRepository extends CrudRepository<ChannelConfiguration, Long> {

}
