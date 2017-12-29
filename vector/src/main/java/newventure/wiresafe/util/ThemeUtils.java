/*
 * Copyright 2017 OpenMarket Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package newventure.wiresafe.util;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;

import java.util.HashMap;
import java.util.Map;

import newventure.wiresafe.R;
import newventure.wiresafe.VectorApp;
import newventure.wiresafe.activity.AccountCreationActivity;
import newventure.wiresafe.activity.CountryPickerActivity;
import newventure.wiresafe.activity.FallbackLoginActivity;
import newventure.wiresafe.activity.HistoricalRoomsActivity;
import newventure.wiresafe.activity.InComingCallActivity;
import newventure.wiresafe.activity.LanguagePickerActivity;
import newventure.wiresafe.activity.LoginActivity;
import newventure.wiresafe.activity.PhoneNumberAdditionActivity;
import newventure.wiresafe.activity.PhoneNumberVerificationActivity;
import newventure.wiresafe.activity.RoomDirectoryPickerActivity;
import newventure.wiresafe.activity.SplashActivity;
import newventure.wiresafe.activity.VectorBaseSearchActivity;
import newventure.wiresafe.activity.VectorCallViewActivity;
import newventure.wiresafe.activity.VectorHomeActivity;
import newventure.wiresafe.activity.VectorMediasPickerActivity;
import newventure.wiresafe.activity.VectorMediasViewerActivity;
import newventure.wiresafe.activity.VectorMemberDetailsActivity;
import newventure.wiresafe.activity.VectorPublicRoomsActivity;
import newventure.wiresafe.activity.VectorRoomActivity;
import newventure.wiresafe.activity.VectorRoomCreationActivity;
import newventure.wiresafe.activity.VectorRoomDetailsActivity;
import newventure.wiresafe.activity.VectorSettingsActivity;
import newventure.wiresafe.activity.VectorUniversalLinkActivity;

/**
 * Util class for managing themes.
 */
public class ThemeUtils {
    // preference key
    public static final String APPLICATION_THEME_KEY = "APPLICATION_THEME_KEY";

    // the theme description
    public static final String THEME_DARK_VALUE = "dark";
    public static final String THEME_LIGHT_VALUE = "light";
    public static final String THEME_BLACK_VALUE = "black";

    public static final Map<Integer, Integer> mColorByAttr = new HashMap<>();

    /**
     * Provides the selected application theme
     * @param context the context
     * @return the selected application theme
     */
    public static String getApplicationTheme(Context context) {
        String appTheme = THEME_LIGHT_VALUE;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        // defines a default value if not defined
        if (!sp.contains(APPLICATION_THEME_KEY)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APPLICATION_THEME_KEY, THEME_LIGHT_VALUE);
            editor.commit();
        } else {
            appTheme = sp.getString(APPLICATION_THEME_KEY, THEME_LIGHT_VALUE);
        }

