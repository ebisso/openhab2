/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.paradox.handler;

import static org.openhab.binding.paradox.ParadoxBindingConstants.*;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.eclipse.smarthome.config.core.Configuration;
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

    private final Logger logger = LoggerFactory.getLogger(ParadoxHandler.class);
    private final int connectInterval = 5000; // Time between reconnection attempts
    private final int connectTimeout = 4000; // Time to try opening CommPort, should be less than connectInterval
    private final Runnable checkConnectionRunnable = new Runnable() { 
        @Override
        public void run() {
            checkConnection();
        }
    };
    private final SerialPortEventListener serialPortEventListener = new SerialPortEventListener() {

        @Override
        public void serialEvent(SerialPortEvent event) {
            handleSerialPortEvent(event);			
        }    	
    };

    private boolean connected = false;
    private String serialPortName = "";
    private SerialPort serialPort = null;
    private DataInputStream inputStream = null;

    public ParadoxHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void initialize() {
        scheduler.scheduleAtFixedRate(checkConnectionRunnable, 0, connectInterval, TimeUnit.MILLISECONDS);
        Configuration configuration = getThing().getConfiguration();
        serialPortName = (String)configuration.get(CONFIGURATION_SERIALPORTNAME);
    }
    
    @Override
    public void dispose() {
        if (connected) {
            disconnect();
        }
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

    private synchronized void checkConnection() {
        if (!connected) {
            connect();
        }		
    }

    private void connect() {
        try {
            CommPortIdentifier commPortIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
            CommPort commPort = commPortIdentifier.open(BINDING_ID, connectTimeout);
            SerialPort newSerialPort = (SerialPort)commPort;
            newSerialPort.setSerialPortParams(9600, SerialPort.DATABITS_8 , SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            newSerialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            newSerialPort.addEventListener(serialPortEventListener);
            newSerialPort.notifyOnDataAvailable(true);
            DataInputStream newInputStream = new DataInputStream(newSerialPort.getInputStream());
            serialPort = newSerialPort;
            inputStream = newInputStream;
            connected = true;
        } catch (Exception e) {
            logger.warn("Failed to open serial port");
        };
    }
    
    private void disconnect() {
        serialPort.removeEventListener();
        serialPort.close();
        serialPort = null;
        inputStream = null;
        connected = false;
    }

    private synchronized void handleSerialPortEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                while (inputStream.available() > 0) {
                    logger.debug("<< %d\r\n", inputStream.read());
                }
            } catch (IOException e) {
                logger.error("Exception caught reading from serial port");
            }
        }
    }
}

