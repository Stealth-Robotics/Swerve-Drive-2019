
package frc.robot.commands.drivebaseCommands;

import edu.wpi.first.wpilibj.command.Command;

import frc.robot.Robot;

/**
 * Allows the user to drive the robot using a joystick
 */
public class MechanumUserDrive extends Command 
{
    public MechanumUserDrive() 
    {
        super("UserDrive");
        // Use requires() here to declare subsystem dependencies
        requires(Robot.mechanumDriveBase);
    }
  
    // Called just before this Command runs the first time
    @Override
    protected void initialize() 
    {
        Robot.mechanumDriveBase.resetHeadingAccumError();
    }
  
    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() 
    {
        if(Robot.mechanumDriveBase.EnableUserDrive)
        {
            Robot.mechanumDriveBase.move(Robot.oi.driveJoystick, false, false); //withPID, then withHeadless
        }
        
        double pov;
        if ((pov = Robot.oi.driveJoystick.getPOV()) != -1)
        {
            Robot.mechanumDriveBase.setTargetHeading(pov * Math.PI / 180);
        }
    }
  
    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() 
    {
        return false;
    }
  
    // Called once after isFinished returns true
    @Override
    protected void end()
    {
        //set motor power to 0
        Robot.mechanumDriveBase.moveWithoutIMU(0, 0, 0);
    }
  
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() 
    {
        end();
    }
}
