package com.wordpress.abhirockzz.cdi.async;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.sse.SseEventSink;

@Stateless
@Path("events")
public class EventsResource {

    @Inject
    private Broadcaster broadcaster;

    //registeration
    @Path("subscribe")
    @GET
    @Produces("text/event-stream")
    public void subscribe(@Context SseEventSink eSink) {
        broadcaster.register(eSink);
    }

}
