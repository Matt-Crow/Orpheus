package util;

public class Number {
	public static int minMax(int min, int value, int max){
		/**
		 * @param min : the smallest number that can be returned
		 * @param value : the value to check
		 * @param max : the largest value that can be returned
		 * @return : if value is between min and max, returns value, if it is larger than max, returns max, otherwise it returns min
		 */
		int ret = value;
		if(value > max){
			ret = max;
		} else if(value < min){
			ret = min;
		}
		return ret;
	}
}
