import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Entity
{
    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;

    public Entity(String id, Point position, List<PImage> images)
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

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getId() {
        return id;
    }
}
