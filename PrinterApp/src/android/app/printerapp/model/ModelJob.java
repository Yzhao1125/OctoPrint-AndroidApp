package android.app.printerapp.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class defines a new Printing Job as a Status listener. Basically it's a reference to the current
 * status of the printer. If there is currently an ongoing job, it'll show the printing status, if there's nothing, 
 * it won't show anything.
 * @author alberto-baeza
 *
 */
public class ModelJob {
	
	//Printer status
	private String mFile;
	private String mFilament;
	private String mSize;
	private String mEstimated;
	private String mTimelapse;
	private String mHeight;
	private String mPrintTime;
	private String mPrintTimeLeft;
	private String mPrinted;
	
	public ModelJob(){
		
		//TODO Hardcoded test values
		mFile = "Piggy-Bank.gcode";
		mSize = "1000KB";
		mPrinted = "390KB";
		mPrintTimeLeft = "290h";
		
	}
	
	/*************
	 * GETS
	 *************/
	
	public String getFilename(){
		return mFile;
	}
	
	public String getFilament(){
		return mFilament;
	}
	
	public String getSize(){
		return mSize;
	}
	
	public String getEstimated(){
		return mEstimated;
	}
	
	public String getPrintTime(){
		return mPrintTime;
	}
	
	public String getPrintTimeLeft(){
		return mPrintTimeLeft;
	}
	
	public String getPrinted(){
		return mPrinted;
	}
	
	
	/***************
	 * 	SETS
	 *****************/
		
	public void updateJob(JSONObject status){
		JSONObject job, progress;
		try {
			
			//Current job status filesize/filament/estimated print time
			job = status.getJSONObject("job");
			
			mFile = job.getString("filename");
			mFilament = job.getString("filament");
			mSize = job.getString("filesize");
			mEstimated = job.getString("estimatedPrintTime");
			
			Log.i("MODEL", "Filename: " + mFile + " Filament: " + mFilament + " Estimated: " + mEstimated);
			
			
			//Progress time/timelapse
			progress = status.getJSONObject("progress");
			
			mPrinted = progress.getString("filepos");
			mPrintTime = progress.getString("printTime");
			mPrintTimeLeft = progress.getString("printTimeLeft");
			
			Log.i("MODEL", "Timelapse: " + mTimelapse + " Height: " + mHeight + " Print time: " + mPrintTime + 
					" Print time left: " + mPrintTimeLeft);
			

		
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
