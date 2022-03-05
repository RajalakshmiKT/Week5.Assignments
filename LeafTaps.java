package week5.assignments;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClass1 {
	public ChromeDriver driver;
	public String filename, sheetname;
	@Parameters({"url", "username" , "password"})
	@BeforeMethod
	public void preCondition(String url, String uNmae, String pwd)
	{


		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.findElement(By.id("username")).sendKeys(uNmae);
		driver.findElement(By.id("password")).sendKeys(pwd);
		driver.findElement(By.className("decorativeSubmit")).click();
		driver.findElement(By.linkText("CRM/SFA")).click();

	}
	@AfterMethod
	public void postCondition()
	{
		driver.close();
	}
}
//-----------------------------------------------------------------------------------------------------------

//CreateLead.java

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test; //Runs as TestNG by importing this

import testng.ReadExcel;




public class CreateLead extends BaseClass1 {
	@BeforeClass 
	public void selData() {
		filename = "CreateLead";
		sheetname = "CreateLead";
	}

	@Test(priority = 1, dataProvider = "createLead" ) 

	public void createLead(String cname, String fname, String lname)
	{

		driver.findElement(By.linkText("Leads")).click();
		driver.findElement(By.linkText("Create Lead")).click();
		driver.findElement(By.id("createLeadForm_companyName")).sendKeys(cname);
		driver.findElement(By.id("createLeadForm_firstName")).sendKeys(fname);
		driver.findElement(By.id("createLeadForm_lastName")).sendKeys(lname);
		driver.findElement(By.name("submitButton")).click();

	}
	@DataProvider(name = "createLead")
	public String[][] sendData() throws IOException
	{
		return ReadClass.readdata(filename,sheetname);


	}

}

//----------------------------------------------------------------------------------------------------------

//EditLead.java

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class EditLead extends BaseClass1 {
	@BeforeClass 
	public void selData() {
		filename = "CreateLead";
		sheetname = "CreateLead";
	}

	@Test(priority = 2, dataProvider = "editLead" )
	public  void editLead() throws InterruptedException 
	{
		driver.findElement(By.linkText("Leads")).click();
		driver.findElement(By.linkText("Find Leads")).click();
		driver.findElement(By.xpath("(//input[@name='firstName'])[3]")).sendKeys("Rishikesh");
		driver.findElement(By.xpath("(//a[@class='linktext'])[4]")).click();
		String title = driver.getTitle();
		System.out.println(title);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[text()='Edit']")).click();

		if(title.equals("Duplicate Lead | opentaps CRM"))
		{
			System.out.println("Verified");
		}

		driver.findElement(By.id("updateLeadForm_companyName")).clear();
		Thread.sleep(2000);

		driver.findElement(By.xpath("(//input[@id='updateLeadForm_companyName'])")).sendKeys("IBM");
		driver.findElement(By.xpath("(//input[@class='smallSubmit'])[1]")).click();
		String text = driver.findElement(By.id("viewLead_companyName_sp")).getText();

		if(text.contains("IBM"))
		{
			System.out.println("Company name changed");
		}
		Thread.sleep(2000);

	}
	@DataProvider(name = "editLead")
	public String[][] sendData() throws IOException
	{
		return ReadClass.readdata(filename,sheetname);


	}




}

//------------------------------------------------------------------------------------------------------------------------

//DuplicateLead.java

import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class DuplicateLead extends BaseClass1{
	@Test(priority = 3)
	public  void duplicateLead() throws InterruptedException {

		driver.findElement(By.linkText("Leads")).click();
		driver.findElement(By.linkText("Find Leads")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//span[text()='Email']")).click();
		driver.findElement(By.name("emailAddress")).sendKeys("divya123@abc.com");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[text()='Find Leads']")).click();
		Thread.sleep(2000);
		String text = driver.findElement(By.xpath("//a[text()='Divya']")).getText();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//a[@class='linktext'])[4]")).click();
		driver.findElement(By.linkText("Duplicate Lead")).click();
		String title = driver.getTitle();
		System.out.println(title);
		if (title.contains("Duplicate Lead"))
		{
			System.out.println("Title is Duplicate Lead");
		}
		driver.findElement(By.xpath("//input[@name='submitButton']")).click();
		String dup = driver.findElement(By.xpath("//span[@id='viewLead_firstName_sp']")).getText();

		if (text.equals(dup)) 
		{

			System.out.println("Both names are same ");

		}
		Thread.sleep(2000);


	}

}

//-----------------------------------------------------------------------------------------------------------------

//DeleteLead.java

import org.openqa.selenium.By;
import org.testng.annotations.Test;


public class DeleteLead extends BaseClass1{

	@Test (priority = 4 , enabled = false)
	public void deleteLead() throws InterruptedException
	{

		driver.findElement(By.linkText("Leads")).click();
		driver.findElement(By.linkText("Find Leads")).click();
		driver.findElement(By.xpath("//span[text()='Phone']")).click();
		driver.findElement(By.xpath("//input[@name='phoneNumber']")).sendKeys("99");
		driver.findElement(By.xpath("//button[text()='Find Leads']/../..")).click();
		Thread.sleep(2000);
		String name = driver.findElement(By.xpath("(//div[@class='x-grid3-cell-inner x-grid3-col-firstName'])/a[1]")).getText();
		System.out.println(name);
		driver.findElement(By.xpath("(//div[@class='x-grid3-cell-inner x-grid3-col-firstName'])/a[1]")).click();
		String title=driver.getTitle();
		System.out.println(title);
		driver.findElement(By.xpath("//a[text()='Delete']")).click();
		driver.findElement(By.linkText("Find Leads")).click();
		driver.findElement(By.xpath("//span[text()='Phone']")).click();
		driver.findElement(By.xpath("//input[@name='phoneNumber']")).sendKeys("99");
		driver.findElement(By.xpath("//button[text()='Find Leads']/../..")).click();
		System.out.println(driver.getTitle());
		Thread.sleep(2000);






	}
}

//----------------------------------------------------------------------------------------------------------------------------------------

//leaftaps_seq_testng.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite">
<parameter name="url"
value="http://leaftaps.com/opentaps/control/main"></parameter>
<parameter name="username" value="DemoSalesManager"></parameter>
<parameter name="password" value="crmsfa"></parameter>

<test thread-count="5" name="Test">
<classes>
<class name="week5.day1assignments.CreateLead" />
<class name="week5.day1assignments.EditLead" />
<class name="week5.day1assignments.DuplicateLead" />
<class name="week5.day1assignments.DeleteLead" />
</classes>
</test> <!-- Test -->
</suite> <!-- Suite -->

//-----------------------------------------------------------------------------------------------------------------------------------------

//leaftaps_par_testng.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite" parallel="classes">
<parameter name="url" value ="http://leaftaps.com/opentaps/control/main"></parameter>
<parameter name="username" value ="DemoSalesManager"></parameter>
<parameter name="password" value ="crmsfa"></parameter>

<test thread-count="5" name="Test" parallel="classes">
<classes>
<class name="week5.day1assignments.CreateLead"/>
<class name="week5.day1assignments.DuplicateLead"/>
<class name="week5.day1assignments.EditLead"/>
<class name="week5.day1assignments.DeleteLead"/>
</classes>
</test> <!-- Test -->
</suite> <!-- Suite -->
//-----------------------------------------------------------------------------------------------------------------------------------------