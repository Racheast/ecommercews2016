import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class SimulationRun implements Runnable{
	
	private static EdgeController initEdgeController(){
		EdgeController controller=new EdgeController();
		//generate Edges and according PMs
		for(int x=0; x<controller.getMap().length;x++){
			for(int y=0; y<controller.getMap()[0].length;y++){
				 //generate uniformly distributed random nr. either 0,1,..,14 or 15
				 Random rand=new Random();
				 int randNr=rand.nextInt(15);
				 if(randNr==1){
					 Edge edge=new Edge(x,y);
					 edge.setPms(generatePMs(10));  //max 10 PMs per edge !!
					 controller.addEdge(x, y, edge);
				 }
			}
		}
		
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
			int network_bandwidth=(int) Math.round(rand.nextGaussian() *2 +5);
			
			double u0=rand.nextGaussian()*10 + 100;
			double u_cpu=rand.nextGaussian()*12 + 200;
			double u_mem=rand.nextGaussian()*11 + 400;
			double u_network=rand.nextGaussian()*10 + 100;
			
			PM pm=new PM(u0,u_cpu,u_mem,u_network,cpu,memory,size, network_bandwidth);
			pms.put(pm.getID(), pm);
		}
		return pms;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		EdgeController controller=initEdgeController();
		System.out.println(controller.printMap());
	}
	
	/*
	 * TODO: Simulate Failures
	 */
}
