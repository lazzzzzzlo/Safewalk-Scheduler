import java.util.ArrayList;

public class RequestScheduler1 extends Scheduler {
    
    public RequestScheduler1(int[][] distances, ArrayList<String> locationList) {
        super(distances, locationList);
    }
    
    public Request getNext(String currentLocation,
                           ArrayList<Request> requests) {
        ArrayList<Integer> requestsIndeces = new ArrayList<Integer>();
        
        for (int i = 0; i < requests.size(); i++) {
            if (currentLocation.equals(requests.get(i).getLocation())) {
                requestsIndeces.add(i);
            }
        }
        
        /*returns null if there are no requests
        at current location */
        if (requestsIndeces.size() == 0) {
            return null;
        }
        
        int max = 0;
        int requestIndex = 0;
        
        for (int i = 0; i < requestsIndeces.size(); i++) {
            if ( max < requests.get(requestsIndeces.get(i)).getPriority()) {
                max = requests.get(requestsIndeces.get(i)).getPriority();
                requestIndex = requestsIndeces.get(i);
            }
        }
        return requests.get(requestIndex);
    }
}