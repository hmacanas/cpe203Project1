import java.util.List;
import java.util.Optional;

import processing.core.PImage;

final class NameTmp implements Entity
{
   private final EntityKind kind;
   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;
   private final int resourceLimit;
   private int resourceCount;
   private final int actionPeriod;
   private final int animationPeriod;

   public NameTmp(EntityKind kind, String id, Point position,
                  List<PImage> images, int resourceLimit, int resourceCount,
                  int actionPeriod, int animationPeriod)
   {
      this.kind = kind;
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = 0;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
      this.actionPeriod = actionPeriod;
      this.animationPeriod = animationPeriod;
   }

   public EntityKind getKind(){return this.kind;}

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

   public Point nextPositionOreBlob(WorldModel world,
                                    Point destPos)
   {
      int horiz = Integer.signum(destPos.x - this.position.x);
      Point newPos = new Point(this.position.x + horiz,
              this.position.y);

      Optional<NameTmp> occupant = world.getOccupant(newPos);

      if (horiz == 0 ||
              (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
      {
         int vert = Integer.signum(destPos.y - this.position.y);
         newPos = new Point(this.position.x, this.position.y + vert);
         occupant = world.getOccupant(newPos);

         if (vert == 0 ||
                 (occupant.isPresent() && !(occupant.get().kind == EntityKind.ORE)))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   public Point nextPositionMiner(WorldModel world,
                                  Point destPos)
   {
      int horiz = Integer.signum(destPos.x - this.position.x);
      Point newPos = new Point(this.position.x + horiz,
              this.position.y);

      if (horiz == 0 || world.isOccupied(newPos))
      {
         int vert = Integer.signum(destPos.y - this.position.y);
         newPos = new Point(this.position.x,
                 this.position.y + vert);

         if (vert == 0 || world.isOccupied(newPos))
         {
            newPos = this.position;
         }
      }

      return newPos;
   }

   public boolean moveToOreBlob(WorldModel world,
                                NameTmp target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.position, target.position))
      {
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionOreBlob(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<NameTmp> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public boolean moveToFull(WorldModel world,
                             NameTmp target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.position, target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = this.nextPositionMiner(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<NameTmp> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public boolean moveToNotFull(WorldModel world,
                                NameTmp target, EventScheduler scheduler)
   {
      if (Point.adjacent(this.position, target.position))
      {
         this.resourceCount += 1;
         world.removeEntity(target);
         scheduler.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = this.nextPositionMiner(world, target.position);

         if (!this.position.equals(nextPos))
         {
            Optional<NameTmp> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               scheduler.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(this, nextPos);
         }
         return false;
      }
   }

   public void transformFull(WorldModel world,
                             EventScheduler scheduler, ImageStore imageStore)
   {
      NameTmp miner = this.position.createMinerNotFull(this.id, this.resourceLimit,
              this.actionPeriod, this.animationPeriod,
              this.images);

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      world.addEntity(miner);
      scheduler.scheduleActions(world, imageStore, miner);
   }

   public boolean transformNotFull(WorldModel world,
                                   EventScheduler scheduler, ImageStore imageStore)
   {
      if (this.resourceCount >= this.resourceLimit)
      {
         NameTmp miner = this.position.createMinerFull(this.id, this.resourceLimit,
                 this.actionPeriod, this.animationPeriod,
                 this.images);

         world.removeEntity(this);
         scheduler.unscheduleAllEvents(this);

         world.addEntity(miner);
         scheduler.scheduleActions(world, imageStore, miner);

         return true;
      }

      return false;
   }

    public void executeVeinActivity(WorldModel world,
                                   ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = this.position.findOpenAround(world);

      if (openPt.isPresent())
      {
         NameTmp ore = openPt.get().createOre(Functions.ORE_ID_PREFIX + this.id,
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

   public void executeQuakeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
   {
      scheduler.unscheduleAllEvents(this);
      world.removeEntity(this);
   }

   public void executeOreBlobActivity(WorldModel world,
                                      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<NameTmp> blobTarget = world.findNearest(
              this.position, EntityKind.VEIN);
      long nextPeriod = this.actionPeriod;

      if (blobTarget.isPresent())
      {
         Point tgtPos = blobTarget.get().position;

         if (this.moveToOreBlob(world, blobTarget.get(), scheduler))
         {
            NameTmp quake = tgtPos.createQuake(
                    imageStore.getImageList(Functions.QUAKE_KEY));

            world.addEntity(quake);
            nextPeriod += this.actionPeriod;
            scheduler.scheduleActions(world, imageStore, quake);
         }
      }

      scheduler.scheduleEvent(this,
              this.createActivityAction(world, imageStore),
              nextPeriod);
   }

   public void executeOreActivity(WorldModel world,
                                  ImageStore imageStore, EventScheduler scheduler)
   {
      Point pos = this.position;  // store current position before removing

      world.removeEntity(this);
      scheduler.unscheduleAllEvents(this);

      NameTmp blob = pos.createOreBlob(this.id + Functions.BLOB_ID_SUFFIX,
              this.actionPeriod / Functions.BLOB_PERIOD_SCALE,
              Functions.BLOB_ANIMATION_MIN +
                      Functions.rand.nextInt(Functions.BLOB_ANIMATION_MAX - Functions.BLOB_ANIMATION_MIN),
              imageStore.getImageList(Functions.BLOB_KEY));

      world.addEntity(blob);
      scheduler.scheduleActions(world, imageStore, blob);
   }

   public void executeMinerNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<NameTmp> notFullTarget = world.findNearest(this.position,
              EntityKind.ORE);

      if (!notFullTarget.isPresent() ||
              !this.moveToNotFull(world, notFullTarget.get(), scheduler) ||
              !this.transformNotFull(world, scheduler, imageStore))
      {
         scheduler.scheduleEvent(this,
                 this.createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void executeMinerFullActivity(WorldModel world,
                                        ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<NameTmp> fullTarget = world.findNearest(this.position,
              EntityKind.BLACKSMITH);

      if (fullTarget.isPresent() &&
              this.moveToFull(world, fullTarget.get(), scheduler))
      {
         this.transformFull(world, scheduler, imageStore);
      }
      else
      {
         scheduler.scheduleEvent(this,
                 this.createActivityAction(world, imageStore),
                 this.actionPeriod);
      }
   }

   public void nextImage()
   {
      this.imageIndex = (this.imageIndex + 1) % this.images.size();
   }

   public int getAnimationPeriod()
   {
      switch (this.kind)
      {
         case MINER_FULL:
         case MINER_NOT_FULL:
         case ORE_BLOB:
         case QUAKE:
            return this.animationPeriod;
         default:
            throw new UnsupportedOperationException(
                    String.format("getAnimationPeriod not supported for %s",
                            this.kind));
      }
   }
}