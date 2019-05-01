final class Activity implements Action
{
    private final Entity entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(Entity entity, WorldModel world,
                  ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public Entity getEntity() {
        return entity;
    }

    public ImageStore getImageStore() {
        return imageStore;
    }

    public WorldModel getWorld() {
        return world;
    }

    public void executeAction(EventScheduler scheduler)
    {
        scheduler.executeActivityAction(this);
    }
}