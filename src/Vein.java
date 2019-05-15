import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class Vein extends ActivityEntity implements Schedulable
{


    public Vein(String id, Point position, List<PImage> images, int actionPeriod) {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = super.getPosition().findOpenAround(world);

        if (openPt.isPresent())
        {
            Ore ore = openPt.get().createOre(Functions.ORE_ID_PREFIX + super.getId(),
                    Functions.ORE_CORRUPT_MIN +
                            Functions.rand.nextInt(Functions.ORE_CORRUPT_MAX - Functions.ORE_CORRUPT_MIN),
                    imageStore.getImageList(Functions.ORE_KEY));
            world.addEntity(ore);
            scheduler.scheduleActions(world, imageStore, ore);
        }

        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                super.getActionPeriod());
    }

    public void scheduleAllEvents(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
    }

}
