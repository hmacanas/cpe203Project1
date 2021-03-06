final class Action
{
   private final ActionKind kind;
   private final Entity entity;
   private final WorldModel world;
   private final ImageStore imageStore;
   private final int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public ActionKind getKind(){return this.kind;}

   public void executeActivityAction(EventScheduler scheduler)
   {
      switch (this.entity.getKind())
      {
      case MINER_FULL:
         this.entity.executeMinerFullActivity(this.world,
            this.imageStore, scheduler);
         break;

      case MINER_NOT_FULL:
         this.entity.executeMinerNotFullActivity(this.world,
            this.imageStore, scheduler);
         break;

      case ORE:
         this.entity.executeOreActivity(this.world, this.imageStore,
            scheduler);
         break;

      case ORE_BLOB:
         this.entity.executeOreBlobActivity(this.world,
            this.imageStore, scheduler);
         break;

      case QUAKE:
         this.entity.executeQuakeActivity(this.world, this.imageStore,
            scheduler);
         break;

      case VEIN:
         this.entity.executeVeinActivity(this.world, this.imageStore,
            scheduler);
         break;

      default:
         throw new UnsupportedOperationException(
            String.format("executeActivityAction not supported for %s",
            this.entity.getKind()));
      }
   }

   public void executeAnimationAction(EventScheduler scheduler)
   {
      this.entity.nextImage();

      if (this.repeatCount != 1)
      {
         scheduler.scheduleEvent(this.entity,
            this.entity.createAnimationAction(
                    Math.max(this.repeatCount - 1, 0)),
            this.entity.getAnimationPeriod());
      }
   }
}
