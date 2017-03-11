
package org.usfirst.frc.team6306.robot;



import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team6306.robot.commands.ExampleCommand;
import org.usfirst.frc.team6306.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.GenericHID.Hand;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	
	Victor RopeClimb; // Motor for the Rope Climb, it's a victor motor
	Victor BallShoot; // Motor for the Ball Shoot, it's a victor motor
	public final int STATE_0 = 0; // First state the robot is in for autonomous
	public final int STATE_1= 1; // Second state the robot is in for autonomous
	public final int STATE_2 = 2; // Third state the robot is in for autonomous
	public final int STATE_3 = 3; // Fourth state the robot is in for autonomous

	private int state = STATE_0; 
	private boolean TRIGGERED = false;
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	RobotDrive myRobot = new RobotDrive(1,2);
	Command autonomousCommand;
	Joystick stick = new Joystick(1); // Joystick in port 0
	SendableChooser<Command> chooser = new SendableChooser<>();
	Timer timer = new Timer(); 
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
		RopeClimb = new Victor(5); // Plug Rope Climb into port 5
		BallShoot = new Victor(6); // Plug Ball Shoot into port 6
	
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();
		timer.reset();
		timer.start();
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();	
		if(state == STATE_0) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
			
			myRobot.arcadeDrive(0.6,0); // Moves the robot forward for 3 seconds
			
			if(timer.get() >= 3) {
				TRIGGERED = false;
				state++;
			}
		}
		
		if(state == STATE_1) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
			
			myRobot.arcadeDrive(0, 0.9); // Turns the robot for 0.7 seconds
			
			if(timer.get() >= .7) {
				TRIGGERED = false;
				state++;
			}
		}
		
		if(state == STATE_2) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
			
			myRobot.arcadeDrive(0.7, 0); // Has the robot return to starting position for 3 seconds
			
			if(timer.get() >= 3) {
				TRIGGERED = false;
				state++;
			}
			
		}
		
		if(state == STATE_3) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
		}
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		myRobot.arcadeDrive(oi.xbox.getRawAxis(1), oi.xbox.getRawAxis(4), true); // Makes the robot move
		
		if(oi.xbox.getRawAxis(2) > 0.1) {
			RopeClimb.set(oi.xbox.getRawAxis(2)); // Starts the Rope Climb
		}
		else RopeClimb.set(0); // Ends the Rope Climb
		
		if(oi.xbox.getRawButton(3)) {
			BallShoot.set(3);
		}
		else BallShoot.set(0);
	}
		
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
}
