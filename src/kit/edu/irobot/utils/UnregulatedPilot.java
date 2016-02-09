package kit.edu.irobot.utils;

import kit.edu.irobot.robot.Robot;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.robotics.EncoderMotor;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveController;
import lejos.robotics.navigation.MoveListener;
import lejos.hardware.ev3.LocalEV3;

public class UnregulatedPilot {

	private final Port left_port, right_port;
	private final UnregulatedMotor left, right;
	private final int abstand;
	private final int durchmesser;

	private boolean isStopped = false;
	
	private int basePWM = 50;
	private int lastSteer = 0;

	private final int initialLeftTachoCount;
	private final int initialRightTachoCount;
	private int lastLeftTachoCount;
	private int lastRightTachoCount;

	private final float unit; // in mm
	public final float abstand_in_degree_inv;

	private int lastDistance;
	private long lastMeasurementForHill = -1;
	private int completeAngle = 0;

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
		this.left_port = MotorPort.B;
		this.right_port = MotorPort.A;
		this.abstand = abstand;
		this.durchmesser = durchmesser;

		this.unit = (float) Math.PI * this.durchmesser / 360.0f;
		this.abstand_in_degree_inv = 360.0f / (2.0f * (float) Math.PI * this.abstand / this.unit);

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

	public float getDegreeSinceLastDirectionChange() {
		int leftDiff = this.left.getTachoCount() - this.lastLeftTachoCount;
		int rightDiff = this.right.getTachoCount() - this.lastRightTachoCount;

		return ((float) (leftDiff - rightDiff)) * this.abstand_in_degree_inv;
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
	 * Return a number, that gets bigger, the longer you go forward. In mm
	 * 
	 * @return
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
	 * Returns an estimate of the heading of the robot, relativ to the start
	 * position
	 * 
	 * @return
	 */
	public float getAngleIncrement() {
		int leftDiff = this.left.getTachoCount() - this.initialLeftTachoCount;
		int rightDiff = this.right.getTachoCount() - this.initialRightTachoCount;

		return ((float) (leftDiff - rightDiff)) * this.abstand_in_degree_inv;
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
			this.right.setPower(-power);
			this.right.backward();
		} else {
			this.right.setPower(power);
			this.right.forward();
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
}
