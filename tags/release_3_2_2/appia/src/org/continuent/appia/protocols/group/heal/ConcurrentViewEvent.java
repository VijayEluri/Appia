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
 
package org.continuent.appia.protocols.group.heal;

import org.continuent.appia.core.AppiaEventException;
import org.continuent.appia.core.Channel;
import org.continuent.appia.core.Session;
import org.continuent.appia.protocols.group.Group;
import org.continuent.appia.protocols.group.ViewID;
import org.continuent.appia.protocols.group.events.GroupEvent;


/**
 * Event sent by the HealLayer, when it detects a concurrent view of a group.
 * <br>
 * By concurrent views of a group we mean two, or more, views of the same group,
 * with a disjoint membership and operating at the same time, independently
 * of each other.
 * <br>
 * The event is sent by the HealLayer, and is usually received by the
 * InterLayer
 *
 * @author Alexandre Pinto
 * @version 0.1
 * @see org.continuent.appia.protocols.group.heal.HealLayer
 * @see org.continuent.appia.protocols.group.inter.InterLayer
 */
public class ConcurrentViewEvent extends GroupEvent {

  /**
   * The view id of the concurrent view.
   */
  public ViewID id;

  /**
   * The concurrent view coordinator address.
   */
  public Object addr;

  /**
   * Constructs an uninitialized ConcurrentViewEvent.
   *
   * @param id the {@link org.continuent.appia.protocols.group.ViewID ViewID} of the concurrent view
   * @param addr the {@link org.continuent.appia.protocols.common.InetWithPort address}
   *             of the concurrent view coordinator
   * @param g the {@link org.continuent.appia.protocols.group.Group Group} of the Event
   * @param vid the {@link org.continuent.appia.protocols.group.ViewID ViewID} of the Event
   */
  public ConcurrentViewEvent(ViewID id, Object addr, Group g, ViewID vid) {
    super(g,vid);
    this.id=id;
    this.addr=addr;
  }

  /**
   * Constructs an initialized ConcurrentViewEvent.
   *
   * @param id the {@link org.continuent.appia.protocols.group.ViewID ViewID} of the concurrent view
   * @param addr the {@link org.continuent.appia.protocols.common.InetWithPort address}
   *             of the concurrent view coordinator
   * @param c the {@link org.continuent.appia.core.Channel Channel} of the Event
   * @param d the {@link org.continuent.appia.core.Direction Direction} of the Event
   * @param s the {@link org.continuent.appia.core.Session Session} that is generating the Event
   * @param g the {@link org.continuent.appia.protocols.group.Group Group} of the Event
   * @param vid the {@link org.continuent.appia.protocols.group.ViewID ViewID} of the Event
   * @throws AppiaEventException as the result of calling
   * {@link org.continuent.appia.core.Event#Event(Channel,int,Session)
   * Event(Channel,Direction,Session)}
   */
  public ConcurrentViewEvent(ViewID id, Object addr, Channel c, int d, Session s, Group g, ViewID vid) throws AppiaEventException {
    super(c,d,s,g,vid);
    this.id=id;
    this.addr=addr;
  }
}
