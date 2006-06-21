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
 /**
 * Title:        Appia<p>
 * Description:  Protocol development and composition framework<p>
 * Copyright:    Copyright (c) Nuno Carvalho and Luis Rodrigues<p>
 * Company:      F.C.U.L.<p>
 * @author Nuno Carvalho and Luis Rodrigues
 * @version 1.0
 */

package org.continuent.appia.management;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Event;
import org.continuent.appia.core.Session;
import org.continuent.appia.xml.utils.SessionProperties;

public class ManagedSessionEvent extends Event {

	private SessionProperties properties;
	
	public SessionProperties getProperties() {
		return properties;
	}

	public void setProperties(SessionProperties properties) {
		this.properties = properties;
	}

	public ManagedSessionEvent(SessionProperties properties) {
		super();
		this.properties = properties;
	}

	public ManagedSessionEvent(Channel channel, int dir, Session src, SessionProperties props)
			throws AppiaEventException {
		super(channel, dir, src);
		properties = props;
	}

}
