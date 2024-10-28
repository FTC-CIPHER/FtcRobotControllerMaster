
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Disabled
@TeleOp(name="Slide", group="TeleOp")
public class Slide extends LinearOpMode {


    private DcMotorEx SlideMotorl;
    private DcMotorEx SlideMotorr;

    @Override
    public void runOpMode() {
        SlideMotorl = hardwareMap.get(DcMotorEx.class, "SlideMotorl");
        SlideMotorr = hardwareMap.get(DcMotorEx.class, "SlideMotorr");
        SlideMotorl.setDirection(DcMotorEx.Direction.REVERSE);
        SlideMotorr.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        SlideMotorl.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        SlideMotorl.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        SlideMotorr.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        waitForStart();
        while (opModeIsActive()) {
            SlideMotorl.setTargetPosition(2800);
            SlideMotorr.setTargetPosition(2800);
            SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            SlideMotorl.setPower(1);
            SlideMotorr.setPower(1);

           // while (Slidemotorl.isBusy() && Slidemotorr.isBusy() && opModeIsActive()) {

            //    telemetry.addData("Motorposition", Slidemotorl.getCurrentPosition());
            //    telemetry.addData("Motorposition2", Slidemotorr.getCurrentPosition());
            //    telemetry.update();


           // }
           // sleep(5000);


            SlideMotorl.setTargetPosition(20);
            SlideMotorr.setTargetPosition(20);
            SlideMotorl.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            SlideMotorr.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            SlideMotorl.setPower(1);
            SlideMotorr.setPower(1);
           // while (Slidemotorl.isBusy() && Slidemotorr.isBusy() && opModeIsActive()) {

               // telemetry.addData("Motorpositionl", Slidemotorl.getCurrentPosition());
              //  telemetry.addData("Motorposition2", Slidemotorr.getCurrentPosition());
             //   telemetry.update();


            //}
            /*

            Slidemotor.setTargetPosition(-2000);
            Slidemotor2.setTargetPosition(-2000);
            Slidemotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor.setPower(1);
            Slidemotor2.setPower(1);
            while (Slidemotor.isBusy() && Slidemotor2.isBusy() && opModeIsActive()) {

                telemetry.addData("Motorposition", Slidemotor.getCurrentPosition());
                telemetry.addData("Motorposition2", Slidemotor2.getCurrentPosition());
                telemetry.update();


            }
            sleep(5000);

            Slidemotor.setTargetPosition(0);
            Slidemotor2.setTargetPosition(0);
            Slidemotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor.setPower(1);
            Slidemotor2.setPower(1);
            while (Slidemotor.isBusy() && Slidemotor2.isBusy() && opModeIsActive()) {

                telemetry.addData("Motorposition", Slidemotor.getCurrentPosition());
                telemetry.addData("Motorposition2", Slidemotor2.getCurrentPosition());
                telemetry.update();


            }


            Slidemotor.setTargetPosition(-3000);
            Slidemotor2.setTargetPosition(-3000);
            Slidemotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor.setPower(1);
            Slidemotor.setPower(1);
            while (Slidemotor.isBusy() && Slidemotor2.isBusy() && opModeIsActive()) {

                telemetry.addData("Motorposition", Slidemotor.getCurrentPosition());
                telemetry.addData("Motorposition2", Slidemotor2.getCurrentPosition());
                telemetry.update();


            }
            sleep(5000);

            Slidemotor.setTargetPosition(0);
            Slidemotor2.setTargetPosition(0);
            Slidemotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor2.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
            Slidemotor.setPower(1);
            Slidemotor2.setPower(1);
            while (Slidemotor.isBusy() && Slidemotor2.isBusy() && opModeIsActive()) {

                telemetry.addData("Motorposition", Slidemotor.getCurrentPosition());
                telemetry.addData("Motorposition2", Slidemotor2.getCurrentPosition());
                telemetry.update();


            }
            */

        }
    }
}