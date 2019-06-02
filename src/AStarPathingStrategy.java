import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {

        PriorityQueue<Node> myQ = new PriorityQueue<>(Comparator.comparing(Node::getFValue));
        List<Point> path = new LinkedList<>();
        HashMap<Point, Node> pointToNode = new HashMap<>();
        HashMap<Point, Node> closedSet = new HashMap<>();
        HashMap<Point, Node> openSet = new HashMap<>();



        Node startNode = new Node(start);
        Node endNode = new Node(end);

        startNode.setGValue(0);
        startNode.setHValue(startNode.calculateGH(endNode));
        startNode.setFValue(startNode.getGValue() + startNode.getHValue());

        endNode.setGValue(endNode.calculateGH(startNode));
        endNode.setHValue(0);
        endNode.setFValue(endNode.getGValue() + endNode.getHValue());


        myQ.add(startNode);

        Node currentNode = null;
        int count = 0;
        while (!myQ.isEmpty())
        {
            count += 1;
            currentNode = myQ.poll();
            closedSet.put(currentNode.getPoint(), currentNode);
            openSet.remove(currentNode.getPoint());

            if (withinReach.test(currentNode.getPoint(), endNode.getPoint()) || count > 10000)
                break;


            List<Point> neighbors =  potentialNeighbors.apply(currentNode.getPoint()).filter(canPassThrough).filter(pt ->
                            !pt.equals(start) && !pt.equals(end) && !closedSet.containsKey(pt)).collect(Collectors.toList());


            for (Point neighbor:neighbors)
            {
                Node temp = pointToNode.get(neighbor);

                if (temp == null) {
                    temp = new Node(neighbor);
                    temp.setCameFrom(currentNode);
                    temp.setGValue(currentNode.getGValue() + 1);
                    temp.setHValue(temp.calculateGH(endNode));
                    temp.setFValue(temp.getGValue() + temp.getHValue());
                    pointToNode.put(neighbor, temp);
                }


                if (!openSet.containsKey(temp.getPoint())) {
                    myQ.add(temp);
                    openSet.put(temp.getPoint(), temp);
                }

                else
                {
                    int old_G= temp.getGValue();
                    int new_G= currentNode.getGValue()+1;

                    if (new_G < old_G)
                    {
                        temp.setGValue(new_G);
                        temp.setFValue(temp.getGValue() + temp.getHValue());
                        temp.setCameFrom(currentNode);
                    }


                }

                openSet.put(temp.getPoint(), temp);
                myQ.add(temp);
            }


        }

        return buildPath(currentNode, startNode);
    }

    private List<Point> buildPath(Node endNode, Node startNode)
    {
        List<Point> path =  new LinkedList<>();


        while ( endNode != startNode)
        {
            path.add(0, endNode.getPoint());
            endNode =  endNode.getCameFrom();
        }

        return path;
    }
}
