package comparables;

import infrastructure.PM;

public class ComparablePM implements Comparable<ComparablePM> {
	private final PM pm;
	private final double expectedEnergyUtilization;
	
	public ComparablePM(PM pm, double expectedEnergyUtilization){
		this.pm = pm;
		this.expectedEnergyUtilization = expectedEnergyUtilization;
	}
	
	public PM getPm() {
		return pm;
	}

	public double getExpectedEnergyUtilization() {
		return expectedEnergyUtilization;
	}
	
	
	@Override
	public String toString() {
		return "ComparablePM [pm=" + pm + ", expectedEnergyUtilization=" + expectedEnergyUtilization + "]";
	}

	@Override
	public int compareTo(ComparablePM pm2) {
		if(this.expectedEnergyUtilization > pm2.expectedEnergyUtilization)
			return 1;
		else if(this.expectedEnergyUtilization < pm2.expectedEnergyUtilization)
			return -1;
		
		return 0;
	}

	

}
