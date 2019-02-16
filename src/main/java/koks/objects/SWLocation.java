package koks.objects;

public class SWLocation {
	
    private int x;
    private int z;
    private int y;
    private int pitch;
    private byte data;
    
    public SWLocation(int x, int y, int z) {
        this.x = x;
        this.z = z;
        this.y = y;
    }
    public SWLocation(int x, int y, int z, byte data) {
        this.x = x;
        this.z = z;
        this.y = y;
        this.data = data;
    }
    public int getX() { return x; }
    public int getZ() { return z; }
    public int getY() { return y; }
    public int getPitch() { return pitch; }
    public void setPitch(int pitch) { this.pitch = pitch; }
    public byte getData() { return data; }
    public void setData(byte data) { this.data = data; }
    public boolean equals(SWLocation location) {
    	if(getX() == location.getX() && getY() == location.getY() && getZ() == location.getZ()) {
    		return true;
    	}
    	return false;
    }
}
