package kit.edu.irobot.utils;

public class Buffer {

	private int size = 0;
	private float[] buffer;
	
	private int count = 0;
	
	public Buffer(int size) {
		this.size = size; 
		this.buffer = new float[this.size];
	}
	
	public void add(float value) {
		this.buffer[count] = value; 
		this.count++;
		
		if(this.count == this.size) {
			this.count = 0;
		}
	}
	
	public float get(int index) {
		return this.buffer[index];
	}
	
	public float sum() {
		float sum = 0;
		for(int index = 0; index < this.size; index++) {
			sum += this.get(index);
		}
		
		return sum; 
	}
	
	public float average() {
		float sum = this.sum(); 
		return sum / this.size; 
	}	
	
	public float max() {
		float max = this.buffer[0];
		
		for(int index = 0; index < this.size; index++) {
			max = Math.max(max, this.buffer[index]);
		}
		
		return max; 
	}
	
	public float min() {
		float min = this.buffer[0];
		
		for(int index = 0; index < this.size; index++) {
			min = Math.min(min, this.buffer[index]);
		}
		
		return min; 
	}
}
