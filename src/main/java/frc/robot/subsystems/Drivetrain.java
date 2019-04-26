/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public abstract class Drivetrain extends Subsystem {
  private double speedMultiplier = 0.7;

  protected abstract void initDefaultCommand();

  public double getSpeedMultiplier() {
    return speedMultiplier;
  }

  public void setSpeedMultiplier(double speedMultiplier){
    this.speedMultiplier = speedMultiplier;
  }
}
