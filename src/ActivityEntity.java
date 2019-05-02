interface ActivityEntity extends Entity {
    Activity createActivityAction(WorldModel world, ImageStore imageStore);
    int getActionPeriod();
    void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    void nextImage();
}
