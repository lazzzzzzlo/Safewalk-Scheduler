import java.util.ArrayList;

public class RequestScheduler2 extends Scheduler {
    
    public RequestScheduler2(int[][] distances, ArrayList<String> locationList) {
        super(distances, locationList);
        // TODO: initialize fields
    }

    public Request getNext(String currentLocation,
                           ArrayList<Request> requests) {   
        return null;
    }
}