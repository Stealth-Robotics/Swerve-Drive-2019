/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class SwerveDriveModule extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  private static final long STALL_TIMEOUT = 2000;

  private long stallTimeBegin = Long.MAX_VALUE;

  private double lastError = 0, lastTargetAngle = 0, previousTargetAngle = 0;

  private final int moduleNumber;

  private final double zeroOffset;

  private final CANSparkMax angleMotor;
  private final CANPIDController angleMotorPID;
  private final CANEncoder angleMotorEncoder;

  private final CANSparkMax driveMotor;

  public SwerveDriveModule(int ModuleNumber, CANSparkMax AngleMotor, CANSparkMax DriveMotor, double ZeroOffset) {
    moduleNumber = ModuleNumber;

    zeroOffset = ZeroOffset;

    angleMotor = AngleMotor;
    angleMotorPID = angleMotor.getPIDController();
    angleMotorEncoder = angleMotor.getEncoder();

    driveMotor = DriveMotor;

    driveMotor.setMotorType(MotorType.kBrushless);
    angleMotor.setMotorType(MotorType.kBrushless);

    angleMotorPID.setReference(0, ControlType.kPosition);
    //set PID values TODO : Tune These Values
    angleMotorPID.setP(20);
    angleMotorPID.setI(0.0);
    angleMotorPID.setD(200);

    driveMotor.setIdleMode(IdleMode.kBrake);

    /*
    // From 2910s code Need to figure out how to do this with spark maxes (might be truned on by default)

    // Set amperage limits
		angleMotor.setCurrentLimit(50);
		angleMotor.EnableCurrentLimit(true);

		driveMotor.setCurrentLimit(50);
		driveMotor.EnableCurrentLimit(true);
    */
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public CANSparkMax getAngleMotor() {
    return angleMotor;
  }

  public CANSparkMax getDriveMotor() {
    return driveMotor;
  }

  public double getTargetAngle() {
    return lastTargetAngle;
  }

  public void robotDisabledInit() {
    stallTimeBegin = Long.MAX_VALUE;
  }

  public void setTargetAngle(double targetAngle) {
    lastTargetAngle = targetAngle;

    targetAngle %= 360;
    targetAngle += zeroOffset;

    double currentAngle = angleMotorEncoder.getPosition() * (360.0 / 1024.0);
    double currentAngleMod = currentAngle % 360;
    if (currentAngleMod < 0) currentAngleMod += 360;

    double delta = currentAngleMod - targetAngle;

    if (delta > 180) {
      targetAngle += 360;
    } else if (delta < -180) {
      targetAngle -= 360;
    }

    delta = currentAngleMod - targetAngle;
    if (delta > 90 || delta < -90) {
      if (delta > 90) {
        targetAngle += 180;
      } else if (delta < -90){
        targetAngle -= 180;
      }
      driveMotor.setInverted(false);
    } else {
      driveMotor.setInverted(true);
    }

    targetAngle += currentAngle - currentAngleMod;

    /*double currentError = angleMotorEncoder.getPosition() - previousTargetAngle;
    if (Math.abs(currentError - lastError) < 7.5 &&
				Math.abs(currentAngle - targetAngle) > 5) {
			if (stallTimeBegin == Long.MAX_VALUE) stallTimeBegin = System.currentTimeMillis();
			if (System.currentTimeMillis() - stallTimeBegin > STALL_TIMEOUT) {
				throw new MotorStallException(String.format("Angle motor on swerve module '%d' has stalled.",
						mModuleNumber));
			}
		} else {
			mStallTimeBegin = Long.MAX_VALUE;
		}
    lastError = currentError;*/

    targetAngle *= 1024.0 / 360.0;
    angleMotorPID.setReference(targetAngle, ControlType.kPosition);
  }

  public void setTargetSpeed(double Speed){
    driveMotor.set(Speed);
  }
}
