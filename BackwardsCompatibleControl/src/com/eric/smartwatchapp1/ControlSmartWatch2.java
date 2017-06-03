/*
Copyright (c) 2011, Sony Ericsson Mobile Communications AB
Copyright (c) 2011-2013, Sony Mobile Communications AB

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Ericsson Mobile Communications AB / Sony Mobile
 Communications AB nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.eric.smartwatchapp1;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlObjectClickEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView;
import com.sonyericsson.extras.liveware.extension.util.control.ControlView.OnClickListener;
import com.sonyericsson.extras.liveware.extension.util.control.ControlViewGroup;
import com.example.sonymobile.smartextension.backwardscompatiblecontrol.R;

/**
 * The sample control for SmartWatch handles the control on the accessory. This
 * class exists in one instance for every supported host application that we
 * have registered to
 */
class ControlSmartWatch2 extends ControlExtension {


//	// structures
//	public HashMap<Integer, String[]> mapMonday = new HashMap<Integer, String[]>();
//	public HashMap<Integer, String[]> mapTuesday = new HashMap<Integer, String[]>();
//	public HashMap<Integer, String[]> mapWednesday = new HashMap<Integer, String[]>();
//	public HashMap<Integer, String[]> mapThursday = new HashMap<Integer, String[]>();
//	public HashMap<Integer, String[]> mapFriday = new HashMap<Integer, String[]>();
//	public String[] namesOfDays =
//	{ "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" };
	
    private static final int ANIMATION_DELTA_MS = 500;
    private static final int SELECT_TOGGLER_MS = 2000;
    private static final int MENU_ITEM_0 = 0;
    private static final int MENU_ITEM_1 = 1;
    private static final int MENU_ITEM_2 = 2;
    private static final int MENU_ITEM_3 = 3;
    private static final int MENU_ITEM_4 = 4;
    private static final int MENU_ITEM_5 = 5;

    private Handler mHandler;

    private boolean mIsShowingAnimation = false;

    private Animation mAnimation = null;

    private ControlViewGroup mLayout = null;

    private boolean mTextMenu = false;
    Bundle[] mMenuItemsText = new Bundle[3];
    Bundle[] mMenuItemsIcons = new Bundle[3];

    /**
     * Create sample control.
     *
     * @param hostAppPackageName Package name of host application.
     * @param context The context.
     * @param handler The handler to use
     */
    ControlSmartWatch2(final String hostAppPackageName, final Context context,
            Handler handler) {
        super(context, hostAppPackageName);
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        mHandler = handler;
        setupClickables(context);
        initializeMenus();
    }

