package GA_ver1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Main {

	static int locationCount = 11;
	static double maxTimer;
	static int populationLength = 50;
	static double selectionPressure = 0.7;
	static double mutateProbability = 0.15;
	static int maxGeneration = 500;
	
	static double location[][] = new double[locationCount][2];
	static double gene[][] = new double[locationCount][locationCount];
	static Chromosome ch[] = new Chromosome[populationLength];
	static Chromosome chMix[] = new Chromosome[populationLength*3/2];
	static Chromosome parentA;
	static Chromosome parentB;

	static Chromosome G;
	//cycle에서 location 가져오기
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
		/*System.out.println("LocationCount : " + locationCount);
		System.out.println("Location Point : ");
		for(int i = 0; i < location.length; i++) {
			for(int j = 0; j < 2; j++) {
				System.out.print(location[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("maxTimer : " + maxTimer);*/
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
	
	//chromosome 초기화
	static void chromoSet() {
		
		for(int index = 0; index <populationLength; index++) {
			//temp에 랜덤 chromosome 저장
			int[] temp = new int[locationCount];
			double sumTemp = 0;
			for(int i = 0; i < locationCount; i++) {
				temp[i] = (int)(Math.random()*locationCount);
				for(int j = 0; j < i; j++) {
					if(temp[i] == temp[j]) {
						i--;
						break;
					}
				}
			}
			//temp의 geneSum 저장
			for(int j = 0; j <locationCount; j++) {
				if(j == locationCount-1) {
					sumTemp += gene[temp[j]][temp[0]];
				}else {
					sumTemp += gene[temp[j]][temp[j+1]];
				}
			}
			ch[index] = new Chromosome(sumTemp, temp);
		}
	}
	
	//Chromosome배열을 geneSum에 따라 오름차순 정렬
	static void sorting(Chromosome[] c) {
		for(int i = 0 ; i < c.length; i ++) {
			for(int j = 0 ; j < c.length-i-1; j ++) {
				if(c[j].geneSum > c[j+1].geneSum) {
					Chromosome temp = c[j];
					c[j] = c[j+1];
					c[j+1] = temp;
				}
			}
		}
	}
	
	static void select() {
		if(Math.random() < selectionPressure) {
			parentA = ch[(int)(Math.random()*populationLength/2)];
		}else {
			parentA = ch[(int)(Math.random()*populationLength/2) + (populationLength/2)];
		}
		if(Math.random() < selectionPressure) {
			parentB = ch[(int)(Math.random()*populationLength/2)];
		}else {
			parentB = ch[(int)(Math.random()*populationLength/2) + (populationLength/2)];
		}
	}
	
	//부모 Chromosome 두 개를 랜덤으로 받아 crossOver
	static Chromosome crossOver(Chromosome a, Chromosome b) {
		Chromosome c;
		int temp[] = new int[locationCount];
		for(int i = 0; i < locationCount; i++) {
			temp[i] = -1;
		}
		double sumTemp = 0;
		int cutPoint = (int)(Math.random()*locationCount);
		for(int i = 0; i < cutPoint; i++) {
			temp[i] = a.geneSource[i];
		}
		for(int i = 0; i < locationCount; i++) {
			boolean tf = false;
			for(int j = 0; j < locationCount; j++) {
				if(b.geneSource[i] == temp[j]) {
					tf = true;
				}
			}
			if(cutPoint == locationCount) {
				break;
			}
			if(tf == false) {
				temp[cutPoint] = b.geneSource[i];
				cutPoint++;
			}
		}
	
		for(int i = 0; i < locationCount; i++) {
			if(i == locationCount-1) {
				sumTemp += gene[temp[i]][temp[0]];
			}else {
				sumTemp += gene[temp[i]][temp[i+1]];
			}
		}
		c = new Chromosome(sumTemp, temp);
		return c;
		
	}
	
	//c를 받아 mutateProbability에 따라 원래 값 혹은 돌연변이 값으로 반환
	static Chromosome mutate(Chromosome c) {
		Chromosome x;
		if(Math.random() < mutateProbability) {
			int[] temp = c.geneSource;
			double sumTemp = 0;
			int sp = (int)(Math.random()*locationCount);
			int ep = (int)(Math.random()*locationCount);
			int sum = (ep+sp);
			if(sp > ep) {
				int tmp = sp;
				sp = ep;
				ep = tmp;
			}
			for(int i = sp; i <= sum/2; i++) {
				int tmp = temp[i];
				temp[i] = temp[sum-i];
				temp[sum-i] = tmp;
			}
			for(int i = 0; i <locationCount; i++) {
				if(i == locationCount-1) {
					sumTemp += gene[temp[i]][temp[0]];
				}else {
					sumTemp += gene[temp[i]][temp[i+1]];
				}
			}
			x = new Chromosome(sumTemp, temp);
			return x;
		}else {
			return c;
		}
	}
	
	static void replace() {
		for(int i = 0; i < populationLength; i++) {
			ch[i] = chMix[i];
		}
	}
	
	static void print(Chromosome[] c) {
		for(int i = 0; i < c.length; i++) {
			System.out.print("geneSource : ");
			for(int j = 0; j < locationCount; j++) {
				System.out.print(c[i].geneSource[j] + " ");
			}
			System.out.println();
			System.out.println("Total Length : " + c[i].geneSum);
		}
	}
	
	static void printGene() {
		for(int i = 0; i < locationCount; i++) {
			for(int j = 0; j < locationCount; j++) {
				System.out.print(gene[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		locationInput();
		geneSet();
		chromoSet();
		int generation = 0;
		while(generation < maxGeneration) {
			sorting(ch);
			for(int i = 0; i < populationLength; i++) {
				chMix[i] = ch[i];
			}
			for(int i = 50; i < chMix.length; i++) {
				select();
				chMix[i] = mutate(crossOver(parentA, parentB));
			}
			sorting(chMix);
			replace();
			System.out.println(generation + 1 + "generation : " + ch[0].geneSum);
			System.out.println(Arrays.toString(ch[0].geneSource));
			generation++;
		}
	}

}
