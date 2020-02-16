package com.example.var;

import java.io.File;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.imgcodecs.Imgcodecs;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import org.opencv.core.Point;

import org.opencv.core.MatOfPoint2f;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;

public class BallActivity extends Activity implements OnTouchListener, CvCameraViewListener2 {
    private static final String TAG = "BallActivity";
    private Double ballstatus = 0.0;
    private boolean mIsColorSelected_ball = false;
    private Integer numScreenTouches = 0;
    private Mat mRgba_ball;
    private Scalar mBlobColorRgba_ball;
    private Scalar mBlobColorHsv_ball;
    private ColorBlobDetector mDetector_ball;
    private Mat mSpectrum_ball;
    private Size SPECTRUM_SIZE_ball;
    private Scalar CONTOUR_COLOR_ball;
    private Integer xball;
    private Integer yball;

    private boolean mIsColorSelected_goal = false;
    private Scalar mBlobColorRgba_goal;
    private Mat mRgba_goal;
    private Scalar mBlobColorHsv_goal;
    private ColorBlobDetector mDetector_goal;
    private Mat mSpectrum_goal;
    private Size SPECTRUM_SIZE_goal;
    private Scalar CONTOUR_COLOR_goal;
    private CameraBridgeViewBase mOpenCvCameraView;
    private Integer xgoal;
    private Integer ygoal;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(BallActivity.this);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    //This is the constructor
    public BallActivity() {

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Sets the look of the screen when this class is called to be R.layout.activity_ball
        setContentView(R.layout.activity_ball);

        //Creates the color blob detection
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    //Unused
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    //Unused
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    //Unused
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    //Initializes values
    public void onCameraViewStarted(int width, int height) {
        mRgba_ball = new Mat(height, width, CvType.CV_8UC4);
        mDetector_ball = new ColorBlobDetector();
        mSpectrum_ball = new Mat();
        mBlobColorRgba_ball = new Scalar(255);
        mBlobColorHsv_ball = new Scalar(255);
        SPECTRUM_SIZE_ball = new Size(200, 64);
        CONTOUR_COLOR_ball = new Scalar(255, 0, 0, 255);

        mRgba_goal = new Mat(height, width, CvType.CV_8UC4);
        mDetector_goal = new ColorBlobDetector();
        mSpectrum_goal = new Mat();
        mBlobColorRgba_goal = new Scalar(255);
        mBlobColorHsv_goal = new Scalar(255);
        SPECTRUM_SIZE_goal = new Size(200, 64);
        CONTOUR_COLOR_goal = new Scalar(255, 0, 0, 255);
    }

    public void onCameraViewStopped() {
        mRgba_ball.release();
        mRgba_goal.release();
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.i(TAG, numScreenTouches.toString());
        if(numScreenTouches==0){
            Log.w(TAG, "0 touch");

            numScreenTouches++;
            int cols = mRgba_ball.cols();
            int rows = mRgba_ball.rows();

            int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
            int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

            int x = (int) event.getX() - xOffset;
            int y = (int) event.getY() - yOffset;
            //At this point, the x and the y coordinates of the touch are the variables x and y

            if ((x < 0) || (y < 0) || (x > cols) || (y > rows))
                return false;//This handles offscreen touches

            Rect touchedRect = new Rect();

            touchedRect.x = (x > 4) ? x - 4 : 0;
            touchedRect.y = (y > 4) ? y - 4 : 0;

            touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
            touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

            Mat touchedRegionRgba = mRgba_ball.submat(touchedRect);

            Mat touchedRegionHsv = new Mat();
            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

            // Calculate average color of touched region
            mBlobColorHsv_ball = Core.sumElems(touchedRegionHsv);
            int pointCount = touchedRect.width * touchedRect.height;
            for (int i = 0; i < mBlobColorHsv_ball.val.length; i++)
                mBlobColorHsv_ball.val[i] /= pointCount;

            mBlobColorRgba_ball = converScalarHsv2Rgba(mBlobColorHsv_ball);

            //This sets the color of the ball in ColorBlobDetector, use mColorRadius to change range of colors
            mDetector_ball.setHsvColor(mBlobColorHsv_ball);
            Log.w(TAG, mDetector_ball.getSpectrum().toString()+"SetHSV color ball");

            Imgproc.resize(mDetector_ball.getSpectrum(), mSpectrum_ball, SPECTRUM_SIZE_ball, 0, 0, Imgproc.INTER_LINEAR_EXACT);

            mIsColorSelected_ball = true;

            touchedRegionRgba.release();
            touchedRegionHsv.release();

            return true; // don't need subsequent touch events
        }
        else if(numScreenTouches>5){
            Log.w(TAG, "1 touch");
            numScreenTouches++;
            int cols = mRgba_goal.cols();
            int rows = mRgba_goal.rows();

            int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
            int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

            int x = (int) event.getX() - xOffset;
            int y = (int) event.getY() - yOffset;
            //At this point, the x and the y coordinates of the touch are the variables x and y

            if ((x < 0) || (y < 0) || (x > cols) || (y > rows))
                return false;//This handles offscreen touches

            Rect touchedRect = new Rect();

            touchedRect.x = (x > 4) ? x - 4 : 0;
            touchedRect.y = (y > 4) ? y - 4 : 0;

            touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
            touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

            Mat touchedRegionRgba = mRgba_goal.submat(touchedRect);

            Mat touchedRegionHsv = new Mat();
            Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

            // Calculate average color of touched region
            mBlobColorHsv_goal = Core.sumElems(touchedRegionHsv);
            int pointCount = touchedRect.width * touchedRect.height;
            for (int i = 0; i < mBlobColorHsv_goal.val.length; i++) {
                mBlobColorHsv_goal.val[i] /= pointCount;
            }

            mBlobColorRgba_goal = converScalarHsv2Rgba(mBlobColorHsv_goal);

            //This sets the color of the ball in ColorBlobDetector, use mColorRadius to change range of colors
            mDetector_goal.setHsvColor(mBlobColorHsv_goal);
            Imgproc.resize(mDetector_goal.getSpectrum(), mSpectrum_goal, SPECTRUM_SIZE_goal, 0, 0, Imgproc.INTER_LINEAR_EXACT);

            mIsColorSelected_goal = true;

            touchedRegionRgba.release();
            touchedRegionHsv.release();

            return false; // false for don't need subsequent touch events
        }
        else{
            numScreenTouches++;
            return false;
        }
    }


    public void makeInputFrameList(CvCameraViewFrame inputFrame) {
        VideoCapture cap = new VideoCapture("FILENAME AND PATH HERE"); // PUT FILENAME AND PATH HERE!!!!!!!

        File file = new File("C:\\Users\\meetr\\Documents\\personal_projects\\var\\dataFrames");
        boolean isMkdir = file.mkdir();
        int currentFrame = 0;
        Mat pic = new Mat();
        boolean isNotOver = true;

        while (isNotOver) {
            isNotOver = cap.read(pic);
            String name = "./data/frame" + currentFrame + ".jpg";
            Imgcodecs.imwrite("C:\\Users\\meetr\\Documents\\personal_projects\\var\\dataFrames\\" + name, pic);
            currentFrame += 1;
        }
        cap.release();
//        return;
    }
//    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba_goal = inputFrame.rgba();
        mRgba_ball = inputFrame.rgba();
        //change
        if (mIsColorSelected_ball && mIsColorSelected_goal) {
            mDetector_ball.process(mRgba_goal);
            mDetector_goal.process(mRgba_ball);
            List<MatOfPoint> contours_ball = mDetector_ball.getContours();
            List<MatOfPoint> contours_goal = mDetector_goal.getContours();
            //This draws the contours onto the screen
            double maxVal = 0;
            int maxValIdx = -1;
            for (int contourIdx = 0; contourIdx < contours_ball.size(); contourIdx++) {
                double contourArea = Imgproc.contourArea(contours_ball.get(contourIdx));
                if (maxVal < contourArea) {
                    maxVal = contourArea;
                    maxValIdx = contourIdx;
                }
            }
            if (contours_ball.size() > 0) {
                MatOfPoint maxContour = contours_ball.get(maxValIdx);
                Moments M = Imgproc.moments(maxContour);
                Imgproc.drawContours(mRgba_ball, contours_ball, maxValIdx, new Scalar(255, 255, 255), 10);
                xball = (int) (M.get_m10() / M.get_m00());
                yball = (int) (M.get_m01() / M.get_m00());
            }
            maxVal = 0;
            maxValIdx = -1;
            for (int contourIdx = 0; contourIdx < contours_goal.size(); contourIdx++) {
                double contourArea = Imgproc.contourArea(contours_goal.get(contourIdx));
                if (maxVal < contourArea) {
                    maxVal = contourArea;
                    maxValIdx = contourIdx;
                }
            }
            if (contours_goal.size() > 0) {
                MatOfPoint maxContour = contours_goal.get(maxValIdx);
                Imgproc.drawContours(mRgba_ball, contours_goal, maxValIdx, new Scalar(255, 0, 255), 10);
                Moments M = Imgproc.moments(maxContour);
                MatOfPoint2f  maxContour2f = new MatOfPoint2f( maxContour.toArray() );
                Point ballcoordinates = new Point(xball, yball);
                ballstatus = Imgproc.pointPolygonTest(maxContour2f, ballcoordinates, false);
                Log.i(TAG, ballstatus.toString());
                xgoal = (int) (M.get_m10() / M.get_m00());
                ygoal = (int) (M.get_m01() / M.get_m00());
            }
            //detect circle contours and line contours here
            Mat colorLabel = mRgba_ball.submat(4, 68, 4, 68);
            colorLabel.setTo(mBlobColorRgba_ball);

            Mat spectrumLabel = mRgba_ball.submat(4, 4 + mSpectrum_ball.rows(), 70, 70 + mSpectrum_ball.cols());
            mSpectrum_ball.copyTo(spectrumLabel);

        }
        if (xball != null && yball != null && xgoal != null && ygoal!= null){
            Log.e(TAG, "xball:"+xball.toString()+"yball"+yball.toString());
            Log.e(TAG, "xgoal"+xgoal.toString()+"ygoal"+ygoal.toString());
            checkCollision();
        }
        //goal scored
        if(ballstatus > 0){
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Button playButton = (Button) findViewById(R.id.goalScoredButton);
                    playButton.setVisibility(View.VISIBLE);


                }
            });
        }

        if(ballstatus <= 0){
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    Button playButton = (Button) findViewById(R.id.goalScoredButton);
                    playButton.setVisibility(View.INVISIBLE);
                }
            });
        }



        return mRgba_ball;
    }

    public void checkCollision(){
        Double distancebetween = 0.0;
        Double maxdistancebetween = 50.0;
        distancebetween = Math.sqrt(Math.pow((xball - xgoal),2) + Math.pow((yball - ygoal),2));
        if (distancebetween < maxdistancebetween) {
            //call method here to launch a new class
            Log.w(TAG, distancebetween.toString());
            Log.w(TAG, "The distance is close enough to warrant a collision");
        }

    }
    private Scalar converScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }
}