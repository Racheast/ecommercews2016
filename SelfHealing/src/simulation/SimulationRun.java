
package simulation;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import constant.CommonType;
import infrastructure.Edge;
import infrastructure.EdgeController;
import infrastructure.PM;
import infrastructure.Request;
import infrastructure.VM;
import interfaces.Remote;

import sla.SLA;

public class SimulationRun{
	private final int x_max;
	private final int y_max;
	private HashMap<Integer, SLA> slas;
	private HashMap<Integer, VM> vms;
	private final Timer timer;
	private final EdgeController controller;
	//private FailureSimulator failureSimulator;
	private FailureSimulator2 failureSimulator2_pm;
	private FailureSimulator2 failureSimulator2_edge;
	
	private ExecutorService executor;
	
	/*
	 * Randomized constructor
	 */
	public SimulationRun(int x_max, int y_max, boolean improved) {
		this.executor = Executors.newCachedThreadPool();

		this.timer=new Timer();
		vms = new HashMap<Integer, VM>();
		slas = generateSLAs();
		this.x_max = x_max;
		this.y_max = y_max;
		this.controller = new EdgeController(generateRandomEdgeMap(), improved);
		
		if(improved == false){
			this.failureSimulator2_pm = new FailureSimulator2(this.controller, CommonType.PM, CommonType.RETRY);
			this.failureSimulator2_edge = new FailureSimulator2(this.controller, CommonType.EDGE, CommonType.RETRY);
		} else {
			this.failureSimulator2_pm = new FailureSimulator2(this.controller, CommonType.PM, CommonType.JOB_MIGRATION);
			this.failureSimulator2_edge = new FailureSimulator2(this.controller, CommonType.EDGE, CommonType.JOB_MIGRATION);
		}
	}
	
	/*
	 * Non-randomized constructor
	 */
	public SimulationRun(boolean improved){
		this.executor = Executors.newCachedThreadPool();
		this.timer=new Timer();
		this.x_max=10;
		this.y_max=10;
		vms = new HashMap<Integer, VM>();
		slas = generateSLAs();
		this.controller = new EdgeController(generateFixedEdgeMap(), improved);
		
		if(improved == false){
			this.failureSimulator2_pm = new FailureSimulator2(this.controller, CommonType.RETRY, CommonType.PM);
			this.failureSimulator2_edge = new FailureSimulator2(this.controller, CommonType.RETRY, CommonType.EDGE);
		} else {
			this.failureSimulator2_pm = new FailureSimulator2(this.controller, CommonType.JOB_MIGRATION, CommonType.PM);
			this.failureSimulator2_edge = new FailureSimulator2(this.controller, CommonType.JOB_MIGRATION, CommonType.EDGE);
		}
	}
	
	private HashMap<Integer, SLA> generateSLAs(){
		HashMap<Integer,SLA> slas=new HashMap<Integer,SLA>();
		
		/*
		 * 
		this.agreedSize = agreedSize;
		this.agreedMemory = agreedMemory;
		this.agreedCPU = agreedCPU;
		this.agreedNetworkBandwidth = agreedNetworkBandwidth;
		this.agreedNetworkBandwidthBEdges = agreedNetworkBandwidthBEdges;
		this.agreedLatency = agreedLatency;
		this.agreedInfrasrtuctureAvailability = agreedInfrasrtuctureAvailability;
		 * 
		 */
		
		SLA sla1 = new SLA(1, 3, 4, 54, 600, 70, 0.9995);
		SLA sla2 = new SLA(2, 4, 8, 108, 600, 90, 0.9895);
		SLA sla3 = new SLA(2, 2, 3, 27, 600, 140, 0.85);
		slas.put(1, sla1);
		slas.put(2, sla2);
		slas.put(3, sla3);
		return slas;
	}
	
	private Edge[][] generateFixedEdgeMap(){
		Edge[][] map=new Edge[x_max][y_max];
		for(int x=0; x<x_max; x++){
			for(int y=0; y<y_max; y++){
				if(x % 3 == 0){
					if(y % 3 == 2){
						double u0 = 100;
						double bandwidth = 10000000;
						Edge edge=new Edge(x,y,u0,bandwidth);
						edge.installPms(generateFixedPMs());
						map[x][y]=edge;
					}
				}
			}
		}
		return map;
	}
	
	private Edge[][] generateRandomEdgeMap(){
		System.out.println("SIMULATOR: Randomly initializing map...");
		Edge[][] map=new Edge[x_max][y_max];
		for (int x = 0; x < x_max; x++) {
			for (int y = 0; y < y_max; y++) {
				// generate uniformly distributed random nr. either 0,1,..,14 or 15
				Random rand = new Random();
				int randNr = rand.nextInt(15);
				//TODO: Adjust these values realistically!
				double u0 = rand.nextGaussian() * 10 + 100;
				double bandwidth = (int) Math.round(rand.nextGaussian() * 3 + 10000000);
				if (randNr == 1) {
					Edge edge = new Edge(x, y, u0, bandwidth);
					System.out.println(edge);
					randNr = rand.nextInt(10) + 1;
					edge.installPms(generateRandomPMs(randNr)); // max 10 PMs per edge
					map[x][y]=edge;
				}
			}
		}
		return map;
	}

