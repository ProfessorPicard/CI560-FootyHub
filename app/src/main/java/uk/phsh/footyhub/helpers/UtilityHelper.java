package uk.phsh.footyhub.helpers;

import android.content.Context;
import android.os.Handler;

/**
 * Singleton helper class for general utilities
 * @author Peter Blackburn
 */
public class UtilityHelper {

    private static UtilityHelper _instance;

    private UtilityHelper() { }

    /**
     * @return The current instance of UtilityHelper
     */
    public static UtilityHelper getInstance() {
        if(_instance == null)
            _instance = new UtilityHelper();
        return _instance;
    }



    public void runOnUiThread(Context context, Runnable runnable) {
        Handler handler = new Handler(context.getMainLooper());
        handler.post(runnable);
    }



}
