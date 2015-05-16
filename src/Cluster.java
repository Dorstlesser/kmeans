import java.util.ArrayList;

public class Cluster {
	final double bigNumber = Math.pow(10, 10);  // some big number that's sure to
												// be larger than our data
												// range.
	double minimum = bigNumber; 				// The minimum value to beat.
	double distance = 0.0; 						// The current minimum value.
	int sampleNumber = 0;
	int cluster = 0;
	boolean isStillMoving = true;
	Data newData = null;

	private final int NUM_CLUSTERS = 3; // Total clusters.
	private final int TOTAL_DATA = 7; // Total data points.

	private final double SAMPLES[][] = new double[][] { { 1.0, 1.0 },
			{ 1.5, 2.0 }, { 3.0, 4.0 }, { 5.0, 7.0 }, { 3.5, 5.0 },
			{ 4.5, 5.0 }, { 3.5, 4.5 } };

	private ArrayList<Data> dataSet = new ArrayList<Data>();
	private ArrayList<Centroid> centroids = new ArrayList<Centroid>();

	public void initialize() {
		
		// Initializing Centroids depending on how many K is
		
		System.out.println("Centroids made:");

		centroids.add(new Centroid(1.0, 1.0)); // lowest set.
		centroids.add(new Centroid(5.0, 7.0)); // highest set.
		centroids.add(new Centroid(3.5, 5.0)); // cluster 3
		
		//Print where Centroids have been initialized
		System.out.println("     (" + centroids.get(0).X() + ", "
				+ centroids.get(0).Y() + ")");
		System.out.println("     (" + centroids.get(1).X() + ", "
				+ centroids.get(1).Y() + ")");
		System.out.print("\n");
		return;
	}

	public void kMeans() {
		Euclidean euc = new Euclidean();

		// Add in new data, one at a time, recalculating centroids with each new
		// one.
		while (dataSet.size() < TOTAL_DATA) {
			newData = new Data(SAMPLES[sampleNumber][0],
					SAMPLES[sampleNumber][1]);
			dataSet.add(newData);
			minimum = bigNumber;
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				distance = euc.calculate(newData, centroids.get(i));
				if (distance < minimum) {
					minimum = distance;
					cluster = i;
				}
			}
			newData.cluster(cluster);

			// Calculate the new Centroids
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				int totalX = 0;
				int totalY = 0;
				int totalInCluster = 0;
				for (int j = 0; j < dataSet.size(); j++) {
					if (dataSet.get(j).cluster() == i) {
						totalX += dataSet.get(j).X();
						totalY += dataSet.get(j).Y();
						totalInCluster++;
					}
				}
				if (totalInCluster > 0) {
					centroids.get(i).X(totalX / totalInCluster);
					centroids.get(i).Y(totalY / totalInCluster);
				}
			}
			sampleNumber++;

			// Now, keep shifting centroids until equilibrium occurs.
			while (isStillMoving) {
				// calculate new centroids.
				for (int i = 0; i < NUM_CLUSTERS; i++) {
					int totalX = 0;
					int totalY = 0;
					int totalInCluster = 0;
					for (int j = 0; j < dataSet.size(); j++) {
						if (dataSet.get(j).cluster() == i) {
							totalX += dataSet.get(j).X();
							totalY += dataSet.get(j).Y();
							totalInCluster++;
						}
					}
					if (totalInCluster > 0) {
						centroids.get(i).X(totalX / totalInCluster);
						centroids.get(i).Y(totalY / totalInCluster);
					}
				}

				// Assign all data to the new centroids
				isStillMoving = false;

				for (int i = 0; i < dataSet.size(); i++) {
					Data tempData = dataSet.get(i);
					minimum = bigNumber;
					for (int j = 0; j < NUM_CLUSTERS; j++) {
						distance = euc.calculate(tempData, centroids.get(j));
						if (distance < minimum) {
							minimum = distance;
							cluster = j;
						}
					}
					tempData.cluster(cluster);
					if (tempData.cluster() != cluster) {
						tempData.cluster(cluster);
						isStillMoving = true;
					}
				}

			}
			return;
			
		}
	}

}
