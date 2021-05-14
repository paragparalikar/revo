package com.revosystems.cbms.web.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SeriesRequestDto {

	@NotNull private Long thingId;
	@NotNull private Long sensorId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@NotNull private Date from;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
	@NotNull private Date to;
	
}
