/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivebaseCommands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.HolonomicDrivetrain;

public class HolonomicDriveCommand extends Command {
  private final HolonomicDrivetrain drivetrain;

  public HolonomicDriveCommand(HolonomicDrivetrain drivetrain) {
    this.drivetrain = drivetrain;

    requires(drivetrain);
  }

  private double deadband(double input){
    if (Math.abs(input) < 0.05) return 0;
    return input;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double forward = Robot.oi.driveJoystick.getY();
    double strafe = Robot.oi.driveJoystick.getX();
    double rotation = Robot.oi.driveJoystick.getZ() * 0.5;

    forward = deadband(forward);
    strafe = deadband(strafe);
    rotation = deadband(rotation);

    SmartDashboard.putNumber("DriveBase/Input/Forward", forward);
    SmartDashboard.putNumber("DriveBase/Input/Strafe", strafe);
    SmartDashboard.putNumber("DriveBase/Input/Rotation", rotation);

    drivetrain.holonomicDrive(forward, strafe, rotation);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    drivetrain.stopDriveMotors();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