    private void initializeMenus() {
        mMenuItemsText[0] = new Bundle();
        mMenuItemsText[0].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_0);
        mMenuItemsText[0].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 1");
        mMenuItemsText[1] = new Bundle();
        mMenuItemsText[1].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_1);
        mMenuItemsText[1].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 2");
        mMenuItemsText[2] = new Bundle();
        mMenuItemsText[2].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_2);
        mMenuItemsText[2].putString(Control.Intents.EXTRA_MENU_ITEM_TEXT, "Item 3");

        mMenuItemsIcons[0] = new Bundle();
        mMenuItemsIcons[0].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_3);
        mMenuItemsIcons[0].putString(Control.Intents.EXTRA_MENU_ITEM_ICON,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_call));
        mMenuItemsIcons[1] = new Bundle();
        mMenuItemsIcons[1].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_4);
        mMenuItemsIcons[1].putString(Control.Intents.EXTRA_MENU_ITEM_ICON,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_reply));
        mMenuItemsIcons[2] = new Bundle();
        mMenuItemsIcons[2].putInt(Control.Intents.EXTRA_MENU_ITEM_ID, MENU_ITEM_5);
        mMenuItemsIcons[2].putString(Control.Intents.EXTRA_MENU_ITEM_ICON,
                ExtensionUtils.getUriString(mContext, R.drawable.actions_view_in_phone));
    }

    /**
     * Get supported control width.
     *
     * @param context The context.
     * @return the width.
     */
    public static int getSupportedControlWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_width);
    }

    /**
     * Get supported control height.
     *
     * @param context The context.
     * @return the height.
     */
    public static int getSupportedControlHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.smart_watch_2_control_height);
    }

    @Override
    public void onDestroy() {
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "SampleControlSmartWatch onDestroy");
        stopAnimation();
        mHandler = null;
    };

    @Override
    public void onStart() {
        // Nothing to do. Animation is handled in onResume.
    }

    @Override
    public void onStop() {
        // Nothing to do. Animation is handled in onPause.
    }

    @Override
    public void onResume() {
    	
    	Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "clicking the button");
    	showLayout(R.layout.sample_control_2,null);
    	
    
    }

    @Override
    public void onPause() {
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "Stopping animation");
        stopAnimation();
    }

    private void toggleAnimation() {
        if (mIsShowingAnimation) {
            stopAnimation();
        }
        else {
            startAnimation();
        }
    }

    /**
     * Start showing animation on control.
     */
    private void startAnimation() {
        if (!mIsShowingAnimation) {
            mIsShowingAnimation = true;
            mAnimation = new Animation();
            mAnimation.run();
        }
    }

    /**
     * Stop showing animation on control.
     */
    private void stopAnimation() {
        if (mIsShowingAnimation) {
            // Stop animation on accessory
            if (mAnimation != null) {
                mAnimation.stop();
                mHandler.removeCallbacks(mAnimation);
                mAnimation = null;
            }
            mIsShowingAnimation = false;
        }
    }

    @Override
    public void onTouch(final ControlTouchEvent event) {
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "onTouch() " + event.getAction());
        if (event.getAction() == Control.Intents.TOUCH_ACTION_RELEASE) {
            Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "Toggling animation");
            toggleAnimation();
        }
    }
    
