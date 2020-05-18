package GA_ver1;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Setting {
	
	static int locationCount = 11;
	static double maxTimer;
	static int PopulationLength = 50;
	
	static double location[][] = new double[locationCount][2];
	static double gene[][] = new double[locationCount][locationCount];
	
	void locationInput() {
		try {
			String filePath = "cycles/cycle11.in";
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String temp = br.readLine();
			locationCount = Integer.parseInt(temp);
			
			for(int i = 0; i < locationCount; i++) {
				temp = br.readLine();
				String tempArray[] = temp.split(" ");
				location[i][0] = Double.parseDouble(tempArray[0]);
				location[i][1] = Double.parseDouble(tempArray[1]);
			}
			temp = br.readLine();
			maxTimer = Double.parseDouble(temp);
		}catch (IOException e) {
			e.getStackTrace();
		}
		System.out.println("LocationCount : " + locationCount);
		System.out.println("Location Point : ");
		for(int i = 0; i < location.length; i++) {
			for(int j = 0; j < 2; j++) {
				System.out.print(location[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("maxTimer : " + maxTimer);
	}
	
	void geneSet() {
		for(int i = 0; i < locationCount; i++) {
			for(int j = 0; j < locationCount; j++) {
				double xlength = location[i][0] - location[j][0]; 
				double ylength = location[i][1] - location[j][1]; 
				gene[i][j] = Math.sqrt((xlength*xlength)+(ylength*ylength));
			}
		}
	}
}
