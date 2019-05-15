import java.util.List;
import java.util.Optional;

import processing.core.PImage;
final class Obstacle extends Entity
{
    public Obstacle(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }
}