import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity
{
    public AnimationEntity(String id, Point position, List<PImage> images, int imageIndex) {
        super(id, position, images, imageIndex);
    }

    public abstract Animation createAnimationAction(int repeatCount);
    public abstract int getAnimationPeriod();
}
