package com.company.rafles.controler;

import com.company.rafles.elevator.Elevator;
import com.company.rafles.entity.Clock;
import com.company.rafles.generator.Generator;
import com.company.rafles.generator.IGenerator;
import com.company.rafles.policy.IPolicy;
import com.company.rafles.policy.Policy;

public class RealTimeController extends Thread {

    private Clock clock;
    private Elevator elevator;
    private IPolicy policy;
    private IGenerator generator;

    @Override
    public synchronized void start() {
        this.clock = new Clock();
        this.elevator = new Elevator(10,this.clock,10);
        this.generator = new Generator(elevator, clock);
        this.policy = new Policy(elevator);

        generator.generate();

        super.start();
    }

    @Override
    public void run() {
        while (clock.isTicking()) {
            clock.tick();
            try {
                wait(clock.nextTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (elevator.isBlocking()) {
            policy.decide();
        }
    }
}
