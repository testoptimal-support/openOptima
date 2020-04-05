package openOptima;

public interface Flag {

	public static final String Importance = "I";
	public static final String Critical = "C";
	public static final String Informational = "N";
	public static final String Overflow = "O";
	public static final String Underflow = "U";
	
	public void setFlag (String flagName_p, String flagVal_p);
	public void clearAllFlags ();
	public boolean hasFlag (String flagName_p);
	public void removeFlag (String flagName_p);
	public String[] getAllFlags ();
	public String getFlag (String flagName_p);
}
