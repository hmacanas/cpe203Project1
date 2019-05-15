import processing.core.PImage;

import java.util.List;
import java.util.Optional;


final class Miner_Full extends AnimationEntity implements Schedulable {

    private final int resourceLimit;

    public Miner_Full(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
    }

    public Point nextPositionOreBlob(WorldModel world,
                                     Point destPos) {
        int horiz = Integer.signum(destPos.x - super.getPosition().x);
        Point newPos = new Point(super.getPosition().x + horiz,
                super.getPosition().y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Ore.class))) {
            int vert = Integer.signum(destPos.y - super.getPosition().y);
            newPos = new Point(super.getPosition().x, super.getPosition().y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class))) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    public Point nextPositionMiner(WorldModel world,
                                   Point destPos) {
        int horiz = Integer.signum(destPos.x - super.getPosition().x);
        Point newPos = new Point(super.getPosition().x + horiz,
                super.getPosition().y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - super.getPosition().y);
            newPos = new Point(super.getPosition().x,
                    super.getPosition().y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = super.getPosition();
            }
        }

        return newPos;
    }

    public boolean moveToFull(WorldModel world,
                              Entity target, EventScheduler scheduler) {
        if (Point.adjacent(super.getPosition(), target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void transformFull(WorldModel world,
                              EventScheduler scheduler, ImageStore imageStore) {
        Miner_Not_Full miner = super.getPosition().createMinerNotFull(super.getId(), this.resourceLimit,
                super.getActionPeriod(), super.getAnimationPeriod(),
                super.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        scheduler.scheduleActions(world, imageStore, miner);
    }

    public void executeActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(super.getPosition(),
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }

    public void scheduleAllEvents(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                this.createActivityAction(world, imageStore),
                this.getActionPeriod());
        scheduler.scheduleEvent(this, this.createAnimationAction(0),
                this.getAnimationPeriod());
    }
}