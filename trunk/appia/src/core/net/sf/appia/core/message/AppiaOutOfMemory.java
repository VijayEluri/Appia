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
 package net.sf.appia.core.message;

/**
 * This class defines a AppiaOutOfMemory Error
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class AppiaOutOfMemory extends OutOfMemoryError {
  private static final long serialVersionUID = -4853326694790664140L;

  /**
   * Creates a new AppiaOutOfMemory.
   */
  public AppiaOutOfMemory () {
    super();
  }
  
  /**
   * Creates a new AppiaOutOfMemory.
   * @param s the error message.
   */
  public AppiaOutOfMemory(String s) {
    super(s);
  }
}
