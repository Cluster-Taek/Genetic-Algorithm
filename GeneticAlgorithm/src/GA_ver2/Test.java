package GA_ver2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {

	static int locationCount = 11;
	/*
	 case 1 : 실행속도 감소를 감안하고 ArrayList로 변형
	 case 2 : 실행속도 증가를 위해 array를 사용하고 locationCount만 수동으로 초기화
	 */
	static double maxTimer;
	static int generation;
	static int populationLength = 50; //유전자의 양은 얼마나 많이 보유할 것인가?
	static double selectionPressure = 0.7; //상위 유전자를 얼마나 들고 올 것인가?
	static double mutateProbability = 0.2; //돌연변이의 확률은 얼마인가?
	static int generationPrint = 1000; //몇 세대마다 출력을 할 것인가?
	static int generationCut = 500; //몇 세대가 반복 시 Hill Climbing을 사용할 것인가?
	
	static double location[][] = new double[locationCount][2];
	static double gene[][] = new double[locationCount][locationCount];
	static Chromosome ch[] = new Chromosome[populationLength];
	static Chromosome chMix[] = new Chromosome[populationLength*3/2];
	static Chromosome chNew[] = new Chromosome[populationLength];
	static Chromosome chHistory[] = new Chromosome[generationCut];
	
	static Chromosome parentA;
	static Chromosome parentB;
	
	
	static double sumSet(int[] temp) {
		double sumTemp = 0;
		for(int j = 0; j <locationCount; j++) {
			if(j == locationCount-1) {
				sumTemp += gene[temp[j]][temp[0]];
			}else {
				sumTemp += gene[temp[j]][temp[j+1]];
			}
		}
		return sumTemp;
	}
	static Chromosome twoopt(Chromosome c) {
		Chromosome x = c;
		double minSum = c.geneSum;
		for(int sp = 0; sp < locationCount; sp++) {
			for(int ep = sp; ep < locationCount; ep++) {
				int[] temp = c.geneSource;
				int sum = (ep+sp);
				for(int i = sp; i <= sum/2; i++) {
					int tmp = temp[i];
					temp[i] = temp[sum-i];
					temp[sum-i] = tmp;
				}
				if(sumSet(temp) < minSum) {
					x = new Chromosome(sumSet(temp), temp);
					minSum = sumSet(temp);
				}
			}
		}
		return x;
	}
	static int[] randomSet() {
		int temp[] = new int[locationCount];
		List<Integer> tempList = new ArrayList<Integer>();
		for(int i = 0; i < locationCount; i++) {
			tempList.add(i);
		}
		Collections.shuffle(tempList);
		for(int i = 0; i < locationCount; i++) {
			temp[i] = tempList.get(i);
		}
		return temp;
	}
	static void chromoSet() {
		for(int index = 0; index <populationLength; index++) {
			int temp[] = randomSet();
			ch[index] = new Chromosome(sumSet(temp), temp);
			ch[index] = twoopt(ch[index]);
		}
	}
	static void locationInput() {
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
	}
	//gene 초기화
	static void geneSet() {
		for(int i = 0; i < locationCount; i++) {
			for(int j = 0; j < locationCount; j++) {
				double xlength = location[i][0] - location[j][0]; 
				double ylength = location[i][1] - location[j][1]; 
				gene[i][j] = Math.sqrt((xlength*xlength)+(ylength*ylength));
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		locationInput();
		geneSet();
		int a[] = randomSet();
		Chromosome test = new Chromosome(sumSet(a), a);
		System.out.println(Arrays.toString(test.geneSource) + "\n" + test.geneSum + "\n");
		Chromosome tpt = twoopt(test);
		System.out.println(Arrays.toString(tpt.geneSource) + "\n" + tpt.geneSum + "\n");
	}

}
