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
 package net.sf.appia.protocols.tcpcomplete;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.sf.appia.core.AppiaEventException;
import net.sf.appia.core.AppiaException;
import net.sf.appia.core.Channel;
import net.sf.appia.core.Direction;
import net.sf.appia.core.Event;
import net.sf.appia.core.EventQualifier;
import net.sf.appia.core.Layer;
import net.sf.appia.core.Session;
import net.sf.appia.core.events.AppiaMulticast;
import net.sf.appia.core.events.SendableEvent;
import net.sf.appia.core.events.channel.ChannelClose;
import net.sf.appia.core.events.channel.ChannelInit;
import net.sf.appia.core.message.Message;
import net.sf.appia.core.message.MsgBuffer;
import net.sf.appia.protocols.common.RegisterSocketEvent;
import net.sf.appia.protocols.utils.HostUtils;
import net.sf.appia.protocols.utils.ParseUtils;
import net.sf.appia.xml.interfaces.InitializableSession;
import net.sf.appia.xml.utils.SessionProperties;

import org.apache.log4j.Logger;


/**
 * Uses TCP to send/receive events to/from other Appia instances.<br>
 * TCP connections are established automatically when required and 
 * terminated after an inactivity period. <br>
 * <br>
 * <b>The TCP socket is bound to a local address</b>.
 * If {@link net.sf.appia.protocols.common.RegisterSocketEvent#localHost} is null, 
 * {@link net.sf.appia.protocols.utils.HostUtils} is used to select one. 
 * 
 * @author Pedro Vicente, Alexandre Pinto
 */
public class TcpCompleteSession extends Session implements InitializableSession{
  
    private static Logger log = Logger.getLogger(TcpCompleteSession.class);

  private static final int DEST_TIMEOUT=150000; // 2,5 minutes
  private static final int MAX_INACTIVITY=2;
  private static final int SOTIMEOUT=5000;
  
  protected int param_DEST_TIMEOUT=DEST_TIMEOUT, param_MAX_INACTIVITY=MAX_INACTIVITY, 
  	param_SOTIMEOUT=SOTIMEOUT;
  
  //Channels
  protected HashMap channels;
  
  //Open Sockets created by this node
//  protected HashMap ourSockets;
  protected HashMap ourReaders;
  
  //Sockets opened to us
//  protected HashMap otherSockets;
  protected HashMap otherReaders;
  
  //Accept Thread
  protected AcceptReader acceptThread;
  
  protected int ourPort = -1;
  
  protected Object socketLock;
  protected Object channelLock;
  
//  private Benchmark bench=null;
  
  private Channel timerChannel=null;
  
  /**
   * Constructor for NewTcpSession.
   * @param layer
   */
  public TcpCompleteSession(Layer layer) {
    super(layer);
    
    //init all
    channels = new HashMap();
//    ourSockets = new HashMap();
    ourReaders = new HashMap();
//    otherSockets = new HashMap();
    otherReaders = new HashMap();
    
    socketLock = new Object();
    channelLock = new Object();    
  }
  
  /**
   * Initializes the session using the parameters given in the XML configuration.
   * Possible parameters:
   * <ul>
   * <li><b>dest_timeout</b> time between unused open connections verification. (in milliseconds)
   * <li><b>max_inactivity</b> number of times that the dest_timeout expires without closing the connection..
   * <li><b>reader_sotimeout</b> the timeout of the threads that listen on TCP sockets. (in milliseconds)
   * </ul>
   * 
   * @param params The parameters given in the XML configuration.
   * @see net.sf.appia.xml.interfaces.InitializableSession#init(SessionProperties)
   */
public void init(SessionProperties params) {
    if (params.containsKey("reader_sotimeout"))
        param_SOTIMEOUT=params.getInt("reader_sotimeout");
    if (params.containsKey("dest_timeout"))
        param_DEST_TIMEOUT=params.getInt("dest_timeout");
    if (params.containsKey("max_inactivity"))
        param_MAX_INACTIVITY=params.getInt("max_inactivity");    
	}
  
