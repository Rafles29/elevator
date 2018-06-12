package listeners;

import elevator.Passenger;
import java.util.List;

public interface IListener {

    default void newPersonOnFloor(int floor, Passenger passenger) {}
    default void personLeftFloor(int floor, Passenger passenger) {}
    default void peopleExited(int floor, int newCount, List<Passenger> passengers) {}
    default void peopleEntered(int floor, int newCount, List<Passenger> passengers) {}
    default void elevatorToFrom(int to, int from, int passengersCount) {}
    default void openDoor(int floor) {}
    default void closeDoor(int floor) {}
    default void pressUpButton(int floor) {}
    default void pressDownButton(int floor) {}
    default void turnOffButtons(int floor) {}
    default void initElevator(int floor, int passengersCount) {}
    default void updateStats(long currentTime, double load) {}
    
}