	private static HashMap<Integer, PM> generateFixedPMs(){
		HashMap<Integer, PM> pms = new HashMap<Integer, PM>();
		
		for(int i=0; i<7; i++){
			//Adjusted to max requirements of SLA

			double cpu = 4; // {2,3,4} in SLAs
			double memory = 8; // {3,4,8} in SLAs
			int size = 2; // {1,2,2} in SLAs
			
			//TODO: ADJUST THESE VALUES REALISTICALLY
			double u0 = 100;
			double u_cpu = 20000;
			double u_mem = 4000;
			double u_network = 10000;
			PM pm = new PM(u0, u_cpu, u_mem, u_network, cpu, memory, size);
			pms.put(pm.getID(), pm);
		}
		
		return pms;
	}
	
	private static HashMap<Integer, PM> generateRandomPMs(int amount) {
		HashMap<Integer, PM> pms = new HashMap<Integer, PM>();
		for (int i = 0; i < amount; i++) {
			// generate random speccs
			Random rand = new Random();
			
			//TODO: ADJUST THESE VALUES REALISTICALLY
			double cpu = Math.round(rand.nextGaussian() * 15 + 100000000);
			double memory = Math.round(rand.nextGaussian() * 15 + 500000000);
			int size = (int) Math.round(rand.nextGaussian() * 2 + 100);
			
			//TODO: ADJUST THESE VALUES REALISTICALLY
			double u0 = rand.nextGaussian() * 10 + 100;
			double u_cpu = rand.nextGaussian() * 40 + 20000;
			double u_mem = rand.nextGaussian() * 20 + 4000;
			double u_network = rand.nextGaussian() * 30 + 10000;

			PM pm = new PM(u0, u_cpu, u_mem, u_network, cpu, memory, size);

			System.out.println(pm);
			pms.put(pm.getID(), pm);
		}
		return pms;
	}

	private VM generateVM() {
		Random rand = new Random();
		
		Request request = generateRequest();
		
		int needed_memory = rand.nextInt(request.getSla().getAgreedMemory()) + 1; //{3,4,8} in SLAs
		int needed_cpu = rand.nextInt(request.getSla().getAgreedCPU())+1; //{2,3,4} in SLAs 
		int needed_bandwidth = rand.nextInt(request.getSla().getAgreedCPU())+24; // {24,57,108} in SLAs
		int needed_size = rand.nextInt(request.getSla().getAgreedSize()) + 1; // {1,2,2} in SLAs
		int runtime = Math.abs((int) Math.round(rand.nextGaussian() * 1000 + 6000)); 
		return new VM(request, needed_size, needed_cpu, needed_memory, needed_bandwidth, runtime);
	}

	private Request generateRequest() {
		Random rand = new Random();
		int xCoordinate = rand.nextInt(x_max);
		int yCoordinate = rand.nextInt(y_max);
		int slaID = rand.nextInt(3) + 1; // we randomize SLA selection
		
		return new Request(xCoordinate, yCoordinate, slas.get(slaID));
	}

	public void start() {
		System.out.println(controller.printMap() + "\n");
		
		//Simulate edge failures and pm failures in the defined controller.
		executor.execute(failureSimulator2_edge);
		executor.execute(failureSimulator2_pm);
		
		//Simulate the generation of new requests each x seconds
		
		timer.schedule(new TimerTask() {
			MoveSimulator moveSimulator=null;
			@Override
			public void run() {
				
				VM vm = generateVM();
				vms.put(vm.getID(), vm);
				
				System.out.println("SIMULATOR: Request generated: " + vm + "\n");

				Remote remote = controller.sendVM(vm);
				if(remote!=null){
					moveSimulator=new MoveSimulator(remote, x_max,y_max);
					moveSimulator.start();
				}
			}
			
			@Override
			public boolean cancel(){
				if(moveSimulator != null){
					moveSimulator.stop();
				}
				return super.cancel();
			}
		}, 0, 5000); // generating new requests
		
	}
	
	public void stop(){
		this.timer.cancel();
		this.timer.purge();
		this.executor.shutdownNow();
	}
	
	public EdgeController getController() {
		return controller;
	}
	
	public String printStatistics(){
		String output=this.failureSimulator2_edge.printStatistics() + this.failureSimulator2_pm.printStatistics();
		System.out.println(output);
		return output;
	}

}