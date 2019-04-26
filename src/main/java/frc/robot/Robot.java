/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.subsystems.MechanumDriveBase;
import frc.robot.util.*;
import frc.robot.commands.drivebaseCommands.MechanumUserDrive;

import frc.robot.util.constants.OIConstants;
import frc.robot.util.constants.Constants;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot 
{
    // SUBSYSTEMS
    public static MechanumDriveBase mechanumDriveBase;

    public static PowerDistributionPanel pdp;
    public static OI oi;

    private Logger logging;
    private Thread loggingThread;
  
    Command m_autonomousCommand;
    SendableChooser<Command> m_chooser = new SendableChooser<>();
  
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() 
    {
        //SUBSYSTEMS
        mechanumDriveBase = new MechanumDriveBase();

        pdp = new PowerDistributionPanel();
        oi = new OI();

        //start logging thread
        logging = new Logger();
        loggingThread = new Thread(logging, "LoggingThread");

        loggingThread.start();
        
        //init USB CAMERA 1
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(0);
        camera.setResolution(160, 120);
        camera.setFPS(15);

        //init USB CAMERA 2
        UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture(1);
        camera1.setResolution(160, 120);
        camera1.setFPS(10);

        //AUTO CHOOSER
        m_chooser.setDefaultOption("Default Auto", new MechanumUserDrive());
        SmartDashboard.putData("Auto mode", m_chooser);
    }
  
    /**
     * This function is called every robot packet, no matter the mode. Use
     * this for items like diagnostics that you want ran during disabled,
     * autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before
     * LiveWindow and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() 
    {
        // PowerDistributionPanel pdp = new PowerDistributionPanel();
        // SmartDashboard.putNumber("Lifter/CurrentL", pdp.getCurrent(?));
        // SmartDashboard.putNumber("Lifter/CurrentR", pdp.getCurrent(?));
        // SmartDashboard.putNumber("Lifter/CurrentB", pdp.getCurrent(?));

        putOiInfo();
    }
  
    /**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
     * the robot is disabled.
     */
    @Override
    public void disabledInit() 
    {
        System.out.printf("Disabled Init");
    }
  
    /**
     * This function is called periodically during Disabled mode
     */
    @Override
    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
    }
  
    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString code to get the auto name from the text box below the Gyro
     *
     * <p>You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons
     * to the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() 
    {
        m_autonomousCommand = m_chooser.getSelected();
    
        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector",
         * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
         * = new MyAutoCommand(); break; case "Default Auto": default:
         * autonomousCommand = new ExampleCommand(); break; }
         */
    
        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) 
        {
            m_autonomousCommand.start();
        }
    }
  
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic()
    {
        Scheduler.getInstance().run();
    }

    /*@Override
    public boolean isDisabled() {
        return super.isDisabled() || disabled;
    }*/
  
    @Override
    public void teleopInit() 
    {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) 
        {
            m_autonomousCommand.cancel();
        }
    }
  
    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters test mode
     */
    @Override
    public void testInit() 
    {
    }
  
    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() 
    {   
    }

    /**
     * Pushes operation inferface information from joystick to network tables
     */
    public void putOiInfo()
    {
        SmartDashboard.putNumber("OI/X-axis", oi.driveJoystick.getRawAxis(OIConstants.DRIVE_JOYSTICK_X));
        SmartDashboard.putNumber("OI/Y-axis", oi.driveJoystick.getRawAxis(OIConstants.DRIVE_JOYSTICK_Y));
        SmartDashboard.putNumber("OI/Twist", oi.driveJoystick.getRawAxis(OIConstants.DRIVE_JOYSTICK_TWIST));
        SmartDashboard.putNumber("OI/Magnitude", oi.driveJoystick.getMagnitude());
        SmartDashboard.putNumber("OI/Direction", oi.driveJoystick.getDirectionDegrees());
        SmartDashboard.putNumber("Telemetry/Heading", mechanumDriveBase.getHeading());
        SmartDashboard.putNumber("Telemetry/TargetHeading", mechanumDriveBase.getTargetHeading());
    }
}
