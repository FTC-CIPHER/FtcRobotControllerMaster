package org.firstinspires.ftc.teamcode;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/*public class OpenCVProcessor implements VisionProcessor {
    Rect LEFT_RECTANGLE;
    Rect MIDDLE_RECTANGLE;
    Rect RIGHT_RECTANGLE;
    Mat hsvMat = new Mat();
    Mat lowMat = new Mat();
    Mat highMat = new Mat();
    Mat detectedMat = new Mat();
    Scalar YellowThresholdLow = new Scalar(12.5, 203, 250);
    Scalar YellowThresholdHigh = new Scalar(22.5, 255, 255);
    double Threshold = 0.1;
    Samplelocation samplelocation;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        LEFT_RECTANGLE = new Rect(
                new Point(0, 0),
                new Point((1 / 3) * width, height)
        );
        MIDDLE_RECTANGLE = new Rect(
                new Point((1 / 3) * width, 0),
                new Point((2 / 3) * width, height)
        );
        RIGHT_RECTANGLE = new Rect(
                new Point((2 / 3) * width, 0),
                new Point(width, height)
        );
    }

    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        // 25-45,
        // 95%-99%
        Core.inRange(hsvMat, YellowThresholdLow, YellowThresholdHigh, lowMat);
        Core.bitwise_or(lowMat, highMat, detectedMat);
        double leftPercent = (Core.sumElems(detectedMat.submat(LEFT_RECTANGLE)).val[0] / 255) / LEFT_RECTANGLE.area();
        double middlePercent = (Core.sumElems(detectedMat.submat(MIDDLE_RECTANGLE)).val[0] / 255) / MIDDLE_RECTANGLE.area();
        double rightPercent = (Core.sumElems(detectedMat.submat(RIGHT_RECTANGLE)).val[0] / 255) / RIGHT_RECTANGLE.area();
        Scalar redBorder = new Scalar(255, 0, 0);
        Scalar greenBorder = new Scalar(0, 255, 0);
        //Scalar blueBorder = new Scalar (0, 0, 255);
        if (leftPercent > middlePercent && leftPercent > rightPercent && leftPercent > Threshold) {
            samplelocation = Samplelocation.LEFT;
            Imgproc.rectangle(frame, LEFT_RECTANGLE, greenBorder);
            Imgproc.rectangle(frame, MIDDLE_RECTANGLE, redBorder);
            Imgproc.rectangle(frame, RIGHT_RECTANGLE, redBorder);
        } else if (middlePercent > leftPercent && middlePercent > rightPercent && middlePercent > Threshold) {
            samplelocation = Samplelocation.MIDDLE;
            Imgproc.rectangle(frame, LEFT_RECTANGLE, redBorder);
            Imgproc.rectangle(frame, MIDDLE_RECTANGLE, greenBorder);
            Imgproc.rectangle(frame, RIGHT_RECTANGLE, redBorder);
        } else if (rightPercent > leftPercent && rightPercent > middlePercent && rightPercent > Threshold) {
            samplelocation = Samplelocation.RIGHT;
            Imgproc.rectangle(frame, LEFT_RECTANGLE, redBorder);
            Imgproc.rectangle(frame, MIDDLE_RECTANGLE, redBorder);
            Imgproc.rectangle(frame, RIGHT_RECTANGLE, greenBorder);
        } else {

        }
        return null;
    }

    public Samplelocation getSamplelocation() {
        return samplelocation;
    }
    public enum Samplelocation {
        LEFT(1),
        MIDDLE(2),
        RIGHT(3);
        public final int posNum;

        Samplelocation(int posNum) {
            this.posNum = posNum;
        }
    }
}
 */
