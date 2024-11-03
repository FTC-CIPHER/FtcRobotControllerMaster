package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Drivers {
    static class Properties {
        public double leftFrontPower = 0;
        public double rightFrontPower = 0;
        public double leftBackPower = 0;
        public double rightBackPower = 0;
    }

    final Properties properties = new Properties();

    public float left_trigger_threshold = 0.9f;

    private final DcMotor driverFrontLeft;
    private final DcMotor driverFrontRight;
    private final DcMotor driverBackLeft;
    private final DcMotor driverBackRight;
    private final Gamepad gamepad;
    private final IMU imu;
    private final Telemetry telemetry;

    private double normalSpeedCoefficient = 0.5;
    private double turboSpeedCoefficient = 1;

    public Drivers(DcMotor driverFrontLeft,
                   DcMotor driverFrontRight,
                   DcMotor driverBackLeft,
                   DcMotor driverBackRight,
                   Gamepad gamepad,
                   IMU imu,
                   Telemetry telemetry) {
        this.driverFrontLeft = driverFrontLeft;
        this.driverFrontRight = driverFrontRight;
        this.driverBackLeft = driverBackLeft;
        this.driverBackRight = driverBackRight;
        this.gamepad = gamepad;
        this.imu = imu;
        this.telemetry = telemetry;
    }

    public void setSpeedCoefficients(double normalSpeedCoefficient, double turboSpeedCoefficient) {
        this.normalSpeedCoefficient = normalSpeedCoefficient;
        this.turboSpeedCoefficient = turboSpeedCoefficient;
    }

    public void update() {
        telemetry.addData("Step: ", "Updating drivers.");

        CalculateDriverProperties();
        UpdateDriverStates();

        telemetry.addData("Step: ", "Complete updating drivers.");
    }

    void CalculateDriverProperties() {
        // Turbo mode if left trigger is pressed
        double speedCoefficient =
                gamepad.left_trigger >= left_trigger_threshold ? turboSpeedCoefficient :
                        normalSpeedCoefficient;

        double gyroYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double gyroYawDisplay = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        double mecanumTheta = Math.atan2(-gamepad.left_stick_y, gamepad.left_stick_x);
        mecanumTheta -= gyroYaw;

        double mecanumPower = Math.hypot(-gamepad.left_stick_y, gamepad.left_stick_x);
        double mecanumSin = Math.sin(mecanumTheta - Math.PI / 4);
        double mecanumCos = Math.cos(mecanumTheta - Math.PI / 4);
        double mecanumMax = Math.max(Math.abs(mecanumSin), Math.abs(mecanumCos));


        double leftFrontDrivePower =
                (mecanumPower * mecanumCos / mecanumMax + gamepad.right_stick_x) *
                        speedCoefficient;
        double rightFrontDrivePower =
                (mecanumPower * mecanumSin / mecanumMax - gamepad.right_stick_x) *
                        speedCoefficient;
        double leftBackDrivePower =
                (mecanumPower * mecanumSin / mecanumMax + gamepad.right_stick_x) *
                        speedCoefficient;
        double rightBackDrivePower =
                (mecanumPower * mecanumCos / mecanumMax - gamepad.right_stick_x) *
                        speedCoefficient;

        double maxPower =
                Math.max(Math.max(Math.abs(leftFrontDrivePower), Math.abs(rightFrontDrivePower)),
                         Math.max(Math.abs(leftBackDrivePower), Math.abs(rightBackDrivePower)));

        if (maxPower > 1) {
            // Normalize the motor power values
            leftFrontDrivePower /= maxPower;
            rightFrontDrivePower /= maxPower;
            leftBackDrivePower /= maxPower;
            rightBackDrivePower /= maxPower;
        }

        properties.leftFrontPower = leftFrontDrivePower;
        properties.rightFrontPower = rightFrontDrivePower;
        properties.leftBackPower = leftBackDrivePower;
        properties.rightBackPower = rightBackDrivePower;

        telemetry.addData("IMU Gryo Yaw (deg)", gyroYawDisplay);
        telemetry.addData("Speed Coefficient", speedCoefficient);
        telemetry.addData("theta", mecanumTheta);
        telemetry.addData("LeftFrontPower", properties.leftFrontPower);
        telemetry.addData("RightFrontPower", properties.rightFrontPower);
        telemetry.addData("LeftBackPower", properties.leftBackPower);
        telemetry.addData("RightBackPower", properties.rightBackPower);

    }

    protected void UpdateDriverStates() {
        driverFrontLeft.setPower(properties.leftFrontPower);
        driverFrontRight.setPower(properties.rightFrontPower);
        driverBackLeft.setPower(properties.leftBackPower);
        driverBackRight.setPower(properties.rightBackPower);
    }
}
