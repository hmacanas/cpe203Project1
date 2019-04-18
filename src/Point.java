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

   public Optional<Point> findOpenAround(WorldModel world)
   {
      for (int dy = -Functions.ORE_REACH; dy <= Functions.ORE_REACH; dy++)
      {
         for (int dx = -Functions.ORE_REACH; dx <= Functions.ORE_REACH; dx++)
         {
            Point newPt = new Point(this.x + dx, this.y + dy);
            if (Functions.withinBounds(world, newPt) &&
               !Functions.isOccupied(world, newPt))
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