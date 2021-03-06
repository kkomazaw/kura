/*******************************************************************************
 * Copyright (c) 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/
package org.eclipse.kura.asset.provider.test;

import static org.eclipse.kura.driver.DriverFlag.READ_SUCCESSFUL;
import static org.eclipse.kura.driver.DriverFlag.WRITE_SUCCESSFUL;

import java.util.List;
import java.util.Map;

import org.eclipse.kura.driver.ChannelDescriptor;
import org.eclipse.kura.driver.Driver;
import org.eclipse.kura.driver.DriverConstants;
import org.eclipse.kura.driver.DriverEvent;
import org.eclipse.kura.driver.DriverRecord;
import org.eclipse.kura.driver.DriverStatus;
import org.eclipse.kura.driver.listener.DriverListener;
import org.eclipse.kura.type.DataType;
import org.eclipse.kura.type.TypedValues;

/**
 * Stub Driver implementation required for test
 */
public final class StubDriver implements Driver {

    /** flag to check if driver is connected */
    private boolean isConnected;

    /** {@inheritDoc} */
    @Override
    public void connect() throws ConnectionException {
        this.isConnected = true;
    }

    /** {@inheritDoc} */
    @Override
    public void disconnect() throws ConnectionException {
        this.isConnected = false;
    }

    /** {@inheritDoc} */
    @Override
    public ChannelDescriptor getChannelDescriptor() {
        return new StubChannelDescriptor();
    }

    /** {@inheritDoc} */
    @Override
    public List<DriverRecord> read(final List<DriverRecord> records) throws ConnectionException {
        if (!this.isConnected) {
            this.connect();
        }

        for (final DriverRecord record : records) {
            final Map<String, Object> driverRecordConf = record.getChannelConfig();
            switch ((DataType) driverRecordConf.get(DriverConstants.CHANNEL_VALUE_TYPE.value())) {
            case BOOLEAN:
                record.setValue(TypedValues.newBooleanValue(true));
                break;
            case FLOAT:
                record.setValue(TypedValues.newFloatValue(1f));
                break;
            case LONG:
                record.setValue(TypedValues.newLongValue(1L));
                break;
            case BYTE_ARRAY:
                record.setValue(TypedValues.newByteArrayValue("dummy".getBytes()));
                break;
            case DOUBLE:
                record.setValue(TypedValues.newDoubleValue(1.0));
                break;
            case STRING:
                record.setValue(TypedValues.newStringValue("dummy"));
                break;
            case INTEGER:
                record.setValue(TypedValues.newIntegerValue(1));
                break;
            default:
                break;
            }
            record.setDriverStatus(new DriverStatus(READ_SUCCESSFUL, null, null));
        }
        return records;
    }

    /** {@inheritDoc} */
    @Override
    public void registerDriverListener(final Map<String, Object> channelConfig, final DriverListener listener)
            throws ConnectionException {
        final DriverRecord record = new DriverRecord();
        record.setChannelConfig(channelConfig);
        record.setValue(TypedValues.newIntegerValue(1));
        record.setDriverStatus(new DriverStatus(READ_SUCCESSFUL, null, null));
        record.setTimestamp(System.currentTimeMillis());
        listener.onDriverEvent(new DriverEvent(record));
    }

    /** {@inheritDoc} */
    @Override
    public void unregisterDriverListener(final DriverListener listener) throws ConnectionException {
        // not used
    }

    /** {@inheritDoc} */
    @Override
    public List<DriverRecord> write(final List<DriverRecord> records) throws ConnectionException {
        if (!this.isConnected) {
            this.connect();
        }

        for (final DriverRecord record : records) {
            record.setDriverStatus(new DriverStatus(WRITE_SUCCESSFUL, null, null));
        }
        return records;
    }

}
