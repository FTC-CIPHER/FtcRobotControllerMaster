package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

// TODO:
// 1. extract all controllable variables into a class
// 2. use a clock to time operations instead of fixed number of iterations
// 3. fix servo stuck issue
// 4. add unit tests
// 5. update naming conventions.
@TeleOp(name = "LinearSlideTesting")
public class LinearSlideTesting extends LinearOpMode {
    // Naming conventions:
    // 1. local variables: camelCase.
    static class Properties {
        // The global coefficient for motor speed.
        // Modify this value will change the speed of all motors in the robot.
        // TODO: use two gamepad keys to increase/decrease speedCoefficient?
        public static double normalDriversSpeedCoefficient = 0.5;
        public static double turboDriversSpeedCoefficient = 1.0;

        // The global coefficient for claw transit mode speed.
        public static double clawTransitSpeedFactor = 1.0;

    }

    enum ClawMode {
        NONE, GRAB, TRANSIT, OUTTAKE
    }

    double TransitTime;
    double OuttakeTime;
    double WaitForBackClawDrop;
    double WaitForBackClawReturn;
    double WaitForBackClawFront;
    double Gamepad2LeftY;
    double Gamepad2RightY;
    double Gamepad2RightX;
    double LinearSlideBackLeftCurrentPosition = 0.5135;
    double LinearSlideBackRightCurrentPostion = 0.4928;
    double ViperSlideLeftCurrentPosition = 0.5;
    double ViperSlideRightCurrentPosition = 0.5;
    double MisumiLeftCurrentPosition = 1;
    double MisumiRightCurrentPosition = 0;
    int SlideLeftCurrentPosition = 0;
    int SlideRightCurrentPosition = 0;
    volatile double FrontClawRollPosition = 0.5;
    volatile int FrontClawCycle = 1;
    volatile int BackClawCycle = 1;
    volatile boolean HorizontalSlideControl = false;
    boolean OverrideMode = false;
    boolean Gamepad2DpadUp = false;
    boolean Gamepad2Cross = false;
    boolean Gamepad2Square = false;
    ClawMode currentClawMode = ClawMode.NONE;
    private Servo LinearSlideBackLeft;
    private Servo LinearSlideBackRight;
    private Servo viperSlideLeft;
    private Servo viperSlideRight;
    private Servo Misumil;
    private Servo Misumir;
    private Servo FrontClawGrab;
    private Servo FrontClawRoll;
    private Servo BackClawGrab;
    private DcMotorEx SlideMotorl;
    private DcMotorEx SlideMotorr;

    volatile boolean transitCompleted;

    private Drivers drivers;

    Thread transitThread;

    public LinearSlideTesting() {

        setupDrivers();

        LinearSlideBackLeft = hardwareMap.get(Servo.class, "HiTechl");
        LinearSlideBackRight = hardwareMap.get(Servo.class, "HiTechr");
        viperSlideLeft = hardwareMap.get(Servo.class, "ViperSlidel");
        viperSlideRight = hardwareMap.get(Servo.class, "ViperSlider");
        Misumil = hardwareMap.get(Servo.class, "Misumil");
        Misumir = hardwareMap.get(Servo.class, "Misumir");
        SlideMotorl = hardwareMap.get(DcMotorEx.class, "SlideMotorl");
        SlideMotorr = hardwareMap.get(DcMotorEx.class, "SlideMotorr");
        FrontClawGrab = hardwareMap.get(Servo.class, "FrontClawGrab");
        FrontClawRoll = hardwareMap.get(Servo.class, "ClawRoll");
        BackClawGrab = hardwareMap.get(Servo.class, "BackClawGrab");
        SlideMotorl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorl.setMode((DcMotor.RunMode.RUN_USING_ENCODER));
        SlideMotorl.setDirection(DcMotorSimple.Direction.REVERSE);
        SlideMotorr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorr.setMode((DcMotor.RunMode.RUN_USING_ENCODER));

    }

