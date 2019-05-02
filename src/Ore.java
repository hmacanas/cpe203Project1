import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class Ore implements ActivityEntity, Schedulable
{

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    public Ore(String id, Point position,
                   List<PImage> images, int resourceLimit, int resourceCount,
                   int actionPeriod, int animationPeriod)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.actionPeriod = actionPeriod;
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

    public void executeActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.position;  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Ore_Blob blob = pos.createOreBlob(this.id + Functions.BLOB_ID_SUFFIX,
                this.actionPeriod / Functions.BLOB_PERIOD_SCALE,
                Functions.BLOB_ANIMATION_MIN +
                        Functions.rand.nextInt(Functions.BLOB_ANIMATION_MAX - Functions.BLOB_ANIMATION_MIN),
                imageStore.getImageList(Functions.BLOB_KEY));

        world.addEntity(blob);
        scheduler.scheduleActions(world, imageStore, blob);
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public void scheduleAllEvents(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }
}