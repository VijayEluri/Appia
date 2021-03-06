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
 /*
 * Created on Mar 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sf.appia.project.group.server;

import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.project.group.event.proxy.BlockOkProxyEvent;
import net.sf.appia.project.group.event.proxy.DecidedProxyEvent;
import net.sf.appia.project.group.event.proxy.GroupSendableProxyEvent;
import net.sf.appia.project.group.event.proxy.LeaveClientProxyEvent;
import net.sf.appia.project.group.event.proxy.NewClientProxyEvent;
import net.sf.appia.project.group.event.proxy.UpdateDecideProxyEvent;
import net.sf.appia.project.group.event.proxy.UpdateProxyEvent;
import net.sf.appia.project.group.event.stub.BlockOkStubEvent;
import net.sf.appia.project.group.event.stub.GroupInitStubEvent;
import net.sf.appia.project.group.event.stub.GroupSendableStubEvent;
import net.sf.appia.project.group.event.stub.LeaveStubEvent;
import net.sf.appia.project.group.event.stub.PongEvent;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import net.sf.appia.protocols.group.events.GroupInit;
import net.sf.appia.protocols.group.intra.View;
import net.sf.appia.protocols.group.sync.BlockOk;


/**
 * This class defines the Proxy Layer for VS
 * 
 * @author Joao Trindade
 * @version 1.0
 */
public class VsProxyLayer extends Layer {

	public VsProxyLayer() {
		
		evRequire = new Class[]{
				ChannelInit.class,
		};
        
		evProvide = new Class[] {
				RegisterSocketEvent.class,
				GroupInit.class,
        };
		
		evAccept = new Class[]{
				ChannelInit.class,
				BlockOk.class,
                View.class,
				NewClientProxyEvent.class,
                RegisterSocketEvent.class,
				GroupInitStubEvent.class,
				LeaveStubEvent.class,
				LeaveClientProxyEvent.class,
				GroupSendableStubEvent.class,
				GroupSendableProxyEvent.class,
				PongEvent.class,
				BlockOkStubEvent.class,
				BlockOkProxyEvent.class,
				DecidedProxyEvent.class,
				UpdateProxyEvent.class,
				UpdateDecideProxyEvent.class,
		};
	}
	
	/**
     * Creates the session for this protocol.
	 * @see Layer#createSession()
	 */
	public Session createSession() {
		return new VsProxySession(this);
	}
}
