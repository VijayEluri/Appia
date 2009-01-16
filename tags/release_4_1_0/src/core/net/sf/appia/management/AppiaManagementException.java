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
 * Title:        Appia<p>
 * Description:  Protocol development and composition framework<p>
 * Copyright:    Copyright (c) Nuno Carvalho and Luis Rodrigues<p>
 * Company:      F.C.U.L.<p>
 * @author Nuno Carvalho and Luis Rodrigues
 * @version 1.0
 */

package net.sf.appia.management;

import net.sf.appia.core.AppiaException;

/**
 * This class defines a AppiaManagementException
 * 
 * @author <a href="mailto:nunomrc@di.fc.ul.pt">Nuno Carvalho</a>
 * @version 1.0
 */
public class AppiaManagementException extends AppiaException {

    private static final long serialVersionUID = 7635406621774706090L;

    /**
     * Creates a new AppiaManagementException.
     */
    public AppiaManagementException() {
        super();
    }

    /**
     * 
     * Creates a new AppiaManagementException.
     * @param s
     */
    public AppiaManagementException(String s) {
        super(s);
    }

    /**
     * Creates a new AppiaManagementException.
     * @param s
     * @param cause
     */
    public AppiaManagementException(String s, Throwable cause) {
        super(s, cause);
    }

    /**
     * Creates a new AppiaManagementException.
     * @param cause
     */
    public AppiaManagementException(Throwable cause) {
        super("", cause);
    }

}
