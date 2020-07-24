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
import it.uniroma2.sel.simlab.ebpmn.math.ExponentialProbabilityDistribution;
import it.uniroma2.sel.simlab.ebpmn.math.ProbabilityDistribution;
import it.uniroma2.sel.simlab.ebpmn.resources.Request;
import it.uniroma2.sel.simlab.ebpmn.resources.Request.RequestType;
import it.uniroma2.sel.simlab.ebpmn.resources.broker.Standby;
import it.uniroma2.sel.simlab.ebpmn.resources.performer.Performer;
import it.uniroma2.sel.simlab.simarch.exceptions.InvalidNameException;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.Layer2InternalException;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.UnableToStartEngineException;

public class OrderProcessing {
	Simulazione simulazione = new Simulazione();
	Map<String, Integer> performers = new HashMap<String, Integer>() {
		{
			put("WS_Credit_Card", 1);
			put("DB", 1);
			put("WS_Shipping", 1);
			put("Server", 1);
			put("Net", 1);
			put("CRM", 1);
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
    int replications = 50;
    
    double simulationTimeValue = 1;
    EBPMNTime.Unit simulationTimeUnit = EBPMNTime.Unit.YEAR;

    double simulationTime = simulationTimeValue * simulationTimeUnit.getConversionFactor();
    
    /**
     *  Token inter-arrival
     */
    double tokenInterarrivalTimeValue = 30;
    EBPMNTime.Unit tokenInterarrivalTimeUnit = EBPMNTime.Unit.DAY;
    
    double tokenInterarrival = tokenInterarrivalTimeValue * tokenInterarrivalTimeUnit.getConversionFactor();
    
    int totalTokens = 1000000;                      
    
    /**
     * Other parameters
     */
    Standby standby = Standby.HOT;
    RequestType requestType = RequestType.EXPONENTIAL;
    ArrivalType arrivalType = ArrivalType.EXPONENTIAL;

	Boolean failable = true;
    
	// declaration of eBPMN entities
    Task Update_Delivery_Time;
    Task Checkout_Order;
    Task Send_Confirmation;
    ExclusiveDivergingGateway ExclusiveGateway_Credit_Card_1;
    Task Check_Stock;
    ExclusiveDivergingGateway ExclusiveGateway_Check_Stock_1;
    Task Request_Shipping_Order;
    Task Send_Order_to_Warehouse;
    ExclusiveConvergingGateway ExclusiveGateway_Check_Stock_2;
    Task Check_Credit_Card;
    Start StartEvent_1;
    Task Prepare_Invoice;
    ParallelConvergingGateway ParallelGateway_Send_Order_Warehouse_2;
    End EndEvent_Credit_Card;
    End Order_Confirmed;
    ParallelDivergingGateway ParallelGateway_Send_Order_Warehouse_1;
	Performer WS_Credit_Card;
	Performer DB;
	Performer WS_Shipping;
	Performer Server;
	Performer Net;
	Performer CRM;
	
	
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
				 *  WS_Credit_Card
				 */
				double WS_Credit_Card_service = 1.2; // unit is: s
				double WS_Credit_Card_service_conv_factor = 1.0;
				
				double WS_Credit_Card_mttf = 260; // unit is: day
				double WS_Credit_Card_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution WS_Credit_Card_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (WS_Credit_Card_mttf * WS_Credit_Card_mttf_conv_factor)) : null;
				
				double WS_Credit_Card_mttr = 4; // unit is: hour
				double WS_Credit_Card_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution WS_Credit_Card_repairFunction = new ExponentialProbabilityDistribution(1.0 / (WS_Credit_Card_mttr * WS_Credit_Card_mttr_conv_factor));
				              				
				WS_Credit_Card = new Performer(new EBPMNName("WS_Credit_Card"),
				    getWorkingUnits("WS_Credit_Card"), 
				    EBPMNTime.makeFrom(WS_Credit_Card_service * WS_Credit_Card_service_conv_factor), 
				    WS_Credit_Card_failureFunction, 
				    WS_Credit_Card_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  DB
				 */
				double DB_service = 420; // unit is: ms
				double DB_service_conv_factor = 1.0 / 1000;
				
				double DB_mttf = 110; // unit is: day
				double DB_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution DB_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (DB_mttf * DB_mttf_conv_factor)) : null;
				
				double DB_mttr = 36; // unit is: hour
				double DB_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution DB_repairFunction = new ExponentialProbabilityDistribution(1.0 / (DB_mttr * DB_mttr_conv_factor));
				              
				
				DB = new Performer(new EBPMNName("DB"),
				    getWorkingUnits("DB"), 
				    EBPMNTime.makeFrom(DB_service * DB_service_conv_factor), 
				    DB_failureFunction, 
				    DB_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  WS_Shipping
				 */
				double WS_Shipping_service = 2.6; // unit is: s
				double WS_Shipping_service_conv_factor = 1.0;
				
				double WS_Shipping_mttf = 210; // unit is: day
				double WS_Shipping_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution WS_Shipping_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (WS_Shipping_mttf * WS_Shipping_mttf_conv_factor)) : null;
				
				double WS_Shipping_mttr = 8; // unit is: hour
				double WS_Shipping_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution WS_Shipping_repairFunction = new ExponentialProbabilityDistribution(1.0 / (WS_Shipping_mttr * WS_Shipping_mttr_conv_factor));
				              
				
				WS_Shipping = new Performer(new EBPMNName("WS_Shipping"),
				    getWorkingUnits("WS_Shipping"), 
				    EBPMNTime.makeFrom(WS_Shipping_service * WS_Shipping_service_conv_factor), 
				    WS_Shipping_failureFunction, 
				    WS_Shipping_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Server
				 */
				double Server_service = 300; // unit is: ms
				double Server_service_conv_factor = 1.0 / 1000;
				
				double Server_mttf = 150; // unit is: day
				double Server_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution Server_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (Server_mttf * Server_mttf_conv_factor)) : null;
				
				double Server_mttr = 16; // unit is: hour
				double Server_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution Server_repairFunction = new ExponentialProbabilityDistribution(1.0 / (Server_mttr * Server_mttr_conv_factor));
				              
				
				Server = new Performer(new EBPMNName("Server"),
				    getWorkingUnits("Server"), 
				    EBPMNTime.makeFrom(Server_service * Server_service_conv_factor), 
				    Server_failureFunction, 
				    Server_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  Net
				 */
				double Net_service = 0.8; // unit is: s
				double Net_service_conv_factor = 1.0;
				
				double Net_mttf = 180; // unit is: day
				double Net_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution Net_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (Net_mttf * Net_mttf_conv_factor)) : null;
				
				double Net_mttr = 12; // unit is: hour
				double Net_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution Net_repairFunction = new ExponentialProbabilityDistribution(1.0 / (Net_mttr * Net_mttr_conv_factor));
				              
				
				Net = new Performer(new EBPMNName("Net"),
				    getWorkingUnits("Net"), 
				    EBPMNTime.makeFrom(Net_service * Net_service_conv_factor), 
				    Net_failureFunction, 
				    Net_repairFunction,
				    EBPMNTime.zero()); 
				
				
				/**
				 *  CRM
				 */
				double CRM_service = 850; // unit is: ms
				double CRM_service_conv_factor = 1.0 / 1000;
				
				double CRM_mttf = 125; // unit is: day
				double CRM_mttf_conv_factor = 1.0 * 3600*24;
				
				ProbabilityDistribution CRM_failureFunction = failable ? new ExponentialProbabilityDistribution(1.0 / (CRM_mttf * CRM_mttf_conv_factor)) : null;
				
				double CRM_mttr = 45; // unit is: hour
				double CRM_mttr_conv_factor = 1.0 * 3600;
				
				ProbabilityDistribution CRM_repairFunction = new ExponentialProbabilityDistribution(1.0 / (CRM_mttr * CRM_mttr_conv_factor));
				              
				
				CRM = new Performer(new EBPMNName("CRM"),
				    getWorkingUnits("CRM"), 
				    EBPMNTime.makeFrom(CRM_service * CRM_service_conv_factor), 
				    CRM_failureFunction, 
				    CRM_repairFunction,
				    EBPMNTime.zero()); 
				
				
                /**
                 *  Update_Delivery_Time
                 */
                Update_Delivery_Time = new Task(new EBPMNName("Update Delivery Time"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Update_Delivery_Time_to_Server_serviceQuantity = 1.0;
                Update_Delivery_Time.addResource(new Request(Server, requestType, Req_1_from_Update_Delivery_Time_to_Server_serviceQuantity));   
                  
                
                
                /**
                 *  Checkout_Order
                 */
                Checkout_Order = new Task(new EBPMNName("Checkout Order"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Checkout_Order_to_Net_serviceQuantity = 0.3;
                Checkout_Order.addResource(new Request(Net, requestType, Req_1_from_Checkout_Order_to_Net_serviceQuantity));   
                  
                
                double Req_2_from_Checkout_Order_to_Server_serviceQuantity = 1.0;
                Checkout_Order.addResource(new Request(Server, requestType, Req_2_from_Checkout_Order_to_Server_serviceQuantity));   
                  
                
                double Req_3_from_Checkout_Order_to_CRM_serviceQuantity = 1.0;
                Checkout_Order.addResource(new Request(CRM, requestType, Req_3_from_Checkout_Order_to_CRM_serviceQuantity));   
                  
                
                double Req_4_from_Checkout_Order_to_Server_serviceQuantity = 1.0;
                Checkout_Order.addResource(new Request(Server, requestType, Req_4_from_Checkout_Order_to_Server_serviceQuantity));   
                  
                
                
                /**
                 *  Send_Confirmation
                 */
                Send_Confirmation = new Task(new EBPMNName("Send Confirmation"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Send_Confirmation_to_Server_serviceQuantity = 1.0;
                Send_Confirmation.addResource(new Request(Server, requestType, Req_1_from_Send_Confirmation_to_Server_serviceQuantity));   
                  
                
                double Req_2_from_Send_Confirmation_to_CRM_serviceQuantity = 1.0;
                Send_Confirmation.addResource(new Request(CRM, requestType, Req_2_from_Send_Confirmation_to_CRM_serviceQuantity));   
                  
                
                double Req_3_from_Send_Confirmation_to_Net_serviceQuantity = 0.7;
                Send_Confirmation.addResource(new Request(Net, requestType, Req_3_from_Send_Confirmation_to_Net_serviceQuantity));   
                  
                
                
                /**
                 *  ExclusiveGateway_Credit_Card_1
                 */
                // number of outgoing sequence flows
                int ExclusiveGateway_Credit_Card_1_TotOutgoing = 2;
                
                // probabilities
                Double [] ExclusiveGateway_Credit_Card_1_Probs = {0.9, 0.1};
                
                ExclusiveGateway_Credit_Card_1 = new ExclusiveDivergingGateway(new EBPMNName("ExclusiveGateway_Credit_Card_1"),
                	ExclusiveGateway_Credit_Card_1_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(ExclusiveGateway_Credit_Card_1_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  Check_Stock
                 */
                Check_Stock = new Task(new EBPMNName("Check Stock"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Check_Stock_to_Server_serviceQuantity = 1.0;
                Check_Stock.addResource(new Request(Server, requestType, Req_1_from_Check_Stock_to_Server_serviceQuantity));   
                  
                
                double Req_2_from_Check_Stock_to_Net_serviceQuantity = 0.3;
                Check_Stock.addResource(new Request(Net, requestType, Req_2_from_Check_Stock_to_Net_serviceQuantity));   
                  
                
                double Req_3_from_Check_Stock_to_DB_serviceQuantity = 1.0;
                Check_Stock.addResource(new Request(DB, requestType, Req_3_from_Check_Stock_to_DB_serviceQuantity));   
                  
                
                double Req_4_from_Check_Stock_to_Net_serviceQuantity = 0.1;
                Check_Stock.addResource(new Request(Net, requestType, Req_4_from_Check_Stock_to_Net_serviceQuantity));   
                  
                
                double Req_5_from_Check_Stock_to_Server_serviceQuantity = 1.0;
                Check_Stock.addResource(new Request(Server, requestType, Req_5_from_Check_Stock_to_Server_serviceQuantity));   
                  
                
                
                /**
                 *  ExclusiveGateway_Check_Stock_1
                 */
                // number of outgoing sequence flows
                int ExclusiveGateway_Check_Stock_1_TotOutgoing = 2;
                
                // probabilities
                Double [] ExclusiveGateway_Check_Stock_1_Probs = {0.75, 0.25};
                
                ExclusiveGateway_Check_Stock_1 = new ExclusiveDivergingGateway(new EBPMNName("ExclusiveGateway_Check_Stock_1"),
                	ExclusiveGateway_Check_Stock_1_TotOutgoing,
                	new ProbabilityBasedRoutingPolicy(ExclusiveGateway_Check_Stock_1_Probs),
                	EBPMNTime.zero());
                
                
                /**
                 *  Request_Shipping_Order
                 */
                Request_Shipping_Order = new Task(new EBPMNName("Request Shipping Order"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Request_Shipping_Order_to_Server_serviceQuantity = 1.0;
                Request_Shipping_Order.addResource(new Request(Server, requestType, Req_1_from_Request_Shipping_Order_to_Server_serviceQuantity));   
                  
                
                double Req_2_from_Request_Shipping_Order_to_Net_serviceQuantity = 0.5;
                Request_Shipping_Order.addResource(new Request(Net, requestType, Req_2_from_Request_Shipping_Order_to_Net_serviceQuantity));   
                  
                
                double Req_3_from_Request_Shipping_Order_to_WS_Shipping_serviceQuantity = 1.0;
                Request_Shipping_Order.addResource(new Request(WS_Shipping, requestType, Req_3_from_Request_Shipping_Order_to_WS_Shipping_serviceQuantity));   
                  
                
                double Req_4_from_Request_Shipping_Order_to_Net_serviceQuantity = 0.2;
                Request_Shipping_Order.addResource(new Request(Net, requestType, Req_4_from_Request_Shipping_Order_to_Net_serviceQuantity));   
                  
                
                
                /**
                 *  Send_Order_to_Warehouse
                 */
                Send_Order_to_Warehouse = new Task(new EBPMNName("Send Order to Warehouse"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Send_Order_to_Warehouse_to_Server_serviceQuantity = 1.0;
                Send_Order_to_Warehouse.addResource(new Request(Server, requestType, Req_1_from_Send_Order_to_Warehouse_to_Server_serviceQuantity));   
                  
                
                double Req_2_from_Send_Order_to_Warehouse_to_Net_serviceQuantity = 0.3;
                Send_Order_to_Warehouse.addResource(new Request(Net, requestType, Req_2_from_Send_Order_to_Warehouse_to_Net_serviceQuantity));   
                  
                
                double Req_3_from_Send_Order_to_Warehouse_to_DB_serviceQuantity = 1.0;
                Send_Order_to_Warehouse.addResource(new Request(DB, requestType, Req_3_from_Send_Order_to_Warehouse_to_DB_serviceQuantity));   
                  
                
                
                /**
                 *  ExclusiveGateway_Check_Stock_2
                 */
                // number of incoming sequence flows
                int ExclusiveGateway_Check_Stock_2_TotIncoming = 2;
                
                ExclusiveGateway_Check_Stock_2 = new ExclusiveConvergingGateway(new EBPMNName("ExclusiveGateway_Check_Stock_2"),
                	ExclusiveGateway_Check_Stock_2_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  Check_Credit_Card
                 */
                Check_Credit_Card = new Task(new EBPMNName("Check Credit Card"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Check_Credit_Card_to_Net_serviceQuantity = 0.3;
                Check_Credit_Card.addResource(new Request(Net, requestType, Req_1_from_Check_Credit_Card_to_Net_serviceQuantity));   
                  
                
                double Req_2_from_Check_Credit_Card_to_WS_Credit_Card_serviceQuantity = 1.0;
                Check_Credit_Card.addResource(new Request(WS_Credit_Card, requestType, Req_2_from_Check_Credit_Card_to_WS_Credit_Card_serviceQuantity));   
                  
                
                double Req_3_from_Check_Credit_Card_to_Net_serviceQuantity = 0.1;
                Check_Credit_Card.addResource(new Request(Net, requestType, Req_3_from_Check_Credit_Card_to_Net_serviceQuantity));   
                  
                
                double Req_4_from_Check_Credit_Card_to_Server_serviceQuantity = 1.0;
                Check_Credit_Card.addResource(new Request(Server, requestType, Req_4_from_Check_Credit_Card_to_Server_serviceQuantity));   
                  
                
                
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
                 *  Prepare_Invoice
                 */
                Prepare_Invoice = new Task(new EBPMNName("Prepare Invoice"), 
                    EBPMNTime.zero());
                
                double Req_1_from_Prepare_Invoice_to_Server_serviceQuantity = 1.0;
                Prepare_Invoice.addResource(new Request(Server, requestType, Req_1_from_Prepare_Invoice_to_Server_serviceQuantity));   
                  
                
                double Req_2_from_Prepare_Invoice_to_CRM_serviceQuantity = 1.0;
                Prepare_Invoice.addResource(new Request(CRM, requestType, Req_2_from_Prepare_Invoice_to_CRM_serviceQuantity));   
                  
                
                double Req_3_from_Prepare_Invoice_to_Server_serviceQuantity = 1.0;
                Prepare_Invoice.addResource(new Request(Server, requestType, Req_3_from_Prepare_Invoice_to_Server_serviceQuantity));   
                  
                
                
                /**
                 *  ParallelGateway_Send_Order_Warehouse_2
                 */
                // number of incoming sequence flows
                int ParallelGateway_Send_Order_Warehouse_2_TotIncoming = 2;
                
                ParallelGateway_Send_Order_Warehouse_2 = new ParallelConvergingGateway(new EBPMNName("ParallelGateway_Send_Order_Warehouse_2"),
                	ParallelGateway_Send_Order_Warehouse_2_TotIncoming,            		
                	EBPMNTime.zero());
                
                
                /**
                 *  EndEvent_Credit_Card
                 */
                EndEvent_Credit_Card = new End(new EBPMNName("EndEvent_Credit_Card"));
                
                
                /**
                 *  Order_Confirmed
                 */
                Order_Confirmed = new End(new EBPMNName("Order_Confirmed"));
                
                
                /**
                 *  ParallelGateway_Send_Order_Warehouse_1
                 */
                // number of outgoing sequence flows
                int ParallelGateway_Send_Order_Warehouse_1_TotOutgoing = 2;
                
                ParallelGateway_Send_Order_Warehouse_1 = new ParallelDivergingGateway(new EBPMNName("ParallelGateway_Send_Order_Warehouse_1"),
                	ParallelGateway_Send_Order_Warehouse_1_TotOutgoing,
                	EBPMNTime.zero());
                
                

                // Sequence flows
                SequenceFlow SequenceFlow_9 = new SequenceFlow(
                	ExclusiveGateway_Check_Stock_2.getOutPort(),
                	Send_Order_to_Warehouse.getInPort());
                
                SequenceFlow SequenceFlow_4 = new SequenceFlow(
                	ExclusiveGateway_Credit_Card_1.getOutPort(0),
                	Check_Stock.getInPort());
                
                SequenceFlow SequenceFlow_13 = new SequenceFlow(
                	ExclusiveGateway_Check_Stock_1.getOutPort(1),
                	Update_Delivery_Time.getInPort());
                
                SequenceFlow SequenceFlow_16 = new SequenceFlow(
                	ExclusiveGateway_Credit_Card_1.getOutPort(1),
                	EndEvent_Credit_Card.getInPort());
                
                SequenceFlow SequenceFlow_28 = new SequenceFlow(
                	Update_Delivery_Time.getOutPort(),
                	ExclusiveGateway_Check_Stock_2.getInPort(1));
                
                SequenceFlow SequenceFlow_1 = new SequenceFlow(
                	StartEvent_1.getOutPort(),
                	Checkout_Order.getInPort());
                
                SequenceFlow SequenceFlow_3 = new SequenceFlow(
                	Check_Credit_Card.getOutPort(),
                	ExclusiveGateway_Credit_Card_1.getInPort());
                
                SequenceFlow SequenceFlow_24 = new SequenceFlow(
                	ParallelGateway_Send_Order_Warehouse_2.getOutPort(),
                	Send_Confirmation.getInPort());
                
                SequenceFlow SequenceFlow_12 = new SequenceFlow(
                	ParallelGateway_Send_Order_Warehouse_1.getOutPort(1),
                	Prepare_Invoice.getInPort());
                
                SequenceFlow SequenceFlow_23 = new SequenceFlow(
                	Request_Shipping_Order.getOutPort(),
                	ParallelGateway_Send_Order_Warehouse_2.getInPort(1));
                
                SequenceFlow SequenceFlow_10 = new SequenceFlow(
                	Send_Order_to_Warehouse.getOutPort(),
                	ParallelGateway_Send_Order_Warehouse_1.getInPort());
                
                SequenceFlow SequenceFlow_25 = new SequenceFlow(
                	Send_Confirmation.getOutPort(),
                	Order_Confirmed.getInPort());
                
                SequenceFlow SequenceFlow_11 = new SequenceFlow(
                	ParallelGateway_Send_Order_Warehouse_1.getOutPort(0),
                	Request_Shipping_Order.getInPort());
                
                SequenceFlow SequenceFlow_27 = new SequenceFlow(
                	Prepare_Invoice.getOutPort(),
                	ParallelGateway_Send_Order_Warehouse_2.getInPort(0));
                
                SequenceFlow SequenceFlow_5 = new SequenceFlow(
                	Check_Stock.getOutPort(),
                	ExclusiveGateway_Check_Stock_1.getInPort());
                
                SequenceFlow SequenceFlow_6 = new SequenceFlow(
                	ExclusiveGateway_Check_Stock_1.getOutPort(0),
                	ExclusiveGateway_Check_Stock_2.getInPort(0));
                
                SequenceFlow SequenceFlow_2 = new SequenceFlow(
                	Checkout_Order.getOutPort(),
                	Check_Credit_Card.getInPort());

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