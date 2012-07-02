package com.test.testproj;

import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

public class MerProjList {

	static WebDriver driver;
	static Wait<WebDriver> wait;

	public static void main(String[] args) throws Exception {

		// driver = new HtmlUnitDriver();
		// driver.get("http://lmrmerl1/");
		// listProjects();
		// driver.close();

		 

		List<String> projectList = readProjectFile();
		
		for(String currentProject : projectList)
		{
			list(currentProject);
		}
			
		
		
	}

	private static List<String> readProjectFile() {

		List<String> projectList = new ArrayList<String>();

		try {
			// Open the file that is the first
			// command line parameter
//			FileInputStream fstream = new FileInputStream("//projects.properties");
//			// Get the object of DataInputStream
//			DataInputStream in = new DataInputStream(fstream);
//			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			InputStream in = new MerProjList().getClass().getResourceAsStream("/projects.properties");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				projectList.add(strLine);
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		return projectList;
		
	}

	private static void list(String currentProject) throws FileNotFoundException, IOException,
			XmlPullParserException, URISyntaxException {

		String baseurl = "http://lmrmerl1/";
		String projectURL = baseurl + "which-switch-ui" + "/file/tip/pom.xml";

		String fileName = String.format("http://lmrmerl1/%s/raw-file/tip/pom.xml",currentProject);

		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = reader.read(new URL(fileName).openStream());

		List<Dependency> dependencies = model.getDependencies();
		Collection<Dependency> filtered = filter(
				having(on(Dependency.class).getGroupId(), equalTo("com.which")),
				dependencies);


		System.out.println("=========== " + model.getArtifactId() + "==================");
		for (Dependency aDependency : filtered) {
			System.out.println("\t\t"+ aDependency.getArtifactId() + ":"
					+ aDependency.getVersion());
		}
		
		System.out.println("=============================");

	}

	private static void listProjects() {
		List<WebElement> rows = driver.findElements(By
				.xpath("//tr/td/a[@class='list']/b"));

		for (WebElement row : rows) {

			System.out.println(row.getText());

		}
	}

}
