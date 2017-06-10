package com.wordpress.abhirockzz.cdi.async;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.event.ObservesAsync;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/")
public class TickTockWebsocket {
    
    private static final List<Session> SUBSCRIBERS = new CopyOnWriteArrayList<>();
    
    @OnOpen
    public void register(Session subscriber){
        System.out.println("Peer "+ subscriber.getId() +" joined");
        SUBSCRIBERS.add(subscriber);
        try {
            subscriber.getBasicRemote().sendText("hello there.. get ready for CDI + Websocket powered TickTocks!");
        } catch (IOException ex) {
            Logger.getLogger(TickTockWebsocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @OnClose
    public void deregister(Session subscriber){
        System.out.println("Peer "+ subscriber.getId() +" left");
        SUBSCRIBERS.remove(subscriber);
    }
    
    public void listener(@ObservesAsync @TickTockQualfier TickTock ticktock){
        System.out.println("Event in Websocket endpoint thread " +Thread.currentThread().getName()+ ". this will be broadcasted...");
        SUBSCRIBERS.stream()
                   .filter(s -> s.isOpen())
                   .forEach(s -> s.getAsyncRemote().sendText(ticktock.toString(), (SendResult sr) -> {
                       if(sr.isOK()){
                           System.out.println("sent event to websocket peer "+ s.getId());
                       }
        }));
    }
}
