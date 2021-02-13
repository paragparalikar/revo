package com.revosystems.cbms.web.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class SeriesRequestDto {

	@NotNull private Long thingId;
	@NotNull private Long sensorId;
	@NotNull private Date from;
	@NotNull private Date to;
	
}
