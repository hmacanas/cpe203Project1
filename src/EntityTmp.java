import processing.core.PImage;

import java.util.List;

interface EntityTmp
{
    Point getPosition();
    void etPosition(Point newPt);
    List<PImage> getImages();
    int getImageIndex();
}
