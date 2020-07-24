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
import it.uniroma2.sel.simlab.ebpmn.flownodes.gateways.ExclusiveConvergingGateway;
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

public class HardwareRetailer {
	
	Simulazione simulazione = new Simulazione();
	Map<String, Integer> performers = new HashMap<String, Integer>() {
		{
			put("WharehouseWorker", 1);
			put("Clerk", 1);
			put("LogisticManager", 1);
		}
	};
	
	/**
     * SIMULATION PARAMETERS
     */
    
    
    /**
     *  Simulation units
     */
    EBPMNTime.Unit performanceTimeUnit = EBPMNTime.Unit.S;
    EBPMNTime.Unit reliabilityTimeUnit = EBPMNTime.Unit.DAY;
    
    /**
     *  Simulation duration
     */
    int replications = 1;
    
    double simulationTimeValue = 1;
    EBPMNTime.Unit simulationTimeUnit = EBPMNTime.Unit.YEAR;

    double simulationTime = simulationTimeValue * simulationTimeUnit.getConversionFactor();
    
    /**
     *  Token inter-arrival
     */
    double tokenInterarrivalTimeValue = 3;
    EBPMNTime.Unit tokenInterarrivalTimeUnit = EBPMNTime.Unit.S;
    
    double tokenInterarrival = tokenInterarrivalTimeValue * tokenInterarrivalTimeUnit.getConversionFactor();
    
    int totalTokens = 1000000;                      
    
    /**
     * Other parameters
     */
    Standby standby = Standby.HOT;
    RequestType requestType = RequestType.EXPONENTIAL;
    ArrivalType arrivalType = ArrivalType.EXPONENTIAL;
    
