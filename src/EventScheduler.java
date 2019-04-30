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
      switch (action.entity.getKind())
      {
      case MINER_FULL:
         action.entity.executeMinerFullActivity(action.world,
            action.imageStore, this);
         break;

      case MINER_NOT_FULL:
         action.entity.executeMinerNotFullActivity(action.world,
            action.imageStore, this);
         break;

      case ORE:
         action.entity.executeOreActivity(action.world, action.imageStore,
                 this);
         break;

      case ORE_BLOB:
         action.entity.executeOreBlobActivity(action.world,
            action.imageStore, this);
         break;

      case QUAKE:
         action.entity.executeQuakeActivity(action.world, action.imageStore,
                 this);
         break;

      case VEIN:
         action.entity.executeVeinActivity(action.world, action.imageStore,
                 this);
         break;

      default:
         throw new UnsupportedOperationException(
            String.format("executeActivityAction not supported for %s",
            action.entity.getKind()));
      }
   }

   public void executeAnimationAction(Animation animation)
   {
      animation.entity.nextImage();

      if (animation.repeatCount != 1)
      {
         scheduleEvent(animation.entity, animation.entity.createAnimationAction(Math.max(animation.repeatCount - 1, 0)), animation.entity.getAnimationPeriod());
      }
   }
}
