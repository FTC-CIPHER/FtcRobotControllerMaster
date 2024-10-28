package org.firstinspires.ftc.teamcode;


import android.app.ActionBar;


import androidx.annotation.NonNull;


import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


@Config
@Autonomous(name = "RightRedAuto", group = "Autonomous")
public class RightRedAuto extends LinearOpMode {
    public class HorizontalSlides{
        private Servo HiTechl;
        private Servo HiTechr;
        public HorizontalSlides(HardwareMap hardwareMap) {
            HiTechl = hardwareMap.get(Servo.class, "HiTechl");
            HiTechr = hardwareMap.get(Servo.class, "HiTechr");
        }
        public class TransferHorizontal implements Action {
            private boolean initialized = false;
            @Override
            public boolean run(@NonNull TelemetryPacket packet){
                HiTechl.setPosition(0.5193);
                HiTechr.setPosition(0.4881);
                //sleep(2000);
                int delay = 2000; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action TransferHorizontal() {
            return new TransferHorizontal();
        }
    }
    public class Lift {
        private DcMotorEx SlideMotorl;
        private DcMotorEx SlideMotorr;
        public Lift(HardwareMap hardwareMap) {
            SlideMotorl = hardwareMap.get(DcMotorEx.class, "SlideMotorl");
            SlideMotorr = hardwareMap.get(DcMotorEx.class, "SlideMotorr");
            SlideMotorl.setDirection(DcMotorEx.Direction.REVERSE);
            SlideMotorr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            SlideMotorl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
            SlideMotorl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            SlideMotorr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        }
        public class LiftUp implements Action {
            private boolean initialized = false;


            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //if (!initialized) {
                //    SlideMotorl.setPower(-1);
                //    SlideMotorr.setPower(1);
                //    initialized = true;
                //}
                double posl = SlideMotorl.getCurrentPosition();
                double posr = SlideMotorr.getCurrentPosition();
                packet.put("leftliftPos", posl);
                packet.put("rightliftPos", posr);
                //TO BE CHANGED (2800):
                //if (posr < 20 && posl > -20) {
                SlideMotorl.setTargetPosition(2800);
                SlideMotorr.setTargetPosition(2800);
                SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorl.setPower(1);
                SlideMotorr.setPower(1);
                //sleep(2000);
                int delay = 2000; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
                //} else {
                //    SlideMotorl.setPower(0);
                //   SlideMotorr.setPower(0);
                //    return false;
                //}
            }
        }


        public Action LiftUp() {
            return new LiftUp();
        }


        public class LiftDown implements Action {
            private boolean initialized = false;


            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //if (!initialized) {
                //    SlideMotorl.setPower(1);
                //    SlideMotorr.setPower(-1);
                //    initialized = true;
                //}
                double posl = SlideMotorl.getCurrentPosition();
                double posr = SlideMotorr.getCurrentPosition();
                packet.put("leftliftPos", posl);
                packet.put("rightliftPos", posr);
                //if (posr > 10.0 && posl < -10.0) {
                SlideMotorl.setTargetPosition(10);
                SlideMotorr.setTargetPosition(10);
                SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorl.setPower(-1);
                SlideMotorr.setPower(-1);
                int delay = 2000; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
                // } else {
                //    SlideMotorl.setPower(0);
                //    SlideMotorr.setPower(0);
                //    return false;
                //}
            }
        }


