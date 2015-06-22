/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.paradox.handler;

import static org.openhab.binding.paradox.ParadoxBindingConstants.*;

import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link ParadoxHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 * 
 * @author Eric Bissonnette - Initial contribution
 */
public class ParadoxHandler extends BaseThingHandler {

    private Logger logger = LoggerFactory.getLogger(ParadoxHandler.class);

	public ParadoxHandler(Thing thing) {
		super(thing);
		
		logger.debug("Test");
	}

	@Override
	public void handleCommand(ChannelUID channelUID, Command command) {
        if(channelUID.getId().equals(CHANNEL_1)) {
            // TODO: handle command
        }
        if(channelUID.getId().equals(CHANNEL_2)) {
            // TODO: handle command
        }
	}
}
