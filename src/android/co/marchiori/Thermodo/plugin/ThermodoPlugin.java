package co.marchiori.Thermodo.plugin;

import java.util.Locale;

import android.util.Log;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import com.robocatapps.thermodosdk.Thermodo;
import com.robocatapps.thermodosdk.ThermodoFactory;
import com.robocatapps.thermodosdk.ThermodoListener;

public class ThermodoPlugin extends CordovaPlugin implements ThermodoListener {

    private static final String TAG = "ThermodoPlugin";
    private static final String START_MEASURE = "start_measure";
    private static final String STOP_MEASURE = "stop_measure";
    
    private Thermodo mThermodo;
    private CallbackContext callbackCtx;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, action);
        
        
        callbackCtx = callbackContext;
        
        PluginResult progressResult = new PluginResult(PluginResult.Status.OK, "STARTED");
        progressResult.setKeepCallback(true);
        
        mThermodo = ThermodoFactory.getMockThermodoInstance(cordova.getActivity().getBaseContext());
		mThermodo.setThermodoListener(this);
		
        if (action.equals(START_MEASURE)) {
        	mThermodo.start();
        }
        
        if (action.equals(STOP_MEASURE)) {
        	this.mThermodo.stop();
		    this.mThermodo.setThermodoListener(null);
        }
        
        //callbackContext.success();
        return true;
    }
    
    @Override
	public void onStartedMeasuring() {
    	//showToast("Started measuring", 1);
    	Log.d(TAG, "Started measuring");
    	PluginResult progressResult = new PluginResult(PluginResult.Status.OK, "STARTED");
        progressResult.setKeepCallback(true);
        callbackCtx.sendPluginResult(progressResult);
	}

	@Override
	public void onStoppedMeasuring() {
		//showToast("Stopped measuring", 1);
		Log.d(TAG, "Stopped measuring");
		PluginResult progressResult = new PluginResult(PluginResult.Status.OK, "STOPPED");
        progressResult.setKeepCallback(true);
        callbackCtx.sendPluginResult(progressResult);
	}

	@Override
	public void onTemperatureMeasured(float temperature) {
		
		String temp = String.format(Locale.ENGLISH, "%.2f", temperature);
		
		Log.d(TAG, "Temperature Measured is: " + temp);
		
		//for single callback
		//callbackCtx.success(temp);
		
		//for multiple callback
		PluginResult progressResult = new PluginResult(PluginResult.Status.OK, temp);
        progressResult.setKeepCallback(true);
        callbackCtx.sendPluginResult(progressResult);
		
	}

	@Override
	public void onErrorOccurred(int what) {
		String error = "ERROR Generic";
		
		switch (what) {
			case Thermodo.ERROR_AUDIO_FOCUS_GAIN_FAILED:
			    error = "ERROR An error has occurred: Audio Focus Gain Failed";
				Log.d(TAG,error);
				break;
			case Thermodo.ERROR_AUDIO_RECORD_FAILURE:
				error = "ERROR An error has occurred: Audio Record Failure";
				Log.d(TAG,error);
				break;
			case Thermodo.ERROR_SET_MAX_VOLUME_FAILED:
				error = "ERROR An error has occurred: The volume could not be set to maximum";
				Log.d(TAG,error);
				break;
			default:
				error = "ERROR An unidentified error has occurred: " + what;
				Log.d(TAG,error);
		}
		
		PluginResult progressResult = new PluginResult(PluginResult.Status.OK, error);
        progressResult.setKeepCallback(true);
        callbackCtx.sendPluginResult(progressResult);
	}
}