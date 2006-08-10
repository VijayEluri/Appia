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
 * IgnoreEvent.java
 *
 * Created on 10 de Julho de 2003, 16:05
 */

package org.continuent.appia.protocols.nakfifo;

import org.continuent.appia.core.*;
import org.continuent.appia.core.events.SendableEvent;


/**
 * Event sent to initiate FIFO communication with a peer.
 *
 * @author  alexp
 */
public class IgnoreEvent extends SendableEvent {
  
  /** Creates a new instance of IgnoreEvent */
  public IgnoreEvent() {}
  
  /** Creates a new instance of IgnoreEvent */
  public IgnoreEvent(Channel channel, Session source) throws AppiaEventException {
    super(channel,Direction.DOWN,source);
  }
}
