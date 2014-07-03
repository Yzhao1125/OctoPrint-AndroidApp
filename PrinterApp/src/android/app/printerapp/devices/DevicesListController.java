package android.app.printerapp.devices;

import java.util.ArrayList;

import android.app.printerapp.devices.database.DatabaseController;
import android.app.printerapp.model.ModelPrinter;
import android.database.Cursor;
import android.util.Log;


/**
 * This class will handle list events such as add, remove or update
 * @author alberto-baeza
 *
 */
public class DevicesListController {
	
	//List for the printers found
	private static ArrayList<ModelPrinter> mList;
	
	public DevicesListController(){
		
		mList = new ArrayList<ModelPrinter>();
		loadList();
		
	}
	
	//Add element to the list
	public static void addToList(ModelPrinter m){
		mList.add(m);
		
	}
	
	//Return the list
	public static ArrayList<ModelPrinter> getList(){
		
		return mList;
	}
	
	//Load device list from the Database
	public void loadList(){
		
		Cursor c = DatabaseController.retrieveDeviceList();
		
		c.moveToFirst();
		
		while (!c.isAfterLast()){
			
			Log.i("OUT","Entry: " + c.getString(1) + ";" + c.getString(2));
			
			ModelPrinter m = new ModelPrinter(c.getString(1),c.getString(2) );
			
			addToList(m);
			m.startUpdate();
			
			c.moveToNext();
		}
	   
	   DatabaseController.closeDb();

	}
		
}
