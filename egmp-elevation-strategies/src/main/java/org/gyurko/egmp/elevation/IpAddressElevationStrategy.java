package org.gyurko.egmp.elevation;

import org.gyurko.egmp.core.DummyElevationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 EGMP - Extensible Group Management Protocol

 Copyright (C) 2013-2015  Szabolcs Gyurko <szabolcs@gyurko.org>

 This file is part of EGMP project.

 EGMP is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, version 2 of the License, but not
 any later version.

 EGMP is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with EGMP.  If not, see <http://www.gnu.org/licenses/>.
 */
public class IpAddressElevationStrategy extends DummyElevationStrategy {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(IpAddressElevationStrategy.class);
    /** bytes in java are signed, therefore if a negative number comes out, we have to do binary complement operation */
    private static final long BYTE_OVERFLOW_VALUE = 127;

    @Override
    public long getElevationLevel() {
        InetAddress ip = null;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface current = interfaces.nextElement();
                LOGGER.trace("Checking: {} P2P: {}, LOOP: {}, Virtual: {}, Up: {}", current, current.isPointToPoint(), current.isLoopback(), current.isVirtual(), current.isUp());
                if (!current.isUp() || current.isLoopback() || current.isVirtual() || current.isPointToPoint()) continue;
                LOGGER.trace("Matching interface: {}", current);
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress currentAddress = addresses.nextElement();
                    LOGGER.trace("Checking address: {}", currentAddress);
                    if (currentAddress.isLoopbackAddress() || (!(currentAddress instanceof Inet4Address) && !(currentAddress instanceof Inet6Address))) continue;
                    ip = currentAddress;
                }
            }
        } catch (SocketException e) {
            LOGGER.warn("Cannot get local host IP");
        }

        if (ip == null) {
            try {
                ip = Inet4Address.getLocalHost();
            } catch (UnknownHostException e) {
                LOGGER.warn("Cannot get local host IP");
                LOGGER.error("Returning 0 as elevation number due to failure detecting IP address of the node.");
                return 0;
            }
        }
        LOGGER.debug("IP address of node: {} {}", ip.getHostAddress(), ip.getAddress());

        long elevation = ip.getAddress()[ip.getAddress().length - 1];
        if (elevation < 0) elevation = Math.abs(elevation) + BYTE_OVERFLOW_VALUE;

        LOGGER.debug("Elevation level based on IP address: {}", elevation);

        return elevation;
    }

    @Override
    public String getDescription() {
        return "IPv4 address Elevation Strategy";
    }
}
