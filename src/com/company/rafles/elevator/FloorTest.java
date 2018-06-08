package com.company.rafles.elevator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {

    private Floor floor;
    private Passenger passengerUp;
    private Passenger passengerDown;

    @BeforeEach
    void setUp() {
        this.floor = new Floor();
        this.passengerUp = new Passenger(0,2,0);
        this.passengerDown = new Passenger(2,0,0);
    }


    @Test
    void pushPassenger() {
        Assertions.assertEquals(0,this.floor.getGoingDown().size());
        Assertions.assertEquals(0,this.floor.getGoingUp().size());

        this.floor.pushPassenger(passengerUp);

        Assertions.assertEquals(1,this.floor.getGoingUp().size());
        Assertions.assertEquals(0,this.floor.getGoingDown().size());

        this.floor.pushPassenger(passengerDown);

        Assertions.assertEquals(1,this.floor.getGoingUp().size());
        Assertions.assertEquals(1,this.floor.getGoingDown().size());

        Assertions.assertEquals(passengerUp.getDestination(),this.floor.getGoingUp().getFirst().getDestination());
        Assertions.assertEquals(passengerDown.getDestination(),this.floor.getGoingDown().getFirst().getDestination());
    }

    @Test
    void popPassenger() {
        Assertions.assertEquals(0,this.floor.getGoingUp().size());
        this.floor.pushPassenger(passengerUp);
        Assertions.assertEquals(1,this.floor.getGoingUp().size());
        this.floor.popPassenger(Floor.Direction.UP);
        this.floor.getGoingUp().size();
        Assertions.assertEquals(0,this.floor.getGoingUp().size());
    }

    @Test
    void popPassengers() {
        Assertions.assertEquals(0,this.floor.getGoingUp().size());
        this.floor.pushPassenger(passengerUp);
        this.floor.pushPassenger(passengerUp);
        this.floor.pushPassenger(passengerUp);
        this.floor.pushPassenger(passengerUp);
        this.floor.pushPassenger(passengerUp);
        this.floor.pushPassenger(passengerUp);
        Assertions.assertEquals(6,this.floor.getGoingUp().size());
        this.floor.popPassengers(Floor.Direction.UP,2);
        Assertions.assertEquals(4,this.floor.getGoingUp().size());
        this.floor.popPassengers(Floor.Direction.UP,6);
        Assertions.assertEquals(0,this.floor.getGoingUp().size());
    }

    @Test
    void isButtonPressed() {
        Assertions.assertEquals(false,this.floor.isButtonPressed(Floor.Direction.UP));
        Assertions.assertEquals(false,this.floor.isButtonPressed(Floor.Direction.DOWN));
    }

}