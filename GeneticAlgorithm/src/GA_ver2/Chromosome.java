package GA_ver2;

public class Chromosome implements Comparable<Chromosome>{

	
	public Chromosome(double geneSum, int[] geneSource) {
		super();
		this.geneSum = geneSum;
		this.geneSource = geneSource;
	}
	double geneSum;
	int geneSource[] = new int[Source.locationCount];
	
	@Override
	public int compareTo(Chromosome c) {
		// TODO Auto-generated method stub
		if(this.geneSum < c.geneSum) {
			return -1;
		} else if(this.geneSum == c.geneSum) {
			return 0;
		} else {
			return 1;
		}
	}
	
}
