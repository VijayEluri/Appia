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
package net.sf.appia.protocols.common;

import java.util.concurrent.ThreadFactory;

/**
 * This class defines a AppiaThreadFactory.
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class AppiaThreadFactory implements ThreadFactory {

	public AppiaThreadFactory() {}

	public Thread newThread(Runnable runnable) {
		return new Thread(runnable);
	}

	public Thread newThread(Runnable runnable, String name) {
		return new Thread(runnable,name);
	}

	public Thread newThread(Runnable runnable, String name, boolean isDaemon) {
		final Thread thread = newThread(runnable,name);
		thread.setDaemon(isDaemon);
		return thread;
	}

}
