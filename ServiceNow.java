package week5.assignments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseClaseServiceNow {
	public ChromeDriver driver;

	@Parameters({"url", "username" , "password"})
	@BeforeMethod
	public void preCondition(String url, String uNmae, String pwd) throws InterruptedException
	{
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.get(url);
		Thread.sleep(10000);
		driver.switchTo().frame("gsft_main");		
		driver.findElement(By.xpath("//input[@id='user_name']")).sendKeys(uNmae);
		driver.findElement(By.xpath("//input[@id='user_password']")).sendKeys(pwd);
		driver.findElement(By.xpath("//button[@id='sysverb_login']")).click();
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys("incident");
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys(Keys.ENTER);
	}

}
//----------------------------------------------------------------------------------------------------------------------------

//ReadExcel.java

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadClass {
	public static String[][] readdata(String fileName , String sheetName) throws IOException // static to directly implement classname.methodname
	{
		
	
		XSSFWorkbook book = new XSSFWorkbook("./data/"+fileName+".xlsx");
		XSSFSheet  ws = book.getSheet(sheetName);
		int rows = ws.getLastRowNum();
		//System.out.println(rows);
		int allRows = ws.getPhysicalNumberOfRows();
		//System.out.println(allRows);
		short cells = ws.getRow(0).getLastCellNum();
		//System.out.println(cells);
		
		String [][] data = new String[rows][cells];
		
		for (int i = 1; i <= rows; i++) {
			XSSFRow row = ws.getRow(i);
			
			for (int j = 0; j < cells; j++) {
				XSSFCell cell = row.getCell(j);
				
				String cellvalue = cell.getStringCellValue();
				System.out.println(cellvalue);
				data[i-1][j]= cellvalue; 
			}
			
		}
		book.close();
	return data;
	}	

}
//----------------------------------------------------------------------------------------------------------------------------

//CreateIncident.java

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class CreateIncident extends BaseClaseServiceNow {

	@Test(priority = 1)
	public void createIncident() throws InterruptedException 
	{
		driver.findElement(By.xpath("//input[@id='filter']")).clear();
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys("incident");
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("(//div[text()='All'])[2]")).click();

		driver.switchTo().frame("gsft_main");
		driver.findElement(By.xpath("//button[@id='sysverb_new']")).click();
		Thread.sleep(2000);

		driver.findElement(By.xpath("//input[@id='incident.short_description']")).sendKeys("Test Admin Incident");
		String incNo= driver.findElement(By.xpath("//input[@id='incident.number']")).getAttribute("value");
		System.out.println(incNo);

		driver.findElement(By.xpath("//button[@id='lookup.incident.caller_id']")).click();

		Set<String> windowHandles = driver.getWindowHandles();
		System.out.println(windowHandles);
		List<String>l=new ArrayList<String>(windowHandles);

		System.out.println( driver.getCurrentUrl());
		driver.switchTo().window(l.get(1));    
		System.out.println(l.get(1));
		driver.findElement(By.linkText("Abraham Lincoln")).click();
		Thread.sleep(2000);
		driver.switchTo().window(l.get(0)) ;
		System.out.println( driver.getCurrentUrl());
		System.out.println(l.get(0));

		driver.switchTo().frame("gsft_main");

		driver.findElement(By.xpath("//button[@id='sysverb_insert']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//input[@class='form-control'])[1]")).sendKeys(incNo);
		driver.findElement(By.xpath("(//input[@class='form-control'])[1]")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);

		String SearchIncNo = driver.findElement(By.xpath("//a[@class='linked formlink']")).getText();
		System.out.println(SearchIncNo);
		if(incNo.equals(SearchIncNo))
		{
			System.out.println("Incident : " + SearchIncNo + " created and verified successfully");
		}

		driver.findElement(By.xpath("//a[@class='linked formlink']")).click();
		Thread.sleep(2000);
	}
}
//-------------------------------------------------------------------------------------------------------------------------------

//UpdateIncident.java

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class UpdateIncident extends BaseClaseServiceNow{
	@Test (priority = 2)
	public void updateIncident() throws InterruptedException {

		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys("incident");
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("(//div[text()='Open'])[1]")).click();
		driver.switchTo().frame("gsft_main");
		String incNo="INC0010004";
		driver.findElement(By.xpath("//input[@class='form-control']")).sendKeys(incNo);
		driver.findElement(By.xpath("//input[@class='form-control']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='linked formlink']")).click();

		WebElement incUrgency = driver.findElement(By.id("incident.urgency"));
		Select urgency =new Select(incUrgency);
		urgency.selectByIndex(1);

		WebElement incState = driver.findElement(By.id("incident.state"));
		Select state=new Select(incState); 
		state.selectByIndex(1);

		String text = driver.findElement(By.id("incident.state")).getText();
		System.out.println(text);
		driver.findElement(By.xpath("(//option[@selected='SELECTED'])[2]")).getText();
		
		driver.findElement(By.id("activity-stream-work_notes-textarea")).sendKeys("TestUpdate");
		driver.findElement(By.id("sysverb_update")).click();


	}


}

//-------------------------------------------------------------------------------------------------------------------

//DeleteIncident.java
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

public class DeleteIncident extends BaseClaseServiceNow{

	@Test
	public  void deleteIncident() throws InterruptedException
	{
		
		driver.findElement(By.xpath("//input[@id='filter']")).clear();
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys("incident");
		driver.findElement(By.xpath("//input[@id='filter']")).sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("(//div[text()='Open'])[1]")).click();
		driver.switchTo().frame("gsft_main");
		String incNo="INC00000037";
		driver.findElement(By.xpath("//input[@class='form-control']")).sendKeys(incNo);
		driver.findElement(By.xpath("//input[@class='form-control']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[@class='linked formlink']")).click();
		driver.findElement(By.id("sysverb_delete_bottom")).click();
		Thread.sleep(2000);
		driver.findElement(By.id("ok_button")).click();
		Alert alert=driver.switchTo().alert();
		alert.accept();
		

	}
}

//-------------------------------------------------------------------------------------------------------------

//incident_seq_testng.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite" verbose="5">
	<parameter name="url"
		value="https://dev119620.service-now.com/navpage.do"></parameter>
	<parameter name="username" value="admin"></parameter>
	<parameter name="password" value="Cktsrj@3"></parameter>

	<test thread-count="5" name="Test">
		<classes>
			<class name="week5.day1assignments.CreateIncident" />
			<class name="week5.day1assignments.UpdateIncident" />
			<class name="week5.day1assignments.DeleteIncident" />
		</classes>
	</test> <!-- Test -->
</suite> <!-- Suite -->

//-------------------------------------------------------------------------------------------------------------

//incident_par_testng.xml

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite" parallel="classes" verbose = "5" >
  <test thread-count="5" name="Test" >
    <classes>
      <class name="week5.day1assignments.UpdateIncident"/>
      <class name="week5.day1assignments.CreateIncident"/>
      <class name="week5.day1assignments.DeleteIncident"/>
    </classes>
  </test> <!-- Test -->
</suite> <!-- Suite -->

