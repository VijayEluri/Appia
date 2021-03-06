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
 * 
 */
package org.continuent.appia.protocols.group.inter;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Session;
import org.continuent.appia.core.events.SendableEvent;
import org.continuent.appia.core.message.Message;


/**
 * @author alexp
 *
 */
public class MergeEvent extends SendableEvent {

  public static final int PROPOSE=1;
  public static final int DECIDE=2;
  public static final int ABORT=3;
  
  /**
   * 
   */
  public MergeEvent() {
    super();
  }

  /**
   * @param channel
   * @param dir
   * @param source
   * @throws AppiaEventException
   */
  public MergeEvent(Channel channel, int dir, Session source)
      throws AppiaEventException {
    super(channel, dir, source);
  }
  
  /**
   * Returns the event payload already casted as an ExtendedMessage.
   * 
   * @return the event payload
   */
  public Message getExtendedMessage() {
    return (Message)message;
  }
}
