package impl;
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
	
	private VM generateVM(){
		Random rand=new Random();
		int needed_memory=Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1000));
		int needed_cpu=Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1500));
		int needed_bandwidth=Math.abs((int) Math.round(rand.nextGaussian() * 15 + 1500));
		int needed_size=Math.abs((int) Math.round(rand.nextGaussian() * 1.75 + 1));
		int runtime=Math.abs((int) Math.round(rand.nextGaussian() * 7000 + 10000));  //runtime 10000msec = 10seconds in average
				
		return new VM(generateRequest(),needed_size,needed_cpu,needed_memory,needed_bandwidth,runtime);
	}
	
	private Request generateRequest(){
		Random rand=new Random();
		int xCoordinate=rand.nextInt(x_max);
		int yCoordinate=rand.nextInt(y_max);
		return new Request(xCoordinate,yCoordinate);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("SIMULATOR: Initializing map...");
		EdgeController controller=initEdgeController();
		System.out.println(controller.printMap()+"\n");
		
		VM vm=generateVM();
		System.out.println("SIMULATOR: Request generated: "+vm+"\n");
		
		Remote remote=controller.sendRequest(vm);
		
		if(remote!=null){
			Timer timer=new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					Random rand=new Random();
					int x=vm.getRequest().getxCoordinate();
					int y=vm.getRequest().getyCoordinate();
					int move_X=(int) Math.round(rand.nextGaussian() * 5);
					int move_Y=(int) Math.round(rand.nextGaussian() * 5);
					int new_x=x+move_X;
					int new_y=y+move_Y;
					
					if(new_x >= x_max){
						new_x=x_max-1;
					}else if(new_x < 0){
						new_x=0;
					}
					
					if(new_y >= y_max){
						new_y=y_max-1;
					}else if(new_y < 0){
						new_y=0;
					}
					
					vm.getRequest().setxCoordinate(new_x);
					vm.getRequest().setyCoordinate(new_y);
					
					System.out.println("SIMULATOR: Moving "+vm.getRequest().compactString()+" from ("+x+"/"+y+") to ("+vm.getRequest().getxCoordinate()+"/"+vm.getRequest().getyCoordinate()+")\n");
					remote.move(vm.getRequest().getxCoordinate(), vm.getRequest().getyCoordinate());
					
				} },0, 1000); //moving every 6seconds
		}
		
		
		/*
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Request request=generateRequest();
				System.out.println("\nSIMULATOR: Request generated: "+request);
				Remote remote=controller.sendRequest(request);
				
				if(remote!=null){
					Timer timer=new Timer();
					timer.schedule(new TimerTask(){
						@Override
						public void run() {
							Random rand=new Random();
							int x=request.getxCoordinate();
							int y=request.getyCoordinate();
							int move_X=rand.nextInt(3)+1;
							int move_Y=rand.nextInt(3)+1;
							request.setxCoordinate(x+move_X);
							request.setyCoordinate(y+move_Y);
							remote.move(request.getxCoordinate(), request.getyCoordinate());
							
						} },0, 6000); //moving every 6seconds
					
					
				}
				
			} }, 0, 5000);  //generating a request every 5seconds
		*/
		
	}
	
	private void start(Remote remote, Request request){
		/*Timer timer=new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				remote.stop();
				this.cancel();
			} }, request.getRuntime()); 
		
		Timer timer2=new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				Random rand=new Random();
				int x=rand.nextInt(x_max);
				int y=rand.nextInt(y_max);
				remote=remote.move(x, y);
			} },0, request.getRuntime()/3);
			*/
	}
	
	/*
	private void shutDown(Remote remote, Request request){
		Timer timer=new Timer();
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				remote.stop();
				this.cancel();
			} }, request.getRuntime()); 
	}
	*/
	
	/*
	 * TODO: Simulate Failures
	 */
}
