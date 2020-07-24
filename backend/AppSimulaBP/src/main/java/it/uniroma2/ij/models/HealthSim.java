package it.uniroma2.ij.models;

import it.uniroma2.sel.simlab.ebpmn.general.Simulazione;

import java.util.HashMap;
import java.util.Map;

import it.uniroma2.sel.simlab.ebpmn.buffers.*;
import it.uniroma2.sel.simlab.ebpmn.connections.*;
import it.uniroma2.sel.simlab.ebpmn.data.*;
import it.uniroma2.sel.simlab.ebpmn.data.policies.*;
import it.uniroma2.sel.simlab.ebpmn.exceptions.*;
import it.uniroma2.sel.simlab.ebpmn.flownodes.*;
import it.uniroma2.sel.simlab.ebpmn.flownodes.activities.*;
import it.uniroma2.sel.simlab.ebpmn.flownodes.events.*;
import it.uniroma2.sel.simlab.ebpmn.flownodes.events.Start.*;
import it.uniroma2.sel.simlab.ebpmn.flownodes.gateways.*;
import it.uniroma2.sel.simlab.ebpmn.general.*;
import it.uniroma2.sel.simlab.ebpmn.math.*;
import it.uniroma2.sel.simlab.ebpmn.resources.*;
import it.uniroma2.sel.simlab.ebpmn.resources.broker.*;
import it.uniroma2.sel.simlab.ebpmn.resources.broker.policies.*;
import it.uniroma2.sel.simlab.ebpmn.resources.performer.*;
import it.uniroma2.sel.simlab.ebpmn.resources.subsystem.*;
import it.uniroma2.sel.simlab.ebpmn.resources.Request.*;
import it.uniroma2.sel.simlab.jrand.objectStreams.numericStreams.*;
import it.uniroma2.sel.simlab.jrand.objectStreams.numericStreams.pseudoRandomGenerators.*;
import it.uniroma2.sel.simlab.simarch.data.*;
import it.uniroma2.sel.simlab.simarch.exceptions.*;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.*;

public class HealthSim {

	Simulazione simulazione = new Simulazione();
	Map<String, Integer> performers = new HashMap<String, Integer>() {
		{
			put("Doctor", 1);
			put("Medical_Center", 1);
			put("Salesman", 1);
			put("Hospital", 2);
			put("Pharma_Industry", 3);
		}
	};
	
	 /******************************************************************
     * SIMULATION PARAMETERS
     *
     * Please modify following parameters according to simulation needs
     *****************************************************************/


    /*****************************************************************
     *  Simulation results time units
     *
     *  These are the time units used when providing performance
     *  and reliability results (us, ms, s, min, hour, day, working
     *  day, year, working year)
     *****************************************************************/
    EBPMNTime.Unit performanceTimeUnit = EBPMNTime.Unit.MIN;
    EBPMNTime.Unit reliabilityTimeUnit = EBPMNTime.Unit.DAY;


    /*****************************************************************
     *  Simulation limits
     *
     *  Set up simulation duration (specifying value and time unit)
     *  and number of simulation replications
     *****************************************************************/          
    double simulationTimeValue = 365;
    EBPMNTime.Unit simulationTimeUnit = EBPMNTime.Unit.DAY;

    int replications = 1;
	
    
    /*****************************************************************
     *  Process workload
     *
     *  Specify token inter-arrival time (value and unit), arrivals
     *  distribution (constant, exponential) and maximum amount of
     *  token to generate
     *****************************************************************/          
    double tokenInterarrivalTimeValue = 180;
    EBPMNTime.Unit tokenInterarrivalTimeUnit = EBPMNTime.Unit.MIN;

    ArrivalType arrivalType = ArrivalType.EXPONENTIAL;
    
    int totalTokens = 10000;                      
    
	// declaration of eBPMN entities
    Start _9;
    Task _47;
    Task _18;
    Task _26;
    Task _29;
    Task _43;
    ExclusiveConvergingGateway _11;
    Task _37;
    Task _24;
    End _52;
    ExclusiveDivergingGateway _33;
    ExclusiveDivergingGateway _20;
    Task _14;
    Task _39;
    ExclusiveConvergingGateway _22;
	Performer Doctor;
	Performer Medical_Center;
	Performer Salesman;
	Performer Hospital;
	Performer Pharma_Industry;
	
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
	
