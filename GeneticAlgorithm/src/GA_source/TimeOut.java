package GA_source;

public class TimeOut {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	}
	
	public static void test() {
		StopThread stThread = new StopThread();
		Thread thread = new Thread(stThread);
		thread.start();
		
	}
	

}

class StopThread implements Runnable {
	public void run() {
		try {
			while(!Thread.currentThread().isInterrupted()) {
				System.out.println("sleeping");
				Thread.sleep(10000);
				System.out.println("live!");
			}
		}catch (InterruptedException e) {
			
		}finally {
			System.out.println("dead");
		}
	}
}