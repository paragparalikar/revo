package com.revosystems.cbms.domain.model;

import com.revosystems.cbms.domain.enumeration.Channel;
import com.revosystems.cbms.domain.intf.Identifiable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelConfiguration implements Identifiable<Channel> {

	private Channel channel;
	
	private Thing thing;
	
	private Sensor sensor;

	@Override
	public Channel getId() {
		return getChannel();
	}

	@Override
	public void setId(Channel id) {
		setChannel(id);
	}

}
