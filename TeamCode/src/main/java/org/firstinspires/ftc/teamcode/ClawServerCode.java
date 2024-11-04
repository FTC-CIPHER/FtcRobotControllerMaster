package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ArmTest2")
public class ClawServerCode extends LinearOpMode{

    private Servo HiTechr;
    private Servo HiTechl;

    @Override
    public void runOpMode() {

        HiTechl = hardwareMap.get(Servo.class, "HiTechl");
        HiTechr = hardwareMap.get(Servo.class, "HiTechr");
        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.square){
                HiTechr.setPosition(0.5);
            }
            if(gamepad1.circle){
                HiTechl.setPosition(0.5);
            }
            telemetry.addData("Hitechr", HiTechr.getPosition());
            telemetry.addData("Hitechl", HiTechl.getPosition());
            telemetry.update();
            /*
            BottomMotorTheta += (int) Math.round(ThetaChange1/TickTheta1);
            BottomMotorTheta = Math.max(-650, Math.min(2000, BottomMotorTheta));
            MiddleMotorTheta += (int) Math.round(DeltaThetaChange/TickTheta2);
            MiddleMotorTheta = Math.max(-650, Math.min(2000, MiddleMotorTheta));    \

            ClawArmMiddle.setTargetPosition(MiddleMotorTheta);
            ClawArmBottom.setTargetPosition(BottomMotorTheta);
            ClawArmMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);;
            ClawArmBottom.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if(ClawArmBottom.isBusy()) {
                ClawArmBottom.setPower(1);
            }else {
                ClawArmBottom.setPower(0);
            }
            if(ClawArmMiddle.isBusy()) {
                ClawArmMiddle.setPower(1);
            }else {
                ClawArmMiddle.setPower(0);
            }
            */
            /*
            TargetPosition += gamepad1.left_stick_y*20;
            ClawArmMiddle.setTargetPosition(TargetPosition);
            ClawArmMiddle.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if(ClawArmMiddle.isBusy()) {
                ClawArmMiddle.setPower(1);
            }else {
                ClawArmMiddle.setPower(0);
            }
            telemetry.addData("ClawArmMiddle Position", ClawArmMiddle.getCurrentPosition());
            telemetry.update();
            yBottom = gamepad1.left_stick_y;
            yMiddle = gamepad1.right_stick_y;
            ClawArmBottom.setPower(yBottom*0.75);
            ClawArmMiddle.setPower(yMiddle*-0.5);
            */
        }
    }
}

/*
Full rotation is 5200 ticks
Claw Bottom Limit: -650 && 2000
 */
