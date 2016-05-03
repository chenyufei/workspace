package Tools;

import com.example.blemasterv3.Log;

public class Tools {
public static String decTohex(String decimal){
	 
	int number = Integer.parseInt(decimal);
	String a = Integer.toHexString(number);
	return a;
	 
}
	 
}