        return appTheme;
    }

    /**
     * Update the application theme
     * @param aTheme the new theme
     */
    public static void setApplicationTheme(Context context, String aTheme) {
        if (null != aTheme) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APPLICATION_THEME_KEY, aTheme);
            editor.commit();
        }

        if (TextUtils.equals(aTheme, THEME_DARK_VALUE)) {
            VectorApp.getInstance().setTheme(R.style.AppTheme_Dark);
        }
        else if (TextUtils.equals(aTheme, THEME_BLACK_VALUE)) {
            VectorApp.getInstance().setTheme(R.style.AppTheme_Black);
        }
        else {
            VectorApp.getInstance().setTheme(R.style.AppTheme);
        }

        mColorByAttr.clear();
    }

    /**
     * Set the activity theme according to the selected one.
     *
     * @param activity the activity
     */
    public static void setActivityTheme(Activity activity) {
        if (TextUtils.equals(getApplicationTheme(activity), THEME_DARK_VALUE)) {
            if (activity instanceof AccountCreationActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof AccountCreationActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof CountryPickerActivity) {
                activity.setTheme(R.style.CountryPickerTheme_Dark);
            } else if (activity instanceof FallbackLoginActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof HistoricalRoomsActivity) {
                activity.setTheme(R.style.HomeActivityTheme_Dark);
            } else if (activity instanceof InComingCallActivity) {
                activity.setTheme(R.style.CallAppTheme_Dark);
            } else if (activity instanceof LanguagePickerActivity) {
                activity.setTheme(R.style.CountryPickerTheme_Dark);
            } else if (activity instanceof LoginActivity) {
                activity.setTheme(R.style.LoginAppTheme_Dark);
            } else if (activity instanceof PhoneNumberAdditionActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Dark);
            } else if (activity instanceof PhoneNumberVerificationActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Dark);
            } else if (activity instanceof RoomDirectoryPickerActivity) {
                activity.setTheme(R.style.DirectoryPickerTheme_Dark);
            } else if (activity instanceof SplashActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Dark);
            } else if (activity instanceof VectorBaseSearchActivity) {
                activity.setTheme(R.style.SearchesAppTheme_Dark);
            } else if (activity instanceof VectorCallViewActivity) {
                activity.setTheme(R.style.CallActivityTheme_Dark);
            } else if (activity instanceof VectorHomeActivity) {
                activity.setTheme(R.style.HomeActivityTheme_Dark);
            } else if (activity instanceof VectorMediasPickerActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_FullScreen_Dark);
            } else if (activity instanceof VectorMediasViewerActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof VectorMemberDetailsActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Dark);
            } else if (activity instanceof VectorPublicRoomsActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof VectorRoomActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Dark);
            } else if (activity instanceof VectorRoomCreationActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof VectorRoomDetailsActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof VectorSettingsActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            } else if (activity instanceof VectorUniversalLinkActivity) {
                activity.setTheme(R.style.AppTheme_Dark);
            }
        }

        if (TextUtils.equals(getApplicationTheme(activity), THEME_BLACK_VALUE)) {
            if (activity instanceof AccountCreationActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof AccountCreationActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof CountryPickerActivity) {
                activity.setTheme(R.style.CountryPickerTheme_Black);
            } else if (activity instanceof FallbackLoginActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof HistoricalRoomsActivity) {
                activity.setTheme(R.style.HomeActivityTheme_Black);
            } else if (activity instanceof InComingCallActivity) {
                activity.setTheme(R.style.CallAppTheme_Black);
            } else if (activity instanceof LanguagePickerActivity) {
                activity.setTheme(R.style.CountryPickerTheme_Black);
            } else if (activity instanceof LoginActivity) {
                activity.setTheme(R.style.LoginAppTheme_Black);
            } else if (activity instanceof PhoneNumberAdditionActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Black);
            } else if (activity instanceof PhoneNumberVerificationActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Black);
            } else if (activity instanceof RoomDirectoryPickerActivity) {
                activity.setTheme(R.style.DirectoryPickerTheme_Black);
            } else if (activity instanceof SplashActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Black);
            } else if (activity instanceof VectorBaseSearchActivity) {
                activity.setTheme(R.style.SearchesAppTheme_Black);
            } else if (activity instanceof VectorCallViewActivity) {
                activity.setTheme(R.style.CallActivityTheme_Black);
            } else if (activity instanceof VectorHomeActivity) {
                activity.setTheme(R.style.HomeActivityTheme_Black);
            } else if (activity instanceof VectorMediasPickerActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_FullScreen_Black);
            } else if (activity instanceof VectorMediasViewerActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof VectorMemberDetailsActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Black);
            } else if (activity instanceof VectorPublicRoomsActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof VectorRoomActivity) {
                activity.setTheme(R.style.AppTheme_NoActionBar_Black);
            } else if (activity instanceof VectorRoomCreationActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof VectorRoomDetailsActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof VectorSettingsActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            } else if (activity instanceof VectorUniversalLinkActivity) {
                activity.setTheme(R.style.AppTheme_Black);
            }
        }

        mColorByAttr.clear();
    }
    
    /**
     * Translates color attributes to colors
     * @param c Context
     * @param colorAttribute Color Attribute
     * @return Requested Color
     */
    public static @ColorInt int getColor(Context c, @AttrRes final int colorAttribute) {
        if (mColorByAttr.containsKey(colorAttribute)) {
            return mColorByAttr.get(colorAttribute);
        }

        int matchedColor;

        try {
            TypedValue color = new TypedValue();
            c.getTheme().resolveAttribute(colorAttribute, color, true);
            matchedColor = color.data;
        } catch (Exception e) {
            matchedColor = ContextCompat.getColor(c, android.R.color.holo_red_dark);
        }

        mColorByAttr.put(colorAttribute, matchedColor);

        return matchedColor;
    }

    /**
     * Get the resource Id applied to the current theme
     * @param c the context
     * @param resourceId the resource id
     * @return the resource Id for the current theme
     */
    public static int getResourceId(Context c, int resourceId) {
        if (TextUtils.equals(getApplicationTheme(c), THEME_DARK_VALUE)) {

            if (resourceId == R.drawable.line_divider_light) {
                return R.drawable.line_divider_dark;
            }
        }
        return resourceId;
    }
}