 // declaration of eBPMN entities
    ExclusiveDivergingGateway _16;
    Task _30;
    Start _7;
    Task _10;
    ParallelConvergingGateway _41;
    ParallelDivergingGateway _8;
    Task _12;
    ExclusiveConvergingGateway _32;
    End _46;
    ExclusiveDivergingGateway _28;
    Task _20;
    ParallelConvergingGateway _35;
    ExclusiveConvergingGateway _38;
    Task _18;
    Task _26;
    Task _44;
    ParallelDivergingGateway _24;
    Task _22;
	Performer WharehouseWorker;
	Performer Clerk;
	Performer LogisticManager;
	
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
				 *  WharehouseWorker
				 */
				double WharehouseWorker_service = 1; // unit is: min
				double WharehouseWorker_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution WharehouseWorker_failureFunction = null;
				ProbabilityDistribution WharehouseWorker_repairFunction = null;
				  
				
				WharehouseWorker = new Performer(new EBPMNName("WharehouseWorker"),
				    getWorkingUnits("WharehouseWorker"), 
				    EBPMNTime.makeFrom(WharehouseWorker_service * WharehouseWorker_service_conv_factor), 
				    WharehouseWorker_failureFunction, 
				    WharehouseWorker_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Clerk
				 */
				double Clerk_service = 1; // unit is: min
				double Clerk_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Clerk_failureFunction = null;
				ProbabilityDistribution Clerk_repairFunction = null;
				            
				
				Clerk = new Performer(new EBPMNName("Clerk"),
				    getWorkingUnits("Clerk"), 
				    EBPMNTime.makeFrom(Clerk_service * Clerk_service_conv_factor), 
				    Clerk_failureFunction, 
				    Clerk_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  LogisticManager
				 */
				double LogisticManager_service = 1; // unit is: min
				double LogisticManager_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution LogisticManager_failureFunction = null;
				ProbabilityDistribution LogisticManager_repairFunction = null;
				              
				
				LogisticManager = new Performer(new EBPMNName("LogisticManager"),
				    getWorkingUnits("LogisticManager"), 
				    EBPMNTime.makeFrom(LogisticManager_service * LogisticManager_service_conv_factor), 
				    LogisticManager_failureFunction, 
				    LogisticManager_repairFunction,
				    EBPMNTime.zero()); 
				
				
                /**
                 *  _16
                 */
                // number of outgoing sequence flows
                int _16_TotOutgoing = 2;
                
                // probabilities
                Double [] _16_Probs = {0.85, 0.15};
                
                _16 = new ExclusiveDivergingGateway(new EBPMNName("_16"),
                	_16_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(_16_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  _30
                 */
                _30 = new Task(new EBPMNName("Take out extra insurance"), 
                    EBPMNTime.zero());
                
                double Req_1_from__30_to_LogisticManager_serviceQuantity = 30;
                _30.addResource(new Request(LogisticManager, requestType, Req_1_from__30_to_LogisticManager_serviceQuantity));   
                  
                
                
                /**
                 *  _7
                 */
                // set jobs number and interarrival for simulation
                double _7_Interarrival = tokenInterarrival;
                int _7_nToken = totalTokens;
                
                _7 = new Start(new EBPMNName("_7"),
                    _7_Interarrival,
                    arrivalType,
                    new TokenBasedTerminationPolicy(_7_nToken),
                    EBPMNTime.zero());
                
                
                /**
                 *  _10
                 */
                _10 = new Task(new EBPMNName("Decide if normal post or special shipment"), 
                    EBPMNTime.zero());
                
                double Req_1_from__10_to_Clerk_serviceQuantity = 5;
                _10.addResource(new Request(Clerk, requestType, Req_1_from__10_to_Clerk_serviceQuantity));   
                  
                
                
                /**
                 *  _41
                 */
                // number of incoming sequence flows
                int _41_TotIncoming = 2;
                
                _41 = new ParallelConvergingGateway(new EBPMNName("_41"),
                	_41_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  _8
                 */
                // number of outgoing sequence flows
                int _8_TotOutgoing = 2;
                
                _8 = new ParallelDivergingGateway(new EBPMNName("_8"),
                	_8_TotOutgoing,
                	EBPMNTime.zero());
                
                
                /**
                 *  _12
                 */
                _12 = new Task(new EBPMNName("Package goods"), 
                    EBPMNTime.zero());
                
                double Req_1_from__12_to_WharehouseWorker_serviceQuantity = 30;
                _12.addResource(new Request(WharehouseWorker, requestType, Req_1_from__12_to_WharehouseWorker_serviceQuantity));   
                  
                
                
                /**
                 *  _32
                 */
                // number of incoming sequence flows
                int _32_TotIncoming = 2;
                
                _32 = new ExclusiveConvergingGateway(new EBPMNName("_32"),
                	_32_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  _46
                 */
                _46 = new End(new EBPMNName("_46"));
                
                
                /**
                 *  _28
                 */
                // number of outgoing sequence flows
                int _28_TotOutgoing = 2;
                
                // probabilities
                Double [] _28_Probs = {0.2, 0.8};
                
                _28 = new ExclusiveDivergingGateway(new EBPMNName("_28"),
                	_28_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(_28_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  _20
                 */
                _20 = new Task(new EBPMNName("Request quotes from carriers"), 
                    EBPMNTime.zero());
                
                double Req_1_from__20_to_Clerk_serviceQuantity = 40;
                _20.addResource(new Request(Clerk, requestType, Req_1_from__20_to_Clerk_serviceQuantity));   
                  
                
                
                /**
                 *  _35
                 */
                // number of incoming sequence flows
                int _35_TotIncoming = 2;
                
                _35 = new ParallelConvergingGateway(new EBPMNName("_35"),
                	_35_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  _38
                 */
                // number of incoming sequence flows
                int _38_TotIncoming = 2;
                
                _38 = new ExclusiveConvergingGateway(new EBPMNName("_38"),
                	_38_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  _18
                 */
                _18 = new Task(new EBPMNName("Check if extra insurance is necessary"), 
                    EBPMNTime.zero());
                
                double Req_1_from__18_to_Clerk_serviceQuantity = 10;
                _18.addResource(new Request(Clerk, requestType, Req_1_from__18_to_Clerk_serviceQuantity));   
                  
                
                
                /**
                 *  _26
                 */
                _26 = new Task(new EBPMNName("Fill in a post label"), 
                    EBPMNTime.zero());
                
                double Req_1_from__26_to_Clerk_serviceQuantity = 5;
                _26.addResource(new Request(Clerk, requestType, Req_1_from__26_to_Clerk_serviceQuantity));   
                  
                
                
                /**
                 *  _44
                 */
                _44 = new Task(new EBPMNName("Move package to pick area"), 
                    EBPMNTime.zero());
                
                double Req_1_from__44_to_WharehouseWorker_serviceQuantity = 20;
                _44.addResource(new Request(WharehouseWorker, requestType, Req_1_from__44_to_WharehouseWorker_serviceQuantity));   
                  
                
                
                /**
                 *  _24
                 */
                // number of outgoing sequence flows
                int _24_TotOutgoing = 2;
                
                _24 = new ParallelDivergingGateway(new EBPMNName("_24"),
                	_24_TotOutgoing,
                	EBPMNTime.zero());
                
                
                /**
                 *  _22
                 */
                _22 = new Task(new EBPMNName("Assign carrier and prepare paperworks"), 
                    EBPMNTime.zero());
                
                double Req_1_from__22_to_Clerk_serviceQuantity = 15;
                _22.addResource(new Request(Clerk, requestType, Req_1_from__22_to_Clerk_serviceQuantity));   
                  
                
                

                // Sequence flows
                SequenceFlow _39 = new SequenceFlow(
                	_22.getOutPort(),
                	_38.getInPort(0));
                
                SequenceFlow _47 = new SequenceFlow(
                	_44.getOutPort(),
                	_46.getInPort());
                
                SequenceFlow _21 = new SequenceFlow(
                	_16.getOutPort(1),
                	_20.getInPort());
                
                SequenceFlow _31 = new SequenceFlow(
                	_28.getOutPort(0),
                	_30.getInPort());
                
                SequenceFlow _34 = new SequenceFlow(
                	_28.getOutPort(1),
                	_32.getInPort(1));
                
                SequenceFlow _45 = new SequenceFlow(
                	_41.getOutPort(),
                	_44.getInPort());
                
                SequenceFlow _25 = new SequenceFlow(
                	_18.getOutPort(),
                	_24.getInPort());
                
                SequenceFlow _40 = new SequenceFlow(
                	_35.getOutPort(),
                	_38.getInPort(1));
                
                SequenceFlow SequenceFlow_2 = new SequenceFlow(
                	_8.getOutPort(0),
                	_10.getInPort());
                
                SequenceFlow _29 = new SequenceFlow(
                	_24.getOutPort(1),
                	_28.getInPort());
                
                SequenceFlow SequenceFlow_3 = new SequenceFlow(
                	_8.getOutPort(1),
                	_12.getInPort());
                
                SequenceFlow _37 = new SequenceFlow(
                	_32.getOutPort(),
                	_35.getInPort(0));
                
                SequenceFlow _27 = new SequenceFlow(
                	_24.getOutPort(0),
                	_26.getInPort());
                
                SequenceFlow _36 = new SequenceFlow(
                	_26.getOutPort(),
                	_35.getInPort(1));
                
                SequenceFlow _23 = new SequenceFlow(
                	_20.getOutPort(),
                	_22.getInPort());
                
                SequenceFlow _43 = new SequenceFlow(
                	_38.getOutPort(),
                	_41.getInPort(0));
                
                SequenceFlow _33 = new SequenceFlow(
                	_30.getOutPort(),
                	_32.getInPort(0));
                
                SequenceFlow SequenceFlow_1 = new SequenceFlow(
                	_7.getOutPort(),
                	_8.getInPort());
                
                SequenceFlow _17 = new SequenceFlow(
                	_10.getOutPort(),
                	_16.getInPort());
                
                SequenceFlow _19 = new SequenceFlow(
                	_16.getOutPort(0),
                	_18.getInPort());
                
                SequenceFlow _42 = new SequenceFlow(
                	_12.getOutPort(),
                	_41.getInPort(1));
                

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