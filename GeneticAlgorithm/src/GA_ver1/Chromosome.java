package GA_ver1;

public class Chromosome {

	
	public Chromosome(double geneSum, int[] geneSource) {
		super();
		this.geneSum = geneSum;
		this.geneSource = geneSource;
	}
	double geneSum;
	int geneSource[] = new int[Main.locationCount];
	
}
