package GA_ver2;

import java.util.Arrays;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int a[] = {1,2,3,4,5};
		int b[] = new int[5];
		System.out.println(Arrays.toString(a));
		System.out.println(Arrays.toString(b));
		b = a;
		System.out.println(Arrays.toString(b));
	}

}
