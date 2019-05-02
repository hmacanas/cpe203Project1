import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Quake implements AnimationEntity, ActivityEntity, Schedulable
{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;
    private final int animationPeriod;

    public Quake(String id, Point position,
                   List<PImage> images, int resourceLimit, int resourceCount,
                   int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getActionPeriod(){return actionPeriod;}

    public Point getPosition(){return this.position;}

    public void setPosition(Point newPt) { this.position = newPt;}

    public List<PImage> getImages(){return this.images;}

    public int getImageIndex(){return this.imageIndex;}

    public Activity createActivityAction(WorldModel world,
                                         ImageStore imageStore)
    {
        return new Activity(this, world, imageStore, 0);
    }

    public Animation createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }


    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public int getAnimationPeriod()
    {
        return this.animationPeriod;
    }

    public void scheduleAllEvents(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, this.createActivityAction(world, imageStore), this.getActionPeriod());
        scheduler.scheduleEvent(this,
                this.createAnimationAction(Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                this.getAnimationPeriod());
    }

}