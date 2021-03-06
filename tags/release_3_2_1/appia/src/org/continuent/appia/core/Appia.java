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
 
package org.continuent.appia.core;

//Change log:
//Nuno Carvalho - changed this to support flow control
//				 on channels
//(28-Feb-2003)

import java.util.Vector;

import org.continuent.appia.core.events.channel.ExternalEvent;

/**
 * <i>Appia</i> main class.
 * <br>
 * This class has the <i>static</i> methods responsible for the execution of all <i>Appia</i>s
 * functionality.
 * <br>
 * It is this class that calls the
 * {@link org.continuent.appia.core.EventScheduler EventSchedulers}
 * and therefore makes the entire <i>Appia</i> system run.
 * <br>
 * It also keeps the current {@link org.continuent.appia.core.TimerManager TimerManager}.
 *
 * @author Alexandre Pinto
 * @version 0.1
 * @see org.continuent.appia.core.EventScheduler
 */
public class Appia {
  
  protected Vector eventSchedulers=new Vector();
  protected TimerManager timerManager=null;
  protected Thread thread = null;
  protected int nEvents=0;
  
  private boolean running = true;
  
  /**
   * Default constructor.
   * <br>
   * It creates, and starts, the default {@link org.continuent.appia.core.TimerManager TimerManager}.
   */
  public Appia() {
      timerManager=new TimerManager();
  }
  
  public TimerManager instanceGetTimerManager() {
    return timerManager;
  }
  
  public void instanceSetTimerManager(TimerManager timerManager) {
    if (this.timerManager!=null) {
      this.timerManager.stop();
    }
    
    this.timerManager=timerManager;
    this.timerManager.start();
  }

  public void instanceInsertEventScheduler(EventScheduler eventScheduler) {
    if ( ! eventSchedulers.contains(eventScheduler) )
      eventSchedulers.addElement(eventScheduler);
  }

  public void instanceRemoveEventScheduler(EventScheduler eventScheduler) {
    eventSchedulers.removeElement(eventScheduler);
  }

  public void instanceInsertListenRequest(ExternalEvent descriptor) {}
  
  public void instanceRemoveListenRequest(ExternalEvent descriptor) {}

  public synchronized void instanceInsertedEvent() {
    if ( nEvents == 0 )
      notify();
    nEvents++;
  }
  
  public Thread instanceGetAppiaThread() {
    return thread;
  }

  public void instanceRun() {
    // Starting associated TimerManager
    timerManager.start();

    //some final initializations
    int i;
    thread = Thread.currentThread();
    for (i=0 ; i < eventSchedulers.size() ; i++) {
      final EventScheduler es=(EventScheduler)eventSchedulers.elementAt(i);
      es.start();
    }
    
    i=0;
    boolean consumedEvent;
    EventScheduler es;
    
    while (true) {	
      try {
        es=(EventScheduler)eventSchedulers.elementAt(i);
      } catch (ArrayIndexOutOfBoundsException e) {
        es=null;
      }
      
      if ( es != null )
        consumedEvent=es.consumeEvent();
      else
        consumedEvent=false;
      
      synchronized (this) {
    	  if ( consumedEvent )
    		  nEvents--;
    	  
    	  while ( running && nEvents == 0 ) {
    		  try {
    			  wait();
    		  } catch (InterruptedException e) {}
    	  }        
        
    	if(!running)
    		break;    		
      }
      
      i++;
      if ( i >= eventSchedulers.size() ) {
        i=0;
        //so other threads can run
        Thread.yield();
      }
      // notify threads that are blocked in asyncGo
      //synchronized(Appia.class){
      //  Appia.class.notifyAll();
      //}
    }

  }

//  public void instanceStop() {
//      synchronized (this) {
//      	running = false;
//      	timerManager.stop();
//      	instanceGetAppiaThread().interrupt();
//  	}
//  }
  
  /* the instance of Appia! */
  protected static Appia appia=new Appia();

  /**
   * Get the current {@link org.continuent.appia.core.TimerManager TimerManager}.
   *
   * @return the current {@link org.continuent.appia.core.TimerManager TimerManager}
   */
  public static TimerManager getTimerManager() {
    return appia.timerManager;
  }
  
  /**
   * Set the {@link org.continuent.appia.core.TimerManager TimerManager}.
   * <br>
   * The current {@link org.continuent.appia.core.TimerManager TimerManager} will be
   * {@link org.continuent.appia.core.TimerManager#stop stoped}. The new
   * {@link org.continuent.appia.core.TimerManager TimerManager} will be
   * {@link org.continuent.appia.core.TimerManager#start started}.
   *
   * @param timerManager the new {@link org.continuent.appia.core.TimerManager TimerManager}
   */
  public static void setTimerManager(TimerManager timerManager) {
    appia.instanceSetTimerManager(timerManager);
  }
  
  
  /**
   * Registers a new {@link org.continuent.appia.core.EventScheduler EventScheduler}.
   * <br>
   * <b>This method is called by {@link org.continuent.appia.core.Channel#start Channel.start()}</b>
   *
   * @param eventScheduler the new {@link org.continuent.appia.core.EventScheduler EventScheduler}
   */
  public static void insertEventScheduler(EventScheduler eventScheduler) {
    appia.instanceInsertEventScheduler(eventScheduler);
  }
  
  /**
   * Deregisters a {@link org.continuent.appia.core.EventScheduler EventScheduler}.
   *
   * @param eventScheduler the {@link org.continuent.appia.core.EventScheduler EventScheduler} to deregister.
   * It does nothing if the given {@link org.continuent.appia.core.EventScheduler EventScheduler} is not registered.
   */
  public static void removeEventScheduler(EventScheduler eventScheduler) {
    appia.instanceRemoveEventScheduler(eventScheduler);
  }
  
  /**
   * <b>In the Java version this method does nothing.</b>
   */
  public static void insertListenRequest(ExternalEvent descriptor) {
    appia.instanceInsertListenRequest(descriptor);
  }
  
  /**
   * <b>In the Java version this method does nothing.</b>
   */
  public static void removeListenRequest(ExternalEvent descriptor) {
    appia.instanceRemoveListenRequest(descriptor);
  }
  
  /**
   * Starts <i>Appia</i> operation.
   * <br>
   * This method implements the infinite loop that calls the
   * {@link org.continuent.appia.core.EventScheduler#consumeEvent consumeEvent()} method
   * of the registered {@link org.continuent.appia.core.EventScheduler EventSchedulers}.
   * <br>
   * It's this method that makes the <i>Appia</i> run.
   */
  public static void run() {
      appia.instanceRun();
  }
  
  /**
   * Method used by any {@link org.continuent.appia.core.EventScheduler EventScheduler} to signal
   * that a new {@link org.continuent.appia.core.Event Event} has been inserted.
   * <br>
   * The <i>Appia</i> keeps track of how many events exist in all the
   * {@link org.continuent.appia.core.EventScheduler EventSchedulers}, and if none exist it simply
   * waits idle.
   */
  public static void insertedEvent() {
    appia.instanceInsertedEvent();
  }
  
  /**
   * Gets the Thread where Appia is running.
   * @return Thread
   */
  public static Thread getAppiaThread() {
    return appia.thread;
  }
  
}