  public void handle(Event e){
    if(e instanceof SendableEvent)
      handleSendable((SendableEvent)e);
    else if(e instanceof RegisterSocketEvent)
      handleRegisterSocket((RegisterSocketEvent)e);
    else if(e instanceof ChannelInit)
      handleChannelInit((ChannelInit)e);
    else if(e instanceof ChannelClose)
      handleChannelClose((ChannelClose)e);
    else if(e instanceof TcpTimer)
      handleTcpTimer((TcpTimer)e);
  }
  
  private void handleSendable(SendableEvent e){
    
	  if(e.getDir() == Direction.UP){
	      if (e.getChannel().isStarted()) {
	          try {
	              e.go();
	          } catch (AppiaEventException e1) {
	              e1.printStackTrace();
	          }
	      }
		  return;
	  }
	  
    if(TcpCompleteConfig.debugOn)
      debug("preparing to send ::"+e+" CHANNEL: "+e.getChannel().getChannelID());
    
    byte[] data=format(e);
    
    if (e.dest instanceof AppiaMulticast) {
      Object[] dests=((AppiaMulticast)e.dest).getDestinations();
      for (int i=0 ; i < dests.length ; i++) {
        if (dests[i] instanceof InetSocketAddress)
          send(data, (InetSocketAddress)dests[i], e.getChannel());
        else
          sendUndelivered(e.getChannel(),dests[i]);
      }
    } else if (e.dest instanceof InetSocketAddress) {
      send(data, (InetSocketAddress)e.dest, e.getChannel());
    } else {
      sendUndelivered(e.getChannel(),e.dest);
    }
    
    try {
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }
  }
  
  protected void handleRegisterSocket(RegisterSocketEvent e){
      if(log.isDebugEnabled())
          log.debug("TCP Session received RegisterSocketEvent to register a socket in port "+e.port);
    ServerSocket ss= null;
    
    if(ourPort < 0){
        if(e.port == RegisterSocketEvent.FIRST_AVAILABLE){
            try {
                ss = new ServerSocket(0);
            } catch (IOException ex) {
                log.debug("Exception when trying to create a server socket in First Available mode: "+ex);
            }
        }
        else if(e.port == RegisterSocketEvent.RANDOMLY_AVAILABLE){
            final Random rand = new Random();
            int p;
            boolean done = false;
            
            while(!done){
                p = rand.nextInt(Short.MAX_VALUE);
                
                try {
                    ss = new ServerSocket(p);
                    done = true;
                } catch(IllegalArgumentException ex){
                    log.debug("Exception when trying to create a server socket in Randomly Available mode: "+ex);
                } catch (IOException ex) {
                    log.debug("Exception when trying to create a server socket in Randomly Available mode: "+ex);
                }
            }
        }
        else{
            try {
                ss = new ServerSocket(e.port);
            } catch (IOException ex) {
                log.debug("Exception when trying to create a server socket using the port: "+e.port+"\nException: "+ex);
            }
        }
    }
    if (ss != null) {
        ourPort = ss.getLocalPort();
        if(log.isDebugEnabled())
            log.debug("TCP Session registered a socket in port "+ourPort);
        
        //create accept thread int the request port.
      acceptThread = new AcceptReader(ss,this,e.getChannel(),socketLock);
      final Thread t = e.getChannel().getThreadFactory().newThread(acceptThread);
      t.setName("TCP Accept thread from port "+ourPort);
      t.start();
      
      e.localHost=HostUtils.getLocalAddress();
      e.port=ourPort;
      e.error=false;
    } else {
      e.error=true;
      if(acceptThread != null && acceptThread.getPort() == e.port){
          e.setErrorCode(RegisterSocketEvent.RESOURCE_ALREADY_BOUND_ERROR);
          e.setErrorDescription("Socket already bound in port "+e.port);
      }
      else {
          e.setErrorCode(RegisterSocketEvent.RESOURCE_BUSY_ERROR);
          e.setErrorDescription("Could not create socket. Resource is busy.");
      }
    }
    
    //		send RegisterSocketEvent
    e.setDir(Direction.invert(e.getDir()));
    e.setSource(this);
    
    try {
      e.init();
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }
  }
  
