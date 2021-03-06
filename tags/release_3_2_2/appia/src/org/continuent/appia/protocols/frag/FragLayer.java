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
 package org.continuent.appia.protocols.frag;

import org.continuent.appia.core.Layer;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.events.channel.ChannelClose;
import org.continuent.appia.core.events.channel.ChannelInit;
import org.continuent.appia.core.events.channel.Debug;
import org.continuent.appia.core.events.channel.EchoEvent;


//////////////////////////////////////////////////////////////////////
//                                                                  //
// Appia: protocol development and composition framework            //
//                                                                  //
// Class: FragLayer: Layer class of the Frag protocol               //
//                                                                  //
// Author: Hugo Miranda, 09/2000                                    //
//                                                                  //
// Change Log:                                                      //
//                                                                  //
//////////////////////////////////////////////////////////////////////


/**
 * Layer component for the fragmentation
 * protocol.
 *
 * This protocol provides the following events:
 * <ul>
 * <li>FragEvent: used by the protocol to pass message
 * fragments.
 * <li>EchoEvent: For carrying the MaxPDUSizeEvent
 * <li>MaxPDUSizeEvent: Event that queries the lower layers for the maximum
 * PDU size
 *</ul>
 * The protocol accepts the following events:
 * <ul>
 * <li>SendableEvent (require): Messages to be fragmented and
 * reassembled (inc. FragEvent)
 * <li>ChannelInit (require): Used by the session to learn that it belongs
 * to a new channel and that it must discover the maximum PDU size for it.
 * <li>ChannelClose (accept): Frees the channel when there are no more
 * fragments to send.
 * <li>Debug (accept): This protocol handles correctly the Debug event.
 * <li>MaxPDUSizeEvent (accept)
 * </ul>
 * @author Hugo Miranda
 * @see FragSession
 * @see org.continuent.appia.core.Layer
 * @see FragEvent
 * @see org.continuent.appia.core.events.channel.EchoEvent
 * @see org.continuent.appia.protocols.frag.MaxPDUSizeEvent
 * @see org.continuent.appia.core.events.SendableEvent
 * @see org.continuent.appia.core.events.channel.ChannelInit
 * @see org.continuent.appia.core.events.channel.ChannelClose
 * @see org.continuent.appia.core.events.channel.Debug
 * @see org.continuent.appia.core.Event
 */

public class FragLayer extends Layer {
  
  /**
   * Standard Empty constructor.
   *
   * @see Layer
   */
  public FragLayer() {
    evRequire=new Class[2];
    evRequire[0]=SendableEvent.class;
    evRequire[1]=ChannelInit.class;
    evAccept=new Class[6];
    evAccept[0]=SendableEvent.class;
    evAccept[1]=ChannelInit.class;
    evAccept[2]=ChannelClose.class;
    evAccept[3]=Debug.class;
    evAccept[4]=org.continuent.appia.protocols.frag.MaxPDUSizeEvent.class;
    evAccept[5]=org.continuent.appia.protocols.frag.FragTimer.class;
    evProvide=new Class[4];
    evProvide[0]=org.continuent.appia.protocols.frag.FragEvent.class;
    evProvide[1]=EchoEvent.class;
    evProvide[2]=org.continuent.appia.protocols.frag.MaxPDUSizeEvent.class;
    evProvide[3]=org.continuent.appia.protocols.frag.FragTimer.class;
  }
  
  /**
   * Session instantiation
   */
  public Session createSession() {
    return new FragSession(this);
  }
}