    private void setupDrivers() {
        IMU imu = hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                                             RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));
        imu.initialize(parameters);
        imu.resetYaw();

        DcMotor driveMotorFrontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        DcMotor driveMotorFrontRight = hardwareMap.get(DcMotor.class, "rightFront");
        DcMotor driveMotorBackLeft = hardwareMap.get(DcMotor.class, "leftBack");
        DcMotor driveMotorBackRight = hardwareMap.get(DcMotor.class, "rightBack");

        driveMotorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        drivers = new Drivers(driveMotorFrontLeft, driveMotorFrontRight, driveMotorBackLeft,
                              driveMotorBackRight, gamepad1, imu, telemetry);
        drivers.setSpeedCoefficients(Properties.normalDriversSpeedCoefficient,
                                     Properties.turboDriversSpeedCoefficient);
    }

    /**
     * Main entrance point for the running mode.
     * The code will execute a {@code while} loop to respond to gamepad operations until the OpMode
     * is stopped.
     */
    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        while (opModeIsActive()) {

            drivers.update();

            // Update and log claw states
            // TODO: extract claw to a separate class similar to drivers.
            CalculateClawValues();
            UpdateComponentStates();
            LogTelemetryData();

            // TODO: any event we can listen to to avoid a while loop and busy waiting?
            Thread.sleep(1);  // call sleep to reduce CPU load.
        }
    }

    private void UpdateComponentStates() {
        if (FrontClawRoll.getPosition() != FrontClawRollPosition) {
            FrontClawRoll.setPosition(FrontClawRollPosition);
        }
        // TODO: Check position is diff first before setPosition, similar to above.
        //       Depending on the actual implementation of setPosition, this could
        //       result in great performance improvements.
        LinearSlideBackRight.setPosition(LinearSlideBackRightCurrentPostion);
        LinearSlideBackLeft.setPosition(LinearSlideBackLeftCurrentPosition);
        SlideMotorl.setTargetPosition(SlideLeftCurrentPosition);
        SlideMotorr.setTargetPosition(SlideRightCurrentPosition);
        viperSlideLeft.setPosition(ViperSlideLeftCurrentPosition);
        viperSlideRight.setPosition(ViperSlideRightCurrentPosition);
        Misumir.setPosition(MisumiRightCurrentPosition);
        Misumil.setPosition(MisumiLeftCurrentPosition);
        SlideMotorl.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SlideMotorr.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        SlideMotorl.setPower(1);
        SlideMotorr.setPower(1);
    }

    private void LogTelemetryData() {
        telemetry.addData("Current Mode", currentClawMode);
        telemetry.addData("HSlideLeft", LinearSlideBackLeft.getPosition());
        telemetry.addData("HSlideRight", LinearSlideBackRight.getPosition());
        telemetry.addData("VSlideLeft", SlideLeftCurrentPosition);
        telemetry.addData("VSlideRight", SlideRightCurrentPosition);
        telemetry.addData("FrontClawLeft", Misumil.getPosition());
        telemetry.addData("FrontClawRight", Misumir.getPosition());
        telemetry.addData("BackClawLeft", viperSlideLeft.getPosition());
        telemetry.addData("BackClawRight", viperSlideRight.getPosition());
        telemetry.addData("Front Claw Mode", FrontClawCycle);
        telemetry.addData("Back Claw Mode", BackClawCycle);
        telemetry.addData("Rotation Claw", FrontClawRoll.getPosition());
        telemetry.addData("Transit Time", TransitTime);
        telemetry.update();
    }

    private void CalculateClawValues() {
        Gamepad2LeftY = gamepad2.left_stick_y;
        Gamepad2RightY = gamepad2.right_stick_y;
        Gamepad2RightX = gamepad2.right_stick_x;

        if (BackClawCycle == 1) {
            BackClawGrab.setPosition(0.525);
        } else {
            if (BackClawCycle == 2) {
                BackClawGrab.setPosition(0.75);
            } else {
                if (BackClawCycle == 3) {
                    BackClawGrab.setPosition(0.9);
                } else {
                    BackClawCycle = 1;
                }
            }
        }
        if (FrontClawCycle == 1) {
            FrontClawGrab.setPosition(0.525);
        } else {
            if (FrontClawCycle == 2) {
                FrontClawGrab.setPosition(0.75);
            } else {
                if (FrontClawCycle == 3) {
                    FrontClawGrab.setPosition(0.9);
                } else {
                    FrontClawCycle = 1;
                }
            }
        }


        if (HorizontalSlideControl) {
            LinearSlideBackLeftCurrentPosition += (-Gamepad2LeftY * 0.0005);
            LinearSlideBackRightCurrentPostion += (Gamepad2LeftY * 0.0005);
            MisumiLeftCurrentPosition += (Gamepad2RightY * 0.001);
            MisumiRightCurrentPosition += (-Gamepad2RightY * 0.001);
            FrontClawRollPosition += (Gamepad2RightX * 0.006);
        }
        FrontClawRollPosition = Math.max(Math.min(FrontClawRollPosition, 1), 0);
        MisumiLeftCurrentPosition = Math.max(Math.min(MisumiLeftCurrentPosition, 1), 0);
        MisumiRightCurrentPosition = Math.max(Math.min(MisumiRightCurrentPosition, 1), 0);
        LinearSlideBackLeftCurrentPosition =
                Math.max(Math.min(LinearSlideBackLeftCurrentPosition, 0.5455), 0.5135);
        LinearSlideBackRightCurrentPostion =
                Math.max(Math.min(LinearSlideBackRightCurrentPostion, 0.4928), 0.4628);


        if (gamepad2.dpad_up) {
            if (!Gamepad2DpadUp) {
                if (OverrideMode) {
                    OverrideMode = false;
                    SlideMotorr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    SlideMotorl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    SlideLeftCurrentPosition = 0;
                    SlideRightCurrentPosition = 0;
                } else {
                    OverrideMode = true;
                }
            }
            Gamepad2DpadUp = true;
        } else {
            Gamepad2DpadUp = false;
        }


        if (gamepad2.square) {
            if (!Gamepad2Square) {
                TransitMode();
            }
            Gamepad2Square = true;
        } else {
            Gamepad2Square = false;
        }


        if (gamepad2.triangle) {
            GrabMode();
        }
        if (gamepad2.circle) {
            OuttakeMode();
        }

        switch (currentClawMode) {
            case GRAB:
                if (gamepad2.cross) {
                    if (!Gamepad2Cross) {
                        FrontClawCycle += 1;
                    }
                    Gamepad2Cross = true;
                } else {
                    Gamepad2Cross = false;
                }
                break;
            case TRANSIT:
                if (transitThread == null) {
                    transitThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                transit();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                // TODO: RESET claw states
                                transitCompleted = true;
                            }
                        }
                    });
                    transitThread.start();
                }
                if (transitCompleted) {
                    // The thread shall be completed and in terminated state at this point
                    // If we made any mistake in the code and the thread is actually still running,
                    // consider interrupting it before removing the reference to it.
                    transitThread = null;
                }
                break;
            case OUTTAKE:
                OuttakeTime += 50;
                if (OuttakeTime == 3000) {
                    LinearSlideBackLeftCurrentPosition = 0.5135;
                    LinearSlideBackRightCurrentPostion = 0.4928;
                }
                if ((WaitForBackClawDrop != 0) && (OuttakeTime == WaitForBackClawDrop)) {
                    BackClawCycle = 1;
                }
                if (WaitForBackClawFront != 0 && OuttakeTime == WaitForBackClawFront) {
                    ViperSlideLeftCurrentPosition = 0.5;
                    ViperSlideRightCurrentPosition = 0.5;
                }
                if (WaitForBackClawReturn != 0 && OuttakeTime == WaitForBackClawReturn) {
                    SlideRightCurrentPosition = -100;
                    SlideLeftCurrentPosition = -100;
                    GrabMode();
                }
                if (gamepad2.cross) {
                    if (!Gamepad2Cross) {
                        synchronized (this) {
                            BackClawCycle += 1;
                        }
                    }
                    Gamepad2Cross = true;
                } else {
                    Gamepad2Cross = false;
                }
                if (gamepad2.right_stick_button) {
                    WaitForBackClawDrop = OuttakeTime + 1000;
                    WaitForBackClawFront = OuttakeTime + 2000;
                    WaitForBackClawReturn = OuttakeTime + 3000;
                    ViperSlideLeftCurrentPosition = 0.175;
                    ViperSlideRightCurrentPosition = 0.825;
                }
                SlideLeftCurrentPosition += (Math.round(-Gamepad2LeftY) * 50);
                SlideRightCurrentPosition += (Math.round(-Gamepad2LeftY) * 50);
                ViperSlideLeftCurrentPosition += (-Gamepad2RightY * 0.006);
                ViperSlideRightCurrentPosition += (Gamepad2RightY * 0.006);
                break;
            default:
        }
        ViperSlideLeftCurrentPosition = Math.max(Math.min(ViperSlideLeftCurrentPosition, 1), 0);
        ViperSlideRightCurrentPosition = Math.max(Math.min(ViperSlideRightCurrentPosition, 1), 0);
        if (OverrideMode) {
            SlideLeftCurrentPosition = (Math.min(SlideLeftCurrentPosition, 2800));
            SlideRightCurrentPosition = (Math.min(SlideRightCurrentPosition, 2800));
        } else {
            SlideLeftCurrentPosition = Math.max(Math.min(SlideLeftCurrentPosition, 2800), 0);
            SlideRightCurrentPosition = Math.max(Math.min(SlideRightCurrentPosition, 2800), 0);
        }
    }

    public void GrabMode() {
        currentClawMode = ClawMode.GRAB;
        HorizontalSlideControl = true;
        FrontClawCycle = 1;
        BackClawCycle = 3;
        MisumiLeftCurrentPosition = 1;
        MisumiRightCurrentPosition = 0;
        ViperSlideLeftCurrentPosition = 0.5;
        ViperSlideRightCurrentPosition = 0.5;
        FrontClawRollPosition = 0.5;
    }


    public void TransitMode() {
        currentClawMode = ClawMode.TRANSIT;
        HorizontalSlideControl = false;
        FrontClawCycle = 3;
        BackClawCycle = 2;
        TransitTime = 0;
    }

    public void OuttakeMode() {
        currentClawMode = ClawMode.OUTTAKE;
        HorizontalSlideControl = false;
        FrontClawCycle = 1;
        BackClawCycle = 3;
        MisumiLeftCurrentPosition = 1;
        MisumiRightCurrentPosition = 0;
        ViperSlideLeftCurrentPosition = 0.5;
        ViperSlideRightCurrentPosition = 0.5;
        WaitForBackClawDrop = 0;
        WaitForBackClawReturn = 0;
        OuttakeTime = 0;
    }

    void transit() throws InterruptedException {
        transitCompleted = false;
        Thread.sleep((long) (1000 * Properties.clawTransitSpeedFactor));
        BackClawCycle = 1;
        FrontClawRollPosition = 0.835;

        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));

        MisumiRightCurrentPosition = 0.33;
        MisumiLeftCurrentPosition = 0.67;
        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));

        MisumiLeftCurrentPosition = 0.0355;
        MisumiRightCurrentPosition = 0.9645;
        FrontClawRollPosition = 0.5;

        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));
        LinearSlideBackLeftCurrentPosition = 0.5193;
        LinearSlideBackRightCurrentPostion = 0.4881;

        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));
        ViperSlideLeftCurrentPosition = 0.8699;
        ViperSlideRightCurrentPosition = 0.1301;
        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));
        BackClawCycle = 3;
        Thread.sleep((long) (2000 * Properties.clawTransitSpeedFactor));
        FrontClawCycle = 1;
        Thread.sleep((long) (1500 * Properties.clawTransitSpeedFactor));
        OuttakeMode();
        transitCompleted = true;

    }

}


