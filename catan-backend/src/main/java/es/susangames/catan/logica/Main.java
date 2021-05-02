package logica;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JSONObject jsObject = new JSONObject();
		Tablero t = new Tablero(4);
		try {
			jsObject = t.returnMessage();
			System.out.println(jsObject.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
