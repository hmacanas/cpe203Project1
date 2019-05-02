public interface Schedulable extends Entity {
    void scheduleAllEvents(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
}
