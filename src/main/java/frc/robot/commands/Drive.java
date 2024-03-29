/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;

public class Drive extends Command {
  public Drive() {
    requires(Robot.m_driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double bufferZone = 0.2; 
    double minSpeed = 0.3;
    double throttleSpeed = mapToExp((-Robot.m_oi.rightStick.getY()), bufferZone, minSpeed);
    double twistSpeed = mapToExp((Robot.m_oi.rightStick.getRawAxis(3)), bufferZone, minSpeed);
    double throttleLimiter = 0.8;
    double twistLimiter = 0.8;

    //Robot.m_driveTrain.tankDrive(leftSpeed*limiter, rightSpeed*limiter);
    Robot.m_driveTrain.arcadeDrive(throttleSpeed*throttleLimiter, twistSpeed*twistLimiter);

    if (Robot.m_oi.rightStick.getRawButton(1)) { //if trigger is pressed
      driveStraight();
    }

    if (Robot.m_oi.rightStick.getRawButtonReleased(RobotMap.gearSwitcherButton)) {
      Robot.m_driveTrain.switchGear();
    }
  }

  double mapToExp(double val, double bufferZone, double minSpeed) {
    double returnVal = 0.0;
    if (val >= bufferZone) {
      returnVal = (1.0 - minSpeed)*Math.pow(val, 3) + minSpeed;
    } else if (val <= -bufferZone) {
      returnVal = (1.0 - minSpeed)*Math.pow(val, 3) - minSpeed;
    }

    return returnVal;
  }

  void driveStraight() {
    double speed = 0.4;
    double leftSpeed = -Robot.m_oi.leftStick.getY();
    double rightSpeed = -Robot.m_oi.rightStick.getY();
    double twistSpeed = Robot.m_oi.rightStick.getX();//Robot.m_oi.rightStick.getRawAxis(3);
    double limiter = 0.3;

    //Robot.m_driveTrain.tankDrive(speed, speed);
    Robot.m_driveTrain.tankDrive(leftSpeed*limiter+speed, rightSpeed*limiter+speed);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.m_driveTrain.tankDrive(0.0, 0.0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
