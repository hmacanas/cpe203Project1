import processing.core.PImage;

import java.util.List;

interface EntityTmp
{
    Point getPosition();
    void setPosition(Point newPt);
    List<PImage> getImages();
    int getImageIndex();
}
