package com.revo.llms.ticket;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.revo.llms.department.Department;
import com.revo.llms.part.Part;
import com.revo.llms.reason.Reason;
import com.revo.llms.station.Station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.With;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull 
	@ManyToOne(optional = false)
	private Station station;
	
	@With
	@NonNull 
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TicketStatus status;
	
	@NonNull
	@ManyToOne(optional = false)
	private Department department;
	
	@ManyToOne
	private Reason reason;
	
	@ManyToOne
	private Part part;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, updatable = false)
	private Date openTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date closedTimestamp;
	
	public boolean isOpen() {
		return TicketStatus.OPEN.equals(status);
	}
	
	public boolean isClosed() {
		return TicketStatus.CLOSED.equals(status);
	}
	
}
