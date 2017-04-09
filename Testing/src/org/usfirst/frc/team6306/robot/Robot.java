
package org.usfirst.frc.team6306.robot;



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
	Victor RopeClimb2; // Motor for the Ball Shoot, it's a victor motor
	public final int STATE_0 = 0; // First state the robot is in for autonomous
	public final int STATE_1= 1; // Second state the robot is in for autonomous
	public final int STATE_2 = 2; // Third state the robot is in for autonomous
	public final int STATE_3 = 3; // Fourth state the robot is in for autonomous

	private int state = STATE_0; 
	private boolean TRIGGERED = false;
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	//RobotDrive myRobot = new RobotDrive(1,2,3,4);
	Command autonomousCommand;
	Joystick stick = new Joystick(1); // Joystick in port 1
	SendableChooser<Command> chooser = new SendableChooser<>();
	Timer timer = new Timer(); 
	
	Spark r1, r2, l1, l2;
	
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
		CameraServer.getInstance().startAutomaticCapture();
		RopeClimb = new Victor(5); // Plug Rope Climb into port 5
		RopeClimb2 = new Victor(6); // Plug Ball Shoot into port 6
		r1 = new Spark(4);
		r2 = new Spark(2);
		l1 = new Spark(3);
		l2 = new Spark(1);
		
		timer.reset();
		state = STATE_0;
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
		/*if(Timer.getMatchTime() < 1.5) {
			arcade(0.7, 0);
			
		}*/
		timer.start();
		state = STATE_0;
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
			
			arcade(0.7,0); // Moves the robot forward for 1 second(s)
			
			if(timer.get() >= 1.0) {
				TRIGGERED = false;
				state ++;
			
			}
		}
		
		if(state == STATE_1) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
			
			arcade(0, -0.3); // Placeholder
			
			if(timer.get() >= 0.3) {
				TRIGGERED = false;
				state++;
			}
		}
		
			if(state == STATE_2) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			}
			
			arcade(0.7, 0); // 
			
			if(timer.get() >= 1) {
				TRIGGERED = false;
				state++;
			}
			
		}
		
		if(state == STATE_3) {
			if(!TRIGGERED) {
				timer.start();
				TRIGGERED = true;
			} 
			
			arcade(0,0);
			
			if(timer.get() >= 0.3) {
				TRIGGERED = false;
				state ++;
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
		//myRobot.arcadeDrive(oi.xbox.getRawAxis(1), oi.xbox.getRawAxis(4)); // Makes the robot move
		
		arcade(oi.xbox.getRawAxis(1), oi.xbox.getRawAxis(4));
		if(oi.xbox.getRawAxis(2) > 0.1) {
			RopeClimb.set(oi.xbox.getRawAxis(2)); // Starts the Rope Climb
		}
		else RopeClimb.set(0); // Ends the Rope Climb
		
		if(oi.xbox.getRawAxis(3) > 0.1) {
			RopeClimb2.set(3);
		}
		else RopeClimb2.set(0);
	}
		
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	
	public void tank(double right, double left, double diff) {
		
		double aLeft = -left;
		double aRight = right;
		
		if((left/Math.abs(left)) == (right/Math.abs(right))) {
			if(Math.abs(left - right) < diff) {
				aLeft = -(right + left) / 2;
				aRight = (right + left) / 2;
			}
		}
		
		if (Math.abs(right) > .15) {
			r1.set(-aRight);
			r2.set(aRight);
		}
		else {
			r1.set(0);
			r2.set(0);
		}
		
		if (Math.abs(left) > .15) {
			l1.set(-aLeft);
			l2.set(aLeft);
		}
		else {
			l1.set(0);
			l2.set(0);
		}
	}
	
	public void arcade(double fwd, double turn) {
		double afwd = 0;
		double aturn = 0;
		
		if (Math.abs(fwd) > .25) {
			afwd = fwd;
		}
		if (Math.abs(turn) > .25) {
			aturn = turn;
		}
			r1.set(afwd + aturn);
			r2.set(-afwd + aturn); 
			l1.set(-afwd + aturn);
			l2.set(afwd + aturn);
	}
}
