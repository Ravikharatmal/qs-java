package com.docusign.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopeTemplateResults;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.TemplateRole;
import com.docusign.esign.model.Text;

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
public class GetSignedDocumentController {

	@RequestMapping(path = "/getdoc", method = RequestMethod.POST)
	public Object create(ModelMap model) throws ApiException, IOException {
		model.addAttribute("title", "Embedded Signing Ceremony");

		// Data for this example
		// Fill in these constants
		//
		// Obtain an OAuth access token from
		// https://developers.docusign.com/oauth-token-generator
		/*
		 * Ravi: Generated at https://developers.docusign.com/oauth-token-generator
		 */
		String accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6IjY4MTg1ZmYxLTRlNTEtNGNlOS1hZjFjLTY4OTgxMjIwMzMxNyJ9.eyJUb2tlblR5cGUiOjUsIklzc3VlSW5zdGFudCI6MTU4NTUzOTAyOCwiZXhwIjoxNTg1NTY3ODI4LCJVc2VySWQiOiJmY2Q0MjU1NC1hZmNmLTRlY2QtOGM2My01NDkzMWY4OTRiODkiLCJzaXRlaWQiOjEsInNjcCI6WyJzaWduYXR1cmUiLCJjbGljay5tYW5hZ2UiLCJvcmdhbml6YXRpb25fcmVhZCIsInJvb21fZm9ybXMiLCJncm91cF9yZWFkIiwicGVybWlzc2lvbl9yZWFkIiwidXNlcl9yZWFkIiwidXNlcl93cml0ZSIsImFjY291bnRfcmVhZCIsImRvbWFpbl9yZWFkIiwiaWRlbnRpdHlfcHJvdmlkZXJfcmVhZCIsImR0ci5yb29tcy5yZWFkIiwiZHRyLnJvb21zLndyaXRlIiwiZHRyLmRvY3VtZW50cy5yZWFkIiwiZHRyLmRvY3VtZW50cy53cml0ZSIsImR0ci5wcm9maWxlLnJlYWQiLCJkdHIucHJvZmlsZS53cml0ZSIsImR0ci5jb21wYW55LnJlYWQiLCJkdHIuY29tcGFueS53cml0ZSJdLCJhdWQiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJhenAiOiJmMGYyN2YwZS04NTdkLTRhNzEtYTRkYS0zMmNlY2FlM2E5NzgiLCJpc3MiOiJodHRwczovL2FjY291bnQtZC5kb2N1c2lnbi5jb20vIiwic3ViIjoiZmNkNDI1NTQtYWZjZi00ZWNkLThjNjMtNTQ5MzFmODk0Yjg5IiwiYXV0aF90aW1lIjoxNTg1NTM4NzM4LCJwd2lkIjoiMjlhOWIzNmMtYzY3OS00NjMzLThkZWYtYjFhM2U2ZWEyYzk3In0.U9YayuB9gFLavJsBh7fohp2dShu6CAPpCgCatkrR3TpBxEpSBJmVJMupMyMQnKqdJWfplJq5dQ2qOLuZRzv0nudOQ-Fh9Z5qgReH7L3LVOQXQkUvGzxE9tl-OemBifb1ZEa2GzAvWrAvU0Q1vYdCTtCGz_nyJwp70yL174xFOx3gvv5hB_J59p31znKmRxxuuYTKZ94ZJ4x4gziG6-IBDW3IWSguJhMw30uUg-1E_-RV6MLj-cBtP0nRaQxADyHSX0qpFpPLini9D6R7pymykijIUliy4QfwnN0ByKc340PLBiJbn1tpaQ64wIBlzuXRbSzmi9UteOp5lKyCNFVGiA";
		Long tokenExpirationSeconds = 8 * 60 * 60L;
		// Obtain your accountId from demo.docusign.com -- the account id is shown in
		// the drop down on the
		// upper right corner of the screen by your picture or the default picture.
		/*
		 * Ravi: This account id is taken by logging into docusign sandbox & going to
		 * admin screen. https://appdemo.docusign.com/home
		 */
		String accountId = "10241252";

		//
		// The API base path
		String basePath = "https://demo.docusign.net/restapi";

		// Step 1. Call the API
		ApiClient apiClient = new ApiClient(basePath);
		apiClient.setAccessToken(accessToken, tokenExpirationSeconds);

		// Step 2. Call DocuSign to create and send the envelope
		// ApiClient apiClient = new ApiClient(basePath);
		// apiClient.setAccessToken(accessToken, tokenExpirationSeconds);
		EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

		/*
		 * After signing is done. run get envelop list API & get envelop id & document
		 * id from that.
		 */
		byte[] bytes = envelopesApi.getDocument(accountId, "e268d083-245b-4c45-beff-d90598ec989d", "1");
		String filePath = "C:\\MSTD\\Codebases\\GIT_CODE\\docusign-poc-qa-java\\Downloaded-signed-document.pdf";
		File newFile = new File(filePath);
		FileUtils.writeByteArrayToFile(newFile, bytes);

		// Show results
		String title = "getdoc";
		model.addAttribute("title", title);
		model.addAttribute("h1", title);
		model.addAttribute("message", "Envelopes::getdoc results - Downloaded. Check " + filePath);
		model.addAttribute("json", new JSONObject().toString(4));
		return "pages/example_done";
	}

	// Handle get request to show the form
	@RequestMapping(path = "/getdoc", method = RequestMethod.GET)
	public String get(ModelMap model) {
		model.addAttribute("title", "getdoc");
		return "pages/getdoc";
	}
}
