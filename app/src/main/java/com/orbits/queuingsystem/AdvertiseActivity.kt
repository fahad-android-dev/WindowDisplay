package com.orbits.queuingsystem

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.orbits.queuingsystem.helper.interfaces.CounterListener
import java.io.File
import kotlin.math.ln

class AdvertiseActivity : Fragment(), CounterListener {
    var image_FileNames: Array<String?>? = null
    var image_FilePaths: Array<String?>? = null
    var video_FileNames: Array<String?>? = null
    var video_FilePaths: Array<String?>? = null
    var currentVideoIndex: Int = 0

    //public RelativeLayout kbdLayout;
    var advLayout: LinearLayout? = null
    var videoLayout: RelativeLayout? = null
    var videoPlayer: VideoView? = null
    var advView: View? = null
    var a: Int = 0
    var refreshHandler: RefreshHandler = RefreshHandler()
    var txtCounterNo: TextView? = null
    var ARABIC_FONT: Int = 0
    private var ADVTISE_DELAY = 0
    private val WINDOW_DISPLAY = 0
    private var VIDEO_VOLUME = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        advView = inflater.inflate(R.layout.activity_advertise, container, false)
        return advView
    }

    override fun onStart() {
        super.onStart()
        var advertiseDelay: String
        var videoVolume: String?

        advLayout = advView!!.findViewById<View>(R.id.leftLayout) as LinearLayout
        videoLayout = advView!!.findViewById<View>(R.id.videoLayout) as RelativeLayout
        videoPlayer = advView!!.findViewById<View>(R.id.videoPlayer) as VideoView
        txtCounterNo = advView!!.findViewById<View>(R.id.txtCounterNo) as TextView
        val arabicFont: String
        try {
            arabicFont = MainActivity.digitMap["Arabic_Font"] ?: ""
            ARABIC_FONT = arabicFont.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            if (AppConstantsFlags.SHOW_ADVERTIZEMENT !== "") {
                if (AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Counter") {
                    println("here is counter without json")
                    setCounterUi()
                }
                else if (AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
                    myFadeInAnimation = AnimationUtils.loadAnimation(
                        activity, R.anim.custom_anim
                    )
                    //kbdLayout = (RelativeLayout) advView.findViewById(R.id.keyboardlayout);
                    txtCounterNo!!.visibility = View.GONE
                    try {
                        advertiseDelay = MainActivity.digitMap["Advertise Delay"] ?: ""
                        videoVolume = MainActivity.digitMap["Video volume"]
                        if (advertiseDelay == null || videoVolume == null) {
                            advertiseDelay = "20"
                            videoVolume = "50"
                        }
                        ADVTISE_DELAY = advertiseDelay.toInt() * 1000
                        VIDEO_VOLUME = videoVolume.toFloat()

                        readAdvImageFile()
                        readAdvVideoFile()
                        mc = MediaController(activity)

                        videoVolumn = (1 - (ln((MAX_VOLUME - VIDEO_VOLUME).toDouble()) / ln(
                            MAX_VOLUME.toDouble()
                        ))).toFloat()

                        if (image_FileNames!!.size != 0 && AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
                            updateUI()
                        } else if (video_FileNames != null && video_FileNames!!.size != 0 && image_FileNames!!.size == 0 && AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Advertisement") {
                            advLayout!!.visibility = View.GONE
                            videoLayout!!.visibility = View.VISIBLE
                            playAdvVideo(0)
                        }
                        //		}
                    } catch (e: NullPointerException) {
                        Log.e("TravellerLog :: ", "Null pointer exception")
                    } catch (e: Exception) {
                        Log.e("TravellerLog :: ", "exception")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setCounterUi(model:JsonObject ?= null){
        var counterColor: Int
        val counterNo: String
        txtCounterNo?.visibility = View.VISIBLE
        MainActivity.viewModel.setCounterListener(this)
        val checkCounter: String =
            MainActivity.sharePrefSettings!!.getString("CounterNo", "")!!
        if (ARABIC_FONT == 1) {
            //if (AppConstantsFlags.SHOW_ADVERTIZEMENT) {
            txtCounterNo?.setTextSize(
                MainActivity.digitMap
                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ARB_COUNTER_FONT_HALF_SCREEN)!!
                    .toInt().toFloat()
            )

        } else {
            // if (AppConstantsFlags.SHOW_ADVERTIZEMENT) {
            txtCounterNo?.setTextSize(
                MainActivity.digitMap
                    .get(AppConstantsFlags.KEY_DIGIT_MAP_ENG_COUNTER_FONT_HALF_SCREEN)!!
                    .toInt().toFloat()
            )

        }

        try {
            txtCounterNo?.setPadding(
                0,
                MainActivity.digitMap[AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_TOP]?.toInt() ?: 0,
                0,
                MainActivity.digitMap[AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_PADDING_BOTTOM]?.toInt() ?: 0
            )
        } catch (e: Exception) {
            Toast.makeText(
                activity,
                "Some error occurred, Please check padding values.",
                Toast.LENGTH_LONG
            ).show()
        }
        var USE_BG_IMG = false
        try {

            val bgColor = Color.parseColor(
                MainActivity.digitMap[AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_BG_COLOR]
            )
            advLayout?.setBackgroundColor(bgColor)
        } catch (e: Exception) {

            USE_BG_IMG = true
            advLayout?.setBackgroundColor(Color.BLACK)
        }
        if (USE_BG_IMG) {
            try {
                val bgImgFile = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/Queue_Config Files/counter_img.png"
                )
                if (bgImgFile.exists()) {
                    advLayout?.background = Drawable
                        .createFromPath(bgImgFile.absolutePath)
                }
            } catch (e: Exception) {
                Toast.makeText(
                    activity,
                    "Some error occurred, Please check bgimg.png file.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        try {

            counterColor = Color.parseColor(
                MainActivity.digitMap
                    .get(AppConstantsFlags.KEY_DIGIT_MAP_COUNTER_COLOR)
            )
        } catch (e: Exception) {
            counterColor = Color.RED
            Toast.makeText(
                activity,
                "Some error occurred, Please check token color value.",
                Toast.LENGTH_LONG
            ).show()
        }
        txtCounterNo?.setTextColor(counterColor)
        if (checkCounter !== "") {
            counterNo = if (ARABIC_FONT != 0) {
                CommonLogic.englishToArabicNumber(
                    checkCounter.toInt(),
                    checkCounter.length
                )
            } else {
                CommonLogic.englishDigits(
                    checkCounter.toInt(),
                    checkCounter.length
                )
            }
            if (model?.isJsonNull == true){
                txtCounterNo?.text = counterNo
            }else {
                txtCounterNo?.text = model?.get("counterId")?.asString
            }
        } else {
            txtCounterNo?.text = "0"
        }
    }

    // Read advertise image file.
    fun readAdvImageFile() {
        try {
            val listFile: Array<File>
            image_FileNames = null
            image_FilePaths = null

            val file = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Advertise images"
            )
            if (file.isDirectory) {
                listFile = file.listFiles() as Array<File>
                // List file path for Images
                image_FilePaths = arrayOfNulls(listFile.size)
                // List file path for Image file names
                image_FileNames = arrayOfNulls(listFile.size)

                for (i in listFile.indices) {
                    // Get the path
                    image_FilePaths!![i] = listFile[i].absolutePath
                    // Get the name
                    image_FileNames!![i] = listFile[i].name
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Read advertise image file.
    // Read advertise video file.
    private fun readAdvVideoFile() {
        try {
            val listFile: Array<File>
            video_FileNames = null
            video_FilePaths = null
            val vidDir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Queue_Config Files/Advertise videos"
            )
            if (vidDir.isDirectory) {
                val vidFiles = vidDir.list()
                if (vidFiles?.size != 0) {
                    listFile = vidDir.listFiles() as Array<File>
                    // List file path for videos
                    video_FilePaths = arrayOfNulls(listFile.size)
                    // List file path for video file names
                    video_FileNames = arrayOfNulls(listFile.size)

                    for (i in listFile.indices) {
                        // Get the path
                        video_FilePaths!![i] = listFile[i].absolutePath
                        // Get the name
                        video_FileNames!![i] = listFile[i].name
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateUI() {
        try {
            if (image_FilePaths != null || video_FilePaths != null) {
                val bitmapImg: Bitmap

                //ImageView imageView;
                //imageView = (ImageView) advView.findViewById(R.id.imgAdvertise);
                val linearLayout = advView!!.findViewById<View>(R.id.leftLayout) as LinearLayout
                refreshHandler.sleep(ADVTISE_DELAY.toLong())
                if (image_FilePaths?.size == 1 && !videoPlayer!!.isPlaying) {
//                bitmapImg = BitmapFactory.decodeFile(image_FilePaths[a]);
//                imageView.setImageBitmap(bitmapImg);
                    linearLayout.background = Drawable.createFromPath(image_FilePaths!![a])
                } else if (a < image_FilePaths!!.size && !videoPlayer!!.isPlaying) {
                    bitmapImg = BitmapFactory.decodeFile(image_FilePaths!![a])
                    //                imageView.setImageBitmap(bitmapImg);
//                imageView.startAnimation(myFadeInAnimation);
                    linearLayout.background = Drawable.createFromPath(image_FilePaths!![a])
                    linearLayout.startAnimation(myFadeInAnimation)
                    a++
                } else {
                    if (video_FilePaths != null && !videoPlayer!!.isPlaying && a >= (image_FilePaths?.size
                            ?: 0)
                    ) {
                        advLayout?.visibility = View.GONE
                        //kbdLayout.setVisibility(View.GONE);
                        videoLayout?.visibility = View.VISIBLE
                        currentVideoIndex = 0
                        playVideo(currentVideoIndex)
                    }
                    a = 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Play only advertise video if image files are empty.
    private fun playAdvVideo(index: Int) {
        pausing = false
        try {
            val mc = MediaController(activity)
            mc.setAnchorView(videoPlayer)
            mc.setMediaPlayer(videoPlayer)
            if (video_FilePaths!![index]!!.endsWith("mp4") || video_FilePaths!![index]!!.endsWith("3gp")) {
                videoPlayer!!.setVideoPath(video_FilePaths!![index])
            } else {
                Toast.makeText(
                    activity,
                    "Only mp4 and 3gp formats are supported.",
                    Toast.LENGTH_LONG
                ).show()
            }

            videoPlayer!!.requestFocus()
            videoPlayer!!.start()

            videoPlayer!!.setOnPreparedListener { mp ->
                var mp = mp
                try {
                    if (mp.isPlaying) {
                        mp.stop()
                        mp.release()
                        mp = MediaPlayer()
                    }
                    mp.setVolume(videoVolumn, videoVolumn)
                    mp.isLooping = true
                    mp.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            videoPlayer!!.setOnCompletionListener { player ->
                player.stop()
                // play next video file
                if (currentVideoIndex < (video_FilePaths!!.size - 1)) {
                    currentVideoIndex = currentVideoIndex + 1
                    playAdvVideo(currentVideoIndex)
                } else {
                    currentVideoIndex = 0
                    playAdvVideo(currentVideoIndex)
                }
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    // Play advertise video
    fun playVideo(index: Int) {
        pausing = false
        try {
            mc!!.setAnchorView(videoPlayer)
            mc!!.setMediaPlayer(videoPlayer)
            if (video_FilePaths!![index]!!.endsWith("mp4") || video_FilePaths!![index]!!.endsWith("3gp")) {
                videoPlayer!!.setVideoPath(video_FilePaths!![index])
            }
            videoPlayer!!.requestFocus()
            videoPlayer!!.start()

            videoPlayer!!.setOnPreparedListener { mp ->
                var mp = mp
                try {
                    if (mp.isPlaying) {
                        mp.stop()
                        mp.release()
                        mp = MediaPlayer()
                    }
                    mp.setVolume(videoVolumn, videoVolumn)
                    mp.isLooping = false
                    mp.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            videoPlayer!!.setOnCompletionListener { player ->
                player.stop()
                // play next video file
                if (currentVideoIndex < (video_FilePaths!!.size - 1)) {
                    currentVideoIndex = currentVideoIndex + 1
                    playVideo(currentVideoIndex)
                } else {
                    advLayout!!.visibility = View.VISIBLE
                    videoLayout!!.visibility = View.GONE
                }
            }

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    // Advertise image handler
    inner class RefreshHandler : Handler() {
        override fun handleMessage(msg: Message) {
            updateUI()
        }

        fun sleep(delayMillis: Long) {
            this.removeMessages(0)
            sendMessageDelayed(obtainMessage(0), delayMillis)
        }
    }

    companion object {
        private const val MAX_VOLUME = 100

        var myFadeInAnimation: Animation? = null
        var mc: MediaController? = null // media controller for video view
        var pausing: Boolean = false
        private var videoVolumn = 0f
    }

    override fun onCounterReceived(jsonObject: JsonObject?) {
        if (AppConstantsFlags.SHOW_ADVERTIZEMENT == "Show Counter") {
            println("here is counter with json")
            val model = jsonObject?.getAsJsonObject("transaction")
            setCounterUi(model)
        }
    }
}
