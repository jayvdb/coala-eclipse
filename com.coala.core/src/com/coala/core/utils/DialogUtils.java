package com.coala.core.utils;

import org.eclipse.jface.dialogs.MessageDialog;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class DialogUtils {
  /**
   * Dialog asking the user to install the coala framework.
   * If the user answers in affirmative the coala website is opened
   * in the default web browser.
   */
  public static void installcoalaDialog() {
    URI coalaWebsite = null;
    try {
      coalaWebsite = new URI("http://coala-analyzer.org");
    } catch (URISyntaxException ex) {
      ex.printStackTrace();
    }
    boolean ret = MessageDialog.openQuestion(null, "coala is not installed",
        "The coala framework is required to run code analysis. "
            + "Do you want to know how to install coala?");
    if (ret) {
      try {
        java.awt.Desktop.getDesktop().browse(coalaWebsite);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
