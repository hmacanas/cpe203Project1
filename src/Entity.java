import processing.core.PImage;

import java.util.List;

interface Entity
{
    Point getPosition();
    void setPosition(Point newPt);
    List<PImage> getImages();
    int getImageIndex();
//    Activity createActivityAction(WorldModel world, ImageStore imageStore);
//    int getActionPeriod();
//    Animation createAnimationAction(int repeatCount);
    int getAnimationPeriod();
    void nextImage();
}
