package policy;

import elevator.Elevator;

public class MorningPolicy extends MomentumPolicy{
    
    public MorningPolicy(Elevator elevator) {
        super(elevator);
    }

    @Override
    protected int choose_next() {
        if(this.floors_queue.isEmpty() && this.elevator.getCurrentFloor() != 0)
            return 0;
        return super.choose_next();
    }

}
