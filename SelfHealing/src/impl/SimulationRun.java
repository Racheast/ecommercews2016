package impl;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import Interfaces.Remote;

public class SimulationRun implements Runnable {
	private final int x_max;
	private final int y_max;
	private HashMap<Integer, SLA> slas;
	private HashMap<Integer, VM> vms;

	public SimulationRun(int x_max, int y_max) {
		vms = new HashMap<Integer, VM>();
		slas = new HashMap<Integer, SLA>();
		SLA sla1 = new SLA(1, 3, 4, 54, 600, 70, 0.9995);
		SLA sla2 = new SLA(2, 4, 8, 108, 600, 90, 0.9895);
		SLA sla3 = new SLA(2, 2, 3, 27, 600, 140, 0.85);
		slas.put(1, sla1);
		slas.put(2, sla2);
		slas.put(3, sla3);

		this.x_max = x_max;
		this.y_max = y_max;

	}

	private EdgeController initEdgeController() {
		EdgeController controller = new EdgeController(x_max, y_max);
		// generate Edges and according PMs
		for (int x = 0; x < controller.getMap().length; x++) {
			for (int y = 0; y < controller.getMap()[0].length; y++) {
				// generate uniformly distributed random nr. either 0,1,..,14 or
				// 15
				Random rand = new Random();
				int randNr = rand.nextInt(15);
				double u0 = rand.nextGaussian() * 10 + 100;
				int bandwidth = (int) Math.round(rand.nextGaussian() * 3 + 10000);
				if (randNr == 1) {
					Edge edge = new Edge(x, y, u0, bandwidth);
					System.out.println(edge);
					randNr = rand.nextInt(10) + 1;
					edge.installPms(generatePMs(randNr)); // max 10 PMs per edge
															// !!
					controller.addEdge(x, y, edge);
				}
			}
		}
		return controller;
	}

	/*
	 * automatically generates a certain amount of PMs (MAX 10 PMs !) for
	 * simulation purposes
	 */
	private static HashMap<Integer, PM> generatePMs(int amount) {
		HashMap<Integer, PM> pms = new HashMap<Integer, PM>();
		for (int i = 0; i < amount; i++) {
			// generate random speccs
			Random rand = new Random();
			int cpu = (int) Math.round(rand.nextGaussian() * 15 + 1000000);
			int memory = (int) Math.round(rand.nextGaussian() * 15 + 5000000);
			int size = (int) Math.round(rand.nextGaussian() * 2 + 5);

			double u0 = rand.nextGaussian() * 10 + 100;
			double u_cpu = rand.nextGaussian() * 40 + 200;
			double u_mem = rand.nextGaussian() * 20 + 400;
			double u_network = rand.nextGaussian() * 30 + 100;

			PM pm = new PM(u0, u_cpu, u_mem, u_network, cpu, memory, size);

			System.out.println(pm);
			pms.put(pm.getID(), pm);
		}
		return pms;
	}

	private VM generateVM() {
		Random rand = new Random();
		int needed_memory = Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1000));
		int needed_cpu = Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1500));
		int needed_bandwidth = Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1500));
		int needed_size = rand.nextInt(3) + 1;
		int runtime = Math.abs((int) Math.round(rand.nextGaussian() * 7000 + 100000)); // runtime

		return new VM(generateRequest(), needed_size, needed_cpu, needed_memory, needed_bandwidth, runtime);
	}

	private Request generateRequest() {
		Random rand = new Random();
		int xCoordinate = rand.nextInt(x_max);
		int yCoordinate = rand.nextInt(y_max);
		int slaID = rand.nextInt(3) + 1; // we randomize SLA selection
		
		return new Request(xCoordinate, yCoordinate, slas.get(slaID));
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("SIMULATOR: Initializing map...");
		EdgeController controller = initEdgeController();
		System.out.println(controller.printMap() + "\n");
		Monitor monitor=new Monitor(controller);
		monitor.start();
		//GridMonitor gridMonitor = new GridMonitor(controller.getMap());
		//gridMonitor.showMonitor();
		
		//GridMonitor2 gridMonitor2 = new GridMonitor2(controller.getMap(),vms);
		//gridMonitor2.showMonitor();
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			// here we generate the requests
			// add SLAs

			@Override
			public void run() {
				
				VM vm = generateVM();
				vms.put(vm.getID(), vm);
				//gridMonitor.addLocationElement(vm.getRequest());
				//gridMonitor.reprintMap();
				System.out.println("SIMULATOR: Request generated: " + vm + "\n");

				Remote remote = controller.sendVM(vm);
				if (remote != null) {
					long end = System.currentTimeMillis() + vm.getRuntime();

					Timer timer2 = new Timer();
					timer2.schedule(new TimerTask() {

						// here we generate the requests
						@Override
						public void run() {
							if (System.currentTimeMillis() < end) {
								int old_x = vm.getRequest().getxCoordinate();
								int old_y = vm.getRequest().getyCoordinate();
								randomlyMoveVM(vm);
								//gridMonitor.moveLocationElement(old_x, old_y, vm.getRequest());
								
								System.out.println("SIMULATOR: Moving " + vm.getRequest().compactString() + " from ("
										+ old_x + "/" + old_y + ") to (" + vm.getRequest().getxCoordinate() + "/"
										+ vm.getRequest().getyCoordinate() + ")\n");
								remote.move(vm.getRequest().getxCoordinate(), vm.getRequest().getyCoordinate());
							} else {
								System.out.println("SIMULATOR: Cancelling " + vm.getRequest().compactString() + "\n");
								remote.stop();
								//gridMonitor.deleteLocationElement(vm.getRequest());
								this.cancel();
							}
						}
					}, 2000, 1000); // moving requests (=users) across the map
				}
			}
		}, 0, 100); // generating new requests
	}
	
	private void randomlyMoveVM(VM vm){
		vm.getRequest().setxCoordinate(randomStep(vm.getRequest().getxCoordinate(), x_max));
		vm.getRequest().setyCoordinate(randomStep(vm.getRequest().getyCoordinate(), y_max));
	}
	
	private int randomStep(int c, int max_c) {
		Random rand = new Random();
		int step = rand.nextInt(3) - 1; // step out of [-1,0,+1]
		int new_c = c + step;
		if (new_c >= max_c) {
			new_c = max_c - 1;
		} else if (new_c < 0) {
			new_c = 0;
		}
		return new_c;
	}

}
