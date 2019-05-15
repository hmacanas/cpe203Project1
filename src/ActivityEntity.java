import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity
{

    public ActivityEntity(String id, Point position, List<PImage> images)
    {
        super(id, position, images);;
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
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }
}
