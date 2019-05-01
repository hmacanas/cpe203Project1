import processing.core.PImage;

import java.util.List;

interface Entity
{
    Point getPosition();
    void setPosition(Point newPt);
    List<PImage> getImages();
    int getImageIndex();
}
