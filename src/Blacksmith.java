import processing.core.PImage;

import java.util.List;

final class Blacksmith extends Entity
{

    public Blacksmith(String id, Point position, List<PImage> images)
    {
        super(id, position, images);
    }

    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public Activity createActivityAction(WorldModel world, ImageStore imageStore) {
        return null;
    }

    public int getActionPeriod() {
        return 0;
    }

    public Animation createAnimationAction(int repeatCount) {
        return null;
    }

    public int getAnimationPeriod() {
        return 0;
    }
}

