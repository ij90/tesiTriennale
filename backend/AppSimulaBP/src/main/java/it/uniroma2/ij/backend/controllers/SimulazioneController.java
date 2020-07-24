package it.uniroma2.ij.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma2.ij.backend.Parametri;
import it.uniroma2.ij.models.EmergenzaAttendanceSim;
import it.uniroma2.ij.models.HardwareRetailer;
import it.uniroma2.ij.models.HealthSim;
import it.uniroma2.ij.models.OrderProcessing;
import it.uniroma2.sel.pybpmn.examples.ebpmn.Emergency_Attendance_Bizagi;
import it.uniroma2.sel.simlab.ebpmn.general.Evento;
import it.uniroma2.sel.simlab.ebpmn.general.Simulazione;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.Layer2InternalException;
import it.uniroma2.sel.simlab.simarch.exceptions.layer2.UnableToStartEngineException;

@RestController
public class SimulazioneController {
	
	Parametri parametri = new Parametri();
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/simulazione", method = { RequestMethod.GET })
	public String getMessaggio() {
		String messaggio = "Ciao dal backend";
		
		return messaggio;
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/simulazione/1", method = { RequestMethod.POST })
	public Simulazione startSimulazione(@RequestBody Parametri params) {
		parametri.setReplications(params.getReplications());
		parametri.setTotalTokens(params.getTotalTokens());
		parametri.setPerformers(params.getPerformers());
		EmergenzaAttendanceSim sim1 = new EmergenzaAttendanceSim();
		Simulazione simulazione = new Simulazione();
		
		if(parametri.getReplications() > 0) {
			try {
				sim1.setReplications(parametri.getReplications());
				sim1.setTotalTokens(parametri.getTotalTokens());
				sim1.setPerformers(parametri.getPerformers());
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		} else {
			try {
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		}
		
		simulazione = sim1.getSimulazione();
		
		return simulazione;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/simulazione/2", method = { RequestMethod.POST })
	public Simulazione startSimulazione2(@RequestBody Parametri params) {
		parametri.setReplications(params.getReplications());
		parametri.setTotalTokens(params.getTotalTokens());
		parametri.setPerformers(params.getPerformers());
		OrderProcessing sim1 = new OrderProcessing();
		Simulazione simulazione = new Simulazione();
		
		if(parametri.getReplications() > 0) {
			try {
				sim1.setReplications(parametri.getReplications());
				sim1.setTotalTokens(parametri.getTotalTokens());
				sim1.setPerformers(parametri.getPerformers());
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		} else {
			try {
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		}
		
		simulazione = sim1.getSimulazione();
		
		return simulazione;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/simulazione/3", method = { RequestMethod.POST })
	public Simulazione startSimulazione3(@RequestBody Parametri params) {
		parametri.setReplications(params.getReplications());
		parametri.setTotalTokens(params.getTotalTokens());
		parametri.setPerformers(params.getPerformers());
		HardwareRetailer sim1 = new HardwareRetailer();
		Simulazione simulazione = new Simulazione();
		
		if(parametri.getReplications() > 0) {
			try {
				sim1.setReplications(parametri.getReplications());
				sim1.setTotalTokens(parametri.getTotalTokens());
				sim1.setPerformers(parametri.getPerformers());
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		} else {
			try {
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		}
		
		simulazione = sim1.getSimulazione();
		
		return simulazione;
	}
	
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/simulazione/4", method = { RequestMethod.POST })
	public Simulazione startSimulazione4(@RequestBody Parametri params) {
		parametri.setReplications(params.getReplications());
		parametri.setTotalTokens(params.getTotalTokens());
		parametri.setPerformers(params.getPerformers());
		HealthSim sim1 = new HealthSim();
		Simulazione simulazione = new Simulazione();
		
		if(parametri.getReplications() > 0) {
			try {
				sim1.setReplications(parametri.getReplications());
				sim1.setTotalTokens(parametri.getTotalTokens());
				sim1.setPerformers(parametri.getPerformers());
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		} else {
			try {
				sim1.startSimulazione();
				} catch (UnableToStartEngineException e) {
		             e.printStackTrace();
		         } catch (Layer2InternalException e) {
		             e.printStackTrace();
		         }
		}
		
		simulazione = sim1.getSimulazione();
		
		return simulazione;
	}

}
