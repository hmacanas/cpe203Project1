import processing.core.PImage;

import java.util.*;

final class WorldModel
{
   private final int numRows;
   private final int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;

   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows(){return this.numRows;}
   public int getNumCols(){return this.numCols;}
   public Set<Entity> getEntities(){return this.entities;}

    public void load(Scanner in, ImageStore imageStore)
    {
       int lineNumber = 0;
       while (in.hasNextLine())
       {
          try
          {
             if (!processLine(in.nextLine(), imageStore))
             {
                System.err.println(String.format("invalid entry on line %d",
                   lineNumber));
             }
          }
          catch (NumberFormatException e)
          {
             System.err.println(String.format("invalid entry on line %d",
                lineNumber));
          }
          catch (IllegalArgumentException e)
          {
             System.err.println(String.format("issue on line %d: %s",
                lineNumber, e.getMessage()));
          }
          lineNumber++;
       }
    }

    public boolean processLine(String line,
                               ImageStore imageStore)
    {
       String[] properties = line.split("\\s");
       if (properties.length > 0)
       {
          switch (properties[Functions.PROPERTY_KEY])
          {
          case Functions.BGND_KEY:
             return parseBackground(properties, imageStore);
          case Functions.MINER_KEY:
             return parseMiner(properties, imageStore);
          case Functions.OBSTACLE_KEY:
             return parseObstacle(properties, imageStore);
          case Functions.ORE_KEY:
             return parseOre(properties, imageStore);
          case Functions.SMITH_KEY:
             return parseSmith(properties, imageStore);
          case Functions.VEIN_KEY:
             return parseVein(properties, imageStore);
          }
       }

       return false;
    }

    public boolean parseMiner(String[] properties,
                              ImageStore imageStore)
    {
       if (properties.length == Functions.MINER_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Functions.MINER_COL]),
             Integer.parseInt(properties[Functions.MINER_ROW]));
          Miner_Not_Full entity = pt.createMinerNotFull(properties[Functions.MINER_ID],
             Integer.parseInt(properties[Functions.MINER_LIMIT]),
                  Integer.parseInt(properties[Functions.MINER_ACTION_PERIOD]),
             Integer.parseInt(properties[Functions.MINER_ANIMATION_PERIOD]),
             imageStore.getImageList(Functions.MINER_KEY));
          tryAddEntity(entity);
       }

       return properties.length == Functions.MINER_NUM_PROPERTIES;
    }

    public boolean parseVein(String[] properties,
                             ImageStore imageStore)
    {
       if (properties.length == Functions.VEIN_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Functions.VEIN_COL]),
             Integer.parseInt(properties[Functions.VEIN_ROW]));
          Vein vein = pt.createVein(properties[Functions.VEIN_ID],
                  Integer.parseInt(properties[Functions.VEIN_ACTION_PERIOD]),
             imageStore.getImageList(Functions.VEIN_KEY));
          tryAddEntity(vein);
       }

       return properties.length == Functions.VEIN_NUM_PROPERTIES;
    }

    public boolean parseSmith(String[] properties,
                              ImageStore imageStore)
    {
       if (properties.length == Functions.SMITH_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Functions.SMITH_COL]),
             Integer.parseInt(properties[Functions.SMITH_ROW]));
          Blacksmith blacksmith = pt.createBlacksmith(properties[Functions.SMITH_ID],
                  imageStore.getImageList(Functions.SMITH_KEY));
          tryAddEntity(blacksmith);
       }

       return properties.length == Functions.SMITH_NUM_PROPERTIES;
    }

    public boolean parseOre(String[] properties,
                            ImageStore imageStore)
    {
       if (properties.length == Functions.ORE_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Functions.ORE_COL]),
             Integer.parseInt(properties[Functions.ORE_ROW]));
          Entity ore = pt.createOre(properties[Functions.ORE_ID],
                  Integer.parseInt(properties[Functions.ORE_ACTION_PERIOD]),
             imageStore.getImageList(Functions.ORE_KEY));
          tryAddEntity(ore);
       }

       return properties.length == Functions.ORE_NUM_PROPERTIES;
    }

    public boolean parseObstacle(String[] properties,
                                 ImageStore imageStore)
    {
       if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES)
       {
          Point pt = new Point(
             Integer.parseInt(properties[Functions.OBSTACLE_COL]),
             Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
          Obstacle entity = pt.createObstacle(properties[Functions.OBSTACLE_ID],
                  imageStore.getImageList(Functions.OBSTACLE_KEY));
          tryAddEntity(entity);
       }

       return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseBackground(String[] properties,
                                   ImageStore imageStore)
    {
       if (properties.length == Functions.BGND_NUM_PROPERTIES)
       {
          Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
             Integer.parseInt(properties[Functions.BGND_ROW]));
          String id = properties[Functions.BGND_ID];
          setBackground(pt,
             new Background(id, imageStore.getImageList(id)));
       }

       return properties.length == Functions.BGND_NUM_PROPERTIES;
    }

    public void setBackgroundCell(Point pos,
                                 Background background)
   {
      this.background[pos.y][pos.x] = background;
   }

   public Background getBackgroundCell(Point pos)
   {
      return this.background[pos.y][pos.x];
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void setOccupancyCell(Point pos,
                                Entity entity)
   {
      this.occupancy[pos.y][pos.x] = entity;
   }

   public Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.y][pos.x];
   }

   public void setBackground(Point pos,
                             Background background)
   {
      if (withinBounds(pos))
      {
         this.setBackgroundCell(pos, background);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(Functions.getCurrentImage(this.getBackgroundCell(pos)));
      }
      else
      {
         return Optional.empty();
      }
   }

   public void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
         && this.getOccupancyCell(pos) != null)
      {
         Entity entity = this.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         this.entities.remove(entity);
         this.setOccupancyCell(pos, null);
      }
   }

   public void removeEntity(Entity entity)
   {
      this.removeEntityAt(entity.getPosition());
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         this.setOccupancyCell(oldPos, null);
         this.removeEntityAt(pos);
         this.setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   /*
         Assumes that there is no entity currently occupying the
         intended destination cell.
      */
   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         this.setOccupancyCell(entity.getPosition(), entity);
         this.entities.add(entity);
      }
   }

   public Optional<Entity> findNearest(Point pos,
                                        Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : this.entities)
      {
         if (entity.getClass() == kind)
         {
            ofType.add(entity);
         }
      }

      return pos.nearestEntity(ofType);
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
         this.getOccupancyCell(pos) != null;
   }

   public boolean withinBounds(Point pos)
   {
      return pos.y >= 0 && pos.y < this.numRows &&
         pos.x >= 0 && pos.x < this.numCols;
   }

   public void tryAddEntity(Entity entity)
    {
       if (this.isOccupied(entity.getPosition()))
       {
          // arguably the wrong type of exception, but we are not
          // defining our own exceptions yet
          throw new IllegalArgumentException("position occupied");
       }

       this.addEntity(entity);
    }
}
