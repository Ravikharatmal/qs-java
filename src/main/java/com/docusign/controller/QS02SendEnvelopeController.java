package com.docusign.controller;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.sun.jersey.core.util.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// Java Quickstart example: Send a signing request by email
//
// Copyright (c) 2018 by DocuSign, Inc.
// License: The MIT License -- https://opensource.org/licenses/MIT
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////


@Controller
public class QS02SendEnvelopeController {

    @RequestMapping(path = "/qs02", method = RequestMethod.POST)
    public Object create(ModelMap model) throws ApiException, IOException {
        // Data for this example
        // Fill in these constants
        //
        // Obtain an OAuth access token from https://developers.docusign.com/oauth-token-generator
        String accessToken = "eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAUABwAAN_AFC43XSAgAAHcTFE6N10gCABav7z-7ZfZDlEcmEgktmBMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzQyZjFiZjgtNTIxNC00OGE0LWE2NjMtMTFiMGQyZTdiOWZiIgAkAAAAMzQyZjFiZjgtNTIxNC00OGE0LWE2NjMtMTFiMGQyZTdiOWZiMAAAN_AFC43XSBIAAQAAAAsAAABpbnRlcmFjdGl2ZQ.f82css31oCZt_Cg-1Appz1DT2nnPwY0vh4Rw6p_uJU8oRG_BFZSDZzlZVyKFy-LVARmmgjXCY-nRh-0GPZnIQWqEm6HKldVdqJ-SZtJ7kCbyul27EsH9YaYidXtIGyJPQRp8cWWU4O2kql0NpQNJvEhSDjeNiaf52LuTd_aBvUyvYjOuqDmjmpmchmME6Tf7wbHlccvhZVPnCP5wQYVcl6pxyxYHna61NitsQxK2Sjfamv2IX2wz8k7gqlZrrJjqAQkctdtIBVb3rY0bgNuwMEUtQjBZfgZ0I6efGZ_kAmhVJdPuH0qLLIloaTs8HFEYozxNY-wLNv55bpXqzgQ6vw";
        Long tokenExpirationSeconds = 8 * 60 * 60L;
        // Obtain your accountId from demo.docusign.com -- the account id is shown in the drop down on the
        // upper right corner of the screen by your picture or the default picture.
        String accountId = "9687112";
        // Recipient Information
        String signerName = "sergii.katruk";
        String signerEmail = "sergii.katruk@sigma.software";

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
        Signer signer = new Signer();
        signer.setEmail(signerEmail);
        signer.setName(signerName);
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
        apiClient.setAccessToken(accessToken, tokenExpirationSeconds);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopeSummary results = envelopesApi.createEnvelope(accountId, envelopeDefinition);

        // Show results
        String title = "Signing Request by Email";
        model.addAttribute("title", title);
        model.addAttribute("h1", title);
        model.addAttribute("message", "Envelopes::create results");
        model.addAttribute("json", new JSONObject(results).toString(4));
        return "pages/example_done";
    }


    // Read a file
    private byte[] readFile(String path) throws IOException {
        InputStream is = QS02SendEnvelopeController.class.getResourceAsStream("/" + path);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }


    // Handle get request to show the form
    @RequestMapping(path = "/qs02", method = RequestMethod.GET)
    public String get(ModelMap model) {
        model.addAttribute("title","Signing Request by Email");
        return "pages/qs02";
    }
}
