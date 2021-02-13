package com.revosystems.cbms.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.revosystems.cbms.domain.enumeration.Channel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfiguration {

	@Id
	@Enumerated(EnumType.STRING)
	private Channel channel;
	
	@ManyToOne(optional = false)
	private Thing thing;
	
	@ManyToOne(optional = false)
	private Sensor sensor;

}
