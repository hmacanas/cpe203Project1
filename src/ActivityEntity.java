import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity
{
    private int imageIndex;
    private final List<PImage> images;

    public ActivityEntity(String id, Point position, List<PImage> images, int imageIndex)
    {
        super(id, position, images);
        this.images = images;
        this.imageIndex = imageIndex;
    }

    public Activity createActivityAction(WorldModel world,
                                         ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    public abstract int getActionPeriod();

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }
}
