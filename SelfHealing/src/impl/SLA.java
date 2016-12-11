package impl;

public class SLA {

	private int size;
	private int agreedMemory;
	private int agreedCPU;
	private int networkBandwidth;
	private int networkBandwidthBEdges;
	private int latency;
	private double infrasrtuctureAvailability;

	public SLA(int size, int agreedMemory, int agreedCPU, int networkBandwidth, int networkBandwidthBEdges, int latency,
			double d) {
		super();
		this.size = size;
		this.agreedMemory = agreedMemory;
		this.agreedCPU = agreedCPU;
		this.networkBandwidth = networkBandwidth;
		this.networkBandwidthBEdges = networkBandwidthBEdges;
		this.latency = latency;
		this.infrasrtuctureAvailability = d;
	}

	public int getSize() {
		return size;
	}

	public int getAgreedMemory() {
		return agreedMemory;
	}

	public int getAgreedCPU() {
		return agreedCPU;
	}

	public int getNetworkBandwidth() {
		return networkBandwidth;
	}

	public int getNetworkBandwidthBEdges() {
		return networkBandwidthBEdges;
	}

	public int getLatency() {
		return latency;
	}

	public double getInfrasrtuctureAvailability() {
		return infrasrtuctureAvailability;
	}

	
}

