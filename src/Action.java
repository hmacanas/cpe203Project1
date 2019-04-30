final class Action
{
   public final ActionKind kind;
   public final Entity entity;
   public final WorldModel world;
   public final ImageStore imageStore;
   public final int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }


   public void executeAction(EventScheduler scheduler)
    {
       switch (this.kind)
       {
       case ACTIVITY:
          scheduler.executeActivityAction(this);
          break;

       case ANIMATION:
          scheduler.executeAnimationAction(this);
          break;
       }
    }
}
