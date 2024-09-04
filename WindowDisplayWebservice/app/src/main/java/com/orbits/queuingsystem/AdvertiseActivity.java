package com.orbits.queuingsystem;

import java.io.File;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.KeyboardView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class AdvertiseActivity extends Fragment {
    private final static int MAX_VOLUME = 100;
    //public CustomKeyboard mCustomKeyboard;
    public static Animation myFadeInAnimation;
    public static MediaController mc; // media controller for video view
    static boolean pausing = false;
    private static float videoVolumn;
    public String[] image_FileNames;
    public String[] image_FilePaths;
    public String[] video_FileNames;
    public String[] video_FilePaths;
    public int currentVideoIndex = 0;
    //public RelativeLayout kbdLayout;
    public LinearLayout advLayout;
    public RelativeLayout videoLayout;
    public VideoView videoPlayer;
    View advView;
    int a = 0;
    RefreshHandler refreshHandler = new RefreshHandler();
    TextView txtCounterNo;
    int ARABIC_FONT;
    private int ADVTISE_DELAY;
    private int WINDOW_DISPLAY;
    private float VIDEO_VOLUME;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //		String windowDisplay;
		/*try {
			windowDisplay = MainActivity.digitMap.get("Window Display");
			System.out.println("keypad window==>" + windowDisplay);
			if(windowDisplay == null) {
				windowDisplay = "0";
			}
			System.out.println(windowDisplay);
			WINDOW_DISPLAY  = Integer.parseInt(windowDisplay);

		} catch (Exception e) {
			e.printStackTrace();
		}*/

		/*System.out.println("keypad window==> " + TokenActivity.WINDOW_DISPLAY);
		if(TokenActivity.WINDOW_DISPLAY != 0) {
			advView = inflater.inflate(R.layout.activity_advertise, container, false);
		} else {
			advView = inflater.inflate(R.layout.activity_keyboard, container, false);
		}*/

        advView = inflater.inflate(R.layout.activity_advertise, container, false);
        return advView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String advertiseDelay;
        String videoVolume = null;
        int counterColor;
        String counterNo;

        advLayout = (LinearLayout) advView.findViewById(R.id.leftLayout);
        videoLayout = (RelativeLayout) advView.findViewById(R.id.videoLayout);
        videoPlayer = (VideoView) advView.findViewById(R.id.videoPlayer);
        txtCounterNo = (TextView) advView.findViewById(R.id.txtCounterNo);
        String arabicFont;
        try {
            arabicFont = MainActivity.digitMap.get("Arabic_Font");
            ARABIC_FONT = Integer.parseInt(arabicFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT != "") {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Counter")) {
                    txtCounterNo.setVisibility(View.VISIBLE);
                    String checkCounter = MainActivity.sharePrefSettings.getString("CounterNo", "");
                    if (ARABIC_FONT == 1) {
                        //if (AppConstantsFlags.SHOW_ADVERTIZEMENT) {
                        txtCounterNo
                                .setTextSize(Integer.parseInt(MainActivity.digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN)));
//                } else {
//                    txtCounterNo
//                            .setTextSize(Integer.parseInt(MainActivity.digitMap
//                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_COUNTER_FONT_FULL_SCREEN)));
//                }
                    } else {
                        // if (AppConstantsFlags.SHOW_ADVERTIZEMENT) {
                        txtCounterNo
                                .setTextSize(Integer.parseInt(MainActivity.digitMap
                                        .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN)));
//                } else {
//                    txtCounterNo
//                            .setTextSize(Integer.parseInt(MainActivity.digitMap
//                                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_COUNTER_FONT_FULL_SCREEN)));
//                }
                    }

                    try {
                        // nirmal 08 oct 2014 -- set padding for token-text-view
                        txtCounterNo
                                .setPadding(
                                        0,
                                        Integer.parseInt(MainActivity.digitMap
                                                .get(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_TOP)),
                                        0,
                                        Integer.parseInt(MainActivity.digitMap
                                                .get(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM)));
                    } catch (Exception e) {
                        Toast.makeText(getActivity(),
                                "Some error occurred, Please check padding values.",
                                Toast.LENGTH_LONG).show();
                    }
                    boolean USE_BG_IMG = false;
                    try {
                        // nirmal 07 oct 2014 -- set backgroud color as mentioned in
                        // excel file
                /*USE_BG_IMG = true;
                mTokenLayoutContainer.setBackgroundColor(Color.BLACK);*/
                        int bgColor = Color.parseColor(MainActivity.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_BG_COLOR));
                        advLayout.setBackgroundColor(bgColor);
                    } catch (Exception e) {
                        // nirmal 07 oct 2014 -- use background image if invalid color
                        // is entered
                        USE_BG_IMG = true;
                        advLayout.setBackgroundColor(Color.BLACK);
                    }
                    if (USE_BG_IMG) {
                        try {
                            // nirmal 07 oct 2014 -- set backgroud image
                            File bgImgFile = new File(
                                    Environment.getExternalStorageDirectory()
                                            + "/Queue_Config Files/counter_img.png");
                            if (bgImgFile.exists()) {
                                advLayout.setBackground(Drawable
                                        .createFromPath(bgImgFile.getAbsolutePath()));
                            }
                        } catch (Exception e) {
                            Toast.makeText(
                                    getActivity(),
                                    "Some error occurred, Please check bgimg.png file.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    try {
                        // nirmal 07 oct 2014 -- set token color as mentioned in
                        // excel file
                        counterColor = Color.parseColor(MainActivity.digitMap
                                .get(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_COLOR));
                    } catch (Exception e) {
                        counterColor = Color.RED;
                        Toast.makeText(getActivity(),
                                "Some error occurred, Please check token color value.",
                                Toast.LENGTH_LONG).show();
                    }
                    txtCounterNo.setTextColor(counterColor);
                    if (checkCounter != "") {
                        if (ARABIC_FONT != 0) {
                            counterNo = CommonLogic.englishToArabicNumber(Integer.parseInt(checkCounter), checkCounter.length());
                        } else {
                            counterNo = CommonLogic.englishDigits(Integer.parseInt(checkCounter), checkCounter.length());
                        }
                        txtCounterNo.setText(counterNo);
                    } else {
                        txtCounterNo.setText("0");
                    }
                } else if (AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
                    myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.custom_anim);
                    //kbdLayout = (RelativeLayout) advView.findViewById(R.id.keyboardlayout);
                    txtCounterNo.setVisibility(View.GONE);
                    try {
                        advertiseDelay = MainActivity.digitMap.get("Advertise Delay");
                        videoVolume = MainActivity.digitMap.get("Video volume");
                        if (advertiseDelay == null || videoVolume == null) {
                            advertiseDelay = "20";
                            videoVolume = "50";
                        }
                        ADVTISE_DELAY = Integer.parseInt(advertiseDelay) * 1000;
                        VIDEO_VOLUME = Float.parseFloat(videoVolume);

                        readAdvImageFile();
                        readAdvVideoFile();
                        mc = new MediaController(getActivity());

                        videoVolumn = (float) (1 - (Math.log(MAX_VOLUME - VIDEO_VOLUME) / Math.log(MAX_VOLUME)));
                        //		Toast.makeText(getActivity(), "Video volume " + videoVolume, Toast.LENGTH_SHORT).show();
                        //		if (WINDOW_DISPLAY != 0) {
                        //nirmal 17 june 2014 -- display ads according to settings
                        if (image_FileNames.length != 0 && AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
                            updateUI();
                        } else if (video_FileNames != null && video_FileNames.length != 0 && image_FileNames.length == 0 && AppConstantsFlags.SHOW_ADVERTIZEMENT.equals("Show Advertisement")) {
                            advLayout.setVisibility(View.GONE);
                            videoLayout.setVisibility(View.VISIBLE);
                            playAdvVideo(0);
                        }
                        //		}
                    } catch (NullPointerException e) {
                        Log.e("TravellerLog :: ", "Null pointer exception");
                    } catch (Exception e) {
                        Log.e("TravellerLog :: ", "exception");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    // Read advertise image file.
    public void readAdvImageFile() {
        try {
            File[] listFile;
            image_FileNames = null;
            image_FilePaths = null;

            File file = new File(Environment.getExternalStorageDirectory()
                    + "/Queue_Config Files/Advertise images");
            if (file.isDirectory()) {
                listFile = file.listFiles();
                // List file path for Images
                image_FilePaths = new String[listFile.length];
                // List file path for Image file names
                image_FileNames = new String[listFile.length];

                for (int i = 0; i < listFile.length; i++) {
                    // Get the path
                    image_FilePaths[i] = listFile[i].getAbsolutePath();
                    // Get the name
                    image_FileNames[i] = listFile[i].getName();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Read Token image file.

    // Read advertise video file.
    public void readAdvVideoFile() {
        try {
            File[] listFile;
            video_FileNames = null;
            video_FilePaths = null;
            File vidDir = new File(Environment.getExternalStorageDirectory()
                    + "/Queue_Config Files/Advertise videos");
            if (vidDir.isDirectory()) {
                String[] vidFiles = vidDir.list();
                if (vidFiles.length != 0) {
                    listFile = vidDir.listFiles();
                    // List file path for videos
                    video_FilePaths = new String[listFile.length];
                    // List file path for video file names
                    video_FileNames = new String[listFile.length];

                    for (int i = 0; i < listFile.length; i++) {
                        // Get the path
                        video_FilePaths[i] = listFile[i].getAbsolutePath();
                        // Get the name
                        video_FileNames[i] = listFile[i].getName();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        try {
            if (image_FilePaths != null || video_FilePaths != null) {
                Bitmap bitmapImg;
                ////// manjusha 30/05/2019
                //ImageView imageView;
                //imageView = (ImageView) advView.findViewById(R.id.imgAdvertise);

                LinearLayout linearLayout = (LinearLayout) advView.findViewById(R.id.leftLayout);
                refreshHandler.sleep(ADVTISE_DELAY);
                if (image_FilePaths.length == 1 && !videoPlayer.isPlaying()) {
//                bitmapImg = BitmapFactory.decodeFile(image_FilePaths[a]);
//                imageView.setImageBitmap(bitmapImg);
                    linearLayout.setBackground(Drawable.createFromPath(image_FilePaths[a]));
                } else if (a < image_FilePaths.length && !videoPlayer.isPlaying()) {
                    bitmapImg = BitmapFactory.decodeFile(image_FilePaths[a]);
//                imageView.setImageBitmap(bitmapImg);
//                imageView.startAnimation(myFadeInAnimation);
                    linearLayout.setBackground(Drawable.createFromPath(image_FilePaths[a]));
                    linearLayout.startAnimation(myFadeInAnimation);
                    a++;
                } else {
                    if (video_FilePaths != null && !videoPlayer.isPlaying() && a >= image_FilePaths.length) {
                        advLayout.setVisibility(View.GONE);
                        //kbdLayout.setVisibility(View.GONE);
                        videoLayout.setVisibility(View.VISIBLE);
                        currentVideoIndex = 0;
                        playVideo(currentVideoIndex);
                    }
                    a = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Play only advertise video if image files are empty.
    public void playAdvVideo(int index) {
        pausing = false;
        try {
            MediaController mc = new MediaController(getActivity());
            mc.setAnchorView(videoPlayer);
            mc.setMediaPlayer(videoPlayer);
            if (video_FilePaths[index].endsWith("mp4") || video_FilePaths[index].endsWith("3gp")) {
                videoPlayer.setVideoPath(video_FilePaths[index]);
            } else {
                Toast.makeText(getActivity(), "Only mp4 and 3gp formats are supported.", Toast.LENGTH_LONG).show();
            }

            videoPlayer.requestFocus();
            videoPlayer.start();

            videoPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                            mp = new MediaPlayer();
                        }
                        mp.setVolume(videoVolumn, videoVolumn);
                        mp.setLooping(true);
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            videoPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    player.stop();
                    // play next video file
                    if (currentVideoIndex < (video_FilePaths.length - 1)) {
                        currentVideoIndex = currentVideoIndex + 1;
                        playAdvVideo(currentVideoIndex);
                    } else {
                        currentVideoIndex = 0;
                        playAdvVideo(currentVideoIndex);
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    // Play advertise video
    public void playVideo(int index) {
        pausing = false;
        try {
            mc.setAnchorView(videoPlayer);
            mc.setMediaPlayer(videoPlayer);
            if (video_FilePaths[index].endsWith("mp4") || video_FilePaths[index].endsWith("3gp")) {
                videoPlayer.setVideoPath(video_FilePaths[index]);
            }
            videoPlayer.requestFocus();
            videoPlayer.start();

            videoPlayer.setOnPreparedListener(new OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    try {
                        if (mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                            mp = new MediaPlayer();
                        }
                        mp.setVolume(videoVolumn, videoVolumn);
                        mp.setLooping(false);
                        mp.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            videoPlayer.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer player) {
                    player.stop();
                    // play next video file
                    if (currentVideoIndex < (video_FilePaths.length - 1)) {
                        currentVideoIndex = currentVideoIndex + 1;
                        playVideo(currentVideoIndex);
                    } else {
                        advLayout.setVisibility(View.VISIBLE);
                        videoLayout.setVisibility(View.GONE);
                    }
                }
            });

			/*			videoView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					SeekBar volumeSeekBar = (SeekBar) advView.findViewById(R.id.seekBar1);
					volumeSeekBar.setVisibility(View.VISIBLE);
					return true;
				}
			});*/
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    // Advertise image handler
    class RefreshHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            updateUI();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

/*
	//Pankaj 22 Aug 13 --> show keypad on advertise image click
	public void showKeypad(View view) {
		EditText quickCall = (EditText) advView.findViewById(R.id.editTxtNumPad);
		quickCall.getText().clear();
		advLayout.setVisibility(View.GONE);
		kbdLayout.setVisibility(View.VISIBLE);
		quickCall.requestFocus();
		showCustomKeyboard();
	}
	*/

	/*
	//** Make the CustomKeyboard invisible. *//*
	public void showCustomKeyboard() {
		KeyboardView mKeyboardView = (KeyboardView) advView.findViewById(R.id.keyboardview);
		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);
	}
	*/
}
