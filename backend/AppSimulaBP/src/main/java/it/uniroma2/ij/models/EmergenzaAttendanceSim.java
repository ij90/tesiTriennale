package it.uniroma2.ij.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import it.uniroma2.sel.simlab.ebpmn.connections.SequenceFlow;
import it.uniroma2.sel.simlab.ebpmn.data.policies.ProbabilityBasedRoutingPolicy;
import it.uniroma2.sel.simlab.ebpmn.data.policies.TokenBasedTerminationPolicy;
import it.uniroma2.sel.simlab.ebpmn.flownodes.activities.Task;
import it.uniroma2.sel.simlab.ebpmn.flownodes.events.End;
import it.uniroma2.sel.simlab.ebpmn.flownodes.events.Start;
import it.uniroma2.sel.simlab.ebpmn.flownodes.events.Start.ArrivalType;
import it.uniroma2.sel.simlab.ebpmn.flownodes.gateways.ExclusiveDivergingGateway;
import it.uniroma2.sel.simlab.ebpmn.flownodes.gateways.ParallelConvergingGateway;
import it.uniroma2.sel.simlab.ebpmn.flownodes.gateways.ParallelDivergingGateway;
import it.uniroma2.sel.simlab.ebpmn.general.CollaborationStatistics;
import it.uniroma2.sel.simlab.ebpmn.general.ConfigurationStatistics;
import it.uniroma2.sel.simlab.ebpmn.general.EBPMNName;
import it.uniroma2.sel.simlab.ebpmn.general.EBPMNSimFactory;
import it.uniroma2.sel.simlab.ebpmn.general.EBPMNTime;
import it.uniroma2.sel.simlab.ebpmn.general.Scenario;
import it.uniroma2.sel.simlab.ebpmn.general.Simulazione;
import it.uniroma2.sel.simlab.ebpmn.math.ProbabilityDistribution;
import it.uniroma2.sel.simlab.ebpmn.resources.Request;
import it.uniroma2.sel.simlab.ebpmn.resources.Request.RequestType;
import it.uniroma2.sel.simlab.ebpmn.resources.broker.Standby;
import it.uniroma2.sel.simlab.ebpmn.resources.performer.Performer;
import it.uniroma2.sel.simlab.simarch.exceptions.InvalidNameException;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.Layer2InternalException;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.UnableToStartEngineException;

public class EmergenzaAttendanceSim {
	
	Simulazione simulazione = new Simulazione();
	Map<String, Integer> performers = new HashMap<String, Integer>() {
		{
			put("Basic_Ambulance", 2);
			put("Call_Center_Agent", 2);
			put("Full_Ambulance", 4);
			put("Nurse", 3);
			put("Quick_Attention_Vehicle", 2);
			put("Receptionist", 2);
		}
	}; 
	
	
	/**
     * SIMULATION PARAMETERS
     */
    
    

	/**
     *  Simulation units
     */
    EBPMNTime.Unit performanceTimeUnit = EBPMNTime.Unit.MIN;
    EBPMNTime.Unit reliabilityTimeUnit = EBPMNTime.Unit.DAY;
    
    /**
     *  Simulation duration
     */
    int replications = 1;
    
    double simulationTimeValue = 3*7;
    EBPMNTime.Unit simulationTimeUnit = EBPMNTime.Unit.DAY;

    double simulationTime = simulationTimeValue * simulationTimeUnit.getConversionFactor();
    
    /**
     *  Token inter-arrival
     */
    double tokenInterarrivalTimeValue = 4;
    EBPMNTime.Unit tokenInterarrivalTimeUnit = EBPMNTime.Unit.MIN;
    
    double tokenInterarrival = tokenInterarrivalTimeValue * tokenInterarrivalTimeUnit.getConversionFactor();
    
    int totalTokens = 1000000;                      
    
    /**
     * Other parameters
     */
    Standby standby = Standby.HOT;
    RequestType requestType = RequestType.EXPONENTIAL;
    ArrivalType arrivalType = ArrivalType.EXPONENTIAL;
    
