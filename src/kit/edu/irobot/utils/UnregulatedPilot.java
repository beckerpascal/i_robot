package kit.edu.irobot.utils;

import java.io.Closeable;

import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

/**
 * Unregulated pilot
 * 
 * Use this instead of a differentialPilot, if you need to make motor speed adjustments really fast.
 * We use this for followLine and it worked really well.
 * 
 * ATTENTION: You *cannot* use this class simultaneously with RegulatedMotors or other pilots.
 * This makes it hard to maintain motors and pilot. So only use this if it is worth it for you!
 * 
 * NOTE: There can be problems with synchronization of the motors, which can cause the robot
 * to drift or rotate when stopped!
 * 
 * @author Fabian
 *
 */
public class UnregulatedPilot implements Closeable {

	private final UnregulatedMotor left, right;
	private final int track_width;
	private final int wheel_diameter;

	private boolean isStopped = false;
	
	private int basePWM = 50;
	private int basePWM_rotate = 50;

	private final int initialLeftTachoCount;
	private final int initialRightTachoCount;
	private int lastLeftTachoCount;
	private int lastRightTachoCount;

	private final float unit; // in mm
	public final float track_width_in_degree_inv;

	/**
	 * 
	 * @param left
	 * @param right
	 * @param abstand
	 *            abstand der raeder im mm bitte
	 * @param durchmesser
	 *            durchmesser der raeder in mm bitte
	 */
	public UnregulatedPilot(int abstand, int durchmesser) {
		this.left = new UnregulatedMotor(MotorPort.B);
		this.right = new UnregulatedMotor(MotorPort.A);
		this.track_width = abstand;
		this.wheel_diameter = durchmesser;

		this.unit = (float) Math.PI * this.wheel_diameter / 360.0f;
		this.track_width_in_degree_inv = 360.0f / (2.0f * (float) Math.PI * this.track_width / this.unit);

		this.initialLeftTachoCount = this.left.getTachoCount();
		this.initialRightTachoCount = this.right.getTachoCount();
		this.lastLeftTachoCount = this.initialLeftTachoCount;
		this.lastRightTachoCount = this.initialRightTachoCount;
	}
	
	public void close() {
		this.left.close();
		this.right.close();
	}
	
	public void reset() {
		this.lastLeftTachoCount = this.left.getTachoCount();
		this.lastRightTachoCount = this.right.getTachoCount();
	}

	public void forward() {
		this.isStopped = false;
		left.setPower(basePWM);
		right.setPower(basePWM);
		left.forward();
		right.forward();
	}

	public void backward() {
		this.isStopped = false;
		left.setPower(basePWM);
		right.setPower(basePWM);
		left.backward();
		right.backward();
	}

	public void stop() {
		left.stop();
		right.stop();
		//this.basePWM = 0;
		this.isStopped = true;
		// (new NXTRegulatedMotor(left_port)).resetTachoCount();
		// (new NXTRegulatedMotor(right_port)).resetTachoCount();
	}

	/**
	 * Return the driven distance in mm since relative to START
	 * @return distance in mm (backwards is negative)
	 */
	public int getDistance() {
		int tmp = this.left.getTachoCount() - this.initialLeftTachoCount;
		tmp += this.right.getTachoCount() - this.initialRightTachoCount;
		// tmp = tmp * this.durchmesser * 314 / (2 * 360 * 100); // 2 fuer den
		// // durchschnitt,
		// // 360 fuer
		// // umdrehungen
		tmp = tmp >> 2; // erstaunlich gute annäherung des auskommentierten
		return tmp;
	}
	
	/**
	 * Return the driven distance in mm since last reset()
	 * @return distance in mm (backwards is negative)
	 */
	public int getDistanceIncrement() {
		int tmp = this.left.getTachoCount() - this.lastLeftTachoCount;
		tmp += this.right.getTachoCount() - this.lastRightTachoCount;
		// tmp = tmp * this.durchmesser * 314 / (2 * 360 * 100); // 2 fuer den
		// // durchschnitt,
		// // 360 fuer
		// // umdrehungen
		tmp = tmp >> 2; // erstaunlich gute annäherung des auskommentierten
		return tmp;
	}

	/**
	 * Sets the power used by forward() and backward() 
	 * Robot will not move!
	 * If Robot is moving, speed will not change!
	 * @param power [-100..100]
	 */
	public void setBasePower(int power) {
		if (power < 0) {
			power = 0;
		} else if (power > 100) {
			power = 100;
		}
		this.basePWM = power;
	}

	/**
	 * If greater than 0, turn right
	 * 
	 * @param direction
	 */
	/*public void steer(int direction) {
		this.isStopped = false;
		// calcSpeed();

		if (direction < 0 && this.lastSteer > 0) {
			completeAngle += getDegreeSinceLastDirectionChange();
			saveCurrentTacho();
		} else if (direction > 0 && this.lastSteer < 0) {
			completeAngle += getDegreeSinceLastDirectionChange();
			saveCurrentTacho();
		}
		// LCD.drawInt(completeAngle, 6, 0, 1);
		this.lastSteer = direction;

		int left = this.basePWM + direction;
		int right = this.basePWM - direction;
		int max = Math.max(left, right) - 100;
		max = Math.max(0, max);
		left -= max;
		right -= max;

		left = Math.max(-100, left);
		right = Math.max(-100, right);

		if (left > 0) {
			this.left.forward();
			this.left.setPower(left);
		} else {
			this.left.backward();
			this.left.setPower(-left);
		}

		if (right > 0) {
			this.right.forward();
			this.right.setPower(right);
		} else {
			this.right.backward();
			this.right.setPower(-right);
		}

	}*/