//    public String[] details(){
//    	String moduleRoom [] = new String[2];
//    	
//    	String toCut = null;
//		String buttonDisplay = null;
//		String text1Display = null;
//		String text2Display = null;
//		String text3Display = null;
//		String text4Display = null;
//		String nowDisplay = null;
//		String nextDisplay = null;
//		String week1 = null;
//		String mins = null;
//
//    	
//    	int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//    	Date today = new Date();
//		int hour = (int) today.getHours();
//		int min = (int) today.getMinutes();
//		
//		day = day - 1;
//
//		switch (day)
//		{
//		case 1:
//		{
//			// monday
//			// call mondayMap
//
//			if (hour < 8 || hour >= 18)
//			{
//				buttonDisplay = "no classes";
//				break;
//			}
//
//			toCut = populateTimeTable(day, hour);
//
//			String words[] = new String[4];
//			int i = 0;
//			for (String word : toCut.split("-"))
//			{
//				words[i] = word;
//				i++;
//			}
//			text1Display = words[0];
//			text2Display = words[1];
//			text3Display = words[2];
//			text4Display = words[3];
//
//			buttonDisplay = "Remaining Mins: " + min;
//
//		}
//			break;
//
//		case 2:
//		{
//			// tuesday
//			// call tueMap
//
//			if (hour < 8 || hour >= 18)
//			{
//
//				buttonDisplay = "no classes";
//				break;
//			}
//
//			toCut = populateTimeTable(day, hour);
//
//			String words[] = new String[4];
//			int i = 0;
//			for (String word : toCut.split("-"))
//			{
//				words[i] = word;
//				i++;
//			}
//			text1Display = words[0];
//			text2Display = words[1];
//			text3Display = words[2];
//			text4Display = words[3];
//
//		
//			buttonDisplay = "Remaining Mins: " + min;
//
//		}
//			break;
//
//		case 3:
//		{
//			// wedneday
//			// call wedMap
//
//			if (hour < 9 || hour >= 18)
//			{
//				buttonDisplay = "no classes";
//
//				break;
//			}
//			toCut = populateTimeTable(day, hour);
//
//			String words[] = new String[4];
//			int i = 0;
//			for (String word : toCut.split("-"))
//			{
//				words[i] = word;
//				i++;
//			}
//			text1Display = words[0];
//			text2Display = words[1];
//			text3Display = words[2];
//			text4Display = words[3];
//
//		
//			buttonDisplay = "Remaining Mins: " + min;
//
//		}
//			break;
//
//		case 4:
//		{
//			// thursday
//			// call thurMap
//
//			if (hour < 8 || hour >= 18)
//			{
//				buttonDisplay = "no classes";
//
//				break;
//			}
//			toCut = populateTimeTable(day, hour);
//
//			String words[] = new String[4];
//			int i = 0;
//			for (String word : toCut.split("-"))
//			{
//				words[i] = word;
//				i++;
//			}
//			text1Display = words[0];
//			text2Display = words[1];
//			text3Display = words[2];
//			text4Display = words[3];
//
//		
//			buttonDisplay = "Remaining Mins: " + min;
//
//		}
//			break;
//
//		case 5:
//		{
//			// friday
//			// call friMap
//
//			if (hour < 8 || hour >= 18)
//			{
//				buttonDisplay = "no classes";
//
//				break;
//			}
//			toCut = populateTimeTable(day, hour);
//
//			String words[] = new String[4];
//			int i = 0;
//			for (String word : toCut.split("-"))
//			{
//				words[i] = word;
//				i++;
//			}
//			text1Display = words[0];
//			text2Display = words[1];
//			text3Display = words[2];
//			text4Display = words[3];
//
//			
//
//		}
//			break;
//
//		default:
//		{
//
//			buttonDisplay = "No Classes";
//			text1Display = "no classes";
//			text2Display = "no classes";
//			text3Display = "no classes";
//			text4Display = "no classes";
//
//		}
//			break;
//
//		}// end switch
//
//		/*
//		 * MAIN PART TO BE DISPLAYED
//		 */
//
////		module.setText(text3Display);
////		classroom.setText(text4Display);
//		
//
//		moduleRoom[0] = text3Display;
//		moduleRoom[1] = text4Display;
//		
//    	return moduleRoom;
//    	
//    }//end details
//    
//	public String populateTimeTable(int flag, int key)
//	{
//
//		if (flag == 1)
//		{
//			// each field must have 4 indexes - current class - room,
//			// next class = room
//			mapMonday.put(8, new String[]
//			{ "No class", "No class" });
//			mapMonday.put(9, new String[]
//			{ "Networking", "Annex 3022" });
//			mapMonday.put(10, new String[]
//			{ "Networking", "Annex 3022" });
//			mapMonday.put(11, new String[]
//			{ "Networking lab A", "AU 1005" });
//			mapMonday.put(12, new String[]
//			{ "Networking lab A", "AU 1005" });
//			mapMonday.put(13, new String[]
//			{ "free", "free" });
//			mapMonday.put(14, new String[]
//			{ "Free", "Free" });
//			mapMonday.put(15, new String[]
//			{ "Maths", "Annex 3020" });
//			mapMonday.put(16, new String[]
//			{ "Networking", "Lab AU1005 (group B/C)" });
//			mapMonday.put(17, new String[]
//			{ "Networking", "Lab AU1005(group B/C)" });
//			mapMonday.put(18, new String[]
//			{ "Finished", "Home" });
//
//			// class room
//			return mapMonday.get(key)[0] + "-" + mapMonday.get(key)[1] + "-" + mapMonday.get(key + 1)[0] + "-"
//					+ mapMonday.get(key + 1)[1];
//		}
//
//		else if (flag == 2)
//		{
//			mapTuesday.put(8, new String[]
//			{ "no class", "no class" });
//			mapTuesday.put(9, new String[]
//			{ "DataBases", "Annex 3011" });
//			mapTuesday.put(10, new String[]
//			{ "Java", "Annex G025" });
//			mapTuesday.put(11, new String[]
//			{ "Java", "Annex 3021" });
//			mapTuesday.put(12, new String[]
//			{ "Free", "Free" });
//			mapTuesday.put(13, new String[]
//			{ "DataBases", "Annex 3021" });
//			mapTuesday.put(14, new String[]
//			{ "Free", "Free" });
//			mapTuesday.put(15, new String[]
//			{ "HCI", "Main B034" });
//			mapTuesday.put(16, new String[]
//			{ "HCI", "Lab AU1006" });
//			mapTuesday.put(17, new String[]
//			{ "HCI", "Lab AU1006" });
//			mapTuesday.put(18, new String[]
//			{ "Finished", "No Class" });
//
//			// class room
//			return mapTuesday.get(key)[0] + "-" + mapTuesday.get(key)[1] + "-" + mapTuesday.get(key + 1)[0]
//					+ "-" + mapTuesday.get(key + 1)[1];
//
//		}
//
//		else if (flag == 3)
//		{
//			mapWednesday.put(8, new String[]
//			{ "no class", "no class" });
//			mapWednesday.put(9, new String[]
//			{ "Free", "Free" });
//			mapWednesday.put(10, new String[]
//			{ "Maths", "Annex G028" });
//			mapWednesday.put(11, new String[]
//			{ "Web Development", "Annex 3020" });
//			mapWednesday.put(12, new String[]
//			{ "Web Development", "Annex 3020" });
//			mapWednesday.put(13, new String[]
//			{ "Internship", "Main B034" });
//			mapWednesday.put(14, new String[]
//			{ "Free", "Free" });
//			mapWednesday.put(15, new String[]
//			{ "Free", "Free" });
//			mapWednesday.put(16, new String[]
//			{ "free", "free" });
//			mapWednesday.put(17, new String[]
//			{ "free", "free" });
//			mapWednesday.put(18, new String[]
//			{ "Finished", "No class" });
//
//			// class room
//			return mapWednesday.get(key)[0] + "-" + mapWednesday.get(key)[1] + "-"
//					+ mapWednesday.get(key + 1)[0] + "-" + mapWednesday.get(key + 1)[1];
//
//		}
//
//		else if (flag == 4)
//		{
//			mapThursday.put(8, new String[]
//			{ "no class", "no class" });
//			mapThursday.put(9, new String[]
//			{ "Free", "Free" });
//			mapThursday.put(10, new String[]
//			{ "Java", "Annex  G027" });
//			mapThursday.put(11, new String[]
//			{ "Java", "Lab Annex 3008" });
//			mapThursday.put(12, new String[]
//			{ "Java", "Lab Annex 3008" });
//			mapThursday.put(13, new String[]
//			{ "Internship", "Main G007" });
//			mapThursday.put(14, new String[]
//			{ "Web Development", "Lab Annex 3005" });
//			mapThursday.put(15, new String[]
//			{ "Web Development", "Lab Annex 3005" });
//			mapThursday.put(16, new String[]
//			{ "HCI", "Annex 3023" });
//			mapThursday.put(17, new String[]
//			{ "Free", "Free" });
//			mapThursday.put(18, new String[]
//			{ "finished", "no class" });
//
//			// class room
//			return mapThursday.get(key)[0] + "-" + mapThursday.get(key)[1] + "-" + mapThursday.get(key + 1)[0]
//					+ "-" + mapThursday.get(key + 1)[1];
//
//		}
//
//		else if (flag == 5)
//		{
//			mapFriday.put(8, new String[]
//			{ "no class", "no class" });
//			mapFriday.put(9, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(10, new String[]
//			{ "Maths", "Annex 3022" });
//			mapFriday.put(11, new String[]
//			{ "DataBases", "Lab Annex 1016" });
//			mapFriday.put(12, new String[]
//			{ "DataBases", "Lab Annex 1016" });
//			mapFriday.put(13, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(14, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(15, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(16, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(17, new String[]
//			{ "Free", "Free" });
//			mapFriday.put(18, new String[]
//			{ "finished", "no class" });
//
//			// class room
//			return mapFriday.get(key)[0] + "-" + mapFriday.get(key)[1] + "-" + mapFriday.get(key + 1)[0] + "-"
//					+ mapFriday.get(key + 1)[1];
//
//		}
//
//		else
//		{
//
//			return "No Classes";
//		}
//	}// endfuncion
    

    @Override
    public void onObjectClick(final ControlObjectClickEvent event) {
    	 //ADD IN THE CLICK LISTENER HERE 
    	String module = "test 1";
    	String classroom = "test 2";

    	//String details1[] = details();
//    	String details1[] = {"test1", "test2"};
//    	module = details1[0];
//    	classroom = details1[1];
    	
    	
       
    	
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "onObjectClick() " + event.getClickType());
        if (event.getLayoutReference() == R.id.button1) {
           sendText(R.id.module,"test1"); // change these to variable from the getClassInfo class
           sendText(R.id.classroom, "test2");
           
        
        }//end if
    }

    @Override
    public void onKey(final int action, final int keyCode, final long timeStamp) {
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "onKey()");
        if (action == Control.Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_OPTIONS) {
            toggleMenu();
        }
        else if (action == Control.Intents.KEY_ACTION_RELEASE
                && keyCode == Control.KeyCodes.KEYCODE_BACK) {
            Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "onKey() - back button intercepted.");
        }
    }

    @Override
    public void onMenuItemSelected(final int menuItem) {
        Log.d(BackwardsCompatibleControlExtensionService.LOG_TAG, "onMenuItemSelected() - menu item " + menuItem);
        if (menuItem == MENU_ITEM_0) {
            clearDisplay();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                }
            }, 1000);
        }
    }

    private void toggleMenu() {
        if (mTextMenu) {
            showMenu(mMenuItemsIcons);
        }
        else
        {
            showMenu(mMenuItemsText);
        }
        mTextMenu = !mTextMenu;
    }

    private void setupClickables(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.sample_control_2
                , null);

    }

    private class SelectToggler implements Runnable {

        private int mLayoutReference;
        private int mResourceId;

        SelectToggler(int layoutReference, int resourceId) {
            mLayoutReference = layoutReference;
            mResourceId = resourceId;
        }

        @Override
        public void run() {
            sendImage(mLayoutReference, mResourceId);
        }

    }

    /**
     * The animation class shows an animation on the accessory. The animation
     * runs until mHandler.removeCallbacks has been called.
     */
    private class Animation implements Runnable {

        private int mIndex = 1;
        private boolean mIsStopped = false;

        /**
         * Create animation.
         */
        Animation() {
            mIndex = 1;
        }

        /**
         * Stop the animation.
         */
        public void stop() {
            mIsStopped = true;
        }

        @Override
        public void run() {
            int resourceId;
            switch (mIndex) {
                case 1:
                    resourceId = R.drawable.generic_anim_1_icn;
                    break;
                case 2:
                    resourceId = R.drawable.generic_anim_2_icn;
                    break;
                case 3:
                    resourceId = R.drawable.generic_anim_3_icn;
                    break;
                case 4:
                    resourceId = R.drawable.generic_anim_2_icn;
                    break;
                default:
                    Log.e(BackwardsCompatibleControlExtensionService.LOG_TAG, "mIndex out of bounds: " + mIndex);
                    resourceId = R.drawable.generic_anim_1_icn;
                    break;
            }
            mIndex++;
            if (mIndex > 4) {
                mIndex = 1;
            }

            if (!mIsStopped) {
                updateAnimation(resourceId);
            }
            if (mHandler != null && !mIsStopped) {
                mHandler.postDelayed(this, ANIMATION_DELTA_MS);
            }
        }

        /**
         * Update the animation on the accessory. Only updates the part of the
         * screen which contains the animation.
         *
         * @param resourceId The new resource to show.
         */
        private void updateAnimation(int resourceId) {

        }
    };

}
