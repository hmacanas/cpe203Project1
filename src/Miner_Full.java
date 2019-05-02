import processing.core.PImage;

import java.util.List;
import java.util.Optional;


final class Miner_Full implements Entity, Execute {
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;
    private final int resourceLimit;
    private int resourceCount;
    private final int actionPeriod;
    private final int animationPeriod;

    public Miner_Full(String id, Point position,
                      List<PImage> images, int resourceLimit, int resourceCount,
                      int actionPeriod, int animationPeriod) {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    public Point getPosition() {
        return this.position;
    }

    public void setPosition(Point newPt) {
        this.position = newPt;
    }

    public List<PImage> getImages() {
        return this.images;
    }

    public int getImageIndex() {
        return this.imageIndex;
    }

    public Activity createActivityAction(WorldModel world,
                                         ImageStore imageStore) {
        return new Activity(this, world, imageStore, 0);
    }

    public Animation createAnimationAction(int repeatCount) {
        return new Animation(this, null, null, repeatCount);
    }

    public Point nextPositionOreBlob(WorldModel world,
                                     Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 ||
                (occupant.isPresent() && !(occupant.get().getClass() == Ore.class))) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x, this.position.y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 ||
                    (occupant.isPresent() && !(occupant.get().getClass() == Ore.class))) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public Point nextPositionMiner(WorldModel world,
                                   Point destPos) {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = this.position;
            }
        }

        return newPos;
    }

    public boolean moveToFull(WorldModel world,
                              Entity target, EventScheduler scheduler) {
        if (Point.adjacent(this.position, target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionMiner(world, target.getPosition());

            if (!this.position.equals(nextPos)) {
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
        Miner_Not_Full miner = this.position.createMinerNotFull(this.id, this.resourceLimit,
                this.actionPeriod, this.animationPeriod,
                this.images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        scheduler.scheduleActions(world, imageStore, miner);
    }

    public void executeActivity(WorldModel world,
                                         ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                Blacksmith.class);

        if (fullTarget.isPresent() &&
                this.moveToFull(world, fullTarget.get(), scheduler)) {
            this.transformFull(world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent(this,
                    this.createActivityAction(world, imageStore),
                    this.actionPeriod);
        }
    }

    public void nextImage() {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }
}