import processing.core.PImage;

import java.util.List;
import java.util.Optional;

final class Point
{
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public static int distanceSquared(Point p1, Point p2)
   {
      int deltaX = p1.x - p2.x;
      int deltaY = p1.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

   public NameTmp createVein(String id, int actionPeriod,
                             List<PImage> images)
   {
      return new NameTmp(EntityKind.VEIN, id, this, images, 0, 0,
         actionPeriod, 0);
   }

   public NameTmp createQuake(List<PImage> images)
   {
      return new NameTmp(EntityKind.QUAKE, Functions.QUAKE_ID, this, images,
         0, 0, Functions.QUAKE_ACTION_PERIOD, Functions.QUAKE_ANIMATION_PERIOD);
   }

   public NameTmp createOreBlob(String id,
                                int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new NameTmp(EntityKind.ORE_BLOB, id, this, images,
            0, 0, actionPeriod, animationPeriod);
   }

   public NameTmp createOre(String id, int actionPeriod,
                            List<PImage> images)
   {
      return new NameTmp(EntityKind.ORE, id, this, images, 0, 0,
         actionPeriod, 0);
   }

   public NameTmp createMinerNotFull(String id, int resourceLimit,
                                     int actionPeriod, int animationPeriod,
                                     List<PImage> images)
   {
      return new NameTmp(EntityKind.MINER_NOT_FULL, id, this, images,
         resourceLimit, 0, actionPeriod, animationPeriod);
   }

   public NameTmp createObstacle(String id,
                                 List<PImage> images)
   {
      return new NameTmp(EntityKind.OBSTACLE, id, this, images,
         0, 0, 0, 0);
   }

   public NameTmp createMinerFull(String id, int resourceLimit,
                                  int actionPeriod, int animationPeriod,
                                  List<PImage> images)
   {
      return new NameTmp(EntityKind.MINER_FULL, id, this, images,
         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   public NameTmp createBlacksmith(String id,
                                   List<PImage> images)
    {
       return new NameTmp(EntityKind.BLACKSMITH, id, this, images,
          0, 0, 0, 0);
    }

    public Optional<NameTmp> nearestEntity(List<NameTmp> entities)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         NameTmp nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.getPosition(), this);

         for (NameTmp other : entities)
         {
            int otherDistance = Point.distanceSquared(other.getPosition(), this);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public Optional<Point> findOpenAround(WorldModel world)
   {
      for (int dy = -Functions.ORE_REACH; dy <= Functions.ORE_REACH; dy++)
      {
         for (int dx = -Functions.ORE_REACH; dx <= Functions.ORE_REACH; dx++)
         {
            Point newPt = new Point(this.x + dx, this.y + dy);
            if (world.withinBounds(newPt) &&
               !world.isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
         ((Point)other).x == this.x &&
         ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && Math.abs(p1.y - p2.y) == 1) ||
              (p1.y == p2.y && Math.abs(p1.x - p2.x) == 1);
   }
}