 // declaration of eBPMN entities
    Task Manage_Patient_Entry;
    Task Receive_Emergency_Report;
    Task Classify_Triage;
    ExclusiveDivergingGateway ExclusiveGateway_1;
    End Green_Code;
    Task Arrive_At_Patient_Place_QAV;
    Task Authorize_Entry;
    ParallelDivergingGateway ParallelGateway_1;
    End Yellow_Code;
    Task Pick_Up_Patient;
    End Red_Code;
    ParallelConvergingGateway ParallelGateway_2;
    Start StartEvent_1;
    Task Arrive_At_Patient_Place_BA;
	Performer Call_Center_Agent;
	Performer Nurse;
	Performer Quick_Attention_Vehicle;
	Performer Basic_Ambulance;
	Performer Full_Ambulance;
	Performer Receptionist;
	
	double Req_1_from_Manage_Patient_Entry_to_Nurse_serviceQuantity = 11;
	
	double Req_1_from_Receive_Emergency_Report_to_Call_Center_Agent_serviceQuantity = 4;
	
	double Req_1_from_Classify_Triage_to_Nurse_serviceQuantity = 5;
	
	double Req_1_from_Arrive_At_Patient_Place_QAV_to_Quick_Attention_Vehicle_serviceQuantity = 7;
	
	double Req_1_from_Authorize_Entry_to_Receptionist_serviceQuantity = 4;
	
	double Req_1_from_Pick_Up_Patient_to_Full_Ambulance_serviceQuantity = 20;
	
	double Req_1_from_Arrive_At_Patient_Place_BA_to_Basic_Ambulance_serviceQuantity = 10;
	
	public Map<String, Integer> getPerformers() {
		return performers;
	}

	public void setPerformers(Map<String, Integer> performers) {
		this.performers = performers;
	}
	
	public int getWorkingUnits(String performer) {
		return performers.get(performer);
	}
	
	public void setReplications(int replications) {
		this.replications = replications;
	}
	public int getReplications() {
		return this.replications;
	}
	public int getTotalTokens() {
		return totalTokens;
	}
	public void setTotalTokens(int totalTokens) {
		this.totalTokens = totalTokens;
	}
	
	public Simulazione getSimulazione() {
		return simulazione;
	}
	
