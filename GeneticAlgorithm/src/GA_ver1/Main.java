package GA_ver1;

import java.util.Arrays;

public class Main {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Setting s = new Setting();
		Initialization in = new Initialization();
		
		s.locationInput();
		s.geneSet();
		in.chromoSet();

		for(int i = 0; i < Setting.PopulationLength; i++) {
			for(int j = 0; j < Setting.locationCount; j++) {
				System.out.print(Initialization.ch[i].geneSource[j] + " ");
			}
			System.out.println(i + 1 + " : " + Initialization.ch[i].geneSum);
		}
	}

}
