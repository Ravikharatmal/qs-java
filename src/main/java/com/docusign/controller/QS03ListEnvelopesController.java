package com.docusign.controller;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.*;
import com.docusign.esign.api.EnvelopesApi.ListStatusChangesOptions;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.joda.time.LocalDate;
import java.io.IOException;


///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////
//
// Java Quickstart example: List envelopes whose status has changed
//
// Copyright (c) 2018 by DocuSign, Inc.
// License: The MIT License -- https://opensource.org/licenses/MIT
//
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

@Controller
public class QS03ListEnvelopesController {

    @RequestMapping(path = "/qs03", method = RequestMethod.POST)
    public Object create(ModelMap model) throws ApiException, IOException {
        model.addAttribute("title","Embedded Signing Ceremony");

        // Data for this example
        // Fill in these constants
        //
        // Obtain an OAuth access token from https://developers.docusign.com/oauth-token-generator
        String accessToken = "eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQoAAAABAAUABwAAoKzvN43XSAgAAODP_XqN10gCABav7z-7ZfZDlEcmEgktmBMVAAEAAAAYAAEAAAAFAAAADQAkAAAAMzQyZjFiZjgtNTIxNC00OGE0LWE2NjMtMTFiMGQyZTdiOWZiIgAkAAAAMzQyZjFiZjgtNTIxNC00OGE0LWE2NjMtMTFiMGQyZTdiOWZiMAAAoKzvN43XSBIAAQAAAAsAAABpbnRlcmFjdGl2ZQ.pRc5_7uhrWgfB3oLtQLGDVi0cqE-FtQmtv7k1V-MRbIYw2Ccws_EBEW3SjdpgoLZXowTpIZfqCY_Pv4wisa4-PoGytd2qiRy90Xl_9JZcexqrdfpDdrDpKnE8ugEFfn_Jnqaw_80_P7MYEQfCvna_hkAByPNyP4iZnw5wC7tMhDIKYPFWOn6bumVqVDLup55j0zr9HwcrdirudDg8akR8JzmDWP_vN6Rwe_mw6h8H4nbCkIlteGYmVh1zT9i8NS7yUUQT1iTU3OtLs5vs2HErrlTCJsx8lSOFaswZsZ9eJ6FyWInPsTinchMs6wH-PTu7iOSRXS7jam_9_zB4h45VQ";
        Long tokenExpirationSeconds = 8 * 60 * 60L;
        // Obtain your accountId from demo.docusign.com -- the account id is shown in the drop down on the
        // upper right corner of the screen by your picture or the default picture.
        String accountId = "9687112";

        //
        // The API base path
        String basePath = "https://demo.docusign.net/restapi";

        // Step 1. Call the API
        ApiClient apiClient = new ApiClient(basePath);
        apiClient.setAccessToken(accessToken, tokenExpirationSeconds);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        // prepare the request body
        ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        LocalDate date = LocalDate.now().minusDays(10);
        options.setFromDate(date.toString("yyyy/MM/dd"));
        // call the API
        EnvelopesInformation results = envelopesApi.listStatusChanges(accountId, options);

        // Show results
        String title = "List Updated Envelopes";
        model.addAttribute("title", title);
        model.addAttribute("h1", title);
        model.addAttribute("message", "Envelopes::listStatusChanges results");
        model.addAttribute("json", new JSONObject(results).toString(4));
        return "pages/example_done";
    }


    // Handle get request to show the form
    @RequestMapping(path = "/qs03", method = RequestMethod.GET)
    public String get(ModelMap model) {
        model.addAttribute("title","List Updated Envelopes");
        return "pages/qs03";
    }
}
