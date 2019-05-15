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

   public Vein createVein(String id, int actionPeriod,
                             List<PImage> images)
   {
      return new Vein(id, this, images, actionPeriod);
   }

   public Quake createQuake(List<PImage> images)
   {
      return new Quake(Functions.QUAKE_ID, this, images,
              Functions.QUAKE_ACTION_PERIOD, Functions.QUAKE_ANIMATION_PERIOD);
   }

   public Ore_Blob createOreBlob(String id,
                                int actionPeriod, int animationPeriod, List<PImage> images)
   {
      return new Ore_Blob(id, this, images,
            actionPeriod, animationPeriod);
   }

   public Ore createOre(String id, int actionPeriod,
                            List<PImage> images)
   {
      return new Ore(id, this, images, actionPeriod);
   }

   public Miner_Not_Full createMinerNotFull(String id, int resourceLimit,
                                     int actionPeriod, int animationPeriod,
                                     List<PImage> images)
   {
      return new Miner_Not_Full(id, this, images,
         resourceLimit,  actionPeriod, animationPeriod);
   }

   public Obstacle createObstacle(String id,
                                 List<PImage> images)
   {
      return new Obstacle(id, this, images);
   }

   public Miner_Full createMinerFull(String id, int resourceLimit,
                                  int actionPeriod, int animationPeriod,
                                  List<PImage> images)
   {
      return new Miner_Full(id, this, images,
         resourceLimit, resourceLimit, actionPeriod, animationPeriod);
   }

   public Blacksmith createBlacksmith(String id,
                                   List<PImage> images)
    {
       return new Blacksmith(id, this, images);
    }

    public Optional<Entity> nearestEntity(List<Entity> entities)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = Point.distanceSquared(nearest.getPosition(), this);

         for (Entity other : entities)
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