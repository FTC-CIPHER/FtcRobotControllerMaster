package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;
import java.util.List;

@TeleOp(name = "OpenCVYellow")

public class opencvyellow extends LinearOpMode {

    double cX = 0;
    double cY = 0;
    double width = 0;
    double height = 0;
    double angle=0;
    double clawx = 960;
    double clawy = 900;

    private OpenCvWebcam webcam;  // Use OpenCvCamera class from FTC SDK
    private static final int CAMERA_WIDTH = 1920; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 1080; // height of wanted camera resolution
    // Calculate the distance using the formula
    public static final double objectWidthInRealWorldUnits = 1.5;  // Replace with the actual width of the object in real-world units
    public static final double focalLength = 1430;
    //int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    // Replace with the focal length of the camera in pixels

    @Override
    public void runOpMode() {
        //webcam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
        initOpenCV();
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        FtcDashboard.getInstance().startCameraStream(webcam, 30);
        telemetry.addData("Coordinate", "(" + (int) cX + ", " + (int) cY + ")");
        telemetry.addData("Distance in Inch", (getDistance(width)));
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Coordinate", "(" + (int) cX + ", " + (int) cY + ")");
            telemetry.addData("Distance in Inch", (getDistance(width)));
            telemetry.addData("angle", angle);
            telemetry.update();

            // The OpenCV pipeline automatically processes frames and handles detection
        }

        // Release resources
        webcam.stopStreaming();
    }

    private void initOpenCV() {

        // Create an instance of the camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        // Use OpenCvCameraFactory class from FTC SDK to create camera instance
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        FtcDashboard.getInstance().startCameraStream(webcam, 0);
        webcam.setPipeline(new YellowBlobDetectionPipeline());
        webcam.openCameraDevice();
        webcam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.UPRIGHT);
        VideoCapture cap = new VideoCapture(0);
        cap.set(Videoio.CAP_PROP_FRAME_HEIGHT, 1080);
        cap.set(Videoio.CAP_PROP_EXPOSURE, -11);
    }
    class YellowBlobDetectionPipeline extends OpenCvPipeline {
        @Override
        public Mat processFrame(Mat input) {
            // Preprocess the frame to detect yellow regions
            Mat yellowMask = preprocessFrame(input);

            // Find contours of the detected yellow regions
            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            Imgproc.findContours(yellowMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

            // Find the largest yellow contour (blob)
                MatOfPoint centeredContour = findCenteredContour(contours);

            if (centeredContour != null) {
                // Draw a red outline around the largest detected object
                Imgproc.drawContours(input, contours, contours.indexOf(centeredContour), new Scalar(0, 0, 255), 20);
                // Calculate the width of the bounding box
                width = calculateWidth(centeredContour);
                height = calculateHeight(centeredContour);
                RotatedRect rotatedRect = Imgproc.minAreaRect(new MatOfPoint2f(centeredContour.toArray()));
                angle = rotatedRect.angle;
                if (rotatedRect.size.width<rotatedRect.size.height){
                    angle +=90;
                }
                // Display the width next to the label
                String angleLabel = "Angle: " + (int) angle + "degrees";
                Imgproc.putText(input, angleLabel, new Point(cX-20, cY-20), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, -500), 2);
                String widthLabel = "Width: " + (int) width + " pixels";
                Imgproc.putText(input, widthLabel, new Point(cX + 10, cY - 150), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, -500), 2);
                String heightLabel = "height: " + (int) height + " pixels";
                Imgproc.putText(input, heightLabel, new Point(cX + 10, cY - 60), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, -200), 2);
                //Display the Distance
                String distanceLabel = "Distance: " + String.format("%.2f", getDistance(width)) + " inches";
                Imgproc.putText(input, distanceLabel, new Point(cX + 10, cY + 60), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0, 255, 500), 2);
                // Calculate the centroid of the largest contour
                Moments moments = Imgproc.moments(centeredContour);
                cX = moments.get_m10() / moments.get_m00();
                cY = moments.get_m01() / moments.get_m00();
                double shouldmovex = (cX - clawx)/120;
                double shouldmovey = (clawy-cY)/120;
                // Draw a dot at the centroid
                String label = "(" +  shouldmovex + ", " +  shouldmovey + ")";
                Imgproc.putText(input, label, new Point(cX + 10, cY), Imgproc.FONT_HERSHEY_COMPLEX, 2, new Scalar(255, 0, 0), 10);
                Imgproc.circle(input, new Point(cX, cY), 20, new Scalar(0, 255, 0), -1);

            }

            return input;
        }

        private Mat preprocessFrame(Mat frame) {
            Mat hsvFrame = new Mat();
            Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

            Scalar lowerYellow = new Scalar(60, 60, 230);
            Scalar upperYellow = new Scalar(140, 255, 255);

            Mat yellowMask = new Mat();
            Core.inRange(hsvFrame, lowerYellow, upperYellow, yellowMask);
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));
            Mat erodedYellow = new Mat();
            Imgproc.erode(yellowMask, erodedYellow, kernel);
            Imgproc.morphologyEx(erodedYellow, erodedYellow, Imgproc.MORPH_OPEN, kernel);
            //Imgproc.morphologyEx(yellowMask, yellowMask, Imgproc.MORPH_CLOSE, kernel);
            return erodedYellow;
        }

        private MatOfPoint findCenteredContour(List<MatOfPoint> contours) {
            double centercoordx = 960;
            double centercoordy = 540;
            MatOfPoint centerContour = null;
            double bestxdifference = 1920;
            double bestydifference = 1080;
            double bestaveragedifference =69420;

            for (MatOfPoint contour : contours) {
                Rect rect = Imgproc.boundingRect(contour);
                Moments moments = Imgproc.moments(contour);
                cX = moments.get_m10() / moments.get_m00();
                cY = moments.get_m01() / moments.get_m00();
                double coordx = cX;
                double coordy = cY;
                double xdifference = Math.abs(centercoordx - coordx);
                double ydifference = Math.abs(centercoordy-coordy);
                double averagedifference = (xdifference+ydifference)/2;
                if (averagedifference < bestaveragedifference) {
                    bestaveragedifference = averagedifference;
                    centerContour = contour;
                }
                telemetry.addData("difference", xdifference);
                telemetry.addData("average", averagedifference);
                telemetry.addData("bestaverage", bestaveragedifference);
                telemetry.addData("xvalue", cX);
                telemetry.update();
            }

            return centerContour;
        }
        private double calculateWidth(MatOfPoint contour) {
            Rect boundingRect = Imgproc.boundingRect(contour);
            return boundingRect.width;
        }
        private double calculateHeight(MatOfPoint contour){
            Rect boundingRect = Imgproc.boundingRect(contour);
            return boundingRect.height;
        }

    }
    private static double getDistance(double width){
        double distance = (objectWidthInRealWorldUnits * focalLength) / width;
        return distance;
    }


}