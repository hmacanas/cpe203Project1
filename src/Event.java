final class Event
{
   private Action action;
   private long time;
   private NameTmp entity;

   public Event(Action action, long time, NameTmp entity)
   {
      this.action = action;
      this.time = time;
      this.entity = entity;
   }

   public long getTime(){return this.time;}
   public Action getAction(){return this.action;}
   public NameTmp getEntity(){return this.entity;}
}
