package generator;

import elevator.Elevator;
import entity.Clock;
import entity.Entity;
import java.util.Random;

// maybe create field lambda to allow intensity modification
public abstract class AbstractGenerator extends Entity {
    
    protected Elevator elevator;
    protected Random rand;
    protected int nFloors;

    public AbstractGenerator(Elevator elevator, Clock clock) {
        this.elevator = elevator;
        this.clock = clock;
        
        rand = new Random(42);
        nFloors = elevator.getNumbFloors();
        
        this.clock.pushUpdate(this, 0);        
    }
    
    /*protected static int getPoisson(double lambda) {
        double l = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;

        do {
            k++;
            p *= Math.random();
        } while (p > l);

        return k - 1;
    }*/
    // TODO set seed
    protected long getTime(double lambda) {
        return  Math.round(Math.log(1-rand.nextDouble())/(-lambda)) + 1;
    }
    
}
