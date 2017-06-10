package com.wordpress.abhirockzz.cdi.async;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Path("")
@Singleton
public class Broadcaster {
    @Context
    private Sse sse;
    
    private SseBroadcaster broadcaster;
    
    @PostConstruct
    public void init(){
        broadcaster = sse.newBroadcaster();
        System.out.println("broadcaster created");
    }
    
    @Inject
    TickTockTickTock timer;
    
    public void register(SseEventSink eventSink){
        broadcaster.register(eventSink);
        System.out.println("Registered Event sink");
    }
    public void listener(@ObservesAsync @TickTockQualfier TickTock ticktock){
        System.out.println("SSE event in thread "+ Thread.currentThread().getName());
        OutboundSseEvent sseEvent = sse.newEventBuilder().name(ticktock.getTick()).data(ticktock.getTock()).mediaType(MediaType.TEXT_PLAIN_TYPE).build();
        broadcast(sseEvent);
    }
    private void broadcast(OutboundSseEvent event){
        try {
            broadcaster.broadcast(event);
        } catch (Exception ex) {
            Logger.getLogger(Broadcaster.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @PreDestroy
    public void free(){
        broadcaster.close();
        System.out.println("broadcaster closed");
    }
}
