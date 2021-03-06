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
 * Initial developer(s): Nuno Carvalho.
 * Contributor(s): See Appia web page for a list of contributors.
 */

package net.sf.appia.jgcs.protocols.remote;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.protocols.common.NetworkUndeliveredEvent;
import net.sf.appia.protocols.group.remote.RemoteViewEvent;


/**
 * This class defines a RemoteAddressLayer
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class RemoteAddressLayer extends Layer {

	/**
	 * Creates a new RemoteAddressLayer.
	 */
	public RemoteAddressLayer() {
		super();
		
		evProvide=new Class[]{
				RemoteViewEvent.class,
				RetrieveAddressTimer.class,
		};
	
		evRequire=new Class[]{
				RemoteViewEvent.class,
		};
		
		evAccept=new Class[]{
				RemoteViewEvent.class,
				SendableEvent.class,
				ChannelInit.class,
				ChannelClose.class,
				NetworkUndeliveredEvent.class,
				RetrieveAddressTimer.class,
		};
	}

	/**
	 * @see net.sf.appia.core.Layer#createSession()
	 */
	@Override
	public Session createSession() {
		return new RemoteAddressSession(this);
	}

}
