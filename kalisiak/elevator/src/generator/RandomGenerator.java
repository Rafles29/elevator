package generator;

import elevator.Elevator;
import elevator.Passenger;
import entity.Clock;

public class RandomGenerator extends AbstractGenerator {
    
    public RandomGenerator(Elevator elevator, Clock clock) {
        super(elevator, clock);
    }

    @Override
    public void update() {
        int from = rand.nextInt(nFloors);
        int to;

        do {
            to = rand.nextInt(nFloors);
        } while (to == from);

        elevator.addPassenger(new Passenger(from, to, clock.getCurrentTime()));
        long time = getTime(lambda);
        clock.pushUpdate(this, time);
    }
}
