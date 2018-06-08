package controler;

import controllers.Controller;
import elevator.Elevator;
import elevator.Passenger;
import entity.Clock;
import generator.Generator;
import generator.IGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import policy.IPolicy;
import policy.Policy;

public class RealTimeController extends Thread {

    private Clock clock;
    private Elevator elevator;
    private IPolicy policy;
    private IGenerator generator;
    private Controller controller = null;
    private int floorCount;
    private int elevatorSize;
    private int milisPerTick = 100;
    private boolean isRunning;

    public RealTimeController(int floorCount, int elevatorSize, Controller controller) {
        this.controller = controller;
        this.floorCount = floorCount;
        this.elevatorSize = elevatorSize;
    }
    
    public void setMilis(int milis) {
        this.milisPerTick = milis;
    }
    
    @Override
    public synchronized void start() {
        this.clock = new Clock();
        this.elevator = new Elevator(this.floorCount, this.clock, this.elevatorSize, this.controller);
        this.generator = new Generator(elevator, clock);
        this.policy = new Policy(elevator);
        this.isRunning = true;
        
        super.start();
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

        while(this.isRunning) {
            for (int i = 0;i<10;i++) {
                while (clock.isTicking()) {
                    Platform.runLater(
                        () -> {
                          clock.tick();
                        }
                    );

                    try {
                        //sleep(clock.nextTime() * this.milisPerTick);
                        sleep(300);
                        System.out.println("ticked");
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted");
                        this.isRunning = false;
                        return;
                    }
                }
                if (elevator.isBlocking()) {
                    if (i == 0) elevator.openUp();
                    if (i == 1) elevator.goDown();
                    if (i == 2) elevator.openUp();
                    if (i == 3) elevator.goDown();
                    if (i == 4) elevator.openUp();
                    if (i == 5) elevator.goDown();
                    if (i == 6) elevator.openUp();
                    if (i == 7) elevator.goDown();
                    if (i == 8) elevator.openUp();
                    if (i == 9) elevator.goDown();
                }
            }
            System.out.println("done");
        }
    }
}
