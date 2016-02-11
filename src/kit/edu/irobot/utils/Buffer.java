package kit.edu.irobot.utils;

public class Buffer {
	private float[] buffer;
	
	private int count = 0;
	
	public Buffer(int size) {
		this.buffer = new float[size];
	}
	
	public void add(float value) {
		this.buffer[count] = value; 
		this.count++;
		
		if(this.count == buffer.length) {
			this.count = 0;
		}
	}
	
	public float get(int index) {
		return this.buffer[index];
	}
	
	// TODO calculate sum on add
	public float sum() {
		float sum = 0;
		for(int index = 0; index < buffer.length; index++) {
			sum += this.get(index);
		}
		
		return sum; 
	}
	
	public float average() {
		float sum = this.sum(); 
		return sum / buffer.length; 
	}	
	
	public float max() {
		float max = this.buffer[0];
		
		for(int index = 0; index < buffer.length; index++) {
			max = Math.max(max, this.buffer[index]);
		}
		
		return max; 
	}
	
	public float min() {
		float min = this.buffer[0];
		
		for(int index = 0; index < buffer.length; index++) {
			min = Math.min(min, this.buffer[index]);
		}
		
		return min; 
	}
	
	public void reset(){
		this.buffer = new float[buffer.length];
		this.count = 0;
	}
	
	public void reset(float value) {
		this.reset();
		
		for(int index = 0; index < buffer.length; index++) {
			this.add(value);
		}
	}
}