  private void handleChannelInit(ChannelInit e){
    //add channel to hash map
    putChannel(e.getChannel());
    try {
      e.go();
    } catch (AppiaEventException ex) {
      ex.printStackTrace();
    }
    
    if (timerChannel == null) {
      try {
        TcpTimer timer=new TcpTimer(param_DEST_TIMEOUT, e.getChannel(), this, EventQualifier.ON);
        timer.go();
        timerChannel=timer.getChannel();
      } catch (AppiaEventException ex) {
        ex.printStackTrace();
      } catch (AppiaException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  private void handleChannelClose(ChannelClose e){
	  
    //remove channel.
    removeChannel(e.getChannel());
    
    if(channels.size() == 0){
    	acceptThread.setRunning(false);
    	Iterator it = ourReaders.values().iterator();
    	while(it.hasNext()){
            ((TcpReader)(it.next())).setRunning(false);
            it.remove();
    	}
    	it = otherReaders.values().iterator();
    	while(it.hasNext()){
            ((TcpReader)(it.next())).setRunning(false);
            it.remove();
    	}
    }
    else if (e.getChannel() == timerChannel) {
        try {
            timerChannel=(Channel)channels.values().iterator().next();
            TcpTimer timer=new TcpTimer(param_DEST_TIMEOUT, timerChannel, this, EventQualifier.ON);
            timer.go();
          } catch (Exception ex) {
            timerChannel=null;
            ex.printStackTrace();
          }
        }

  }
  
  private void handleTcpTimer(TcpTimer e) {
	  try {
		e.go();
	} catch (AppiaEventException e1) {
		e1.printStackTrace();
	}
	
  	Iterator it = ourReaders.values().iterator();
	while(it.hasNext()){
		TcpReader reader = (TcpReader) it.next();
		if(reader.sumInactiveCounter() > param_MAX_INACTIVITY){
			reader.setRunning(false);
			it.remove();
		}
	}
	it = otherReaders.values().iterator();
	while(it.hasNext()){
		TcpReader reader = (TcpReader) it.next();
		if(reader.sumInactiveCounter() > param_MAX_INACTIVITY){
			reader.setRunning(false);
			it.remove();
		}
	}
  }
  
  protected void send(byte[] data, InetSocketAddress dest, Channel channel) {
    Socket s = null;
    
    try {
      //check if the socket exist int the opensockets created by us
      if(existsSocket(ourReaders,dest)){
        //if so use that socket
        s = getSocket(ourReaders,dest);
        if(TcpCompleteConfig.debugOn)
          debug("our socket, sending...");
      }
      else{//if not
        //check if socket exist in sockets created by the other
        if(existsSocket(otherReaders,dest)){
          //if so	use that socket
          s = getSocket(otherReaders,dest);
          if(TcpCompleteConfig.debugOn)
            debug("other socket, sending...");
        }
        else{//if not
          //create new socket and put it opensockets created by us
          s = createSocket(ourReaders,dest,channel);
          if(TcpCompleteConfig.debugOn)
            debug("created new socket, sending...");
        }
      }
      //send event by the chosen socket -> formatAndSend()
      if (TcpCompleteConfig.debugOn)
        debug("Sending through "+s);
      s.getOutputStream().write(data);
      s.getOutputStream().flush();
    } catch (IOException ex) {
      if(TcpCompleteConfig.debugOn) {
        ex.printStackTrace();
        debug("Node "+dest+" failed.");
      }
      sendUndelivered(channel,dest);
      removeSocket(dest);
    }
  }
  
  protected boolean existsSocket(HashMap hr, InetSocketAddress iwp){
    synchronized(socketLock){
      if(hr.containsKey(iwp))
        return true;
      else
        return false;
    }
  }
  
  protected Socket getSocket(HashMap hm, InetSocketAddress iwp){
    synchronized(socketLock){
    	TcpReader reader = (TcpReader) hm.get(iwp); 
    	reader.clearInactiveCounter();
      return reader.getSocket();
    }
  }
  
  //create socket, put in hashmap and create thread
  protected Socket createSocket(HashMap hr, InetSocketAddress iwp,Channel channel) throws IOException{
    synchronized(socketLock){
      Socket newSocket = null;
      
      //create socket
      
      newSocket = new Socket(iwp.getAddress(),iwp.getPort());
      newSocket.setTcpNoDelay(true);
      
      byte bPort[]= ParseUtils.intToByteArray(ourPort);
      
      
      newSocket.getOutputStream().write(bPort);
      if(TcpCompleteConfig.debugOn)
        debug("Sending our original port "+ourPort);
      
      addSocket(hr, iwp, newSocket, channel);
      
      return newSocket;
    }
  }
  
  protected void addSocket(HashMap hr, InetSocketAddress iwp,Socket socket,Channel channel){
    synchronized(socketLock){
      final TcpReader reader = new TcpReader(socket,this,ourPort,iwp.getPort(),channel);
      final Thread t = channel.getThreadFactory().newThread(reader);
      t.setName("TCP reader thread ["+iwp+"]");
      t.start();
//      hm.put(iwp,socket);
      hr.put(iwp,reader);
    }
  }
  
  protected void removeSocket(InetSocketAddress iwp){
    synchronized(socketLock){
      if(existsSocket(ourReaders,iwp))
        ((TcpReader)(ourReaders.remove(iwp))).setRunning(false);
      else if(existsSocket(otherReaders,iwp))
          ((TcpReader)(otherReaders.remove(iwp))).setRunning(false);
      else{
        if(TcpCompleteConfig.debugOn)
          debug("no socket anywhere?");
      }
    }
  }
  
  protected Channel getChannel(String channelName){
    synchronized(channelLock){
      return (Channel)channels.get(channelName);
    }
  }
  
  protected void putChannel(Channel channel) {
    synchronized(channelLock){
      channels.put(channel.getChannelID(),channel);
    }
  }
  
  protected void removeChannel(Channel channel) {
    synchronized(channelLock){
      channels.remove(channel.getChannelID());
    }
  }
  
  protected byte[] format(SendableEvent e) {
    MsgBuffer mbuf = new MsgBuffer();
    Message msg = e.getMessage();
    
    byte[] eventType = e.getClass().getName().getBytes();
    byte[] channelID = e.getChannel().getChannelID().getBytes();
    
    
    mbuf.len = channelID.length;
    msg.push(mbuf);
    System.arraycopy(channelID, 0, mbuf.data, mbuf.off, mbuf.len);
    
    mbuf.len = 4;
    msg.push(mbuf);
    ParseUtils.intToByteArray(channelID.length, mbuf.data, mbuf.off);
    
    mbuf.len = eventType.length;
    msg.push(mbuf);
    System.arraycopy(eventType, 0, mbuf.data, mbuf.off, mbuf.len);
    
    mbuf.len = 4;
    msg.push(mbuf);
    ParseUtils.intToByteArray(eventType.length, mbuf.data, mbuf.off);
    
    mbuf.len = 4;
    msg.push(mbuf);
    ParseUtils.intToByteArray(msg.length()-4,mbuf.data,mbuf.off);
    
    return msg.toByteArray();
  }
  
  protected void sendUndelivered(Channel channel, Object who) {
    try {
      TcpUndeliveredEvent undelivered = new TcpUndeliveredEvent(channel,Direction.UP,this,who);
      undelivered.go();
    } catch (AppiaEventException exception) {
      exception.printStackTrace();
    }
  }

  private void debug(String msg){
  	if (TcpCompleteConfig.debugOn)
  		System.out.println("[TcpComplete] ::"+msg);
  }
}