		try {
          

            /*****************************************************************
             *  Resource parameters
             *
             *  Specify if resources can fail, the redundancy policy
             *  (hot or cold) and the statistical distribution of service
             *  demand (constant, exponential, normal, lognormal)
             *****************************************************************/          
            Boolean canFail = false;
            Standby standby = Standby.HOT;
            
            RequestType requestType = RequestType.EXPONENTIAL;


            /*****************************************************************
             * SIMULATION CODE
             *
             * WARNING: MANUAL MODIFICATION OF THE FOLLOWING CODE CAN ALTER
             * EBPMN FROM FUNCTIONING
             *
             *****************************************************************/          
            
            // Simulation limit
            double simulationTime = simulationTimeValue * simulationTimeUnit.getConversionFactor();
            
            // Number of process executions
            Scenario scenario = Scenario.getInstance();
            scenario.setReplications(replications);
            scenario.setPerformanceTimeUnit(performanceTimeUnit);
            scenario.setReliabilityTimeUnit(reliabilityTimeUnit);
			Long simStart;

            // Workload
            double tokenInterarrival = tokenInterarrivalTimeValue * tokenInterarrivalTimeUnit.getConversionFactor();
                

            for (int run=0; run<scenario.getReplications(); run++) {
				simStart = System.currentTimeMillis();
                
                // allocation of Layer 2 (SimJ) implementation
                EBPMNSimFactory.create(EBPMNTime.makeFrom(simulationTime));
                scenario.init();


				/**
				 *  Doctor
				 */
				double Doctor_service = 1; // unit is: min
				double Doctor_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Doctor_failureFunction = null;
				ProbabilityDistribution Doctor_repairFunction = null;
				              
				
				Doctor = new Performer(new EBPMNName("Doctor"),
				    getWorkingUnits("Doctor"), 
				    EBPMNTime.makeFrom(Doctor_service * Doctor_service_conv_factor), 
				    Doctor_failureFunction, 
				    Doctor_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Medical_Center
				 */
				double Medical_Center_service = 1; // unit is: min
				double Medical_Center_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Medical_Center_failureFunction = null;
				ProbabilityDistribution Medical_Center_repairFunction = null;
				              
				
				Medical_Center = new Performer(new EBPMNName("Medical_Center"),
				    getWorkingUnits("Medical_Center"), 
				    EBPMNTime.makeFrom(Medical_Center_service * Medical_Center_service_conv_factor), 
				    Medical_Center_failureFunction, 
				    Medical_Center_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Salesman
				 */
				double Salesman_service = 1; // unit is: min
				double Salesman_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Salesman_failureFunction = null;
				ProbabilityDistribution Salesman_repairFunction = null;
				             
				
				Salesman = new Performer(new EBPMNName("Salesman"),
				    getWorkingUnits("Salesman"), 
				    EBPMNTime.makeFrom(Salesman_service * Salesman_service_conv_factor), 
				    Salesman_failureFunction, 
				    Salesman_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Hospital
				 */
				double Hospital_service = 1; // unit is: min
				double Hospital_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Hospital_failureFunction = null;
				ProbabilityDistribution Hospital_repairFunction = null;
				             
				
				Hospital = new Performer(new EBPMNName("Hospital"),
				    getWorkingUnits("Hospital"), 
				    EBPMNTime.makeFrom(Hospital_service * Hospital_service_conv_factor), 
				    Hospital_failureFunction, 
				    Hospital_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Pharma_Industry
				 */
				double Pharma_Industry_service = 1; // unit is: min
				double Pharma_Industry_service_conv_factor = 1.0 * 60;
				
				ProbabilityDistribution Pharma_Industry_failureFunction = null;
				ProbabilityDistribution Pharma_Industry_repairFunction = null;
				              
				
				Pharma_Industry = new Performer(new EBPMNName("Pharma_Industry"),
				    getWorkingUnits("Pharma_Industry"), 
				    EBPMNTime.makeFrom(Pharma_Industry_service * Pharma_Industry_service_conv_factor), 
				    Pharma_Industry_failureFunction, 
				    Pharma_Industry_repairFunction,
				    EBPMNTime.zero()); 
				
				
                /**
                 *  _9
                 */
                // set jobs number and interarrival for simulation
                double _9_Interarrival = tokenInterarrival;
                int _9_nToken = totalTokens;
                
                _9 = new Start(new EBPMNName("_9"),
                    _9_Interarrival,
                    arrivalType,
                    new TokenBasedTerminationPolicy(_9_nToken),
                    EBPMNTime.zero());
                
                
                /**
                 *  _47
                 */
                _47 = new Task(new EBPMNName("Notify patient of medicines availability"), 
                    EBPMNTime.zero());
                
                double Req_1_from__47_to_Medical_Center_serviceQuantity = 60;
                _47.addResource(new Request(Medical_Center, requestType, Req_1_from__47_to_Medical_Center_serviceQuantity));   
                  
                
                
                /**
                 *  _18
                 */
                _18 = new Task(new EBPMNName("Check therapeutic plan"), 
                    EBPMNTime.zero());
                
                double Req_1_from__18_to_Doctor_serviceQuantity = 45;
                _18.addResource(new Request(Doctor, requestType, Req_1_from__18_to_Doctor_serviceQuantity));   
                  
                
                
                /**
                 *  _26
                 */
                _26 = new Task(new EBPMNName("Check order"), 
                    EBPMNTime.zero());
                
                double Req_1_from__26_to_Pharma_Industry_serviceQuantity = 5;
                _26.addResource(new Request(Pharma_Industry, requestType, Req_1_from__26_to_Pharma_Industry_serviceQuantity));   
                  
                
                
                /**
                 *  _29
                 */
                _29 = new Task(new EBPMNName("Process order"), 
                    EBPMNTime.zero());
                
                double Req_1_from__29_to_Salesman_serviceQuantity = 60;
                _29.addResource(new Request(Salesman, requestType, Req_1_from__29_to_Salesman_serviceQuantity));   
                  
                
                
                /**
                 *  _43
                 */
                _43 = new Task(new EBPMNName("Send medicines to medical center"), 
                    EBPMNTime.zero());
                
                double Req_1_from__43_to_Hospital_serviceQuantity = 180;
                _43.addResource(new Request(Hospital, requestType, Req_1_from__43_to_Hospital_serviceQuantity));   
                  
                
                
                /**
                 *  _11
                 */
                // number of incoming sequence flows
                int _11_TotIncoming = 2;
                
                _11 = new ExclusiveConvergingGateway(new EBPMNName("_11"),
                	_11_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  _37
                 */
                _37 = new Task(new EBPMNName("Prepare medicine"), 
                    EBPMNTime.zero());
                
                double Req_1_from__37_to_Pharma_Industry_serviceQuantity = 360;
                _37.addResource(new Request(Pharma_Industry, requestType, Req_1_from__37_to_Pharma_Industry_serviceQuantity));   
                  
                
                
                /**
                 *  _24
                 */
                _24 = new Task(new EBPMNName("Approve plan"), 
                    EBPMNTime.zero());
                
                double Req_1_from__24_to_Doctor_serviceQuantity = 20;
                _24.addResource(new Request(Doctor, requestType, Req_1_from__24_to_Doctor_serviceQuantity));   
                  
                
                
                /**
                 *  _52
                 */
                _52 = new End(new EBPMNName("_52"));
                
                
                /**
                 *  _33
                 */
                // number of outgoing sequence flows
                int _33_TotOutgoing = 2;
                
                // probabilities
                Double [] _33_Probs = {0.85, 0.15};
                
                _33 = new ExclusiveDivergingGateway(new EBPMNName("_33"),
                	_33_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(_33_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  _20
                 */
                // number of outgoing sequence flows
                int _20_TotOutgoing = 2;
                
                // probabilities
                Double [] _20_Probs = {0.85, 0.15};
                
                _20 = new ExclusiveDivergingGateway(new EBPMNName("_20"),
                	_20_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(_20_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  _14
                 */
                _14 = new Task(new EBPMNName("Gather therapeutic plan"), 
                    EBPMNTime.zero());
                
                double Req_1_from__14_to_Salesman_serviceQuantity = 15;
                _14.addResource(new Request(Salesman, requestType, Req_1_from__14_to_Salesman_serviceQuantity));   
                  
                
                
                /**
                 *  _39
                 */
                _39 = new Task(new EBPMNName("Send medicines to hospital"), 
                    EBPMNTime.zero());
                
                double Req_1_from__39_to_Pharma_Industry_serviceQuantity = 60;
                _39.addResource(new Request(Pharma_Industry, requestType, Req_1_from__39_to_Pharma_Industry_serviceQuantity));   
                  
                
                
                /**
                 *  _22
                 */
                // number of incoming sequence flows
                int _22_TotIncoming = 2;
                
                _22 = new ExclusiveConvergingGateway(new EBPMNName("_22"),
                	_22_TotIncoming,            		
                	EBPMNTime.zero());
                
                

                // Sequence flows
                SequenceFlow _31 = new SequenceFlow(
                	_33.getOutPort(1),
                	_22.getInPort(1));
                
                SequenceFlow _25 = new SequenceFlow(
                	_20.getOutPort(0),
                	_24.getInPort());
                
                SequenceFlow _36 = new SequenceFlow(
                	_43.getOutPort(),
                	_47.getInPort());
                
                SequenceFlow _40 = new SequenceFlow(
                	_37.getOutPort(),
                	_39.getInPort());
                
                SequenceFlow _15 = new SequenceFlow(
                	_9.getOutPort(),
                	_11.getInPort(1));
                
                SequenceFlow _32 = new SequenceFlow(
                	_22.getOutPort(),
                	_29.getInPort());
                
                SequenceFlow _34 = new SequenceFlow(
                	_47.getOutPort(),
                	_52.getInPort());
                
                SequenceFlow _16 = new SequenceFlow(
                	_11.getOutPort(),
                	_14.getInPort());
                
                SequenceFlow _19 = new SequenceFlow(
                	_20.getOutPort(1),
                	_11.getInPort(0));
                
                SequenceFlow _30 = new SequenceFlow(
                	_33.getOutPort(0),
                	_37.getInPort());
                
                SequenceFlow _21 = new SequenceFlow(
                	_18.getOutPort(),
                	_20.getInPort());
                
                SequenceFlow _35 = new SequenceFlow(
                	_39.getOutPort(),
                	_43.getInPort());
                
                SequenceFlow _28 = new SequenceFlow(
                	_26.getOutPort(),
                	_33.getInPort());
                
                SequenceFlow _23 = new SequenceFlow(
                	_24.getOutPort(),
                	_22.getInPort(0));
                
                SequenceFlow _27 = new SequenceFlow(
                	_29.getOutPort(),
                	_26.getInPort());
                
                SequenceFlow _17 = new SequenceFlow(
                	_14.getOutPort(),
                	_18.getInPort());
                

                // Message flows

                // Start the execution container
                scenario.startSimulation();
                System.out.println("Simulation #" + run + " complete in " + (System.currentTimeMillis() - simStart) + " milliseconds\n");
                scenario.collectData();                
		}

            System.out.println("\nCompleted " + scenario.getReplications() + " process simulations.");
            //scenario.printStatistics();
            scenario.setCollabStats();
			scenario.setConfigStats();
			simulazione = scenario.getSimulazione();
            simulazione.setReplications(scenario.getReplications());
            simulazione.setTotalTokens(totalTokens);
            
            
            System.out.println("\nPerformers: ");
            System.out.println(simulazione.getConfigStats().getPerformers());
            
            System.out.println("\nElements: ");
            System.out.println(simulazione.getCollabStats().getElements());
            
            System.out.println("Statistiche Collaboration(EVENTI):");
            System.out.println();
            System.out.println("Total Collaboration Time:");
            System.out.println(simulazione.getCollabStats().getTotalCollaborationTime());
            System.out.println();
            simulazione.getCollabStats().stampaArrayEventi();
            
            System.out.println("Statistiche Collaboration:");
            System.out.println();
            simulazione.getCollabStats().stampaMeasuresMap();
            
            System.out.println("Statistiche Configuration:");
            System.out.println();
            System.out.println("Replications: ");
            System.out.println(simulazione.getReplications());
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
