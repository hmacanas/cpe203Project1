final class Activity implements Action
{
    private final NameTmp entity;
    private final WorldModel world;
    private final ImageStore imageStore;

    public Activity(NameTmp entity, WorldModel world,
                    ImageStore imageStore, int repeatCount)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
    }

    public NameTmp getEntity() {
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