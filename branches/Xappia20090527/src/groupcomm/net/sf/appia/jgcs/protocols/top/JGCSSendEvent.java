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

package net.sf.appia.jgcs.protocols.top;

import java.net.InetSocketAddress;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.message.Message;
import net.sf.appia.jgcs.AppiaMessage;
import net.sf.appia.protocols.group.Group;
import net.sf.appia.protocols.group.ViewID;
import net.sf.appia.protocols.group.events.GroupSendableEvent;
import net.sf.appia.protocols.group.events.Send;


public class JGCSSendEvent extends GroupSendableEvent implements Send {

	public JGCSSendEvent(Channel channel, int dir, Session source, Group group,
			ViewID view_id) throws AppiaEventException {
		super(channel, dir, source, group, view_id);
	}

	public JGCSSendEvent() {
		super(new AppiaMessage());
	}
	
	public JGCSSendEvent(Message message){
		super(message);
	}
	
	public void setDestination(int rank){
		this.dest = new int[]{rank};
	}

	public InetSocketAddress getDestination(){
		return (InetSocketAddress) this.dest;
	}
}
