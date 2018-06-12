package elevator;

import controllers.Controller;
import entity.Clock;
import entity.Entity;
import listeners.IListener;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends Entity {


    public enum Doors {
        OPEN,CLOSED
    }
    public enum State {
        STATIONARY,MOVING_UP,MOVING_DOWN,DOORS_OPENNING,DOORS_CLOSING,GETTING_IN_PEOPLE,GETTING_OUT_PEOPLE
    }
    public enum Decision {
        MOVEUP,MOVEDOWN,OPENDOORS,STAY,CLEAR
    }
    //enums

    private ArrayList<IListener> listeners;
    private ArrayList<Floor> floors;
    private int currentFloor;
    private Doors doors;
    private State state;
    private Decision decision;
    private int numbFloors;
    private int size;
    private ArrayList<Passenger> passengers;

    public Elevator(int numbFloors, Clock clock, int size, Controller controller) {
        this.numbFloors = numbFloors;
        this.clock = clock;
        this.clock.pushUpdate(this,1);
        this.listeners = new ArrayList<>();
        this.listeners.add(controller);
        this.floors = new ArrayList<>();
        for (int i=0;i<numbFloors;i++) {
            this.floors.add(new Floor());
        }
        this.currentFloor = 3; // TODO change
        this.doors = Doors.CLOSED;
        this.size = size;
        this.state = State.STATIONARY;
        this.decision = null;
        this.passengers = new ArrayList<Passenger>();
        this.listeners.stream().forEach((listener) -> {
            listener.initElevator(this.currentFloor, this.passengers.size());
        });
        this.block();
    }


    //Internal
    public  void clearDecision() {
        this.decision = Decision.CLEAR;
    }

    public void openDoors() {
        this.doors = Doors.OPEN;
        this.listeners.stream().forEach((listener) -> {
            listener.openDoor(currentFloor);
            listener.turnOffButtons(currentFloor);
        });
    }

    public void closeDoors() {
        this.doors = Doors.CLOSED;
        this.listeners.stream().forEach((listener) -> {
            listener.closeDoor(currentFloor);
        });
        pressButtons(this.currentFloor);
    }

    private void pressButtons(int floor) {
        if (this.floors.get(floor).isButtonPressed(Floor.Direction.DOWN)) {
            this.listeners.stream().forEach((listener) -> {
                listener.pressDownButton(floor);
            });
        }
        if (this.floors.get(floor).isButtonPressed(Floor.Direction.UP)) {
            this.listeners.stream().forEach((listener) -> {
                listener.pressUpButton(floor);
            });
        }
    }
    
    public boolean isDoorsOpen() {
        if (this.getDoors() == Doors.OPEN)
            return true;
        else
            return false;
    }

    public int getNumberOfPassengers() {
        return this.passengers.size();
    }

    public int calculateSpaceLeft() {
        return this.getSize() - this.getNumberOfPassengers();
    }

    public void moveUp() {
        if(this.isDoorsOpen()) {
            throw new Error("The doors are opened. It's impossible to move with opened doors");
        }
        else if (this.currentFloor == this.floors.size()) {
            throw new Error("You are already on the last floor");
        }
        else {
            this.currentFloor = currentFloor + 1;
            this.listeners.stream().forEach((listener) -> {
                listener.elevatorToFrom(this.currentFloor, this.currentFloor - 1, this.passengers.size());
            });
        }
    }

    public void moveDown() {
        if(this.isDoorsOpen()) {
            throw new Error("The doors are opened. It's impossible to move with opened doors");
        }
        else if (this.currentFloor == 0) {
            throw new Error("You are already on the ground floor");
        }
        else {
            this.currentFloor = currentFloor - 1;
            this.listeners.stream().forEach((listener) -> {
                listener.elevatorToFrom(this.currentFloor, this.currentFloor + 1, this.passengers.size());
            });
        }
    }

    public void releasePassengers() {
        if (!this.isDoorsOpen()) {
            throw new Error("You have to open the doors first!");
        }

        ArrayList<Passenger> passList = (ArrayList)this.getPassengers().clone();
        for (Passenger pass: passList) {
            if(pass.getDestination() == this.getCurrentFloor()) {
                this.passengers.remove(pass);
            }
        }
        
        passList.removeAll(this.passengers);
        this.listeners.stream().forEach((listener) -> {
            listener.peopleExited(this.currentFloor, this.passengers.size(), passList);
        });
    }

    public void takePassengers(Floor.Direction direction) {
        if (!this.isDoorsOpen()) {
            throw new Error("You have to open the doors first!");
        }

        Floor currentFloor = this.getFloors().get(this.currentFloor);
        ArrayList<Passenger> passengers;

        passengers = currentFloor.popPassengers(direction,this.calculateSpaceLeft());
        this.passengers.addAll(passengers);
        for(int i = 0; i < passengers.size(); i++) {
            final Passenger passenger = passengers.get(i);
            this.listeners.stream().forEach((listener) -> {
                listener.personLeftFloor(this.currentFloor, passenger);
            });
        }
        this.listeners.stream().forEach((listener) -> {
            listener.peopleEntered(this.currentFloor, this.passengers.size(), passengers);
        });
    }
    //Internal methods

    public int getNumbFloors() {
        return numbFloors;
    }

    public Decision getDecision() {
        return decision;
    }

    public ArrayList<IListener> getListeners() {
        return listeners;
    }

    public ArrayList<Floor> getFloors() {
        return floors;
    }

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Doors getDoors() {
        return doors;
    }

    public int getSize() {
        return size;
    }

    public State getState() {
        return state;
    }

    public ArrayList<Boolean> getButtons(){
        ArrayList<Boolean> buttons = new ArrayList<>();
        for (int i=0; i<this.floors.size();i++) {
            buttons.add(new Boolean(false));
        }

        for(int i=0;i<this.passengers.size();i++){
            buttons.set(this.passengers.get(i).getDestination(),true);
        }
        return buttons;
    }

    public void goUp() {
        this.unblock();
        this.decision = Decision.MOVEUP;
    }

    public void goDown() {
        this.unblock();
        this.decision = Decision.MOVEDOWN;
    }

    public void openUp() {
        this.unblock();
        this.decision = Decision.OPENDOORS;
    }

    public void stay() {
        this.unblock();
        this.decision = Decision.STAY;
    }

    public void addPassenger(Passenger passenger) {
        int floor = passenger.getFrom();
        this.floors.get(floor).pushPassenger(passenger);
        this.listeners.stream().forEach((listener) -> {
            listener.newPersonOnFloor(floor, passenger);
        });
        pressButtons(floor);
    }
    


    //State Machine
    @Override
    public void update() {
        this.listeners.stream().forEach((listener) -> {
            listener.updateStats(this.clock.getCurrentTime(), (double)this.passengers.size() / this.getSize());
        });
        switch (this.state) {
            case STATIONARY:{
                switch (this.decision) {
                    case OPENDOORS:{
                        if(this.isDoorsOpen()) {
                            block();
                            this.clock.pushUpdate(this,1);
                        } else {
                            this.state = State.DOORS_OPENNING;
                            this.clock.pushUpdate(this,1);
                            this.clearDecision();
                        }
                        return;
                    }
                    case MOVEUP:{
                        if(this.isDoorsOpen()) {
                            this.state = State.GETTING_IN_PEOPLE;
                            this.clock.pushUpdate(this,1);
                        } else {
                            this.state = State.MOVING_UP;
                            this.clock.pushUpdate(this,1);
                            this.clearDecision();
                        }
                        return;
                    }
                    case MOVEDOWN:{
                        if(this.isDoorsOpen()) {
                            this.state = State.GETTING_IN_PEOPLE;
                            this.clock.pushUpdate(this,1);
                        } else {
                            this.state = State.MOVING_DOWN;
                            this.clock.pushUpdate(this,1);
                            this.clearDecision();
                        }
                        return;
                    }
                    case STAY: {
                        this.clock.pushUpdate(this,1);
                        this.clearDecision();
                        break;
                    }
                    default: {
                        block();
                        this.clock.pushUpdate(this,1);
                        break;
                    }
                }
                return;
            }
            case DOORS_OPENNING:{
                this.openDoors();
                this.state = State.GETTING_OUT_PEOPLE;
                this.clock.pushUpdate(this,1);
                return;
            }
            case DOORS_CLOSING:{
                this.closeDoors();
                this.state = State.STATIONARY;
                this.clock.pushUpdate(this,1);
                return;
            }
            case MOVING_UP:{
                this.moveUp();
                this.state = State.STATIONARY;
                this.clock.pushUpdate(this,1);
                return;
            }
            case MOVING_DOWN:{
                this.moveDown();
                this.state = State.STATIONARY;
                this.clock.pushUpdate(this,1);
                return;
            }
            case GETTING_OUT_PEOPLE:{
                this.releasePassengers();
                this.state = State.STATIONARY;
                this.clock.pushUpdate(this,1);
                return;
            }
            case GETTING_IN_PEOPLE:{
                switch (this.decision) {
                    case MOVEUP:{
                        this.takePassengers(Floor.Direction.UP);
                        this.state = State.DOORS_CLOSING;
                        this.clock.pushUpdate(this,1);
                        return;
                    }
                    case MOVEDOWN:{
                        this.takePassengers(Floor.Direction.DOWN);
                        this.state = State.DOORS_CLOSING;
                        this.clock.pushUpdate(this,1);
                        return;
                    }
                }
                return;
            }

        }        
    }
    //State Machine

    //For Goliat
    public FullState getFullState() {
        // TODO: 02.06.2018 change this when State class will be implemented
        return new FullState();
    }
    //For Goliat



}
