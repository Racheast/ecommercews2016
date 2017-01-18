package simulation;

import constant.CommonType;

public class SimulatorFalure {

	public static void main(String[] args) {

		SimulationFailureRun baselineRunForPm = new SimulationFailureRun(true, CommonType.PM, CommonType.RETRY);
		baselineRunForPm.getFailureSimulator().start();

		SimulationFailureRun baselineRunForEdge = new SimulationFailureRun(true, CommonType.EDGE, CommonType.RETRY);
		baselineRunForEdge.getFailureSimulator().start();
		
		SimulationFailureRun improvedRunForPm = new SimulationFailureRun(true, CommonType.PM, CommonType.JOB_MIGRATION);
		improvedRunForPm.getFailureSimulator().start();

		SimulationFailureRun improvedRunForEdge = new SimulationFailureRun(true, CommonType.EDGE, CommonType.JOB_MIGRATION);
		improvedRunForEdge.getFailureSimulator().start();
	}
}