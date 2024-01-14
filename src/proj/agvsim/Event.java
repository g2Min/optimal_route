package proj.agvsim;

public class Event {
    private int priority = 0;        // 이벤트 우선 순위
    private Location source;   // 출발 지점
    private Location destination;  // 도착 지점
    private String partType;	// 부품 이름

    public Event(int priority, Location source, Location destination, String partName) {
        this.priority = priority;
        this.source = source;
        this.destination = destination;
        this.partType = partName;
    }
    
    public Event(Location source, Location destination, String partName) {
        this.source = source;
        this.destination = destination;
        this.partType = partName;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
    	this.priority = priority;
    }
    
    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
    	this.source = source;
    }
    
    public Location getDestination() {
        return destination;
    }
    
    public void setDestination(Location destination) {
    	this.destination = destination;
    }
    
    public String getPartType() {
    	return partType;
    }
    
    public void setPartName(String partName) {
    	this.partType = partName;
    }
}