package GA_ver2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Source extends Thread{

	static int locationCount = 101;
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
	/*
	 * mimitic Algorithm 구현
	 * two-opt 알고리즘 사용
	 * 경로를 다 풀어주고 시작
	 * 
	 * Hill Climbing Method
	 * local에 빠졌다고 확인이 되었을때
	 * 원래 Population을 4분위를 해서 4값만 들고 나머지는 다시 새로 initialization
	 * 
	 * 수렴 : Exploriting 익스플로러레이션
	 * */
	
	
	
	
	//cycle에서 location 가져오기
	static void locationInput() {
		try {
			String filePath = "cycles/cycle101.in";
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
	//temp에 랜덤 chromosome 저장
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
	//temp의 geneSum 저장
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
	
	//chromosome 초기화
	static void chromoSet() {
		for(int index = 0; index <populationLength; index++) {
			int temp[] = randomSet();
			ch[index] = new Chromosome(sumSet(temp), temp);
			ch[index] = twoopt(ch[index]);
		}
	}
	//two-opt 알고리즘 구현
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
	//Chromosome배열을 geneSum에 따라 오름차순 정렬
	static void sorting(Chromosome[] c) {
		Arrays.sort(c);
	}
	//Chromosome배열에서 부모 Chromosome 설정
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
		c = new Chromosome(sumSet(temp), temp);
		return c;
		
	}
	//c를 받아 mutateProbability에 따라 원래 값 혹은 돌연변이 값으로 반환
	static Chromosome mutate(Chromosome c) {
		if(Math.random() < mutateProbability) {
			Chromosome x;
			int[] temp = c.geneSource;
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
			x = new Chromosome(sumSet(temp), temp);
			return x;
		}else {
			return c;
		}
	}
	//chMix에서 ch로 상위 Chromosome만 선택
	static void replace() {
		for(int i = 0; i < populationLength; i++) {
			ch[i] = chMix[i];
		}
	}
	//hill climbing 구현
	static void climbing() {
		for(int index = 0; index < populationLength; index++) {
			int temp[] = randomSet();
			chNew[index] = new Chromosome(sumSet(temp), temp);
		}
		chNew[0] = ch[0];
		chNew[populationLength/2] = ch[populationLength/2];
		chNew[populationLength-1] = ch[populationLength-1];
		ch = chNew;
		sorting(ch);
	}
	//Print
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
	//전체 구동부
	public void run() {
		try {
			int index = 0;
			while(true) {
				locationInput();
				geneSet();
				chromoSet();
				generation = 0;
				while(true) {
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
					if(generation % generationPrint == 0) {
						System.out.println(generation + "generation : " + ch[0].geneSum);
					}
					if(ch[0] == chHistory[index]) {
						index++;
						chHistory[index] = ch[0];
					} else {
						index = 0;
						chHistory[index] = ch[0];
					}
					if(index == generationCut-1) {
						System.out.println("Hill Climbing....");
						climbing();
						index = 0;
					}
					generation++;
					Thread.sleep(0);
				}
			}
		}catch (InterruptedException e) {}
		System.out.println("\nSatisfied Solution\n" + Arrays.toString(ch[0].geneSource) + '\n' + ch[0].geneSum);
	}
}
public class Main extends Source{
	
	public static void main(String [] args) {
		// TODO Auto-generated method stub 
		Source thread = new Source();
		thread.start();
		int timer = 12*60;
		try {
			Thread.sleep(1000 * timer);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		thread.interrupt();
	}
}
