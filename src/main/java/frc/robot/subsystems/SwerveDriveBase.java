/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class SwerveDriveBase extends HolonomicDrivetrain {
  private static final double WHEELBASE = 12.5;
  private static final double TRACKWIDTH = 13.5;
  private static final double RATIO = Math.sqrt(Math.pow(WHEELBASE,2) + Math.pow(TRACKWIDTH,2));

  /*
	 * 0 is Front Right
	 * 1 is Front Left
	 * 2 is Back Left
	 * 3 is Back Right
	 */
  private SwerveDriveModule[] swerveModules = new SwerveDriveModule[] {
    new SwerveDriveModule(0, new CANSparkMax(deviceID, MotorType.kBrushless), new CANSparkMax(deviceID, MotorType.kBrushless), 75.5),
    new SwerveDriveModule(1, new CANSparkMax(deviceID, MotorType.kBrushless), new CANSparkMax(deviceID, MotorType.kBrushless), 13.7),
    new SwerveDriveModule(2, new CANSparkMax(deviceID, MotorType.kBrushless), new CANSparkMax(deviceID, MotorType.kBrushless), 254),
    new SwerveDriveModule(3, new CANSparkMax(deviceID, MotorType.kBrushless), new CANSparkMax(deviceID, MotorType.kBrushless), 157),
  };

  private static PigeonIMU imu; // !< The IMU

  public SwerveDriveBase() {
    zeroGyro();

    swerveModules[0].getDriveMotor().setInverted(true);
    swerveModules[1].getDriveMotor().setInverted(true);
    swerveModules[2].getDriveMotor().setInverted(true);
    swerveModules[3].getDriveMotor().setInverted(true);

    swerveModules[0].getAngleMotor().setInverted(true);
    swerveModules[3].getAngleMotor().setInverted(true);

    for (SwerveDriveModule module : swerveModules){
      module.setTargetAngle(0);
    }
  }

  public PigeonIMU GetIMU() {
    return imu;
  }

  public double getGyroAngle() {
    return ((-imu.getFusedHeading() * Math.PI / 180) - getAdjustmentAngle());
  }

  public double getRawGyroAngle() {
    return (imu.getFusedHeading());
  }

  public SwerveDriveModule getSwerveModule(int i) {
		return swerveModules[i];
  }
  
  @Override
	public void holonomicDrive(double forward, double strafe, double rotation) {
    forward *= getSpeedMultiplier();
    strafe *= getSpeedMultiplier();

    if (isFieldOriented()) {
      double angleRad = Math.toRadians(getGyroAngle());
      double temp = forward * Math.cos(angleRad) +
                      strafe * Math.sin(angleRad);
      strafe = -forward * Math.sin(angleRad) + strafe * Math.cos(angleRad);
      forward = temp;
    }

    double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
    double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
    double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
    double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

    double[] angles = new double[]{
      Math.atan2(b, c) * 180 / Math.PI,
      Math.atan2(b, d) * 180 / Math.PI,
      Math.atan2(a, d) * 180 / Math.PI,
      Math.atan2(a, c) * 180 / Math.PI
    };

    double[] speeds = new double[]{
      Math.sqrt(b * b + c * c),
      Math.sqrt(b * b + d * d),
      Math.sqrt(a * a + d * d),
      Math.sqrt(a * a + c * c)
    };

    double max = speeds[0];

    for (double speed : speeds) {
			if (speed > max) {
				max = speed;
			}
		}

    for (int i = 0; i < 4; i++) {
			if (Math.abs(forward) > 0.05 ||
			    Math.abs(strafe) > 0.05 ||
			    Math.abs(rotation) > 0.05) {
				swerveModules[i].setTargetAngle(angles[i] + 180);
			} else {
				swerveModules[i].setTargetAngle(swerveModules[i].getTargetAngle());
			}
			swerveModules[i].setTargetSpeed(speeds[i]);
		}

  }

  @Override
	public void stopDriveMotors() {
		for (SwerveDriveModule module : swerveModules) {
			module.setTargetSpeed(0);
		}
	}
}
