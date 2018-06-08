package entity;

public abstract class Entity implements IEntity {

    private boolean blocked;
    protected Clock clock;

    protected void block() {
        this.blocked = true;
    }

    protected void unblock() {
        this.blocked = false;
    }

    @Override
    public boolean isBlocking() {
        return this.blocked;
    }
    

}
