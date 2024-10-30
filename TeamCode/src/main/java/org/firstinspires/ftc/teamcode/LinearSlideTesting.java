package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

// TODO:
// 1. extrat all controllable variables into a class
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
        public static double speedCoefficient = 0.5;

    }

    enum ClawMode {
        NONE, GRAB, TRANSIT, OUTTAKE
    }

    double GyroYawDisplay;
    double GyroYaw;
    double MecanumTheta;
    double MecanumPower;
    double MecanumSin;
    double MecanumCos;
    double MecanumMax;
    double LeftFrontDrivePower;
    double RightFrontDrivePower;
    double LeftBackDrivePower;
    double RightBackDrivePower;
    double TransitTime;
    double OuttakeTime;
    double WaitForBackClawDrop;
    double WaitForBackClawReturn;
    double WaitForBackClawFront;
    double Gamepad2LeftY;
    double Gamepad2RightY;
    double GamepadRightY;
    double GamepadLeftY;
    double GamepadLeftX;
    double GamepadRightX;
    double Gamepad2RightX;
    double LinearSlideBackLeftCurrentPosition = 0.5135;
    double LinearSlideBackRightCurrentPostion = 0.4928;
    double ViperSlideLeftCurrentPosition = 0.5;
    double ViperSlideRightCurrentPosition = 0.5;
    double MisumiLeftCurrentPosition = 1;
    double MisumiRightCurrentPosition = 0;
    int SlideLeftCurrentPosition = 0;
    int SlideRightCurrentPosition = 0;
    double FrontClawRollPosition = 0.5;
    int FrontClawCycle = 1;
    int BackClawCycle = 1;
    boolean HorizontalSlideControl = false;
    boolean OverrideMode = false;
    boolean Gamepad2DpadUp = false;
    boolean Gamepad2Cross = false;
    boolean Gamepad2Square = false;
    ClawMode currentClawMode = ClawMode.NONE;
    private final Servo LinearSlideBackLeft;
    private final Servo LinearSlideBackRight;
    private final Servo viperSlideLeft;
    private final Servo viperSlideRight;
    private final Servo Misumil;
    private final Servo Misumir;
    private final Servo FrontClawGrab;
    private final Servo FrontClawRoll;
    private final Servo BackClawGrab;
    private final DcMotorEx SlideMotorl;
    private final DcMotorEx SlideMotorr;
    private final DcMotor driveMotorFrontLeft;
    private final DcMotor driveMotorFrontRight;
    private final DcMotor driveMotorBackLeft;
    private final DcMotor driveMotorBackRight;
    private final IMU imu;

    volatile boolean transitCompleted;


    Thread transitThread;
    public LinearSlideTesting() {
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
        driveMotorFrontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        driveMotorFrontRight = hardwareMap.get(DcMotor.class, "rightFront");
        driveMotorBackLeft = hardwareMap.get(DcMotor.class, "leftBack");
        driveMotorBackRight = hardwareMap.get(DcMotor.class, "rightBack");
        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.RIGHT, RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD));

        imu.initialize(parameters);
        imu.resetYaw();
        driveMotorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        SlideMotorl.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorl.setMode((DcMotor.RunMode.RUN_USING_ENCODER));
        SlideMotorl.setDirection(DcMotorSimple.Direction.REVERSE);
        SlideMotorr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorr.setMode((DcMotor.RunMode.RUN_USING_ENCODER));
    }

    /**
     * Main entrance point for the running mode.
     * The code will execute a {@code while} loop to respond to gamepad operations until the OpMode
     * is stopped.
     */
    @Override
    public void runOpMode() {
        waitForStart();
        while (opModeIsActive()) {

            CalculateMotorValues();
            CalculateClawValues();

            // TODO: can we calculate and update motor and claw states separately?
            UpdateComponentStates();

            LogTelemetryData();
        }
    }

    private void UpdateComponentStates() {
        FrontClawRoll.setPosition(FrontClawRollPosition);
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


        driveMotorFrontLeft.setPower(LeftFrontDrivePower);
        driveMotorFrontRight.setPower(RightFrontDrivePower);
        driveMotorBackLeft.setPower(LeftBackDrivePower);
        driveMotorBackRight.setPower(RightBackDrivePower);
    }

    private void LogTelemetryData() {
        telemetry.addData("Current Mode", currentClawMode);
        telemetry.addData("IMU", GyroYawDisplay);
        telemetry.addData("Speed Coefficient", Properties.speedCoefficient);
        telemetry.addData("LeftFrontPower", LeftFrontDrivePower);
        telemetry.addData("RightFrontPower", RightFrontDrivePower);
        telemetry.addData("LeftBackPower", LeftBackDrivePower);
        telemetry.addData("RightBackPower", RightBackDrivePower);
        telemetry.addData("theta", MecanumTheta);
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
        LinearSlideBackLeftCurrentPosition = Math.max(Math.min(LinearSlideBackLeftCurrentPosition, 0.5455), 0.5135);
        LinearSlideBackRightCurrentPostion = Math.max(Math.min(LinearSlideBackRightCurrentPostion, 0.4928), 0.4628);


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
        if (gamepad1.circle) {
            imu.resetYaw();
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
                        BackClawCycle += 1;
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

    /**
     * Set the motion of the robot based on the joystick on gamepad 1
     */
    private void CalculateMotorValues() {
        GamepadLeftY = gamepad1.left_stick_y;
        GamepadLeftX = gamepad1.left_stick_x;
        GamepadRightY = gamepad1.right_stick_y;
        GamepadRightX = gamepad1.right_stick_x;

        GyroYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        GyroYawDisplay = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);


        MecanumTheta = Math.atan2(-GamepadLeftY, GamepadLeftX);
        MecanumTheta -= GyroYaw;
        MecanumPower = Math.hypot(-GamepadLeftY, GamepadLeftX);
        MecanumSin = Math.sin(MecanumTheta - Math.PI / 4);
        MecanumCos = Math.cos(MecanumTheta - Math.PI / 4);
        MecanumMax = Math.max(Math.abs(MecanumSin), Math.abs(MecanumCos));


        LeftFrontDrivePower = (MecanumPower * MecanumCos / MecanumMax + GamepadRightX) * Properties.speedCoefficient;
        RightFrontDrivePower = (MecanumPower * MecanumSin / MecanumMax - GamepadRightX) * Properties.speedCoefficient;
        LeftBackDrivePower = (MecanumPower * MecanumSin / MecanumMax + GamepadRightX) * Properties.speedCoefficient;
        RightBackDrivePower = (MecanumPower * MecanumCos / MecanumMax - GamepadRightX) * Properties.speedCoefficient;


        double maxPower = Math.max(Math.max(Math.abs(LeftFrontDrivePower), Math.abs(RightFrontDrivePower)), Math.max(Math.abs(LeftBackDrivePower), Math.abs(RightBackDrivePower)));
        if (maxPower > 1) {
            // Normalize the motor power values
            LeftFrontDrivePower /= maxPower;
            RightFrontDrivePower /= maxPower;
            LeftBackDrivePower /= maxPower;
            RightBackDrivePower /= maxPower;
        }

        // TODO: what is this for? Turbo mode?
        if (gamepad1.left_trigger == 1) {
            Properties.speedCoefficient = 1.0;
        } else {
            Properties.speedCoefficient = 0.5;
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
        // do something
        Thread.sleep(1000);
        // do something
        Thread.sleep(1000);
        // TODO: add the actual operations here
//        TransitTime += 50;
//        if (TransitTime == 1000) {
//            BackClawCycle = 1;
//            FrontClawRollPosition = 0.835;
//        }
//        if (TransitTime == 3000) {
//            MisumiRightCurrentPosition = 0.33;
//            MisumiLeftCurrentPosition = 0.67;
//        }
//        if (TransitTime == 5000) {
//            MisumiLeftCurrentPosition = 0.0355;
//            MisumiRightCurrentPosition = 0.9645;
//            FrontClawRollPosition = 0.5;
//        }
//        if (TransitTime == 7000) {
//            LinearSlideBackLeftCurrentPosition = 0.5193;
//            LinearSlideBackRightCurrentPostion = 0.4881;
//        }
//        if (TransitTime == 9000) {
//            ViperSlideLeftCurrentPosition = 0.8699;
//            ViperSlideRightCurrentPosition = 0.1301;
//        }
//        if (TransitTime == 11000) {
//            BackClawCycle = 3;
//        }
//        if (TransitTime == 13000) {
//            FrontClawCycle = 1;
//        }
//        if (TransitTime == 14500) {
//            OuttakeMode();
//        }


        transitCompleted = true;

    }

}


