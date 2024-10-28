package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
@Disabled
@TeleOp(name="sleepy", group="Linear OpMode")
public class sleepy extends LinearOpMode {
    //Outtake Claw positions, CLOSED = 0.8, OPEN = 0.5, ALMOSTCLOSED = 0.7
    //Intake Claw positions, CLOSED = 0.825, OPEN = 0.525, ALMOSTCLOSED = 0.725
    //Intake Claw orientation (roll), SAMPLEVERTICAL = 0.504, SAMPLEROTATELEFT = 0.795, SAMPLEROTATERIGHT = 0.2125
    private Servo ViperSlidel;
    private Servo ViperSlider;
    private Servo HiTechl;
    private Servo HiTechr;
    private Servo Misumil;
    private Servo Misumir;
    private Servo FrontClawGrab;
    private Servo BackClawGrab;
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
        waitForStart();
        while (opModeIsActive()){
            ViperSlidel.setPosition(-gamepad1.right_stick_y/2+0.5);
            ViperSlider.setPosition(gamepad1.right_stick_y/2+0.5);
            Misumil.setPosition(-gamepad2.right_stick_y/2+0.5);
            Misumir.setPosition(gamepad2.right_stick_y/2+0.5);
            if (gamepad1.left_stick_y > 0) {
                HiTechl.setPosition(0.5135);
                HiTechr.setPosition(0.4928);
            } else {
                HiTechl.setPosition(-gamepad1.left_stick_y * 0.032 + 0.5135);
                HiTechr.setPosition(gamepad1.left_stick_y*0.032+0.4928);
            }
            if (gamepad2.square){
                FrontClawGrab.setPosition(0.525);
            }
            if (gamepad2.triangle){
                FrontClawGrab.setPosition(0.75);
            }
            if (gamepad2.circle){
                FrontClawGrab.setPosition(0.9);
            }
            if (gamepad1.square){
                BackClawGrab.setPosition(0.5);
            }
            if (gamepad1.triangle){
                BackClawGrab.setPosition(0.7);
            }
            if (gamepad1.circle){
                BackClawGrab.setPosition(0.85);
            }
            telemetry.addData("ViperSlidelPos", ViperSlidel.getPosition());
            telemetry.addData("ViperSliderPos", ViperSlider.getPosition());
            telemetry.addData("HiTechlPos", HiTechl.getPosition());
            telemetry.addData("HiTechrPos", HiTechr.getPosition());
            telemetry.addData("LeftStickY", gamepad1.left_stick_y);
            telemetry.update();
        }
    }
}
