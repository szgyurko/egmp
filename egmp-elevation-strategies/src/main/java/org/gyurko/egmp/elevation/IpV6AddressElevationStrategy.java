package org.gyurko.egmp.elevation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;

/**
 *
 * @author Szabolcs Gyurko <szabolcs@gyurko.org>
 */
public class IpV6AddressElevationStrategy extends IpAddressElevationStrategy {
    /** Class level logging */
    private static final Logger LOGGER = LoggerFactory.getLogger(IpV6AddressElevationStrategy.class);
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
                    if (currentAddress.isLoopbackAddress() || !(currentAddress instanceof Inet6Address)) continue;
                    ip = currentAddress;
                }
            }
        } catch (SocketException e) {
            LOGGER.warn("Cannot get local host IP");
        }

        if (ip == null) {
            LOGGER.warn("Falling back to InetAddress.getLocalHost() which might result in an IPv4 address! That might work as well, but check your config!");
            try {
                ip = Inet6Address.getLocalHost();
            } catch (UnknownHostException e) {
                LOGGER.warn("Cannot get local host IP");
                LOGGER.error("Returning 0 as elevation number due to failure detecting IP address of the node.");
                return 0;
            }
        }
        LOGGER.debug("IP address of node: {} {}", ip.getHostAddress(), ip.getAddress());

        long elevation = ip.getAddress()[ip.getAddress().length - 1];
        if (elevation < 0) elevation += BYTE_OVERFLOW_VALUE;

        LOGGER.debug("Elevation level based on IP address: {}", elevation);

        return elevation;
    }

    @Override
    public String getDescription() {
        return "IPv6 address Elevation Strategy";
    }
}
