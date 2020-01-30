package com.docusign.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
@Scope("session")
public class GlobalControllerAdvice {
}