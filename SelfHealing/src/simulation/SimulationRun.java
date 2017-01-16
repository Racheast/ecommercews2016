
package simulation;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
	private FailureSimulator failureSimulator;
	
	/*
	 * Randomized constructor
	 */
	public SimulationRun(int x_max, int y_max, boolean improved) {
		this.timer=new Timer();
		vms = new HashMap<Integer, VM>();
		slas = generateSLAs();
		this.x_max = x_max;
		this.y_max = y_max;
		this.controller = new EdgeController(generateRandomEdgeMap(), improved);
		//this.failureSimulator=new FailureSimulator(controller);
		this.failureSimulator = new FailureSimulator(x_max, y_max, slas, vms, timer, controller, null, null);
	}
	
	/*
	 * Non-randomized constructor
	 */
	public SimulationRun(boolean improved){
		this.timer=new Timer();
		this.x_max=10;
		this.y_max=10;
		vms = new HashMap<Integer, VM>();
		slas = generateSLAs();
		this.controller = new EdgeController(generateFixedEdgeMap(), improved);
	}
	
	private HashMap<Integer, SLA> generateSLAs(){
		HashMap<Integer,SLA> slas=new HashMap<Integer,SLA>();
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
		int needed_memory = rand.nextInt(8) + 1; //{3,4,8} in SLAs, here we generate from 1 to 8
		// int needed_memory = (int) Math.round(rand.nextDouble()*5 + 3); this is uniformly distributed
		int needed_cpu = rand.nextInt(4)+1; //{2,3,4} in SLAs - here we generate from 1 to 4
		int needed_bandwidth = rand.nextInt(100)+24; // {24,57,108} in SLAs, here we generate from 24 to 124
		int needed_size = rand.nextInt(2) + 1; // {1,2,2} in SLAs, here we generate 1 or 2
		int runtime = Math.abs((int) Math.round(rand.nextGaussian() * 1000 + 1500)); // runtime was not adjusted

		return new VM(generateRequest(), needed_size, needed_cpu, needed_memory, needed_bandwidth, runtime);
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
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				
				VM vm = generateVM();
				vms.put(vm.getID(), vm);
				
				System.out.println("SIMULATOR: Request generated: " + vm + "\n");

				Remote remote = controller.sendVM(vm);
				if(remote!=null){
					MoveSimulator moveSimulator=new MoveSimulator(remote, x_max,y_max);
					moveSimulator.start();
				}
			}
		}, 0, 1500); // generating new requests
	}
	
	public void stop(){
		this.timer.cancel();
	}

	public EdgeController getController() {
		return controller;
	}

}