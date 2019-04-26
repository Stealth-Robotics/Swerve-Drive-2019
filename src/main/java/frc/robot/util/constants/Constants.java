
package frc.robot.util.constants;

/**
 * Holds the constants in one place
 */
public class Constants
{
    // !< driving speed constants
    public static final double SPEED_SLOW = 0.3;
    public static final double SPEED_SORT_OF_SLOW = 0.6;
    public static final double SPEED_NORMAL = 1;
    public static final double SPEED_TURN = 0.75;

    // !< driving pid constants
    public static final double DRIVE_KP = 0.27;
    public static final double DRIVE_KI = 0.0033;
    public static final double DRIVE_KD = 0.055;

    //Rotation speed when finding vision target
    public static final double FIND_ROT_SPEED = 0.5;

    //Aligning with vision targets rotation PID constants
    public static final double ROT_ALIGN_KP = 0.01;
    public static final double ROT_ALIGN_KI = 0.01;
    public static final double ROT_ALIGN_KD = -0.01;
    public static final double ROT_ALIGN_END = 3;

    //Aligning with vision targets translation PID constants
    public static final double TRANS_ALIGN_KP = 0.1;
    public static final double TRANS_ALIGN_KI = 0.01;
    public static final double TRANS_ALIGN_KD = -0.01;

    //Driving to vision targets PID constants
    public static final double DRIVE_TOWARDS_KP = 0.01;
    public static final double DRIVE_TOWARDS_KI = 0.01;
    public static final double DRIVE_TOWARDS_KD = 0.01;
    public static final int DRIVE_TOWARDS_END = 75;

    public static final double INTAKE_SPEED = 1;

    //More driving to vision targets PID
    public static final double DESIRED_TARGET_AREA = 10;          // Area of the target when the robot reaches the wall
    public static final double SPEEDkP = 0.15;
    public static final double SPEEDkD = 0.5;
    public static final double MAX_DRIVE = 0.3;                   // Simple speed limit so we don't drive too fast

    public static final double STRAFEkP = 0.05;
    public static final double STRAFEkD = 0.5;
    public static final double MAX_STRAFE = 0.1;
    
    public static final double STEER_kP = 0.03;                    // how hard to turn toward the target
    public static final double STEER_kI = 0;   
    public static final double STEER_kD = 0;
    public static final double STEER_TARGET_RATIO = 1.812; //horizontal bounding box over vertical bounding box

    public static final double TWOPI = Math.PI * 2;
}