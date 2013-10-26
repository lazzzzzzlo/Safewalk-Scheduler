import java.util.ArrayList;
import javax.swing.SwingUtilities;

public abstract class Scheduler {
    int[][] distances;
    ArrayList<String> locationList;
    //constructor
    public Scheduler(int[][] distances, ArrayList<String> locationList) {
        this.distances = distances;
        this.locationList = new ArrayList<String>();
    }
    
    //based on the algorithm, the volunteers current location,
    //and the list of Requests, returns a request to handle.
    public abstract Request getNext(String currentLocation,
                                    ArrayList<Request> requests);
}