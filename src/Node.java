import java.util.Comparator;

public class Node
{
    private int gValue;
    private int fValue;
    private int hValue;
    private Point point;
    private Node cameFrom;

    Node(Point pt)
    {
        this.point = pt;
    }

    public int getGValue(){return this.gValue;}
    public int getHValue(){return this.hValue;}
    public int getFValue(){return this.fValue;}
    public Node getCameFrom(){return this.cameFrom;}
    public Point getPoint(){return this.point;}

    public void setHValue(int val){this.hValue = val;}
    public void setFValue(int val){this.fValue = val;}
    public void setCameFrom(Node parent){this.cameFrom = parent;}

    public void setGValue(int val)
    {
        this.gValue = val;
    }

    public boolean equals(Object other)
    {
        if (other == null)
            return false;

        if (!(other instanceof Node))
            return false;
        return this.getPoint() == ((Node) other).getPoint();
//
//        return this.getGValue() == ((Node) other).getGValue() && this.getHValue() == ((Node) other).getHValue() && this.getPoint() == ((Node) other).getPoint();
    }

    public int calculateGH(Node node)
    {
        int dx = Math.abs(this.point.x - node.getPoint().x);
        int dy = Math.abs(this.point.y - node.getPoint().y);

        return dx + dy;
    }

    public int calculateF(Node start, Node end)
    {
        int dx1 = Math.abs(this.point.x - start.getPoint().x);
        int dy1 = Math.abs(this.point.y - start.getPoint().y);
        int dx2 = Math.abs(this.point.x - end.getPoint().x);
        int dy2 = Math.abs(this.point.y - end.getPoint().y);

        return dx1 + dy1 + dx2 + dy2;
    }

}