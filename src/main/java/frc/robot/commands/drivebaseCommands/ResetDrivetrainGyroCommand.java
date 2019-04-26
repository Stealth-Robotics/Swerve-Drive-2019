/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drivebaseCommands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.HolonomicDrivetrain;

public class ResetDrivetrainGyroCommand extends Command {
  private HolonomicDrivetrain drivetrain;

  public ResetDrivetrainGyroCommand(HolonomicDrivetrain drivetrain) {
    this.drivetrain = drivetrain;

    requires(drivetrain);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    drivetrain.zeroGyro();
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }
}
