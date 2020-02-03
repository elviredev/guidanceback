package intra.poleemploi.web;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import intra.poleemploi.entities.FeedbackEmail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.ValidationException;


@RestController
@RequestMapping("/contact")
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = "*")
public class FeedbackController {
    @PostMapping
    public void sendFeedBackEmail(@RequestBody FeedbackEmail feedbackEmail, BindingResult bindingResult) throws MailjetSocketTimeoutException, MailjetException {
        if(bindingResult.hasErrors()){
            throw new ValidationException("FeedbackEmail is not valid");
        }

        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient("83185c061a35ce637884721bf4525538", "70ee763f47fe5905c14b84948dc6e3b1", new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", feedbackEmail.getEmail())
                                        .put("Name", feedbackEmail.getFirstName() + " " + feedbackEmail.getLastName()))
                                        .put("EmailUser", feedbackEmail.getEmailUser())
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", "sandrine.rodriguez@hotmail.com")
                                                .put("Name", "ADMIN")))
                                .put(Emailv31.Message.SUBJECT, "Inscription Guidance-Stats")
                                .put(Emailv31.Message.HTMLPART, "<h3>Une nouvelle demande d'inscription sur le site Guidance-Stats</h3><br />"
                                        + "<p>Identifiant : " + feedbackEmail.getUsername() + "</p>"
                                        + "<p>Prénom : " + feedbackEmail.getFirstName() + "</p>"
                                        + "<p>Nom : " + feedbackEmail.getLastName() + "</p>"
                                        + "<p>Email : " + feedbackEmail.getEmailUser() + "</p>"
                                        + "<p>Message : " + feedbackEmail.getTextMessage() + "</p>")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());

    }
}






