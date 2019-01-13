package Selenim_training;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

/**
 * 
 * @author Sunith
 *
 */
public class hostelworldsearchvalidation {

	WebDriver driver;

	/**
	 * This function has the steps to initialize the chromedriver and navigate to hostelworld.com
	 * @param screenResolution
	 */

	public void initiateChromeBrowser(Dimension screenResolution) {
		Dimension resolution=screenResolution;
		File f = new File("D:\\selenium\\chromedriver\\chromedriver_win32\\chromedriver.exe");
		System.setProperty("webdriver.chrome.driver", f.getAbsolutePath());
		// Open Google
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		
		if(resolution.height==0) {
			System.out.println("The screen resolution is Fullscreen");
			driver.manage().window().maximize();	
		}
		
		else {
			System.out.println("The screen resolution is"+screenResolution);
		driver.manage().window().setSize(screenResolution);
		}
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		driver.get("http://www.google.com");
		driver.navigate().to("https://www.hostelworld.com/");

	}

	/**
	 * This function helps to terminate the browser
	 */
	public void closeBrowser() {
		driver.quit();
	}

	/**
	 * This function contains the steps to verify whether the search results of
	 * Dublin, Ireland is sorted by Names.
	 * @param screenResolution
	 */

	public void verifyPropertiesSorted(Dimension screenResolution) {

		try {
			Dimension resolution=screenResolution;
			WebElement searchBox = driver.findElement(By.xpath("//input[@id='home-search-keywords']"));
			searchBox.sendKeys("Dublin");
			Actions action = new Actions(driver);
			action.moveToElement(searchBox).moveToElement(driver.findElement(By.linkText("Dublin, Ireland"))).click()
					.build().perform();
			WebElement letsGoButton = driver.findElement(By.xpath("//button[@title='SEARCH']"));
			letsGoButton.click();
			Thread.sleep(3000);
			Assert.assertEquals(driver.getTitle(), "Dublin Hostels » Great Selection of Cheap Hostels » Hostelworld");
			System.out.println(driver.getTitle());
			if(resolution.height==732) {
				WebElement sortIcon = driver.findElement(By.xpath("//span[ contains(text(),'SORT')]"));
				sortIcon.click();	
			}
			else {
			WebElement sortIcon = driver.findElement(By.xpath("//span[contains(text(),'Sort')]"));////*[@id="pagebody"]/div[1]/div/div[2]/div[17]/dl/dd[2]/a/span
			sortIcon.click();
			}
			
			WebElement sortByName = driver.findElement(By.xpath("//*[@id='sortByName']"));
			sortByName.click();
			
			if (verifyLinledlistIsSorted(captureList()) == true) {
				System.out.println("The given list is sorted");
			} else {
				System.out.println("The given list is not sorted");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This function stores the result of search web elements in
	 * hostelList_Webelement LinkedList to get the count of available search results
	 * in a page. The headings of the search results are then stored to hostelNames
	 * LinkedList.
	 * 
	 * @return hostelNames
	 * @throws InterruptedException
	 */
	public LinkedList<String> captureList() throws InterruptedException {
		List<WebElement> hostelList_Webelement = new LinkedList<WebElement>();
		LinkedList<String> hostelNames = new LinkedList<String>();
		WebElement paginationNext = driver.findElement(By.id("paginationNext"));
		JavascriptExecutor executor = (JavascriptExecutor) driver;

		boolean LastPage = false;
		do {

			Thread.sleep(10000);
			hostelList_Webelement = driver.findElements(By.xpath("//div[@id='fabResultsContainer']/div"));
			for (int i = 1; i <= hostelList_Webelement.size(); i++) {
				String s = driver.findElement(By.xpath("//div[@id='fabResultsContainer']/div[" + i + "]/div[2]/h2"))
						.getText();

				hostelNames.add(s);

			}
			if (driver.findElement(By.id("paginationNext")).isDisplayed()) {

				executor.executeScript("arguments[0].click();", paginationNext);
			} else {
				LastPage = true;
			}

		} while (LastPage == false);

		System.out.println("The list of hostels are\n" + hostelNames);

		return hostelNames;

	}

	/**
	 * This function checks whether the linked list is arranged in alphabetical
	 * order using compareTo() and return boolean value
	 * 
	 * @param hostelList
	 * @return boolean true or false
	 */
	public static boolean verifyLinledlistIsSorted(LinkedList<String> hostelList) {

		String previous = ""; // empty string

		for (final String current : hostelList) {
			if (current.compareTo(previous) < 0)
				return false;
			previous = current;
		}

		return true;

	}

	/**
	 * main function
	 * This function inputs different screen resolution to verify the scenario in all the required resolutions
	 * @param args
	 */
	public static void main(String[] args) {
		hostelworldsearchvalidation test = new hostelworldsearchvalidation();

		String[] resolutions = { "0x0", "412x732","768x1024"};
		//String[] resolutions = {"412x732"};

		for (String resolution : resolutions) {
			String[] parts = resolution.split("x");

			// Screen resolution
			Dimension screenRes = new Dimension(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
			test.initiateChromeBrowser(screenRes);
			test.verifyPropertiesSorted(screenRes);
			test.closeBrowser();

		}

	}
}
