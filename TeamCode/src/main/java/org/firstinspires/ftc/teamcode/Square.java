package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
@Disabled
@Config
@Autonomous(name = "Square", group = "Autonomous")
    public class Square extends LinearOpMode {
    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(-63, 63, Math.toRadians(0)));
        Action square;
        Action squarewithturn;
        Action swerveconstant;
        Action swerve;
        Action strafeback;
        Action strafeforward;
        square = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(50, 50))
                .strafeTo(new Vector2d(50, -50))
                .strafeTo(new Vector2d(-50, -50))
                .strafeTo(new Vector2d(-50, 50))
                .build();
        squarewithturn = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(50, 50))
                .turn(Math.toRadians(-90))
                .strafeTo(new Vector2d(50, -50))
                .turn(Math.toRadians(-90))
                .strafeTo(new Vector2d(-50, -50))
                .turn(Math.toRadians(-90))
                .strafeTo(new Vector2d(-50, 50))
                .turn(Math.toRadians(-90))
                .build();
        swerveconstant = drive.actionBuilder(drive.pose)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(0, 0), -Math.PI / 2)
                .splineToConstantHeading(new Vector2d(50, -50), 0)
                .build();
        strafeback = drive.actionBuilder(new Pose2d(50, -50, 0))
                .strafeTo(new Vector2d(-50,50))
                .build();
        swerve = drive.actionBuilder(new Pose2d(-50, 50, 0))
                .splineTo(new Vector2d(0, 0), -Math.PI / 2)
                .splineTo(new Vector2d(50, -50), 0)
                .build();
        strafeforward = drive.actionBuilder(drive.pose)
                .strafeTo(new Vector2d(-50, 50))
                .build();
        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        //square,
                        //squarewithturn,
                        //swerveconstant,
                        //strafeback,
                        strafeforward,
                        swerve,
                        strafeback
                )
        );
    }
}
