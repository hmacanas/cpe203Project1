import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity
{
    public AnimationEntity(String id, Point position, List<PImage> images) {
        super(id, position, images);
    }

    public abstract Animation createAnimationAction(int repeatCount);
    public abstract int getAnimationPeriod();

    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }
}
