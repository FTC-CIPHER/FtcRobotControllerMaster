package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp(name="Buttons", group="Linear OpMode")
public class Buttons extends LinearOpMode {
    private Servo ViperSlidel;
    private Servo ViperSlider;
    private Servo HiTechl;
    private Servo HiTechr;
    private Servo Misumil;
    private Servo Misumir;
    private Servo FrontClawGrab;
    private Servo BackClawGrab;
    private Servo ClawRoll;
    private DcMotorEx SlideMotorl;
    private DcMotorEx SlideMotorr;
    @Override
    public void runOpMode(){
        ViperSlidel = hardwareMap.get(Servo.class, "ViperSlidel");
        ViperSlider = hardwareMap.get(Servo.class, "ViperSlider");
        HiTechl = hardwareMap.get(Servo.class, "HiTechl");
        HiTechr = hardwareMap.get(Servo.class, "HiTechr");
        FrontClawGrab = hardwareMap.get(Servo.class, "FrontClawGrab");
        Misumil = hardwareMap.get(Servo.class, "Misumil");
        Misumir = hardwareMap.get(Servo.class, "Misumir");
        BackClawGrab = hardwareMap.get(Servo.class, "BackClawGrab");
        ClawRoll = hardwareMap.get(Servo.class, "ClawRoll");
        SlideMotorl = hardwareMap.get(DcMotorEx.class, "SlideMotorl");
        SlideMotorr = hardwareMap.get(DcMotorEx.class, "SlideMotorr");
        SlideMotorl.setDirection(DcMotorEx.Direction.REVERSE);
        SlideMotorr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        SlideMotorl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        SlideMotorl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            //HighBasket
            double posl = SlideMotorl.getCurrentPosition();
            double posr = SlideMotorr.getCurrentPosition();
            if (gamepad2.dpad_up){
                //packet.put("leftliftPos", posl);
                //packet.put("rightliftPos", posr);
                //TO BE CHANGED (2800):
                SlideMotorl.setTargetPosition(2800);
                SlideMotorr.setTargetPosition(2800);
                SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorl.setPower(1);
                SlideMotorr.setPower(1);
            }
            //DOWN
            if (gamepad2.dpad_right){
                SlideMotorl.setTargetPosition(20);
                SlideMotorr.setTargetPosition(20);
                SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                SlideMotorl.setPower(-1);
                SlideMotorr.setPower(-1);
            }
            //TO DROP
            if (gamepad2.dpad_left){
            }
            telemetry.addData("MotorLeft", SlideMotorl.getCurrentPosition());
            telemetry.addData("MotorLeftPos", posl);
            telemetry.addData("MotorRight", SlideMotorr.getCurrentPosition());
            telemetry.update();
        }
    }
}
