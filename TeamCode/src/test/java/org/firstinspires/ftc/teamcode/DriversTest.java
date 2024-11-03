package org.firstinspires.ftc.teamcode;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;


import junit.framework.TestCase;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DriversTest extends TestCase {
    private static final double TOLERANCE = 0.01;

    @Mock
    private DcMotor mockDriverFrontLeft;
    @Mock
    private DcMotor mockDriverFrontRight;
    @Mock
    private DcMotor mockDriverBackLeft;
    @Mock
    private DcMotor mockDriverBackRight;
    @Mock
    private IMU mockImu;
    @Mock
    private Telemetry mockTelemetry;

    private Gamepad gamepad = new Gamepad();

    private Drivers drivers;

    @Before
    public void setUp() {
        drivers = new Drivers(mockDriverFrontLeft, mockDriverFrontRight, mockDriverBackLeft,
                              mockDriverBackRight, gamepad, mockImu, mockTelemetry);
        drivers.setSpeedCoefficients(0.5, 1.0);
    }

    @Test
    public void test_all_drivers_power_zero_when_no_gamepad_input() {
        gamepad.left_stick_x = 0.0f;
        gamepad.left_stick_y = 0.0f;
        gamepad.right_stick_x = 0.0f;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(0.0, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(0.0, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(0.0, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(0.0, drivers.properties.rightBackPower, TOLERANCE);
    }

    @Test
    public void test_full_left_normal_speed() {
        gamepad.left_stick_x = 1.0f;
        gamepad.left_stick_y = 0.0f;
        gamepad.right_stick_x = 0.0f;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(0.5, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(0.5, drivers.properties.rightBackPower, TOLERANCE);
    }

    @Test
    public void test_full_left_turbo_speed() {
        gamepad.left_stick_x = 1.0f;
        gamepad.left_stick_y = 0.0f;
        gamepad.right_stick_x = 0.0f;
        gamepad.left_trigger = 1;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(1, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-1, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-1, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(1, drivers.properties.rightBackPower, TOLERANCE);
    }

    @Test
    public void test_full_left_up_normal_speed() {
        gamepad.left_stick_x = 1.0f;
        gamepad.left_stick_y = 1.0f;
        gamepad.right_stick_x = 1.0f;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(0.4142, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-1.0, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-0.1715, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(-0.4142, drivers.properties.rightBackPower, TOLERANCE);
    }

    @Test
    public void test_full_left_up_turbo_speed() {
        // TODO: update the gamepad values to trigger power throttling.
        gamepad.left_stick_x = 1.0f;
        gamepad.left_stick_y = 1.0f;
        gamepad.right_stick_x = 1.0f;
        gamepad.left_trigger = 1;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(0.4142, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-1.0, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-0.1715, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(-0.4142, drivers.properties.rightBackPower, TOLERANCE);

        verify(mockDriverFrontLeft).setPower(drivers.properties.leftFrontPower);
        verify(mockDriverFrontRight).setPower(drivers.properties.rightFrontPower);
        verify(mockDriverBackLeft).setPower(drivers.properties.leftBackPower);
        verify(mockDriverBackRight).setPower(drivers.properties.rightBackPower);
    }

    @Test
    public void test_reset_imu() {
        gamepad.left_stick_x = 1.0f;
        gamepad.left_stick_y = 0.0f;
        gamepad.right_stick_x = 0.0f;
        YawPitchRollAngles yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 90, 0.0,
                                                                       0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        assertEquals(-0.5, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.rightBackPower, TOLERANCE);

        gamepad.circle = true;

        yawPitchRollAngles = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 0.0, 0.0, 0L);
        when(mockImu.getRobotYawPitchRollAngles()).thenReturn(yawPitchRollAngles);

        drivers.update();

        verify(mockImu, times(1)).resetYaw();
        assertEquals(0.5, drivers.properties.leftFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.rightFrontPower, TOLERANCE);
        assertEquals(-0.5, drivers.properties.leftBackPower, TOLERANCE);
        assertEquals(0.5, drivers.properties.rightBackPower, TOLERANCE);
    }

}