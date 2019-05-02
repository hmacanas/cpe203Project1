import java.util.*;

final class EventScheduler
{
   private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private final double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void updateOnTime(long time)
   {
      while (!this.eventQueue.isEmpty() &&
         this.eventQueue.peek().getTime() < time)
      {
         Event next = this.eventQueue.poll();

         removePendingEvent(next);

         next.getAction().executeAction(this);
      }
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
         (long)(afterPeriod * this.timeScale);
      Event event = new Event(action, time, entity);

      this.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = this.pendingEvents.getOrDefault(entity,
         new LinkedList<>());
      pending.add(event);
      this.pendingEvents.put(entity, pending);
   }

   public void executeActivityAction(Activity action)
   {
      action.getEntity().executeActivity(action.getWorld(), action.getImageStore(),
              this);
   }

   public void executeAnimationAction(Animation animation)
   {
      animation.getEntity().nextImage();

      if (animation.getRepeatCount() != 1)
      {
         scheduleEvent(animation.getEntity(), animation.getEntity().createAnimationAction(Math.max(animation.getRepeatCount() - 1, 0)), animation.getEntity().getAnimationPeriod());
      }
   }

   public void scheduleActions(WorldModel world, ImageStore imageStore, Entity entity)
   {
         if (entity instanceof Miner_Full) {
               scheduleEvent(entity,
                       entity.createActivityAction(world, imageStore),
                       entity.getActionPeriod());
               scheduleEvent(entity, entity.createAnimationAction(0),
                       entity.getAnimationPeriod());
         }

         else if(entity instanceof Miner_Not_Full) {
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
         }

         else if (entity instanceof Ore) {
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
         }

         else if (entity instanceof Ore_Blob) {
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
         }

         else if (entity instanceof Quake) {
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
         }

         else if (entity instanceof Vein) {
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
         }
   }
}

