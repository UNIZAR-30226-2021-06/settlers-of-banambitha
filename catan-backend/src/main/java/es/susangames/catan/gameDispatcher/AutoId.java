package es.susangames.catan.gameDispatcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutoId {

	private int nextInt;
	
	private char d1;
	private char d2;
	
	public AutoId(char d1, char d2) {
		nextInt = 0;
		
		this.d1 = d1;
		this.d2 = d2;
	}
	
	public String nextId() {
		
		Date date = Calendar.getInstance().getTime();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
		String strDate = dateFormat.format(date);
		
		return strDate + d1 + nextInt++ + d2;
	}
	
}
