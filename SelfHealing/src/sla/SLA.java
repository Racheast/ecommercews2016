package sla;

public class SLA {

	private final int agreedSize;
	private final int agreedMemory;
	private final int agreedCPU;
	private final int agreedNetworkBandwidth;
	private final int agreedNetworkBandwidthBEdges;
	private final int agreedLatency;
	private final double agreedInfrasrtuctureAvailability;
	
	public SLA(int agreedSize, int agreedMemory, int agreedCPU, int agreedNetworkBandwidth,
			int agreedNetworkBandwidthBEdges, int agreedLatency, double agreedInfrasrtuctureAvailability) {
		super();
		this.agreedSize = agreedSize;
		this.agreedMemory = agreedMemory;
		this.agreedCPU = agreedCPU;
		this.agreedNetworkBandwidth = agreedNetworkBandwidth;
		this.agreedNetworkBandwidthBEdges = agreedNetworkBandwidthBEdges;
		this.agreedLatency = agreedLatency;
		this.agreedInfrasrtuctureAvailability = agreedInfrasrtuctureAvailability;
	}

	public int getAgreedSize() {
		return agreedSize;
	}

	public int getAgreedMemory() {
		return agreedMemory;
	}

	public int getAgreedCPU() {
		return agreedCPU;
	}

	public int getAgreedNetworkBandwidth() {
		return agreedNetworkBandwidth;
	}

	public int getAgreedNetworkBandwidthBEdges() {
		return agreedNetworkBandwidthBEdges;
	}

	public int getAgreedLatency() {
		return agreedLatency;
	}

	public double getAgreedInfrasrtuctureAvailability() {
		return agreedInfrasrtuctureAvailability;
	}

	

	
}

