package GA_ver1;

public class Initialization {

	//chromosome 성분 로또형식으로 설정
	int[] temp = new int[Setting.locationCount];
	static Chromosome ch[] = new Chromosome[Setting.PopulationLength];
	void chromoRandom() {
		for(int i = 0; i < Setting.locationCount; i++) {
			temp[i] = (int)(Math.random()*Setting.locationCount);
			for(int j = 0; j < i; j++) {
				if(temp[i] == temp[j]) {
					i--;
					break;
				}
			}
		}
	}
	
	//chromosome 배열 초기화
	void chromoSet() {
		for(int i = 0; i < Setting.PopulationLength; i++) {
			for(int j = 0; j < Setting.locationCount; j++) {
				chromoRandom();
				ch[i] = new Chromosome(0, temp);
			}
			for(int j = 0; j < Setting.locationCount; j++) {
				if(j == Setting.locationCount-1) {
					ch[i].geneSum += Setting.gene[ch[i].geneSource[j]][ch[i].geneSource[0]];
				}else {
					ch[i].geneSum += Setting.gene[ch[i].geneSource[j]][ch[i].geneSource[j+1]];
				}
			}
		}
	}
}
