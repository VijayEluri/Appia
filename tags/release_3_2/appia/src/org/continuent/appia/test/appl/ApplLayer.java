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
 package org.continuent.appia.test.appl;

import org.continuent.appia.core.*;

/*
 * Change Log:
 * Nuno Carvalho: 7/Aug/2002 - Alterei o protocolo para nao usar
 *                        codigo deprecated (asynEvent)
 */

public class ApplLayer extends Layer {
  
  public ApplLayer() {
    
    Class rse=org.continuent.appia.protocols.common.RegisterSocketEvent.class;
    Class ecast=org.continuent.appia.test.appl.ApplCastEvent.class;
    Class debug=org.continuent.appia.core.events.channel.Debug.class;
    Class timer=org.continuent.appia.test.appl.ApplTimer.class;
    Class init=org.continuent.appia.protocols.group.events.GroupInit.class;
    Class esend=org.continuent.appia.test.appl.ApplSendEvent.class;
    Class async=ApplAsyncEvent.class;
    Class view=org.continuent.appia.protocols.group.intra.View.class;
    Class cinit=org.continuent.appia.core.events.channel.ChannelInit.class;
    Class cclose=org.continuent.appia.core.events.channel.ChannelClose.class;
    Class blockok=org.continuent.appia.protocols.group.sync.BlockOk.class;
    Class leave=org.continuent.appia.protocols.group.leave.LeaveEvent.class;
    Class exit=org.continuent.appia.protocols.group.leave.ExitEvent.class;
    Class multicast=org.continuent.appia.protocols.udpsimple.MulticastInitEvent.class;
    
    evProvide=new Class[9];
    evProvide[0]=rse;
    evProvide[1]=init;
    evProvide[2]=debug;
    evProvide[3]=timer;
    evProvide[4]=ecast;
    evProvide[5]=esend;
    evProvide[6]=leave;
    evProvide[7]=multicast;
    evProvide[8]=org.continuent.appia.protocols.sslcomplete.SslRegisterSocketEvent.class;
    
    
    evRequire=new Class[1];
    evRequire[0]=view;
    
    evAccept=new Class[11];
    evAccept[0]=ecast;
    evAccept[1]=esend;
    evAccept[2]=async;
    evAccept[3]=cinit;
    evAccept[4]=cclose;
    evAccept[5]=debug;
    evAccept[6]=timer;
    evAccept[7]=view;
    evAccept[8]=blockok;
    evAccept[9]=exit;
    evAccept[10]=rse;
  }
  public Session createSession() {
    return new ApplSession(this);
  }
}
