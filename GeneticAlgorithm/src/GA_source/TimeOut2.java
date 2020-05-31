package GA_source;

class TestThread2 extends Thread{
	public void run() {
		try {
			while(true) {
				System.out.println("무한 실행");
				Thread.sleep(1);
			}
		}catch (InterruptedException e) {}
		System.out.println("실행 종료");
	}
}

public class TimeOut2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestThread2 thread = new TestThread2();
		thread.start();
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		thread.interrupt();
	}

}
