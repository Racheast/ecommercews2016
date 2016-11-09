
public class VM{
	//
	private final int ID;
	private static int id_counter=1;
	private int size;
	private int consumed_cpu;
	private int consumed_memory;
	private int consumed_networkBandwith;  //depends on the consumed memory
	private int runtime;
	private double page_dirtying_rate;  //depends linearly on the combination of the utilized memory
	
	private Application application;
	
	public VM(int size, int consumed_cpu, int consumed_memory, int consumed_networkBandwith, int runtime,
			double page_dirtying_rate) {
		super();
		this.ID=id_counter++;
		this.size = size;
		this.consumed_cpu = consumed_cpu;
		this.consumed_memory = consumed_memory;
		this.consumed_networkBandwith = consumed_networkBandwith;
		this.runtime = runtime;
		this.page_dirtying_rate = page_dirtying_rate;
	}
	
	public VM(Application application){
		this.ID=id_counter++;
		this.consumed_cpu=application.getNeeded_cpu();
		this.consumed_memory=application.getNeeded_memory();
		this.consumed_networkBandwith=0;  //TODO: calculate nwBandwith. depends on the consumed memory
		this.runtime=0;
		this.page_dirtying_rate=0.2;  //depends linearly on the combination of the utilized memory
	}
	
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		consumeResources(application);
		this.application = application;
	}
	public int getRuntime() {
		return runtime;
	}
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getConsumed_cpu() {
		return consumed_cpu;
	}
	public void setConsumed_cpu(int consumed_cpu) {
		this.consumed_cpu = consumed_cpu;
	}
	public int getConsumed_memory() {
		return consumed_memory;
	}
	public void setConsumed_memory(int comsumed_memory) {
		this.consumed_memory= comsumed_memory;
	}
	public int getConsumed_networkBandwith() {
		return consumed_networkBandwith;
	}
	public void setConsumed_networkBandwith(int consumed_networkBandwith) {
		this.consumed_networkBandwith = consumed_networkBandwith;
	}
	public int getID(){
		return this.ID;
	}
	
	private void consumeResources(Application application){
		if(application!=null){
			this.consumed_cpu=application.getNeeded_cpu();
			this.consumed_memory=application.getNeeded_memory();
			this.consumed_networkBandwith=0; //CALCULATE nwBandwith!!
		}else{
			this.consumed_cpu=0;
			this.consumed_memory=0;
			this.consumed_networkBandwith=0; 
		}
	}
	
}