        public Action LiftDown() {
            return new LiftDown();
        }
    }
    public class ClawFront{
        private Servo FrontClawGrab;
        public ClawFront(HardwareMap hardwareMap){
            FrontClawGrab = hardwareMap.get(Servo.class, "FrontClawGrab");
        }
        public class FrontClawOpen implements Action{
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                FrontClawGrab.setPosition(0.525);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action FrontClawOpen() {
            return new ClawFront.FrontClawOpen();
        }
        public class FrontClawClose implements Action{
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                FrontClawGrab.setPosition(0.9);
                return false;
            }
        }
        public Action FrontClawClose() {
            return new ClawFront.FrontClawClose();
        }
    }
    public class ClawRear {
        private Servo BackClawGrab;
        public ClawRear(HardwareMap hardwareMap) {
            BackClawGrab = hardwareMap.get(Servo.class, "BackClawGrab");
        }
        public class RearClawOpen implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                BackClawGrab.setPosition(0.5);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action RearClawOpen() {
            return new RearClawOpen();
        }
        public class RearClawClose implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                BackClawGrab.setPosition(0.8);
                //sleep(1000);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action RearClawClose() {
            return new RearClawClose();
        }
    }
    public class BackRotate {
        private Servo ViperSlidel;
        private Servo ViperSlider;
        public Servo BackClawGrab;
        public BackRotate(HardwareMap hardwareMap){
            ViperSlidel = hardwareMap.get(Servo.class, "ViperSlidel");
            ViperSlider = hardwareMap.get(Servo.class, "ViperSlider");
            BackClawGrab = hardwareMap.get(Servo.class, "BackClawGrab");
        }
        public class BackRotateTransfer implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                ViperSlidel.setPosition(0.8699);
                ViperSlider.setPosition(0.1301);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action BackRotateTransfer(){
            return new BackRotateTransfer();
        }
        public class ToDrop implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //TO BE CHANGED:
                ViperSlidel.setPosition(0.2);
                ViperSlider.setPosition(0.8);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action ToDrop(){
            return new ToDrop();
        }
        public class Vertical implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                //TO BE CHANGED:
                ViperSlidel.setPosition(0.5);
                ViperSlider.setPosition(0.5);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action Vertical(){
            return new Vertical();
        }
    }
    public class FrontRotate {
        private Servo Misumil;
        private Servo Misumir;
        private Servo FrontClawGrab;
        public FrontRotate(HardwareMap hardwareMap){
            Misumil = hardwareMap.get(Servo.class, "Misumil");
            Misumir = hardwareMap.get(Servo.class, "Misumir");
            FrontClawGrab = hardwareMap.get(Servo.class, "FrontClawGrab");
        }
        public class FrontRotateTransfer implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                FrontClawGrab.setPosition(0.9);
                Misumil.setPosition(0.0355);
                Misumir.setPosition(0.9645);
                //sleep(500);
                int delay = 500; // number of milliseconds to sleep
                long start = System.currentTimeMillis();
                while(start >= System.currentTimeMillis() - delay);
                return false;
            }
        }
        public Action FrontRotateTransfer(){
            return new FrontRotateTransfer();
        }
    }
    @Override
    public void runOpMode(){
        ClawRear BackClawGrab = new ClawRear(hardwareMap);
        Lift lift = new Lift(hardwareMap);
        //Lift SlideMotorr = new Lift(hardwareMap);
        ClawFront FrontClawGrab = new ClawFront(hardwareMap);
        HorizontalSlides horizontalSlides = new HorizontalSlides(hardwareMap);
        //HorizontalSlides HiTechr = new HorizontalSlides(hardwareMap);
        FrontRotate frontRotate = new FrontRotate(hardwareMap);
        //FrontRotate Misumir = new FrontRotate(hardwareMap);
        BackRotate backRotate = new BackRotate(hardwareMap);
        //BackRotate ViperSlider = new BackRotate(hardwareMap);
        Pose2d initialPose = new Pose2d(12, -63, Math.toRadians(90));
        Pose2d secondpose = new Pose2d(12, -40, Math.toRadians(90));
        Pose2d thirdpose = new Pose2d(-55, -55, Math.toRadians(-135));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        TrajectoryActionBuilder tab1 = drive.actionBuilder(secondpose);
        TrajectoryActionBuilder tab2 = drive.actionBuilder(thirdpose);
        TrajectoryActionBuilder tab3 = drive.actionBuilder(initialPose);
        Action leftToNet = tab1.fresh()
                //.forward(12)
                //.forward(15)
                //.strafeTo(new Vector2d(12, -40))
                .turn(Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(-40,-40))
                //new VelConstraint(20.0)
                //.lineToX(-46)
                .turn(Math.toRadians(-135))
                .strafeToConstantHeading(new Vector2d(-55, -55))
                .build();

        Action toPark = tab2.fresh()
                .strafeToConstantHeading(new Vector2d(-40, -40))
                .turn(Math.toRadians(-54))
                .strafeToConstantHeading(new Vector2d(45, -40))
                .strafeToConstantHeading(new Vector2d(45, -52))
                //.lineToX(49)
                .build();
        Action forward = tab3.fresh()
                .strafeToConstantHeading(new Vector2d(12, -40))
                //.lineToY(-40)
                .build();
        waitForStart();
        //sleep(2000);
        int delay = 2000; // number of milliseconds to sleep
        long start = System.currentTimeMillis();
        while(start >= System.currentTimeMillis() - delay);
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        forward,
                        FrontClawGrab.FrontClawClose(),
                        BackClawGrab.RearClawOpen(),
                        FrontClawGrab.FrontClawClose(),
                        horizontalSlides.TransferHorizontal(),
                        backRotate.BackRotateTransfer(),
                        frontRotate.FrontRotateTransfer(),
                        //.wait(2),
                        //TimeUnit.SECONDS.sleep(2.0),
                        BackClawGrab.RearClawClose(),
                        //TimeUnit.SECONDS.sleep(0.5),
                        FrontClawGrab.FrontClawOpen(),
                        //TimeUnit.SECONDS.sleep(0.5),
                        backRotate.Vertical(),
                        BackClawGrab.RearClawClose(),
                        leftToNet,
                        lift.LiftUp(),
                        backRotate.ToDrop(),
                        //TimeUnit.SECONDS.sleep(0.5),
                        BackClawGrab.RearClawOpen(),
                        //TimeUnit.SECONDS.sleep(0.5),
                        backRotate.Vertical(),
                        //TimeUnit.SECONDS.sleep(0.5),
                        lift.LiftDown(),
                        toPark
                )
        );
    }
}



