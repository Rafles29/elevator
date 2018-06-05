package com.company.rafles.elevator;

import com.company.rafles.entity.Clock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

    private Clock clock;
    private Elevator elevator;

    @BeforeEach
    void setUp() {
        this.clock = new Clock();
        this.elevator = new Elevator(4,clock,6);
    }

    @Test
    void moveUp() {
        Assertions.assertEquals(0,this.elevator.getCurrentFloor());
        this.elevator.moveUp();
        Assertions.assertEquals(1,this.elevator.getCurrentFloor());
    }

    @Test
    void moveDown() {
        Assertions.assertEquals(0,this.elevator.getCurrentFloor());
        this.elevator.moveUp();
        this.elevator.moveDown();
        Assertions.assertEquals(0,this.elevator.getCurrentFloor());
    }


    @Test
    void takePassengers() {
        Passenger passenger = new Passenger(0,2,4);
        this.elevator.addPassengerToFloor(0,passenger);
        this.elevator.openDoors();
        this.elevator.takePassengers(Floor.Direction.UP);
        Assertions.assertEquals(1,this.elevator.getPassengers().size());
    }

    @Test
    void releasePassengers() {
        Passenger passenger = new Passenger(0,2,4);
        this.elevator.addPassengerToFloor(0,passenger);
        this.elevator.openDoors();
        this.elevator.takePassengers(Floor.Direction.UP);
        Assertions.assertEquals(1,this.elevator.getPassengers().size());
        this.elevator.releasePassengers();
        Assertions.assertEquals(1,this.elevator.getPassengers().size());

    }

    @Test
    void getButtons() {
        Passenger passenger = new Passenger(0,2,4);
        this.elevator.addPassengerToFloor(0,passenger);
        this.elevator.openDoors();
        this.elevator.takePassengers(Floor.Direction.UP);
        Assertions.assertEquals(1,this.elevator.getPassengers().size());
        ArrayList<Boolean> bools = this.elevator.getButtons();
        Assertions.assertEquals(true,bools.get(2));
    }


    @Test
    void update() {
        Passenger passenger = new Passenger(1,2,4);
        this.elevator.addPassengerToFloor(1,passenger);

        for (int i = 0;i<4;i++) {
            while (clock.isTicking()) {
                clock.tick();
            }
            if (elevator.isBlocking()) {
                if (i == 0) elevator.goUp();
                if (i == 1) elevator.openUp();
                if (i == 2) elevator.goUp();
                if (i == 3) elevator.openUp();

            }
        }
        System.out.println("done");
    }
}