package GA_source;

class TestThread extends Thread{

	private boolean stop;
	
	TestThread() {
		this.stop = false;
	}
	
	public void run() {
		while(!stop) {
			System.out.println("무한 실행");
		}
		System.out.println("실행 종료");
	}
	
	public void threadStop(boolean stop) {
		this.stop = stop;
	}
}

public class TimeOut {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestThread thread = new TestThread();
		thread.start();
		
		try {
			Thread.sleep(500);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		thread.threadStop(true);
	}
	
}