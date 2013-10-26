public class Request {
    private String name;
    private String location;
    private String destination;
    private int priority;
    
    public Request(String name, String location, String destination, int priority) {
        this.name = name;
        this.location = location;
        this.destination = destination;
        this.priority = priority;
    }
    public String getName() {
        return name;
    }
    public String getLocation() {
        return location;
    }
    public String getDestination() {
        return destination;
    }
    
    public int getPriority() {
        return priority;
    }
    
}