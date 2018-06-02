package com.company.rafles.entity;
import java.util.PriorityQueue;

public class Clock {

    private class Element implements Comparable<Element> {

        private IEntity entity;
        private long timeStamp;

        public Element(IEntity entity, long timeStamp) {
            this.entity = entity;
            this.timeStamp = timeStamp;
        }

        public IEntity getEntity() {
            return entity;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        @Override
        public int compareTo(Element e) {
            return Long.compare( e.getTimeStamp(), this.getTimeStamp());

        }

    }

    private long currentTime;
    private PriorityQueue<Element> timeQueue = new PriorityQueue<Element>();

    public long getCurrentTime() {
        return this.currentTime;
    }

    public long nextTime() {
        return this.timeQueue.peek().getTimeStamp();
    }

    public void tick() {
        this.timeQueue.remove().entity.update();
    }

    public boolean isTicking() {
        return !this.timeQueue.peek().entity.isBlocking();
    }

    public void pushUpdate(IEntity entity, long timeDiff) {
        long time = this.getCurrentTime() + timeDiff;
        Element el = new Element(entity,time);
        this.timeQueue.add(el);
    }

    // TODO: 02.06.2018 check if behaves properly 
    public void remove(IEntity entity) {
        while(this.timeQueue.remove(entity)) {

        }
    }

    public void reset() {
        this.currentTime = 0;
    }

}
