
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@Disabled
@TeleOp(name="Testmotor", group="Linear OpMode")
public class Testmotor extends LinearOpMode {

    private DcMotor clawMotorOne;

    @Override
    public void runOpMode() {
        clawMotorOne = hardwareMap.get(DcMotor.class, "clawMotorOne");
        waitForStart();
        while (opModeIsActive()) {
            clawMotorOne.setPower(gamepad1.left_stick_y);
            telemetry.addData("MotorPos", clawMotorOne.getCurrentPosition());
            telemetry.update();
        }
    }
}
