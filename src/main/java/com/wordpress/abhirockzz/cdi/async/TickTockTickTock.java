package com.wordpress.abhirockzz.cdi.async;

import java.util.Date;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.event.Event;
import javax.enterprise.event.NotificationOptions;
import javax.inject.Inject;

@Singleton
@Startup
public class TickTockTickTock {
    @Resource
    private TimerService tsvc;

    @PostConstruct
    public void init() {
        tsvc.createTimer(10000, 5000, null);
        System.out.println("timer created......");
    }
    
    @Inject
    @TickTockQualfier
    private Event<TickTock> eventBus;
    
    
    private static final Random gen = new Random();
    
    @Resource
    private ManagedExecutorService mes;
    
    @Timeout
    public void onTrigger() {
        System.out.println("timer triggered");
        eventBus.fireAsync(new TickTock("tick-"+gen.nextInt(10), "tock-"+gen.nextInt(10)),
                            NotificationOptions.builder().setExecutor(mes).build());
        System.out.println("Fired CDI event from thread "+ Thread.currentThread().getName());
    }
}
