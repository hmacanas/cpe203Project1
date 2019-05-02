final class Animation implements Action
{
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

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

    public Entity getEntity() {
        return entity;
    }

    public int getRepeatCount() {
        return repeatCount;
    }
}
