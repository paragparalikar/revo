package com.revosystems.llms;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "STATION_STATE_CHANGE", schema = "mysql")
public class StationStateChange {

	@Id 
	@GeneratedValue
	private Long id;
	
	@NonNull 
	@Column(nullable = false, updatable = false)
	private Long stationId;
	
	@NonNull 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date timestamp;
	
	@NonNull 
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private StationState state;
	
	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private BreakdownType type;
	
}
