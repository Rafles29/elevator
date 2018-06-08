package com.company.rafles.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClockTest {

    private Clock clock;
    private Entity entity;

    @BeforeEach
    void setUp() {
        clock = new Clock();
        entity = new Entity() {
            @Override
            public void update() {
                System.out.println(10);
            }
        };
    }

    @Test
    void pushUpdate() {
        this.clock.pushUpdate(entity,4);
        Assertions.assertEquals(1,this.clock.getTimeQueue().size());
        Assertions.assertEquals(4,this.clock.getTimeQueue().peek().getTimeStamp());
        Assertions.assertEquals(entity,this.clock.getTimeQueue().peek().getEntity());
    }
    @Test
    void nextTime() {
        this.clock.pushUpdate(entity,5);
        this.clock.pushUpdate(entity,10);
        Assertions.assertEquals(5,this.clock.nextTime());
    }

    @Test
    void tick() {
        this.clock.pushUpdate(entity,5);
        Assertions.assertEquals(1,this.clock.getTimeQueue().size());
        this.clock.tick();
        Assertions.assertEquals(5,this.clock.getCurrentTime());

    }

    @Test
    void isTicking() {
        entity.block();
        this.clock.pushUpdate(entity,5);
        Assertions.assertEquals(false,this.clock.isTicking());
        this.setUp();
        entity.unblock();
        this.clock.pushUpdate(entity,5);
        Assertions.assertEquals(true,this.clock.isTicking());

    }


    @Test
    void remove() {
        this.clock.pushUpdate(this.entity,0);
        Assertions.assertEquals(1,this.clock.getTimeQueue().size());
        this.clock.pushUpdate(this.entity,5);
        Assertions.assertEquals(2,this.clock.getTimeQueue().size());
        this.clock.remove(entity);
        Assertions.assertEquals(0,this.clock.getTimeQueue().size());
    }

}