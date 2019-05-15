import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity
{

    private int actionPeriod;
    public ActivityEntity(String id, Point position, List<PImage> images, int actionPeriod)
    {
        super(id, position, images);;
        this.actionPeriod = actionPeriod;
    }

    public Activity createActivityAction(WorldModel world,
                                         ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    public int getActionPeriod() {
        return this.actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }


}
