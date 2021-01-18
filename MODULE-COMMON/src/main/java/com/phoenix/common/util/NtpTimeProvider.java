/*
 * MIT License
 *
 * Copyright (c) 2020 Đình Tài
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.phoenix.common.util;

import com.phoenix.common.exception.runtime.TimeProviderException;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Getting the time from an NTP Server (Network time protocol)
 * If the system clock cannot be used to accurately get the current time, then you can fetch it from an NTP server with
 * the com.phoenix1.totp.time.NtpTimeProvider class, passing in the NTP server hostname you wish you use.
 *
 *
 *
 * The Network Time Protocol (NTP) is a networking protocol for clock synchronization between computer systems over
 * packet-switched, variable-latency data networks. In operation since before 1985, NTP is one of the oldest Internet
 * protocols in current use. NTP was designed by David L. Mills of the University of Delaware.
 *
 * NTP is intended to synchronize all participating computers to within a few milliseconds of Coordinated Universal Time
 * (UTC).[1]:3 It uses the intersection algorithm, a modified version of Marzullo's algorithm, to select accurate time
 * servers and is designed to mitigate the effects of variable network latency. NTP can usually maintain time to within
 * tens of milliseconds over the public Internet, and can achieve better than one millisecond accuracy in local area
 * networks under ideal conditions. Asymmetric routes and network congestion can cause errors of 100 ms or more.
 */
public class NtpTimeProvider implements TimeProvider {

    private final NTPUDPClient client;
    private final InetAddress ntpHost;

    public NtpTimeProvider(String ntpHostname) throws UnknownHostException {
        // default timeout of  3 seconds
        this(ntpHostname, 3000);
    }

    public NtpTimeProvider(String ntpHostname, int timeout) throws UnknownHostException {
        this(ntpHostname, timeout, "org.apache.commons.net.ntp.NTPUDPClient");
    }

    // Package-private, for tests only
    NtpTimeProvider(String ntpHostname, String dependentClass) throws UnknownHostException {
        // default timeout of  3 seconds
        this(ntpHostname, 3000, dependentClass);
    }

    private NtpTimeProvider(String ntpHostname, int timeout, String dependentClass) throws UnknownHostException {
        // Check the optional commons-net dependency is on the classpath
        checkHasDependency(dependentClass);

        client = new NTPUDPClient();
        client.setDefaultTimeout(timeout);
        ntpHost = InetAddress.getByName(ntpHostname);
    }

    @Override
    public long getTime() throws TimeProviderException {
        try {
            TimeInfo timeInfo = client.getTime(ntpHost);

            return (long) Math.floor(timeInfo.getReturnTime() / 1000L);
        } catch (Exception e) {
            throw new TimeProviderException("Failed to provide time from NTP server. See nested exception.", e);
        }
    }

    private void checkHasDependency(String dependentClass) {
        try {
            Class<?> ntpClientClass = Class.forName(dependentClass);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The Apache Commons Net library must be on the classpath to use the NtpTimeProvider.");
        }
    }
}