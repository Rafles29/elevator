package generator;

import elevator.Elevator;
import entity.Clock;
import entity.Entity;
import java.util.Random;

public abstract class AbstractGenerator extends Entity {
    
    protected Elevator elevator;
    protected Random rand;
    protected int nFloors;
    protected double lambda;

    public AbstractGenerator(Elevator elevator, Clock clock) {
        this.elevator = elevator;
        this.clock = clock;
        
        rand = new Random(42);
        nFloors = elevator.getNumbFloors();
        lambda = 0.1;
        
        long time = Math.round(Math.log(1-(new Random()).nextDouble())/(-lambda));
        this.clock.pushUpdate(this, time);        
    }
    
    protected final long getTime(double lambda) {
        return  Math.round(Math.log(1-rand.nextDouble())/(-lambda));
    }

    public double getLambda() {
        return lambda;
    }

    public void setLambda(double lambda) {
        this.lambda = lambda;
    }
    
}
