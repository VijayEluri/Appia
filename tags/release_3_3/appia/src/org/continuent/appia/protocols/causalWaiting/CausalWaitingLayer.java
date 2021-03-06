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
 package org.continuent.appia.protocols.causalWaiting;

import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.protocols.group.events.GroupSendableEvent;
import org.continuent.appia.protocols.group.intra.View;
import org.continuent.appia.protocols.group.leave.LeaveEvent;
import org.continuent.appia.protocols.group.sync.BlockOk;


/**
 * <p>Layer for the waiting causal order protocol.</p>
 * 
 * <p>Requires the following events:
 * <ul>
 * <li>View</li>
 * <ul>
 * </p>
 * 
 * <p>Accepts the following events:
 * <ul>
 * <li>GroupSendableEvent</li>
 * <li>View</li>
 * </ul>
 * </p>
 * 
 * @see CausalWaitingSession
 * @author Jose Mocito
 */
public class CausalWaitingLayer extends Layer {

	public CausalWaitingLayer() {
		super();
		evAccept = new Class[] {
				GroupSendableEvent.class,
				View.class,
				BlockOk.class,
                LeaveEvent.class,
		};
		
		evRequire = new Class[] {
				View.class,
				BlockOk.class,
		};
		
		evProvide = new Class[] { };
	}
	
	/**
	 * CausalWaitingSession constructor.
	 */
	public Session createSession() {
		return new CausalWaitingSession(this);
	}
}