	public void startSimulazione() throws UnableToStartEngineException, Layer2InternalException {
        Locale.setDefault(new Locale("en", "EN"));
        
        try {
        	
        	/**
             * Simulation code
             */
                
            // Number of process executions
            Scenario scenario = Scenario.getInstance();
            scenario.setReplications(replications);
            scenario.setPerformanceTimeUnit(performanceTimeUnit);
            scenario.setReliabilityTimeUnit(reliabilityTimeUnit);
			Long simStart;
			
			for (int run=0; run<scenario.getReplications(); run++) {
				simStart = System.currentTimeMillis();
                
                // allocation of Layer 2 (SimJ) implementation
                EBPMNSimFactory.create(EBPMNTime.makeFrom(simulationTime));
                scenario.init();


				/**
				 *  Call_Center_Agent
				 */
				double Call_Center_Agent_service = 1; // unit is: min
				double Call_Center_Agent_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Call_Center_Agent_failureFunction = null;
				ProbabilityDistribution Call_Center_Agent_repairFunction = null;
				
				Call_Center_Agent = new Performer(new EBPMNName("Call_Center_Agent"),
	 				    getWorkingUnits("Call_Center_Agent"), 
	 				    EBPMNTime.makeFrom(Call_Center_Agent_service * Call_Center_Agent_service_conv_factor), 
	 				    Call_Center_Agent_failureFunction, 
	 				    Call_Center_Agent_repairFunction,
	 				    EBPMNTime.zero());
				
				/**
 				 *  Nurse
 				 */
 				double Nurse_service = 1; // unit is: min
 				double Nurse_service_conv_factor = 1.0 * 60;
 				
 				ProbabilityDistribution Nurse_failureFunction = null;
 				ProbabilityDistribution Nurse_repairFunction = null;
 				
 				Nurse = new Performer(new EBPMNName("Nurse"), getWorkingUnits("Nurse"), 
 				    EBPMNTime.makeFrom(Nurse_service * Nurse_service_conv_factor), 
 				    Nurse_failureFunction, 
 				    Nurse_repairFunction,
 				    EBPMNTime.zero());
				
 				/**
 				 *  Quick_Attention_Vehicle
 				 */
 				double Quick_Attention_Vehicle_service = 1; // unit is: min
 				double Quick_Attention_Vehicle_service_conv_factor = 1.0 * 60;
 				
 				ProbabilityDistribution Quick_Attention_Vehicle_failureFunction = null;
 				ProbabilityDistribution Quick_Attention_Vehicle_repairFunction = null;
 				               				
 				Quick_Attention_Vehicle = new Performer(new EBPMNName("Quick_Attention_Vehicle"),
 				    getWorkingUnits("Quick_Attention_Vehicle"), 
 				    EBPMNTime.makeFrom(Quick_Attention_Vehicle_service * Quick_Attention_Vehicle_service_conv_factor), 
 				    Quick_Attention_Vehicle_failureFunction, 
 				    Quick_Attention_Vehicle_repairFunction,
 				    EBPMNTime.zero());
 				
 				/**
 				 *  Basic_Ambulance
 				 */
 				double Basic_Ambulance_service = 1; // unit is: min
 				double Basic_Ambulance_service_conv_factor = 1.0 * 60;
 				
 				ProbabilityDistribution Basic_Ambulance_failureFunction = null;
 				ProbabilityDistribution Basic_Ambulance_repairFunction = null;
 				              
 				Basic_Ambulance = new Performer(new EBPMNName("Basic_Ambulance"),
 				    getWorkingUnits("Basic_Ambulance"), 
 				    EBPMNTime.makeFrom(Basic_Ambulance_service * Basic_Ambulance_service_conv_factor), 
 				    Basic_Ambulance_failureFunction, 
 				    Basic_Ambulance_repairFunction,
 				    EBPMNTime.zero());
 				
 				/**
 				 *  Full_Ambulance
 				 */
 				double Full_Ambulance_service = 1; // unit is: min
 				double Full_Ambulance_service_conv_factor = 1.0 * 60;
 				
 				ProbabilityDistribution Full_Ambulance_failureFunction = null;
 				ProbabilityDistribution Full_Ambulance_repairFunction = null;
 				               				
 				Full_Ambulance = new Performer(new EBPMNName("Full_Ambulance"),
 				    getWorkingUnits("Full_Ambulance"), 
 				    EBPMNTime.makeFrom(Full_Ambulance_service * Full_Ambulance_service_conv_factor), 
 				    Full_Ambulance_failureFunction, 
 				    Full_Ambulance_repairFunction,
 				    EBPMNTime.zero());
 				
 				/**
 				 *  Receptionist
 				 */
 				double Receptionist_service = 1; // unit is: min
 				double Receptionist_service_conv_factor = 1.0 * 60;
 				
 				ProbabilityDistribution Receptionist_failureFunction = null;
 				ProbabilityDistribution Receptionist_repairFunction = null;
 				              
 				Receptionist = new Performer(new EBPMNName("Receptionist"),
 				    getWorkingUnits("Receptionist"), 
 				    EBPMNTime.makeFrom(Receptionist_service * Receptionist_service_conv_factor), 
 				    Receptionist_failureFunction, 
 				    Receptionist_repairFunction,
 				    EBPMNTime.zero()); 
 				
 				/**
                 *  Manage_Patient_Entry
                 */
                Manage_Patient_Entry = new Task(new EBPMNName("Manage Patient Entry"), 
                    EBPMNTime.zero());
                Manage_Patient_Entry.addResource(new Request(Nurse, requestType, Req_1_from_Manage_Patient_Entry_to_Nurse_serviceQuantity));   
                  
                /**
                 *  Receive_Emergency_Report
                 */
                Receive_Emergency_Report = new Task(new EBPMNName("Receive Emergency Report"), 
                    EBPMNTime.zero());
                Receive_Emergency_Report.addResource(new Request(Call_Center_Agent, requestType, Req_1_from_Receive_Emergency_Report_to_Call_Center_Agent_serviceQuantity));   
                
                /**
                 *  Classify_Triage
                 */
                Classify_Triage = new Task(new EBPMNName("Classify Triage"), 
                    EBPMNTime.zero());
                Classify_Triage.addResource(new Request(Nurse, requestType, Req_1_from_Classify_Triage_to_Nurse_serviceQuantity));   
                  
                /**
                 *  ExclusiveGateway_1
                 */
                // number of outgoing sequence flows
                int ExclusiveGateway_1_TotOutgoing = 3;
                
                // probabilities
                Double [] ExclusiveGateway_1_Probs = {0.5, 0.3, 0.2};
                
                ExclusiveGateway_1 = new ExclusiveDivergingGateway(new EBPMNName("ExclusiveGateway_1"),
                	ExclusiveGateway_1_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(ExclusiveGateway_1_Probs),
                	EBPMNTime.zero());
                
                /**
                 *  Green_Code
                 */
                Green_Code = new End(new EBPMNName("Green_Code"));
                
                /**
                 *  Arrive_At_Patient_Place_QAV
                 */
                Arrive_At_Patient_Place_QAV = new Task(new EBPMNName("Arrive At Patient Place QAV"), 
                    EBPMNTime.zero());
                Arrive_At_Patient_Place_QAV.addResource(new Request(Quick_Attention_Vehicle, requestType, Req_1_from_Arrive_At_Patient_Place_QAV_to_Quick_Attention_Vehicle_serviceQuantity));   
                  
                /**
                 *  Authorize_Entry
                 */
                Authorize_Entry = new Task(new EBPMNName("Authorize Entry"), 
                    EBPMNTime.zero());
                Authorize_Entry.addResource(new Request(Receptionist, requestType, Req_1_from_Authorize_Entry_to_Receptionist_serviceQuantity));
                
                /**
                 *  ParallelGateway_1
                 */
                // number of outgoing sequence flows
                int ParallelGateway_1_TotOutgoing = 2;
                
                ParallelGateway_1 = new ParallelDivergingGateway(new EBPMNName("ParallelGateway_1"),
                	ParallelGateway_1_TotOutgoing,
                	EBPMNTime.zero());
                
                /**
                 *  Yellow_Code
                 */
                Yellow_Code = new End(new EBPMNName("Yellow_Code"));
                
                /**
                 *  Pick_Up_Patient
                 */
                Pick_Up_Patient = new Task(new EBPMNName("Pick Up Patient"), 
                    EBPMNTime.zero());
                Pick_Up_Patient.addResource(new Request(Full_Ambulance, requestType, Req_1_from_Pick_Up_Patient_to_Full_Ambulance_serviceQuantity));   
                
                /**
                 *  Red_Code
                 */
                Red_Code = new End(new EBPMNName("Red_Code"));
                
                /**
                 *  ParallelGateway_2
                 */
                // number of incoming sequence flows
                int ParallelGateway_2_TotIncoming = 2;
                
                ParallelGateway_2 = new ParallelConvergingGateway(new EBPMNName("ParallelGateway_2"),
                	ParallelGateway_2_TotIncoming,            		
                	EBPMNTime.zero());
                
                /**
                 *  StartEvent_1
                 */
                // set jobs number and interarrival for simulation
                double StartEvent_1_Interarrival = tokenInterarrival;
                int StartEvent_1_nToken = totalTokens;
                
                StartEvent_1 = new Start(new EBPMNName("StartEvent_1"),
                    StartEvent_1_Interarrival,
                    arrivalType,
                    new TokenBasedTerminationPolicy(StartEvent_1_nToken),
                    EBPMNTime.zero());
                
                /**
                 *  Arrive_At_Patient_Place_BA
                 */
                Arrive_At_Patient_Place_BA = new Task(new EBPMNName("Arrive At Patient Place BA"), 
                    EBPMNTime.zero());
                Arrive_At_Patient_Place_BA.addResource(new Request(Basic_Ambulance, requestType, Req_1_from_Arrive_At_Patient_Place_BA_to_Basic_Ambulance_serviceQuantity));   
                
             // Sequence flows
                SequenceFlow SequenceFlow_13 = new SequenceFlow(
                	Pick_Up_Patient.getOutPort(),
                	ParallelGateway_2.getInPort(0));
                
                SequenceFlow SequenceFlow_6 = new SequenceFlow(
                	ExclusiveGateway_1.getOutPort(1),
                	Arrive_At_Patient_Place_QAV.getInPort());
                
                SequenceFlow SequenceFlow_2 = new SequenceFlow(
                	Receive_Emergency_Report.getOutPort(),
                	Classify_Triage.getInPort());
                
                SequenceFlow SequenceFlow_10 = new SequenceFlow(
                	ParallelGateway_1.getOutPort(0),
                	Manage_Patient_Entry.getInPort());
                
                SequenceFlow SequenceFlow_11 = new SequenceFlow(
                	Manage_Patient_Entry.getOutPort(),
                	ParallelGateway_2.getInPort(1));
                
                SequenceFlow SequenceFlow_1 = new SequenceFlow(
                	StartEvent_1.getOutPort(),
                	Receive_Emergency_Report.getInPort());
                
                SequenceFlow SequenceFlow_9 = new SequenceFlow(
                	Arrive_At_Patient_Place_BA.getOutPort(),
                	Yellow_Code.getInPort());
                
                SequenceFlow SequenceFlow_12 = new SequenceFlow(
                	ParallelGateway_1.getOutPort(1),
                	Pick_Up_Patient.getInPort());
                
                SequenceFlow SequenceFlow_7 = new SequenceFlow(
                	ExclusiveGateway_1.getOutPort(2),
                	Arrive_At_Patient_Place_BA.getInPort());
                
                SequenceFlow SequenceFlow_8 = new SequenceFlow(
                	Arrive_At_Patient_Place_QAV.getOutPort(),
                	Green_Code.getInPort());
                
                SequenceFlow SequenceFlow_15 = new SequenceFlow(
                	Authorize_Entry.getOutPort(),
                	Red_Code.getInPort());
                
                SequenceFlow SequenceFlow_3 = new SequenceFlow(
                	Classify_Triage.getOutPort(),
                	ExclusiveGateway_1.getInPort());
                
                SequenceFlow SequenceFlow_4 = new SequenceFlow(
                	ExclusiveGateway_1.getOutPort(0),
                	ParallelGateway_1.getInPort());
                
                SequenceFlow SequenceFlow_14 = new SequenceFlow(
                	ParallelGateway_2.getOutPort(),
                	Authorize_Entry.getInPort());
                
                // Message flows

                // Start the execution container
                scenario.startSimulation();
                scenario.collectData();
			}
			scenario.printStatistics();
			scenario.setCollabStats();
			scenario.setConfigStats();
			scenario.setSimReplications();
			simulazione = scenario.getSimulazione();
            //simulazione.setReplications(scenario.getReplications());
            simulazione.setTotalTokens(totalTokens);
            
            System.out.println("\nReplications: ");
            System.out.println(simulazione.getReplications());
            System.out.println("\nTotal Tokens: ");
            
            System.out.println("\nPerformers: ");
            System.out.println(simulazione.getConfigStats().getPerformers());
            
            System.out.println("\nElements: ");
            System.out.println(simulazione.getCollabStats().getElements());
            
            System.out.println("\n\nStatistiche Collaboration(EVENTI):");
            System.out.println();
            simulazione.getCollabStats().stampaArrayEventi();
            
            System.out.println("\nStatistiche Collaboration:");
            System.out.println();
            System.out.println("Total Collaboration Time:");
            System.out.println(simulazione.getCollabStats().getTotalCollaborationTime());
            System.out.println();
            simulazione.getCollabStats().stampaMeasuresMap();
            
            System.out.println("\nStatistiche Configuration:");
            System.out.println();
            System.out.println("Total Configuration Time:");
            System.out.println(simulazione.getConfigStats().getTotalTime());
            System.out.println();
            simulazione.getConfigStats().stampaMeasuresMap();
       
        } catch (InvalidNameException e) {
        	e.printStackTrace();
        } catch (UnableToStartEngineException e) {
        	e.printStackTrace();
        } catch (Layer2InternalException e) {
        	e.printStackTrace();
        }  catch(ThreadDeath e) {
        	e.printStackTrace();
        }
	}
	
}
