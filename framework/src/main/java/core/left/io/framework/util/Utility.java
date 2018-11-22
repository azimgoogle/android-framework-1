package core.left.io.framework.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.left.io.framework.R;
import timber.log.Timber;

/**
 * ============================================================================
 * Copyright (C) 2018 W3 Engineers Ltd - All Rights Reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * <br>----------------------------------------------------------------------------
 * <br>Created by: Ahmed Mohmmad Ullah (Azim) on [2018-07-18 at 10:41 AM].
 * <br>Email: azim@w3engineers.com
 * <br>----------------------------------------------------------------------------
 * <br>Project: android-framework.
 * <br>Code Responsibility: <Purpose of code>
 * <br>----------------------------------------------------------------------------
 * <br>Edited by :
 * <br>1. <First Editor> on [2018-07-18 at 10:41 AM].
 * <br>2. <Second Editor>
 * <br>----------------------------------------------------------------------------
 * <br>Reviewed by :
 * <br>1. <First Reviewer> on [2018-07-18 at 10:41 AM].
 * <br>2. <Second Reviewer>
 * <br>============================================================================
 **/
public class Utility {


    /**
     * Generate random number with a range
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    /**
     * Retrieve RM service and App layer service
     * @param context
     * @return
     */
    public static List<Integer> getMyProcessIdList(Context context) {

        List<Integer> myProcessIdList = null;

        if(context != null) {
            ActivityManager manager
                    = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

            if (manager != null) {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = manager.getRunningAppProcesses();

                myProcessIdList = new ArrayList<>(2);

                for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {

                    if (processInfo != null) {

                        Timber.d(processInfo.processName);

                        // Actually we need to kill only local scope remote service rather the native
                        // mesh service. Although keeping the scope and commenting code if by anyhow
                        // any particular project is in need of this unusual requirement
                        if (//processInfo.processName.contains(context.getString(R.string.rm_process_name)) ||
                                processInfo.processName.contains(context.getString(R.string.base_rm_process_name))) {
                            myProcessIdList.add(processInfo.pid);
                        }
                    }
                }
            }
        }

        return myProcessIdList;
    }


    /**
     * This kind of checking is not trivial although few devices were automatically recreating service
     * if we call startSeervice although the service is running. e.g: Symphony ZVi
     * @param context
     * @param serviceClass
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {

        if(context == null || serviceClass == null) {
            return false;
        }

        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningServiceInfo> servicesInfo = manager.getRunningServices(Integer.MAX_VALUE);
            for (ActivityManager.RunningServiceInfo service : servicesInfo) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

}
