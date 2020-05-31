package GA_ver2;

class TestThread {
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
