package impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import Interfaces.Remote;

public class SimulationRun implements Runnable {
	private final int x_max;
	private final int y_max;
	private HashMap<Integer, SLA> slas;

	public SimulationRun(int x_max, int y_max) {
		
		slas= new HashMap<Integer, SLA>();
		SLA sla1 = new SLA(1,3,4,54,600,70,0.9995);
		SLA sla2 = new SLA(2,4,8,108,600,90,0.9895);
		SLA sla3 = new SLA(2,2,3,27,600,140,0.85);
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
					randNr = 1 + rand.nextInt(10);
					edge.setPms(generatePMs(randNr)); // max 10 PMs per edge !!
					controller.addEdge(x, y, edge);
				}
			}
		}

		/*
		 * //generate random bandwiths for each pair of edges ArrayList<Edge>
		 * edges=controller.getEdges(); for(Edge e1:edges){ for(Edge e2:edges){
		 * if(e1!=e2){ Random rand=new Random(); int bandwith=(int)
		 * Math.round(rand.nextGaussian() * 3 + 10000); e1.setBandwith(e2,
		 * bandwith); } } }
		 */

		return controller;
	}

	/*
	 * automatically generates a certain amount of PMs (MAX 10 PMs !) for
	 * simulation purposes
	 */
	private static HashMap<Integer,PM> generatePMs(int amount){
		HashMap<Integer,PM> pms=new HashMap<Integer,PM>();
		for(int i=0; i<amount; i++){
			//generate random speccs
			Random rand=new Random();
			int cpu=(int) Math.round(rand.nextGaussian() * 15 + 1000000);
			int memory=(int) Math.round(rand.nextGaussian() * 15 + 5000000);
			int size=(int) Math.round(rand.nextGaussian() *2 +5);
			
			double u0=rand.nextGaussian()*10 + 100;
			double u_cpu=rand.nextGaussian()*12 + 200;
			double u_mem=rand.nextGaussian()*11 + 400;
			double u_network=rand.nextGaussian()*10 + 100;
			
			PM pm=new PM(u0,u_cpu,u_mem,u_network,cpu,memory,size);

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
		int needed_size = Math.abs((int) Math.round(rand.nextGaussian() * 1.75 + 1));
		int runtime = Math.abs((int) Math.round(rand.nextGaussian() * 7000 + 10000)); // runtime
		
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

		/*
		 * VM vm=generateVM();
		 * System.out.println("SIMULATOR: Request generated: "+vm+"\n");
		 * 
		 * Remote remote=controller.sendRequest(vm);
		 * 
		 * if(remote!=null){ Timer timer=new Timer(); timer.schedule(new
		 * TimerTask(){
		 * 
		 * @Override public void run() { int
		 * old_x=vm.getRequest().getxCoordinate(); int
		 * old_y=vm.getRequest().getyCoordinate();
		 * vm.getRequest().setxCoordinate(moveX(vm.getRequest().getxCoordinate()
		 * ,x_max));
		 * vm.getRequest().setyCoordinate(moveY(vm.getRequest().getyCoordinate()
		 * ,y_max));
		 * 
		 * System.out.println("SIMULATOR: Moving "+vm.getRequest().compactString
		 * ()+" from ("+old_x+"/"+old_y+") to ("+vm.getRequest().getxCoordinate(
		 * )+"/"+vm.getRequest().getyCoordinate()+")\n");
		 * remote.move(vm.getRequest().getxCoordinate(),
		 * vm.getRequest().getyCoordinate());
		 * 
		 * } },0, 1000); //moving every 6seconds }
		 */

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			// here we generate the requests
			// add SLAs

			@Override
			public void run() {

				VM vm = generateVM();
				System.out.println("SIMULATOR: Request generated: " + vm + "\n");

				Remote remote = controller.sendRequest(vm);

				if (remote != null) {
					long start = System.currentTimeMillis();
					long end = System.currentTimeMillis() + vm.getRuntime();

					Timer timer2 = new Timer();
					timer2.schedule(new TimerTask() {

						// here we generate the requests
						@Override
						public void run() {
							if (System.currentTimeMillis() < end) {
								int old_x = vm.getRequest().getxCoordinate();
								int old_y = vm.getRequest().getyCoordinate();
								vm.getRequest().setxCoordinate(moveX(vm.getRequest().getxCoordinate(), x_max));
								vm.getRequest().setyCoordinate(moveY(vm.getRequest().getyCoordinate(), y_max));

								System.out.println("SIMULATOR: Moving " + vm.getRequest().compactString() + " from ("
										+ old_x + "/" + old_y + ") to (" + vm.getRequest().getxCoordinate() + "/"
										+ vm.getRequest().getyCoordinate() + ")\n");
								remote.move(vm.getRequest().getxCoordinate(), vm.getRequest().getyCoordinate());
							} else {
								System.out.println("SIMULATOR: Cancelling " + vm.getRequest().compactString() + "\n");
								remote.stop();
								this.cancel();
							}
						}
					}, 2000, 1000); // moving requests (=users) across the map
				}

			}
		}, 0, 10000); // generating new requests
	}

	private int moveX(int x, int maximal_x) {
		Random rand = new Random();
		// int move_x=(int) Math.round(rand.nextGaussian() * 5);
		int move_x = rand.nextInt(3) - 1; // move_x out of [-1,0,+1]
		int new_x = x + move_x;

		if (new_x >= maximal_x) {
			new_x = maximal_x - 1;
		} else if (new_x < 0) {
			new_x = 0;
		}
		return new_x;
	}

	private int moveY(int y, int maximal_y) {
		Random rand = new Random();
		// int move_y=(int) Math.round(rand.nextGaussian() * 5);
		int move_y = rand.nextInt(3) - 1; // move_y out of [-1,0,+1]
		int new_y = y + move_y;

		if (new_y >= maximal_y) {
			new_y = maximal_y - 1;
		} else if (new_y < 0) {
			new_y = 0;
		}
		return new_y;
	}

	private void start(Remote remote, Request request) {
		/*
		 * ^ Timer timer=new Timer(); timer.schedule(new TimerTask(){
		 * 
		 * @Override public void run() { remote.stop(); this.cancel(); } },
		 * request.getRuntime());
		 * 
		 * Timer timer2=new Timer(); timer.schedule(new TimerTask(){
		 * 
		 * @Override public void run() { Random rand=new Random(); int
		 * x=rand.nextInt(x_max); int y=rand.nextInt(y_max);
		 * remote=remote.move(x, y); } },0, request.getRuntime()/3);
		 */
	}

	/*
	 * private void shutDown(Remote remote, Request request){ Timer timer=new
	 * Timer(); timer.schedule(new TimerTask(){
	 * 
	 * @Override public void run() { remote.stop(); this.cancel(); } },
	 * request.getRuntime()); }
	 */

	/*
	 * TODO: Simulate Failures
	 */
}
