import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

final class EventScheduler
{
   public PriorityQueue<Event> eventQueue;
   public Map<Entity, List<Event>> pendingEvents;
   public double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

    public void executeAction(Action action)
    {
       switch (action.kind)
       {
       case ACTIVITY:
          action.executeActivityAction(this);
          break;

       case ANIMATION:
          action.executeAnimationAction(this);
          break;
       }
    }
}
