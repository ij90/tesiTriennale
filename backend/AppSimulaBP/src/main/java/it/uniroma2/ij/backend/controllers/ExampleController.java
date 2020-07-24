package it.uniroma2.ij.backend.controllers;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import it.uniroma2.ij.models.Esempio;

@RestController
public class ExampleController {
	
	Esempio e1 = new Esempio("Emergency_Attendance", "Il processo inizia con la ricezione di un report di emergenza da parte del Call Center Agent, il quale raccoglie tutte le informazioni sulla persona interessata dall'emergenza tra le quali i sintomi e l'indirizzo del paziente. Il Call Center Agent trasmette il report ad una Nurse la quale analizza il report e classifica l'emergenza in base alla sua gravità:Green Code - rischio basso, il paziente può essere curato facilmente; Yellow Code - rischio medio, il paziente richiede alcuni trattamenti speciali però può; essere curato sul luogo dell'emergenza; Red Code - rischio elevato, il paziente deve essere portato ad un ospedale. La tipologia di risposta all'emergenza (realizzata attraverso l'elemento di Exclusive Gateway) dipende dalla gravità della classificazione. In caso di Green Code, il triage oppure lo screening viene assistito da un Quick Attention Vehicle, in caso di Green Code, il triage viene assistito da una Basic Ambulance mentre in caso di Red Code, il triage viene assistito da una Full Ambulance. Per quanto riguarda le situazioni di Green Code e Yellow Code, il processo è completato con l'arrivo sul luogo dell'emergenza. Per i Red Code, la Full Ambulance preleva il paziente e durante il trasferimento all'ospedale, una Nurse prepara la pratica che sarà utilizzata dalla Hospital Receptionist per autorizzare l'ingresso del paziente.", "../../../assets/Emergency_Attendance_Bizagi.png", 1);
	Esempio e2 = new Esempio("Order_Processing", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", "../../../assets/Order_Processing.png", 2);
	Esempio e3 = new Esempio("Hardware_Retailer", "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus.", "../../../assets/.png", 3);
	Esempio e4 = new Esempio("Health", "Aliquam eu quam eu urna mollis auctor. Aenean vitae pretium erat. Suspendisse tempor, ante vel dignissim blandit, orci sem vulputate ligula, hendrerit porttitor tellus purus et urna. Aliquam nec commodo turpis. Mauris id elementum lorem. Etiam faucibus magna justo. Proin turpis erat, tristique in facilisis id, tincidunt feugiat urna.", "../../../assets/Health.png", 4);
	Esempio e5 = new Esempio("Purchase_Service", "Fusce fringilla lobortis porta. Maecenas nec sollicitudin neque. Nulla pharetra risus augue, eu lacinia lorem imperdiet id. Nunc euismod elit id eros tempus volutpat. Etiam tellus ante, varius eu nunc ut, accumsan pharetra turpis. Phasellus vulputate turpis id sollicitudin porttitor. Ut ullamcorper purus sed elit iaculis mattis vitae dignissim tortor.", "../../../assets/Purchase_Service.png", 5);
	
	ArrayList <Esempio> listaEsempi = new ArrayList<Esempio>(
			Arrays.asList(e1, e2, e3, e4, e5));
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/esempi", method = { RequestMethod.GET })
	public ArrayList <Esempio> getExamples() {
	
		return listaEsempi;
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping(value = "/esempi", method = { RequestMethod.POST })
	public String getExampleName(@RequestBody int id) {
		String name = "";
		for(int i = 0; i < listaEsempi.size(); i++) {
			if(listaEsempi.get(i).getId() == id)
				name = listaEsempi.get(i).getNome();
		}
		return name;
	}
}