	/*private void calcSpeed() {
		long curTime = System.currentTimeMillis();
		long distTime = curTime - lastMeasurementForHill;
		if (lastMeasurementForHill != -1) {
			if (distTime > 1000) {
				int curDist = getDistance();
				float speed = (curDist - lastDistance) / ((float) distTime);
				//LCD.drawInt(curDist, 0, 1);
				//LCD.drawInt(lastDistance, 0, 2);
				//LCD.drawString(Float.toString(speed), 0, 3);
				lastDistance = curDist;
				lastMeasurementForHill = curTime;

			}
		} else {
			lastMeasurementForHill = curTime;
			lastDistance = getDistance();
		}

	}*/

	/**
	 * Returns the angle of the robot relativ to the START
	 * @return angle in degrees (anticlockwise > 0)
	 */
	public float getAngle() {
		int leftDiff = this.left.getTachoCount() - this.initialLeftTachoCount;
		int rightDiff = this.right.getTachoCount() - this.initialRightTachoCount;

		return ((float) (-leftDiff + rightDiff)) * this.track_width_in_degree_inv;
	}


	/**
	 * Returns the angle of the robot relative to last reset()
	 * @return angle in degrees (anticlockwise > 0)
	 */
	public float getAngleIncrement() {
		int leftDiff = this.left.getTachoCount() - this.lastLeftTachoCount;
		int rightDiff = this.right.getTachoCount() - this.lastRightTachoCount;

		return ((float) (-leftDiff + rightDiff)) * this.track_width_in_degree_inv;
	}

	public void setBackwardPower(int power) {
		this.isStopped = false;
		this.left.setPower(power);
		this.right.setPower(power);
		this.left.backward();
		this.right.backward();
	}

	public void setForwardPower(int power) {
		this.isStopped = false;
		this.left.setPower(power);
		this.right.setPower(power);
		this.left.forward();
		this.right.forward();

	}
	
	/**
	 * 
	 * @param power [-100...100]
	 */
	public void setPowerRight(int power) {
		this.isStopped = false;
		if (power < 0) {
			this.right.setPower(-power);
			this.right.backward();
		} else {
			this.right.setPower(power);
			this.right.forward();
		}
	}
	
	/**
	 * 
	 * @param power [-100..100]
	 */
	public void setPowerLeft(int power) {
		this.isStopped = false;
		if (power < 0) {
			this.left.setPower(-power);
			this.left.backward();
		} else {
			this.left.setPower(power);
			this.left.forward();
		}
	}
	
	/**
	 * Sets the motors power to the given values [-100..100]
	 * Robot will drive!
	 * 
	 * @param powerLeft [-100..100]
	 * @param powerRight [-100..100]
	 */
	public void setPower(int powerLeft, int powerRight) {
		setPowerLeft(powerLeft);
		setPowerRight(powerRight);
	}

	public boolean isMoving() {
		return !isStopped;
	}
	
	
	/** Rotate the robot with the given power on the motors.
	 * Note: Resets the AngleIncrement and DistanceIncrement!
	 * 
	 * @param degrees angle in degrees (anticlockwise > 0)
	 * @param power [-100...100] (if power is negative degree is inverted!)
	 */
	public void rotate(int degrees, int power) {
		reset();
		isStopped = false;
		if (degrees < 0) {
			// clockwise
			setPower(power, -power);
			while (!isStopped && getAngleIncrement() > degrees) {
				Delay.msDelay(50);
			}	
		} else {
			// anticlockwise
			setPower(-power, power);
			while (!isStopped && getAngleIncrement() < degrees) {
				Delay.msDelay(50);
			}	
		}
		stop();
		reset();
	}
	
	/** Rotate the robot using baseRotatePower on the motors.
	 * Note: Resets the AngleIncrement and DistanceIncrement!
	 * 
	 * @param degrees angle in degrees (anticlockwise > 0)
	 */
	public void rotate(int degrees) {
		rotate(degrees, basePWM_rotate);
	}
	
	/**
	 * 
	 * @param power [0..100]
	 */
	public void setBaseRotatePower(int power) {
		if (power < 0) power = 0;
		if (power > 100) power = 100;
		
		basePWM_rotate = power;
	}
	
	/**
	 * Moves the robot for a specified distance with specified power.
	 * If distance or power is negative, the robot drives backwards.
	 * @param distance travel distance in mm
	 * @param power [-100..100]
	 */
	public void travel(int distance, int power) {
		reset();
		isStopped = false;
		if (distance < 0 || power < 0) {
			// backwards
			distance = -Math.abs(distance);
			power = -Math.abs(power);
			setPower(power, power);
			while (!isStopped && getDistanceIncrement() > distance) {
				Delay.msDelay(50);
			}	
		} else {
			// forward
			distance = Math.abs(distance);
			power = Math.abs(power);
			setPower(power, power);
			while (!isStopped && getDistanceIncrement() < distance) {
				Delay.msDelay(50);
			}	
		}
		stop();
		reset();
	}
	
	/**
	 * Moves the robot for a specified distance with BasePower power.
	 * If distance or power is negative, the robot drives backwards.
	 * @param distance travel distance in mm
	 */
	public void travel(int distance) {
		travel(distance, basePWM);
	}
	
	
}
