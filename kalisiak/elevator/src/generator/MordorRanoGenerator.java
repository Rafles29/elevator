package generator;

import elevator.Elevator;
import elevator.Passenger;
import entity.Clock;
import entity.Entity;
import java.util.ArrayList;

public class MordorRanoGenerator extends AbstractGenerator {

    public MordorRanoGenerator(Elevator elevator, Clock clock) {
        super(elevator, clock);
        
        double groundIntensity = 0.9 * lambda;
        double restIntensity = (lambda / (nFloors - 1)) * 0.1;

        ArrayList<Entity> floors = new ArrayList<>();
        AbstractGenerator ground = new AbstractGenerator(this.elevator, this.clock) {
            
            @Override
            public void update() {
                int from = 0;
                int to = rand.nextInt(nFloors - 1) + 1;

                elevator.addPassenger(new Passenger(from, to, clock.getCurrentTime()));
                long time = getTime(groundIntensity);
                clock.pushUpdate(this, time);
            }
        };
        floors.add(ground);
        
        for (int i = 1; i < nFloors; i++) {
            int destFrom = i;
            AbstractGenerator floor = new AbstractGenerator(this.elevator, this.clock) {

                @Override
                public void update() {
                    int from = destFrom;
                    int to;
                    
                    do {
                        to = rand.nextInt(nFloors);
                    } while (to == from);

                    elevator.addPassenger(new Passenger(from, to, clock.getCurrentTime()));
                    long time = getTime(restIntensity);
                    clock.pushUpdate(this, time);
                }
            };
            floors.add(floor);
        }
    }

    @Override
    public void update() {

    }
    
}
