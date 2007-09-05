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
 package net.sf.appia.protocols.group.bottom;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.protocols.group.Group;
import net.sf.appia.protocols.group.ViewID;
import net.sf.appia.protocols.group.events.GroupEvent;



public class OtherViews extends GroupEvent {

  public static final int WARN=0;
  public static final int BUFFER=1;
  public static final int NOTIFY=2;

  public int state;

  public Object other_addr;
  public Object multicast_addr;

  public OtherViews(
	  int state,
          Channel channel, int dir, Session source,
          Group group, ViewID view_id)
    throws AppiaEventException {
    super(channel,dir,source,group,view_id);
    this.state=state;
  }

  public OtherViews(int state, Group group, ViewID view_id) {
    super(group,view_id);
    this.state=state;
  }
}