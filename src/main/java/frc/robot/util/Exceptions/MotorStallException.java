/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util.Exceptions;

/**
 * A Motor stall exception
 */
public class MotorStallException extends IllegalStateException{
    public MotorStallException(String msg){
        super(msg);
    }
}
