package controler;

import controllers.Controller;
import elevator.Elevator;
import elevator.Passenger;
import entity.Clock;
import entity.Entity;
import generator.AbstractGenerator;
import generator.Generator;
import generator.IGenerator;
import generator.MordorRanoGenerator;
import generator.MordorWieczorGenerator;
import generator.RandomGenerator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import policy.FCFSPolicy;
import policy.IPolicy;
import policy.MomentumPolicy;
import policy.MorningPolicy;
import policy.Policy;

public class RealTimeController implements Runnable {

    private Clock clock;
    private Elevator elevator;
    private IPolicy policy;
    private AbstractGenerator generator;
    private Controller controller = null;
    private int floorCount;
    private int elevatorSize;
    private double lambda = 0.1;
    private int milisPerTick = 1000;
    private boolean isStatic;
    private boolean suspended;
    private Thread thread;
 
    
    public RealTimeController(int floorCount, int elevatorSize, Controller controller) {
        this.controller = controller;
        this.floorCount = floorCount;
        this.elevatorSize = elevatorSize;
    }
    
    public void setMilis(int milis) {
        this.milisPerTick = milis;
    }

    public void setElevatorSize(int elevatorSize) {
        this.elevatorSize = elevatorSize;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
    
    public void setLambda(double lambda) {
        this.lambda = lambda;
        if(this.generator != null) 
            this.generator.setLambda(this.lambda);
    }
    
    public AbstractGenerator createGenerator(String name) {
        if(name.equals("Random"))
            return new RandomGenerator(this.elevator, this.clock);
        if(name.equals("Office Morning"))
            return new MordorRanoGenerator(this.elevator, this.clock);
        if(name.equals("Office Evening"))
            return new MordorWieczorGenerator(this.elevator, this.clock);
        else 
            return new RandomGenerator(this.elevator, this.clock);
    }
    
    public IPolicy createPolicy(String name) {
        if(name.equals("FCFS"))
            return new FCFSPolicy(this.elevator);
        if(name.equals("Momentum"))
            return new MomentumPolicy(this.elevator);
        if(name.equals("Morning"))
            return new MorningPolicy(this.elevator);
        else 
            return new FCFSPolicy(this.elevator);
    }
    
    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notify();
    }
    
    public void stop(){
        if(this.thread != null)
            this.thread.stop();
    }
    
    public void restart() {
        if(this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }
        this.controller.cleanup();
    }
    
    public synchronized void start() {
        this.clock = new Clock();
        this.elevator = new Elevator(this.floorCount, this.clock, this.elevatorSize, this.controller);
        if(!isStatic) {
            this.generator = createGenerator(this.controller.getGeneratorName());
            this.generator.setLambda(this.lambda);
        }
        System.out.println(this.generator);
        this.policy = createPolicy(this.controller.getPolicyName());
        System.out.println(this.policy);
        this.suspended = false;
        
        System.out.println("Starting with " + Integer.toString(milisPerTick));
        if (thread == null) {
           System.out.println("creating new thread ");
           thread = new Thread(this);
           thread.start();
        }
    }
    
    @Override
    public void run() {
        /*
        while (clock.isTicking()) {
            Platform.runLater(
                () -> {
                  clock.tick();
                }
            );
            try {
                sleep(clock.nextTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (elevator.isBlocking()) {
            policy.decide();
        }
        */

        if(isStatic) {            
            Platform.runLater( () -> {
                this.elevator.addPassenger(new Passenger(0,5,0));
                this.elevator.addPassenger(new Passenger(1,3,0));
                this.elevator.addPassenger(new Passenger(1,0,0));
                this.elevator.addPassenger(new Passenger(3,1,0)); 
                this.elevator.addPassenger(new Passenger(3,5,0));
                this.elevator.addPassenger(new Passenger(3,5,0));
                this.elevator.addPassenger(new Passenger(3,5,0));
                this.elevator.addPassenger(new Passenger(3,5,0));
                this.elevator.addPassenger(new Passenger(3,5,0));
            });
        }

        while(true) {
            for (int i = 0;i<4;i++) {
                synchronized(this) {
                    while(suspended) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RealTimeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                while (this.clock.isTicking()) {
                    final CountDownLatch latch = new CountDownLatch(1);
                    Platform.runLater(
                        () -> {
                          this.clock.tick();
                          latch.countDown();
                        }
                    );

                    try {
                        latch.await();
                        Thread.sleep(this.clock.nextTime() * this.milisPerTick);
                        //Thread.sleep(0);
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted");
                        return;
                    }
                }
                if (this.elevator.isBlocking()) {
                    this.policy.decide();
                }
            }
        }
    }
}
