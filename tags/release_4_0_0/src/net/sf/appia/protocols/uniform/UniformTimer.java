
/**
 * Appia: Group communication and protocol composition framework library
 * Copyright 2007 University of Lisbon
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
 * Initial developer(s): Jose Mocito.
 * Contributor(s): See Appia web page for a list of contributors.
 */
 package net.sf.appia.protocols.uniform;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.channel.PeriodicTimer;

/**
 * Timer that signals the periodic send of uniformity information to the group.
 * 
 */
public class UniformTimer extends PeriodicTimer {
	
	public UniformTimer(){
		super();
	}
	
	public UniformTimer(long timer,Channel channel, int dir, Session source, int qualifier)  throws AppiaEventException, AppiaException{
		super("UniformSETOTimer",timer,channel,dir,source,qualifier);
	}
}
