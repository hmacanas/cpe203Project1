import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Vein implements ActivityEntity, Schedulable
{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int actionPeriod;

    public Vein(String id, Point position,
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
        Optional<Point> openPt = this.position.findOpenAround(world);

        if (openPt.isPresent())
        {
            Ore ore = openPt.get().createOre(Functions.ORE_ID_PREFIX + this.id,
                    Functions.ORE_CORRUPT_MIN +
                            Functions.rand.nextInt(Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            scheduler.scheduleActions(world, imageStore, ore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.actionPeriod);
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
