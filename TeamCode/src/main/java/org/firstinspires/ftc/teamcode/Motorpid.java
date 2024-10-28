package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
@Disabled
public class Motorpid extends LinearOpMode{

    DcMotorEx motor;

    double integralSum = 0;
    double Kp = 0;
    double Ki = 0;
    double Kd = 0;
    double Kf = 0;

    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;

    @Override
    public void runOpMode() throws InterruptedException{
        motor = hardwareMap.get(DcMotorEx.class, "MotorArmTwo");
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                waitForStart();
        while (opModeIsActive()) {
            double power = PIDControl(100, motor.getVelocity());
            motor.setPower(power);

        }
    }

    public double PIDControl(double reference, double state) {
        double error = reference - state;
        integralSum += error * timer.seconds();
        double derivative = (error-lastError) / timer.seconds();
        lastError = error;

        timer.reset();

        double output = (error * Kp) + (derivative * Kd) + (integralSum * Ki) + (reference * Kf);
        return output;


    }

}

