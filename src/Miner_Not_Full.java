import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import processing.core.PImage;

final class Miner_Not_Full extends AnimationEntity implements Schedulable
{
    private final int resourceLimit;
    private int resourceCount;
    private final PathingStrategy pathingStrategy;


    public Miner_Not_Full(String id, Point position, List<PImage> images, int resourceLimit, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
//        this.pathingStrategy = new SingleStepPathingStrategy();
        this.pathingStrategy = new AStarPathingStrategy();
    }

    public Point nextPositionMiner(WorldModel world,
                                   Point destPos)
    {
        Point start = super.getPosition();
        Point end = destPos;
        Point command;

        Predicate<Point> canPassThrough = (pt) -> !world.isOccupied(pt);


        // within reach means, condition to stop search and satisfy
        BiPredicate<Point, Point> withinReach = (pt1, pt2) -> Math.abs((pt1.x - pt2.x)) <= 1 || Math.abs((pt1.y - pt2.y)) <= 1;


        List<Point> l = pathingStrategy.computePath(start, end, canPassThrough, (p1, p2) -> Functions.neighbors(p1,p2), PathingStrategy.CARDINAL_NEIGHBORS);

        if(l.isEmpty())
            return super.getPosition();

        return l.get(0);

//        int horiz = Integer.signum(destPos.x - super.getPosition().x);
//        Point newPos = new Point(super.getPosition().x + horiz,
//               super.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.y - super.getPosition().y);
//            newPos = new Point(super.getPosition().x,
//                    super.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos))
//            {
//                newPos = super.getPosition();
//            }
//        }
//
//        return newPos;
    }


    public boolean moveToNotFull(WorldModel world,
                                 Entity target, EventScheduler scheduler)
    {
        if (Point.adjacent(super.getPosition(), target.getPosition()))
        {
            this.resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else
        {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!super.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public boolean transformNotFull(WorldModel world,
                                    EventScheduler scheduler, ImageStore imageStore)
    {
        if (this.resourceCount >= this.resourceLimit)
        {
            Miner_Full miner = super.getPosition().createMinerFull(super.getId(), this.resourceLimit,
                    super.getActionPeriod(), super.getAnimationPeriod(),
                    super.getImages());

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            scheduler.scheduleActions(world, imageStore, miner);

            return true;
        }

        return false;
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(super.getPosition(),
                Ore.class);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(world, notFullTarget.get(), scheduler) ||
                !this.transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    super.getActionPeriod());
        }
    }
}