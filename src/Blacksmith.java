import processing.core.PImage;

import java.util.List;

final class Blacksmith implements Entity
{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Blacksmith(String id, Point position,
                  List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }


    public Point getPosition(){return this.position;}

    public void setPosition(Point newPt) { this.position = newPt;}

    public List<PImage> getImages(){return this.images;}

    public int getImageIndex(){return this.imageIndex;}

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.images.size();
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

