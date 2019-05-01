import java.util.*;

final class EventScheduler
{
   private PriorityQueue<Event> eventQueue;
   private Map<NameTmp, List<Event>> pendingEvents;
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

   public void unscheduleAllEvents(NameTmp entity)
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

   public void scheduleEvent(NameTmp entity, Action action, long afterPeriod)
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
      switch (action.getEntity().getKind())
      {
      case MINER_FULL:
         action.getEntity().executeMinerFullActivity(action.getWorld(),
            action.getImageStore(), this);
         break;

      case MINER_NOT_FULL:
         action.getEntity().executeMinerNotFullActivity(action.getWorld(),
            action.getImageStore(), this);
         break;

      case ORE:
         action.getEntity().executeOreActivity(action.getWorld(), action.getImageStore(),
                 this);
         break;

      case ORE_BLOB:
         action.getEntity().executeOreBlobActivity(action.getWorld(),
            action.getImageStore(), this);
         break;

      case QUAKE:
         action.getEntity().executeQuakeActivity(action.getWorld(), action.getImageStore(),
                 this);
         break;

      case VEIN:
         action.getEntity().executeVeinActivity(action.getWorld(), action.getImageStore(),
                 this);
         break;

      default:
         throw new UnsupportedOperationException(
            String.format("executeActivityAction not supported for %s",
            action.getEntity().getKind()));
      }
   }

   public void executeAnimationAction(Animation animation)
   {
      animation.getEntity().nextImage();

      if (animation.getRepeatCount() != 1)
      {
         scheduleEvent(animation.getEntity(), animation.getEntity().createAnimationAction(Math.max(animation.getRepeatCount() - 1, 0)), animation.getEntity().getAnimationPeriod());
      }
   }

   public void scheduleActions(WorldModel world, ImageStore imageStore, NameTmp entity)
   {
      switch (entity.getKind())
      {
         case MINER_FULL:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity, entity.createAnimationAction(0),
                    entity.getAnimationPeriod());
            break;

         case MINER_NOT_FULL:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
            break;

         case ORE:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            break;

         case ORE_BLOB:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
            break;

         case QUAKE:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            scheduleEvent(entity,
                    entity.createAnimationAction(Functions.QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case VEIN:
            scheduleEvent(entity,
                    entity.createActivityAction(world, imageStore),
                    entity.getActionPeriod());
            break;

         default:
      }
   }
}

