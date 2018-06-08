package controler;

import controllers.Controller;
import elevator.Elevator;
import elevator.Passenger;
import entity.Clock;
import generator.Generator;
import generator.IGenerator;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import policy.IPolicy;
import policy.Policy;

public class RealTimeController implements Runnable {

    private Clock clock;
    private Elevator elevator;
    private IPolicy policy;
    private IGenerator generator;
    private Controller controller = null;
    private int floorCount;
    private int elevatorSize;
    private int milisPerTick = 1000;
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
        this.generator = new Generator(elevator, clock);
        this.policy = new Policy(elevator);
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
        
        Passenger passenger = new Passenger(1,2,0);
        Passenger passenger2 = new Passenger(0,1,0);
        Passenger passenger3 = new Passenger(1,4,0);
        Passenger passenger4 = new Passenger(1,0,0);
        Passenger passenger5 = new Passenger(3,2,0);
        Platform.runLater(
            () -> {
              this.elevator.addPassenger(passenger3);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger);
              this.elevator.addPassenger(passenger2);   
              this.elevator.addPassenger(passenger4);   
              this.elevator.addPassenger(passenger5);   
            }
        );

        while(true) {
            for (int i = 0;i<2;i++) {
                synchronized(this) {
                    while(suspended) {
                        try {
                            wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RealTimeController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                while (clock.isTicking()) {
                    final CountDownLatch latch = new CountDownLatch(1);
                    Platform.runLater(
                        () -> {
                          clock.tick();
                          latch.countDown();
                        }
                    );

                    try {
                        latch.await();
                        Thread.sleep(clock.nextTime() * this.milisPerTick);
                        //Thread.sleep(100);
                        System.out.println("ticked");
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted");
                        return;
                    }
                }
                if (elevator.isBlocking()) {
                    if (i % 2 == 0) elevator.openUp();
                    if (i % 2 == 1) elevator.goDown();
                }
            }
            System.out.println("done");
        }
    }
}
