final class Animation implements Action
{
    public final Entity entity;
    public final WorldModel world;
    public final ImageStore imageStore;
    public final int repeatCount;

    public Animation(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        scheduler.executeAnimationAction(this);
    }
}
