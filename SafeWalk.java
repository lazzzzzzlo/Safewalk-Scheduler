import edu.purdue.cs.cs180.channel.ClientConnection;
import edu.purdue.cs.cs180.channel.MessageListener;
import java.util.ArrayList;

public class SafeWalk implements MessageListener {
    private static String server;
    private static int port;
    private static int scheduler;
    private static final String LOGIN_ID = "lazopard";
    private static  ArrayList<String> locations = new ArrayList<String>();
    private static  ArrayList<Request> requestList = new ArrayList<Request>();
    private static int [][] distances = new int[15][15];
    private static ClientConnection clientConnection;
    private static Cipher cipher;
    private static String currentLocation;
    
    public SafeWalk( String server, int port, int scheduler) {
        this.server = server;
        this.port = port;
        this.scheduler = scheduler;
        this.clientConnection = clientConnection;
        this.cipher = cipher;
        clientConnection = new ClientConnection(server, port);
        clientConnection.setMessageListener(this);
        cipher = new Cipher("sEexps");
        this.scheduler = scheduler;
        this.currentLocation = currentLocation;
        this.requestList = requestList;
        
    }
    
    public static void main( String args []) throws Exception {
        
        SafeWalk safeWalk;
        try {
            safeWalk = new SafeWalk( args[0], 
                                    Integer.parseInt(args[1]),
                                    Integer.parseInt(args[2]));
        } catch(ArrayIndexOutOfBoundsException e) {
            safeWalk = new SafeWalk( "pc.cs.purdue.edu", 12190, 1);
        }
        //Populate distances and locations arrays
        while(true) {
            try {
                clientConnection.sendMessage(LOGIN_ID + ":"
                                                 + cipher.
                                                 encrypt("LIST_DISTANCES"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Populate requests array
            try {
                clientConnection.sendMessage(LOGIN_ID + ":"
                                                 + cipher.
                                                 encrypt("LIST_REQUESTS"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                clientConnection.
                    sendMessage(LOGIN_ID + ":"
                                    + cipher.
                                    encrypt("GET_CURRENT_LOCATION"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            //initialize scheduler object
            Scheduler requestScheduler;
            
            if (scheduler == 1) {
                requestScheduler = new RequestScheduler1(distances, locations);
            }
            else {
                requestScheduler = new RequestScheduler2(distances, locations);
            }
            
            //Get next request
            
            
            try {
                
                Request request = requestScheduler.getNext(safeWalk.
                                                               getCurrentLocation(), 
                                                           safeWalk.
                                                               getRequestList());
                //Escort request
                clientConnection.sendMessage(LOGIN_ID + ":"
                                                 + cipher.
                                                 encrypt("ESCORT" + 
                                                         "(" + request.
                                                             getName() + 
                                                         ")"));
                //handle if the request is already taken
//                    safeWalk.getRequestList().remove(safeWalk.getRequestList().
//                                                         indexOf(request));
//                    request = requestScheduler.
//                        getNext(safeWalk.
//                                    getCurrentLocation(), 
//                                safeWalk.getRequestList());       
            } catch(NullPointerException e) {
                
                //TODO:handle if request there are no requests at the given location
                
            }
            try {
            }
            catch(Exception e) {
            }
            clientConnection.sendMessage(LOGIN_ID + ":"
                                             + cipher.
                                             encrypt("GET_SCORE"));
        }
    }
    
    public void handleRequest(String message) {
        requestList.clear();
        String name;
        int priority;
        String [] requests = message.split(",");
        for(String request : requests) {
            try {
                name = message.substring(0,message.indexOf("|") + 1);
                priority = Integer.parseInt(message.
                                                substring(message.
                                                              indexOf("|"),
                                                          message.
                                                              indexOf("(")));
            }
            catch(NumberFormatException e) {
                name = message.substring(0,message.indexOf("("));
                priority = 0;
            }
            String location = message.substring(message.indexOf("(") + 1,
                                            message.indexOf("-"));
            String destination = message.substring(message.indexOf(">") + 1,
                                            message.indexOf(")"));
            Request newRequest = new Request(name, 
                                             location, 
                                             destination, priority);
            requestList.add(newRequest);
        }
    }
    
    public void handleDistances(String distancesMessage) {
        String[] segments = distancesMessage.split(",");
        ArrayList<String> locationsList = new ArrayList<String>();
        for (String segment : segments) {
            String from = segment.substring(0, segment.indexOf('<'));
            String to = segment.substring(segment.indexOf('>') + 1,
                                          segment.indexOf('='));
            if (!locations.contains(from)) {
                locations.add(from);
            }
            if (!locations.contains(to)) {
                locations.add(to);
            }
        }
        
        for (String segment : segments) {
            String from = segment.substring(0, segment.indexOf('<'));
            String to = segment.substring(segment.indexOf('>') + 1,
                                          segment.indexOf('='));
            int distance = Integer.parseInt(segment.
                                                substring(segment
                                                              .indexOf('=') + 1));
            int fromIndex = locations.indexOf(from);
            int toIndex = locations.indexOf(to);
            distances[fromIndex][toIndex] = distance;
            distances[toIndex][fromIndex] = distance;
        }
    }
    
    
    
    public void handleCurrentLocation(String message) {
        currentLocation = message;
    }
    
    public void messageReceived(String message) {
        
        System.out.println(message);
        
        if (message.substring(0,message.indexOf(":")).
                equals("PENDING_REQUEST")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
            handleRequest(messageToHandle);
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("DISTANCES")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
            handleDistances(messageToHandle);
            
        }   
        else if (message.substring(0,message.indexOf(":")).
                     equals("CURRENT_LOCATION")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
            handleCurrentLocation(messageToHandle);
            
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("MOVE:ACK")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("MOVE:REJECT")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("MOVE:DONE")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("ESCORT:ACK")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("ESCORT:REJECT")) {
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("ESCORT:DONE")) {   
            String messageToHandle = message.substring(message.indexOf(":") + 1,
                                                       message.length());
        }
        else if (message.substring(0,message.indexOf(":")).
                     equals("SCORE")) { 
            System.out.println(message);
        }
   }
    
    public ArrayList<String> getLocations() {
        return locations;
    }
    public static ArrayList<Request> getRequestList() {
        return requestList;
    }
    public int [][] getDistances() {
        return distances;
    }
    public String getCurrentLocation() {
        return currentLocation;
    }
    //TODO: find Closest Location
    public String findClosestLocation() {
        return null;
    }
    
}