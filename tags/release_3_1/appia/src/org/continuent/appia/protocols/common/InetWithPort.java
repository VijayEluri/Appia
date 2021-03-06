/**
 * Appia: Group communication and protocol composition framework library
 * Copyright 2006 University of Lisbon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 * Initial developer(s): Alexandre Pinto and Hugo Miranda.
 * Contributor(s): See Appia web page for a list of contributors.
 */
 package org.continuent.appia.protocols.common;

//////////////////////////////////////////////////////////////////////
//                                                                  //
// Appia: protocol development and composition framework            //
//                                                                  //
// Class: InetWithPort : Keeps inet and port address information    //
//                                                                  //
// Author: Hugo Miranda, 06/2000                                    //
//                                                                  //
// Change Log:                                                      //
//   21/11/2000: Equality operator changed to allow comparisons     //
//               with instances of other classes.                   //
//   09/09/2002 (Nuno Carvalho) Added contructors of this class.    //
//   06/10/2003 Moved to common                                     //
//                                                                  //
//////////////////////////////////////////////////////////////////////


import java.net.*;

import org.continuent.appia.core.message.*;

/**
 * InetWithPort enriches Java InetAddress class with an integer port number. It
 * intends to be a possible instance for the source and dest attributes
 * of SendableEvents.
 *
 * @see org.continuent.appia.protocols.udpsimple.UdpSimpleSession
 * @see org.continuent.appia.core.events.SendableEvent
 * @author Hugo Miranda
 */

public class InetWithPort implements java.io.Serializable {

  private static final long serialVersionUID = 3343979654378608232L;

  /**
	 * The IP address.
	 * @see InetAddress
	 */
	public InetAddress host;

	/**
	 * The port number.
	 */
	public int port;

	/**
	 * Empty constructor of this class.
	 */
	public InetWithPort() {}

	/**
	 * Constructor of this class.
	 * @param host
	 * @param port
	 */
	public InetWithPort(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	/**
	 * Equality comparison. Returns true if the testing object is a
	 * InetWithPort instance and InetAddress and port are equal
	 * on both instances. InetAddress comparison is made using its equal method
	 *
	 * @param test The Object to be compared.
	 * @see InetAddress
	 */
	public boolean equals(Object test) {
		return (test instanceof InetWithPort)
			&& (this.port == ((InetWithPort) test).port)
			&& this.host.equals(((InetWithPort) test).host);
	}

	/**
	 * Method needed for HashTables.
	 * The hashCode of this Class instance is the XOR between
	 * port field and hashCode of InetAddress
	 */
	public int hashCode() {
		return (port ^ host.hashCode());
	}

	/**
	 * Method that returns a text representation of the InetWithPort class.
	 * @return The String with the text representation.
	 */
	public String toString() {
		return (host == null ? "" : host.getHostName()) + ":" + port;
	}

	public static void push(InetWithPort inetp, Message message) {
		MsgBuffer mbuf = new MsgBuffer();
		mbuf.len = 6;
		message.push(mbuf);
		mbuf.data[mbuf.off + 0] = (byte) ((inetp.port >>> 8) & 0xFF);
		mbuf.data[mbuf.off + 1] = (byte) ((inetp.port >>> 0) & 0xFF);
		System.arraycopy(inetp.host.getAddress(), 0, mbuf.data, mbuf.off + 2, 4);
	}

	public static InetWithPort pop(Message message) {
		InetWithPort inetp = new InetWithPort();
		MsgBuffer mbuf = new MsgBuffer();
		mbuf.len = 6;
		message.pop(mbuf);
		String ip = new String();
		for (int i = 0; i < 3; i++) {
			ip += (mbuf.data[mbuf.off + i + 2] & 0xff) + ".";
		}
		ip += (int) mbuf.data[mbuf.off + 3 + 2] & 0xff;
		try {
			inetp.host = InetAddress.getByName(ip);
		} catch (UnknownHostException ex) {}
		inetp.port = (((int) mbuf.data[mbuf.off]) & 0xFF) << 8;
		inetp.port |= (((int) mbuf.data[mbuf.off + 1]) & 0xFF) << 0;
		return inetp;
	}

	public static InetWithPort peek(Message message) {
		InetWithPort inetp = new InetWithPort();
		MsgBuffer mbuf = new MsgBuffer();
		mbuf.len = 6;
		message.peek(mbuf);
		String ip = new String();
		for (int i = 0; i < 3; i++) {
			ip += (mbuf.data[mbuf.off + i + 2] & 0xff) + ".";
		}
		ip += (int) mbuf.data[mbuf.off + 3 + 2] & 0xff;
		try {
			inetp.host = InetAddress.getByName(ip);
		} catch (UnknownHostException ex) {}
		inetp.port = (((int) mbuf.data[mbuf.off]) & 0xFF) << 8;
		inetp.port |= (((int) mbuf.data[mbuf.off + 1]) & 0xFF) << 0;
		return inetp;
	}

}
