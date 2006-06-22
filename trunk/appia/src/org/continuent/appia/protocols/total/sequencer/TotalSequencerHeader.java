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
 package org.continuent.appia.protocols.total.sequencer;

import java.io.*;

/**
 * Header added to the events to be sent in total order.
 */
public class TotalSequencerHeader implements Externalizable {
  private static final long serialVersionUID = 7750742443002978989L;

    private int ordem;
    private int emissor;
    private int nSeqInd;

    public TotalSequencerHeader() {}
    
    /**
     * Constructor.
     * @param o total order 
     * @param e sender 
     * @param nSeq individual sequence number
     */
    public TotalSequencerHeader(int o,int e,int nSeq) {
        ordem=o;
        emissor=e;
        nSeqInd=nSeq;
    }

    /**
     * @return total order
     */
    public int getOrder() {
        return ordem;
    }

    /**
     * @return The event
     */
    public int getSender() {
        return emissor;
    }

    /**
     * @return the individual sequence number
     */
    public int getnSeqInd() {
        return nSeqInd;
    }

	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(ordem);
		out.writeInt(emissor);
		out.writeInt(nSeqInd);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		ordem = in.readInt();
		emissor = in.readInt();
		nSeqInd = in.readInt();
	}
 }
