import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity
{
    private int animationPeriod;
    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }

    public Animation createAnimationAction(int repeatCount)
    {
        return new Animation(this, null, null, repeatCount);
    }

    public int getAnimationPeriod() {
        return this.animationPeriod;
    }
}
