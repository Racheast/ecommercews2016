import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import Interfaces.Remote;

public class SimulationRun implements Runnable{
	private final int x_max;
	private final int y_max;
	
	public SimulationRun(int x_max, int y_max){
		this.x_max=x_max;
		this.y_max=y_max;
	}
	
	private EdgeController initEdgeController(){
		EdgeController controller=new EdgeController(x_max,y_max);
		//generate Edges and according PMs
		for(int x=0; x<controller.getMap().length;x++){
			for(int y=0; y<controller.getMap()[0].length;y++){
				 //generate uniformly distributed random nr. either 0,1,..,14 or 15
				 Random rand=new Random();
				 int randNr=rand.nextInt(15);
				 double u0=rand.nextGaussian()*10 + 100;
				 int bandwidth=(int) Math.round(rand.nextGaussian() * 3 + 10000);
				 if(randNr==1){
					 Edge edge=new Edge(x,y,u0, bandwidth);
					 System.out.println(edge);
					 randNr=1+rand.nextInt(10);
					 edge.setPms(generatePMs(randNr));  //max 10 PMs per edge !!
					 controller.addEdge(x, y, edge);
				 }
			}
		}
	
		/*
		//generate random bandwiths for each pair of edges
		ArrayList<Edge> edges=controller.getEdges();
		for(Edge e1:edges){
			for(Edge e2:edges){
				if(e1!=e2){
					Random rand=new Random();
					int bandwith=(int) Math.round(rand.nextGaussian() * 3 + 10000);
					e1.setBandwith(e2, bandwith);
				}
			}
		}
		*/
		
		return controller;
	}
	
	
	/*
	 * automatically generates a certain amount of PMs (MAX 10 PMs !) for simulation purposes
	 */
	private static HashMap<Integer,PM> generatePMs(int amount){
		HashMap<Integer,PM> pms=new HashMap<Integer,PM>();
		for(int i=0; i<amount; i++){
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
	
	private Request generateRequest(){
		Random rand=new Random();
		int needed_memory=(int) Math.round(rand.nextGaussian() * 15 + 1000);
		int needed_cpu=(int) Math.round(rand.nextGaussian() * 15 + 1500);
		int needed_bandwidth=(int) Math.round(rand.nextGaussian() * 15 + 1500);
		int needed_size=(int) Math.round(rand.nextGaussian() * 1.75 + 1);
		int runtime=(int) Math.round(rand.nextGaussian() * 7000 + 10000);  //runtime 10000msec = 10seconds in average
		if(runtime<0)
			runtime*=(-1);
		int x_coordinate=rand.nextInt(x_max);
		int y_coordinate=rand.nextInt(y_max);
		return new Request(needed_memory,needed_cpu,needed_bandwidth,needed_size,runtime,x_coordinate,y_coordinate);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		EdgeController controller=initEdgeController();
		System.out.println("**Initialized map**");
		System.out.println(controller.printMap());
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Request request=generateRequest();
				System.out.println("\n"+request);
				Remote remote=controller.sendRequest(request);
				
				Timer timer=new Timer();
				timer.schedule(new TimerTask(){
					@Override
					public void run() {
						remote.stop();
						this.cancel();
					} }, request.getRuntime()); 
				
			} }, 0, 5000);
		
		
	}
	
	/*
	 * TODO: Simulate Failures
	 */
}
