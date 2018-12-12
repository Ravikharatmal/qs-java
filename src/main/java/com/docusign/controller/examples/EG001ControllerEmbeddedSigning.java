package com.docusign.controller.examples;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.sun.jersey.core.util.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequestMapping("/eg001")
public class EG001ControllerEmbeddedSigning extends EGController {

    @Override
    protected void addSpecialAttributes(ModelMap model) {}

    @Override
    protected String getEgName() {
        return "eg001";
    }

    @Override
    protected String getTitle() {
        return "Embedded Signing Ceremony";
    }

    @Override
    protected String getResponseTitle() {
        return null;
    }

    @Override
    protected Object doWork(WorkArguments args, ModelMap model) throws ApiException, IOException {
        // Data for this example
        // Fill in these constants
        //
        // Obtain an OAuth access token from https://developers.hqtest.tst/oauth-token-generator
        String accessToken = "{ACCESS_TOKEN";
        // Obtain your accountId from demo.docusign.com -- the account id is shown in the drop down on the
        // upper right corner of the screen by your picture or the default picture.
        String accountId = "{ACCOUNT_ID}";
        // Recipient Information
        String signerName = "{USER_FULLNAME}";
        String signerEmail = "{USER_EMAIL}";


        /////////////////////
        accessToken = "eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQkAAAABAAUABwCA8wFiAmDWSAgAgDMlcEVg1kgCAFCYSRTxQ4RBlE9V5f7RiHAVAAEAAAAYAAEAAAAFAAAADQAkAAAAZjBmMjdmMGUtODU3ZC00YTcxLWE0ZGEtMzJjZWNhZTNhOTc4EgACAAAABwAAAG1hbmFnZWQLAAAAZHNfaW50ZXJuYWwwAADA_yYCYNZI.zKcewRlJJGkkimeaiPUMIhsFxzN3DSdJO2Lk1H1GS4JILknNj8vyBEadiM8qvhqV3buO3lZKjJN0pl9dIGtT5hP6wqtK2VBYgBXW1FGVfC4a21oPyABZu6h4UrggXpkuWE6Tv11tG7TiZbRb_CPyeQlG4d8AdXVKW6jcWF9gILqUzgNTivcI31_LKqIceKoB_IKdyhalPR_oDCvl4QOZSBrIHvsW5amgiuZNKwGLRQUa7XaJ9TrYclSBrspA-91wzFlGnOvjFu3fYDQk279ZaPyAVTSsCFUWH25dAmIydBlmKdTjJlgEn8Fk_U0DR7OHgjH8sg-qcMIo9jSQ9M_OKQ";
        accountId = "3964103";
        signerName = "Larry Kluger";
        signerEmail = "larry.kluger@docusign.com";
        /////////////////////
        /////////////////////
        /////////////////////



        // The url for this web application
        String baseUrl = "http://localhost:8080";
        String clientUserId = "123"; // Used to indicate that the signer will use an embedded
                                     // Signing Ceremony. Represents the signer's userId within
                                     // your application.
        String authenticationMethod = "None"; // How is this application authenticating
                                              // the signer? See the `authenticationMethod' definition
        //  https://developers.docusign.com/esign-rest-api/reference/Envelopes/EnvelopeViews/createRecipient
        //
        // The API base path
        String basePath = "https://demo.docusign.net/restapi";
        // The document to be signed. See /qs-java/src/main/resources/World_Wide_Corp_lorem.pdf
        String docPdf = "World_Wide_Corp_lorem.pdf";

        // Step 1. Create the envelope definition
        // One "sign here" tab will be added to the document.

        byte[] buffer = readFile(docPdf);
        String docBase64 = new String(Base64.encode(buffer));

        // Create the DocuSign document object
        Document document = new Document();
        document.setDocumentBase64(docBase64);
        document.setName("Example document"); // can be different from actual file name
        document.setFileExtension("pdf"); // many different document types are accepted
        document.setDocumentId("1"); // a label used to reference the doc

        // The signer object
        // Create a signer recipient to sign the document, identified by name and email
        // We set the clientUserId to enable embedded signing for the recipient
        // We're setting the parameters via the object creation
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
        signer.clientUserId(clientUserId);
        signer.recipientId("1");

        // Create a signHere tabs (also known as a field) on the document,
        // We're using x/y positioning. Anchor string positioning can also be used
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setPageNumber("1");
        signHere.setRecipientId("1");
        signHere.setTabLabel("SignHereTab");
        signHere.setXPosition("195");
        signHere.setYPosition("147");

        // Add the tabs to the signer object
        // The Tabs object wants arrays of the different field/tab types
        Tabs signerTabs = new Tabs();
        signerTabs.setSignHereTabs(Arrays.asList(signHere));
        signer.setTabs(signerTabs);

        // Next, create the top level envelope definition and populate it.
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
        envelopeDefinition.setEmailSubject("Please sign this document");
        envelopeDefinition.setDocuments(Arrays.asList(document));
        // Add the recipient to the envelope object
        Recipients recipients = new Recipients();
        recipients.setSigners(Arrays.asList(signer));
        envelopeDefinition.setRecipients(recipients);
        envelopeDefinition.setStatus("sent"); // requests that the envelope be created and sent.

        // Step 2. Call DocuSign to create and send the envelope
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.addDefaultHeader("Authorization", "Bearer " + accessToken);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        String envelopeId = results.getEnvelopeId();

        // Step 3. The envelope has been created.
        //         Request a Recipient View URL (the Signing Ceremony URL)
        RecipientViewRequest viewRequest = new RecipientViewRequest();
        // Set the url where you want the recipient to go once they are done signing
        // should typically be a callback route somewhere in your app.
        viewRequest.setReturnUrl(baseUrl + "/ds-return");
        viewRequest.setAuthenticationMethod(authenticationMethod);
        viewRequest.setEmail(signerEmail);
        viewRequest.setUserName(signerName);
        viewRequest.setClientUserId(clientUserId);
        // call the CreateRecipientView API
        ViewUrl results1 = envelopesApi.createRecipientView(accountId, envelopeId, viewRequest);

        // Step 4. The Recipient View URL (the Signing Ceremony URL) has been received.
        //         The user's browser will be redirected to it.
        String redirectUrl = results1.getUrl();
        args.setRedirectUrl("redirect:" + redirectUrl);
        return null;
    }
}