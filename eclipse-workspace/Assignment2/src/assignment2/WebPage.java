/*
 * File name: WebPage.java
 * Author: Rifat Shams, 040898113
 * Course: CST8284 - OOP
 * Assignment: 2
 * Date: 12 January, 2018
 * Professor: Dave Houtman
 * Purpose: Contains code to create and manage webview for the web browser
 */

package assignment2;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

//This code provided by Dave Houtman (2017) personal communication
/**
 * Class to create and manage webview for the web page
 * @author Rifat Shams
 * @version 1.0.0 
 * @see java.io.StringWriter
 * @see javax.xml.transform.Transformer
 * @see javax.xml.transform.TransformerFactory
 * @see javax.xml.transform.dom.DOMSource
 * @see javax.xml.transform.stream.StreamResult
 * @see javafx.beans.value.ChangeListener
 * @see javafx.beans.value.ObservableValue
 * @see javafx.concurrent.Worker
 * @see javafx.concurrent.Worker.State
 * @see javafx.scene.control.TextArea
 * @see javafx.scene.control.TextField
 * @see javafx.scene.web.WebEngine
 * @see javafx.scene.web.WebView
 * @see javafx.stage.Stage
 * @since 1.0.0
 */
public class WebPage {
	
	/**
	 * The webview object to display web pages to the user
	 */
	private WebView webview = new WebView();
	
	/**
	 * The webengine object to load the URLs in the webview
	 */
	private WebEngine engine;
	
	/**
	 * Method to create the webengine for the web browser
	 * <p>
	 * The method also updates the title and address bar when a new web page is loaded. It also fills in the code panel
	 * when a new web page is loaded.
	 * @param stage The main stage of the web browser
	 * @param addressBar The address bar of the browser
	 * @param codeArea The text area to hold the source code of a web page
	 * @return Returns a web engine for the web browser
	 */
	public WebEngine createWebEngine(Stage stage, TextField addressBar, TextArea codeArea) {
		
		WebView wv = getWebView();
		engine = wv.getEngine();
		
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == Worker.State.RUNNING) {
					stage.setTitle(engine.getLocation());
				}
				else if(newState == Worker.State.SUCCEEDED) {
					addressBar.setText(engine.getLocation());
					
					//java2s.com. JavaFX Tutorial - JavaFX WebEngine [Blog post]. Retrieved from
					//Source: http://www.java2s.com/Tutorials/Java/JavaFX/1500__JavaFX_WebEngine.htm
					 try {
		                  TransformerFactory transformerFactory = TransformerFactory.newInstance();
		                  Transformer transformer = transformerFactory.newTransformer();
		                  StringWriter stringWriter = new StringWriter();
		                  transformer.transform(new DOMSource(engine.getDocument()), new StreamResult(stringWriter));
		                  String xml = stringWriter.getBuffer().toString();
		                  codeArea.setText(xml);
					 } catch (Exception e) {
		                  
					 }
				}
			}
		});
		return engine;
	}
	
	/**
	 * Method that returns the current webview
	 * @return Returns the current webview object
	 */
	public WebView getWebView() {
		return webview;
	}
	
	/**
	 * Method that returns the current webengine
	 * @return Returns webengine object
	 */
	public WebEngine getWebEngine() {
		return engine;
	}
}
