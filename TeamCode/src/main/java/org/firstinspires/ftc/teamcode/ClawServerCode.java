package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="ArmTest2")
@Disabled
public class ClawServerCode extends LinearOpMode{
    private DcMotor ClawArmBottom;
    private DcMotor ClawArmMiddle;
    private Servo ClawPitch;
    private Servo ClawGrab;
    private Servo ClawRoll;

    @Override
    public void runOpMode() {
        double a,b,c,d,Theta1,Theta2,Theta3,ThetaChange1,ThetaChange2,XChange,ZChange,T,Tinv,DeltaTheta,DeltaThetaChange,X,Z;
        final double L1 = 12, L2 = 13.5;
        final double TickTheta1 = 0.0128351397604107, TickTheta2 = 0.033457249070632;
        double IntTheta1 = 52.5, IntDeltaTheta = 133.12;
        X = 0;
        Z = 0;
        ClawArmMiddle = hardwareMap.get(DcMotor.class, "clawMotorTwo");
        ClawArmBottom = hardwareMap.get(DcMotor.class, "clawMotorOne");
        ClawPitch = hardwareMap.get(Servo.class, "ClawPitch");
        ClawGrab = hardwareMap.get(Servo.class, "ClawGrab");
        ClawRoll = hardwareMap.get(Servo.class, "ClawRoll");
        ClawArmMiddle.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ClawArmBottom.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        ClawArmMiddle.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ClawArmBottom.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        waitForStart();
        while (opModeIsActive()) {

            XChange = -(gamepad1.left_stick_y);
            ZChange = -(gamepad1.right_stick_y);
            X += XChange;
            Z += ZChange;
            Theta1 = ((-ClawArmBottom.getCurrentPosition()*TickTheta1)+IntTheta1)*Math.PI/180;
            DeltaTheta = ((ClawArmMiddle.getCurrentPosition()*TickTheta2)+IntDeltaTheta)*Math.PI/180;
            Theta2 = Theta1 - DeltaTheta;
            Theta3 = Math.PI + Theta2;
            a = (-L1)*Math.sin(Theta1);
            b = (-L2)*Math.sin(Theta2);
            c = (L1)*Math.cos(Theta1);
            d = (L2)*Math.cos(Theta2);
            //Tinv=((a*d)-(b*c)); //To be put out
            //if (Math.abs(Tinv) <= (L1*L2)*0.05) {
            //   Tinv=(L1*L2)*0.05*Math.signum(Tinv);
            //}
            //T = 1/Tinv;
            T = -1/L1/L2;

            ThetaChange1 = T*((d*XChange)-(b*ZChange));
            ThetaChange2 = T*((-c*XChange)+(a*ZChange));
            DeltaThetaChange = ThetaChange1 - ThetaChange2;

            /*if(DeltaTheta*180/Math.PI >= 5){
                if(DeltaTheta*180/Math.PI <=10){
                    if(DeltaThetaChange < 0){
                        DeltaThetaChange *= (DeltaTheta-5)/5;
                    }
                }

            }

             */

            ClawArmBottom.setPower(-ThetaChange1*10);//Math.max(-1, Math.min(1, ThetaChange1*100)));
            ClawArmMiddle.setPower(DeltaThetaChange*5);//Math.max(-1, Math.min(1, DeltaThetaChange*100)));
            ClawPitch.setPosition((-0.652/Math.PI)*Theta3+1.243);
            ClawGrab.setPosition(-gamepad1.left_trigger * 0.25 + 0.58);
            ClawRoll.setPosition(-gamepad1.left_stick_x * 0.5 + 0.52);
            //ClawArmBottom.setPower(gamepad1.left_stick_y);
            //ClawArmMiddle.setPower(gamepad1.right_stick_y);
            telemetry.addData("Theta Change 1", Math.max(-1, Math.min(1, ThetaChange1*100)));
            telemetry.addData("Delta Theta Change", Math.max(-1, Math.min(1, DeltaThetaChange*100)));
            telemetry.addData("Theta1", Theta1*180/Math.PI);
            telemetry.addData("Theta2", Theta2*180/Math.PI);
            telemetry.addData("Delta Theta", DeltaTheta*180/Math.PI);
            telemetry.addData("X Pos", X);
            telemetry.addData("Z Pos", Z);
            telemetry.addData("Bottom Power", ThetaChange1*10);//Math.max(-1, Math.min(1, ThetaChange1*1000)));
            telemetry.addData("Middle Power", DeltaThetaChange*5);//;.max(-1, Math.min(1, DeltaThetaChange*100)));
            telemetry.addData("Bottom Motor Position (Ticks)", ClawArmBottom.getCurrentPosition());
            telemetry.addData("Middle Motor Position (Ticks)", ClawArmMiddle.getCurrentPosition());
            telemetry.addData("LeftJoystickX", gamepad1.left_stick_x);
            telemetry.addData("LeftTrigger", gamepad1.left_trigger);
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
