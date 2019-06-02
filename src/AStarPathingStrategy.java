import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        // path
        List<Point> path = new LinkedList<>();

        // open and closed lists
        HashSet<Point> openList = new HashSet<>();
        HashSet<Point> closedList = new HashSet<>();

        // distances
        HashMap<Point, Integer> gDistance = new HashMap<>();
        HashMap<Point, Integer> hDistance = new HashMap<>();
        HashMap<Point, Integer> fDistance = new HashMap<>();

        // prior point
        HashMap<Point, Point> parent = new HashMap<>();



        return path;
    }
}




