package simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import constant.CommonType;
import exceptions.EdgeFailureException;
import exceptions.PMFailureException;
import infrastructure.Edge;
import infrastructure.EdgeController;
import infrastructure.PM;
import infrastructure.VM;
import interfaces.Remote;

public class FailureSimulator2 implements Runnable {

	private EdgeController controller;
	private volatile Thread t;

	private CommonType handType;
	private CommonType simuType;

	public FailureSimulator2(EdgeController controller, CommonType handType, CommonType simuType) {
		super();
		this.controller = controller;
		this.handType = handType;
		this.simuType = simuType;
	}

	private int totalPMBaseJobFailures = 0;
	private int totalEdgeBaseJobFailures = 0;
	private int totalPMBaseJobFixes = 0;
	private int totalEdgeBaseJobFixes = 0;
	private int totalPMImproveJobFailures = 0;
	private int totalEdgeImproveJobFailures = 0;
	private int totalPMImproveJobFixes = 0;
	private int totalEdgeImproveJobFixes = 0;

	// public void start() {

	// final Timer timer = new Timer();

	// timer.schedule(new TimerTask() {

	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		Random rand = new Random();
		t = thisThread;
		while (t == thisThread) {
			System.out.println("");
			System.out.println("FailureSimulator: START " + getHandType());
			try {
				Edge edge = controller.getListOfEdges().get(rand.nextInt(controller.getListOfEdges().size()));
				PM pm = edge.getListOfPMs().get(rand.nextInt(edge.getListOfPMs().size()));

				ArrayList<Integer> pm_ids = getAllPMIDs();

				if (getSimuType() == CommonType.PM) {
					simulatePMFailure(pm_ids, getSimuType(), getHandType());
					System.out.println("-------------------- REPORT --------------------");
					System.out.println("Total PM base job failures = " + getTotalPMBaseJobFailures());
					System.out.println("Total PM base job Fixes = " + getTotalPMBaseJobFixes());
					System.out.println("Total PM improved job failures = " + getTotalPMImproveJobFailures());
					System.out.println("Total PM improved job Fixes = " + getTotalPMImproveJobFixes());
					System.out.println("-------------------------------------------------");
				} else if (getSimuType() == CommonType.EDGE) {
					simulateEdgeFailure(getSimuType(), getHandType());
					System.out.println("-------------------- REPORT --------------------");
					System.out.println("Total EDGE base job failures = " + getTotalEdgeBaseJobFailures());
					System.out.println("Total EDGE base job Fixes = " + getTotalEdgeBaseJobFixes());
					System.out.println("Total EDGE improved job failures = " + getTotalEdgeImproveJobFailures());
					System.out.println("Total EDGE improved job Fixes = " + getTotalEdgeImproveJobFixes());
					System.out.println("-------------------------------------------------");
				} else {
					System.out.println("No such failure simulation type. Retry... getSimuType()="+getSimuType());
				}

				long sleep = (long) Math.abs(Math.round(rand.nextGaussian() * 2000 + 2000));
				thisThread.sleep(sleep);

			} catch (InterruptedException e) {
			}
		}
	}


	public synchronized void simulatePMFailure(ArrayList<Integer> pm_ids, CommonType simuType, CommonType handType) {
		Random r = new Random();
		int numberFails = (int) Math.abs(Math.round(r.nextGaussian() * 2 + pm_ids.size() * 0.1));
		for (int i = 0; i < numberFails; i++) {
			int i2 = r.nextInt(pm_ids.size());
			PM pm = getPMbyID(pm_ids.get(i2));
			pm_ids.remove(i2);
			if (pm != null) {
				try {
					System.out.println("---------------------------");
					System.out.println("PM fails");
					pm.simulatePMFailure();
				} catch (PMFailureException e) {
					Thread failureThread = Thread.currentThread();
					if (failureThread.equals(t)) {
						if (handType == CommonType.RETRY) {
							int failedCount = 0;
							boolean isFailed = true;
							failedCount = recurRetry(isFailed, pm.getID(), failedCount, simuType);
							System.out.println("Total job failed: " + failedCount);
							totalPMBaseJobFailures = totalPMBaseJobFailures + failedCount;
							totalPMBaseJobFixes++;
						} else if (handType == CommonType.JOB_MIGRATION) {
							boolean isFailed = jobMigrationRequest(pm.getID(), simuType);
							if (!isFailed) {
								totalPMImproveJobFixes++;
							}
							if (isFailed) {
								totalPMImproveJobFailures++;
								int ran = (int) (Math.random() * 100);
								System.out.println("The request sent to PM " + ran);
								boolean isFaildAgain = recurMigration(isFailed, ran, 1, simuType);
								if (!isFaildAgain) {
									totalPMImproveJobFixes++;
								}
								if (isFaildAgain) {
									totalPMImproveJobFailures++;
									System.out.println("Retry failed again for PM " + ran);
								}
							}
						} else {
							System.out.println("No such failure handling type. Retry...");
						}
					}
				}
			}
		}
	}

	public synchronized void simulateEdgeFailure(CommonType simuType, CommonType handType) {
		HashMap<Integer, Edge> edges = controller.getEdges();
		ArrayList<Integer> edge_ids = new ArrayList<Integer>(edges.keySet());
		Random r = new Random();
		int numberFails = (int) Math.abs(Math.round(r.nextGaussian() * 2 + edge_ids.size() * 0.17));
		for (int i = 0; i < numberFails; i++) {
			int i2 = r.nextInt(edge_ids.size());
			Edge edge = edges.get(edge_ids.get(i2));
			edge_ids.remove(i2);
			if (edge != null) {
				try {
					System.out.println("---------------------------");
					System.out.println("Edge fails");
					edge.simulateEdgeFailure();
				} catch (EdgeFailureException e) {
					Thread failureThread = Thread.currentThread();
					if (failureThread.equals(t)) {
						if (handType == CommonType.RETRY) {
							int failedCount = 0;
							boolean isFailed = true;
							failedCount = recurRetry(isFailed, edge.getID(), failedCount, simuType);
							System.out.println("Total job failed: " + failedCount);
							totalEdgeBaseJobFailures = totalEdgeBaseJobFailures + failedCount;
							totalEdgeBaseJobFixes++;
						} else if (handType == CommonType.JOB_MIGRATION) {
							boolean isFailed = jobMigrationRequest(edge.getID(), simuType);
							if (!isFailed) {
								totalEdgeImproveJobFixes++;
							}
							if (isFailed) {
								totalEdgeImproveJobFailures++;
								int ran = (int) (Math.random() * 100);
								System.out.println("The request sent to EDGE " + ran);
								boolean isFaildAgain = recurMigration(isFailed, ran, 1, simuType);
								if (!isFaildAgain) {
									totalEdgeImproveJobFixes++;
								}
								if (isFaildAgain) {
									totalEdgeImproveJobFailures++;
									System.out.println("Retry failed again for EDGE " + ran);
								}
							}
						} else {
							System.out.println("No such failure handling type. Retry...");
						}
					}
				}
			}
		}
	}

	private ArrayList<Integer> getAllPMIDs() {
		ArrayList<Integer> pm_ids = new ArrayList<Integer>();
		for (Edge e : controller.getListOfEdges()) {
			for (int pm_id : e.getPms().keySet()) {
				pm_ids.add(pm_id);
			}
		}
		return pm_ids;
	}

	private PM getPMbyID(int id) {
		for (Edge e : controller.getListOfEdges()) {
			PM p = e.getPms().get(id);
			if (p != null) {
				return p;
			}
		}
		return null;
	}

	private ArrayList<Integer> prepareAvailablePMsOrEdges(int NUM) {
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		for (int i = 1; i <= NUM; i++) {
			int ran = (int) (Math.random() * 100);
			arrayList.add(ran);
		}
		return arrayList;

	}

	private boolean retryRequest(int id, CommonType simuType) {
		boolean isFailed = false;
		int count = 0;
		for (int j = 0; j < 5; j++) {
			ArrayList<Integer> avbPMList = prepareAvailablePMsOrEdges(50);
			count++;
			System.out.println("Retry request " + count);
			for (Iterator<Integer> iterator = avbPMList.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				if (integer.equals(id)) {
					isFailed = false;
				} else {
					isFailed = true;
				}
			}
			if (!isFailed) {
				if (simuType == CommonType.PM) {
					totalPMBaseJobFixes++;
				} else if (simuType == CommonType.EDGE) {
					totalEdgeBaseJobFixes++;
				}
				System.out.println("Failure fixed for " + simuType + " " + id);
				break;
			}
		}
		return isFailed; 
	}

	private boolean jobMigrationRequest(int id, CommonType simuType) {
		boolean isFailed = false;
		int count = 0;
		for (int j = 0; j < 2; j++) {
			ArrayList<Integer> avbPMList = prepareAvailablePMsOrEdges(100);
			count++;
			System.out.println("Retry request " + count);
			for (Iterator<Integer> iterator = avbPMList.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				if (integer.equals(id)) {
					isFailed = false;
				} else {
					isFailed = true;
				}
			}
			if (!isFailed) {
				System.out.println("Retry success with " + simuType + " " + id);
				break;
			}
		}
		return isFailed;
	}

	public int recurRetry(boolean isFailed, int id, int count, CommonType simuType) {
		if (!isFailed)
			return count;
		isFailed = retryRequest(id, simuType);
		if (isFailed)
			count++;
		return recurRetry(isFailed, id, count, simuType);
	}

	public boolean recurMigration(boolean isFailed, int id, int count, CommonType simuType) {
		if (count > 1 || !isFailed)
			return isFailed;
		isFailed = jobMigrationRequest(id, simuType);
		if (isFailed)
			count++;
		return recurMigration(isFailed, id, count, simuType);
	}

	public CommonType getHandType() {
		return handType;
	}

	public CommonType getSimuType() {
		return simuType;
	}

	public int getTotalPMBaseJobFailures() {
		return totalPMBaseJobFailures;
	}

	public int getTotalEdgeBaseJobFailures() {
		return totalEdgeBaseJobFailures;
	}

	public int getTotalPMBaseJobFixes() {
		return totalPMBaseJobFixes;
	}

	public int getTotalEdgeBaseJobFixes() {
		return totalEdgeBaseJobFixes;
	}

	public int getTotalPMImproveJobFailures() {
		return totalPMImproveJobFailures;
	}

	public int getTotalEdgeImproveJobFailures() {
		return totalEdgeImproveJobFailures;
	}

	public int getTotalPMImproveJobFixes() {
		return totalPMImproveJobFixes;
	}

	public int getTotalEdgeImproveJobFixes() {
		return totalEdgeImproveJobFixes;
	}
}
