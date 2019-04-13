package webportal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Main {

	private static Logger log = LogManager.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws IOException {
//		getCurrentDate();
//		screenshot(setupDriver());
		webPortalLogin();
//		sendMsg();
//		enterTimesheet();
//		delete_Hours();
//		accessHours();
//		testEndTime("1:30");

	}
	
	public static WebDriver setupDriver() {
		log.info("starting up Chrome Driver");
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver");
		WebDriver driver = new ChromeDriver();
		log.info("Chrome Driver started successfully");
		return driver;
	}
	
	public static Properties setupProperties() throws IOException {
		log.info("setting up properties");
		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"/src/main/resources/data.properties");
		prop.load(fis);
		log.info("completed property setup");
		
		return prop;
			
	}
	
	public static String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMdd_uuuu");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
//		System.out.println(currentDate);
		return currentDate;
	}
	 
	public static WebDriver webPortalLogin() throws IOException {
		Properties prop = setupProperties();
//		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/chromedriver");
//		WebDriver driver = new ChromeDriver();
		WebDriver driver = setupDriver();
		log.info("logging into website");
		driver.get(prop.getProperty("loginurl"));
//		System.out.println("before login");
		WebDriverWait d = new WebDriverWait(driver, 20);
		LoginPage lp = new LoginPage(driver);
		d.until(ExpectedConditions.visibilityOfElementLocated(lp.login));
		lp.username().sendKeys(prop.getProperty("username"));
		lp.password().sendKeys(prop.getProperty("pass"));
		lp.submit().click();
		log.info("successfully logged into website");

		return driver;
	}
	
	public static void enterTimesheet() throws IOException {
		WebDriver driver = webPortalLogin();
		TimesheetPage tp = new TimesheetPage(driver);
		log.info("setting up times");
		ArrayList<String> startTime = new ArrayList<String>
		(Arrays.asList("4:29","4:29","","","4:30","4:25","4:27"));
		ArrayList<String> lunchEnd = new ArrayList<String>();
		
		ArrayList<String> endTime = new ArrayList<String>
		(Arrays.asList("1:30","1:30","","","1:30","1:31","1:31"));
		log.info("waiting for page to load");
		WebDriverWait d = new WebDriverWait(driver, 20);
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.page));
		
		Actions a = new Actions(driver);
		
		a.moveToElement(tp.selectRow()).click().build().perform();
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.save));
		
		ampmDropDown(driver, "PM", "1");
		ArrayList<String> lunchStart = randomDinner(lunchEnd, driver);
		ampmDropDown(driver, "AM", "4");
		
		fillTime(driver,startTime,1);
		fillTime(driver,lunchStart,2);
		fillTime(driver,lunchEnd,3);
		fillTime(driver,endTime,4);
		writeComments(driver, startTime,tp);

		tp.saveTimesheet().click();
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.delete));	
//		tp.submitTimesheet().click();	
//		[@value='Save'] or id=updateit
//		d.until(ExpectedConditions.visibilityOfElementLocated(tp.delete));
//		String filename = screenshot(driver);
//		sendMsg(filename);
//		tp.logout().click();
//		LoginPage lp = new LoginPage(driver);
//		d.until(ExpectedConditions.visibilityOfElementLocated(lp.login));
//		driver.close();
		
	}
	
	public static void ampmDropDown(WebDriver driver,String time, String index) {
		for(int i =0; i<7; i++) {
			Select s = new Select(driver.findElement(By.id("s_" + i + "_" + index)));
			s.selectByValue(time);
		}	
	}
	
	public static void delete_Hours() throws IOException {
		WebDriver driver = webPortalLogin();
		TimesheetPage tp = new TimesheetPage(driver);
		WebDriverWait d = new WebDriverWait(driver, 20);
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.page));
		
		Actions a = new Actions(driver);
		
		a.moveToElement(tp.selectRow()).click().build().perform();
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.delete));
		tp.deleteHours().click();
		driver.switchTo().alert().accept();
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.submit));
		tp.logout();
		driver.close();
		
	}
	
	public static void accessHours() throws IOException {
		WebDriver driver = webPortalLogin();
		TimesheetPage tp = new TimesheetPage(driver);
		WebDriverWait d = new WebDriverWait(driver, 20);
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.page));
		
		Actions a = new Actions(driver);
		
		a.moveToElement(tp.selectRow()).click().build().perform();
		d.until(ExpectedConditions.visibilityOfElementLocated(tp.delete));
//		tp.deleteHours().click();
//		driver.switchTo().alert().accept();
//		d.until(ExpectedConditions.visibilityOfElementLocated(tp.submit));
//		tp.logout();
//		driver.close();
		
	}
	 
	public static void fillTime(WebDriver driver, String time, int column) {
		for(int i = 0; i<7; i++) {
			driver.findElement(By.id("t_" + i + "_" + column)).sendKeys(time);
			if(i==1)
				i = 3;
		}
	}
	
	public static void fillTime(WebDriver driver, ArrayList<String> time, int column) {
		for(int i = 0; i<7; i++) {
			driver.findElement(By.id("t_" + i + "_" + column)).sendKeys(time.get(i));
			if(i==1)
				i = 3;
		}
	}

	public static ArrayList<String> randomDinner(ArrayList<String> lunchEnd, WebDriver driver) {
		log.info("Randomizing dinner times");
		ArrayList<String> carDinners = new ArrayList<String>();
		HashMap<Integer, String> dinners = createDinners();
		for(int i =0; i<7; i++) {
			int car = createRandomCar();
			String dinnerStart = dinners.get(car);
			carDinners.add(dinnerStart);
			String lunch = "8:30";
			if(car == 2) {
				lunch = "12:00";
				Select s = new Select(driver.findElement(By.id("s_" + i + "_" + 3)));
				s.selectByValue("AM");
			}
			lunchEnd.add(lunch);
		}
		return carDinners;
	}
	
	public static HashMap<Integer, String> createDinners() {
		HashMap<Integer, String> dinners = new HashMap<Integer, String>();
		dinners.put(1, "7:30");
		dinners.put(2, "11:00");		
		return dinners;
	}	
	
	public static int createRandomCar() {
		return (int) (Math.random()*2) +1;
	}
		
	public static void writeComments(WebDriver driver, ArrayList<String> startTime, TimesheetPage tp) {
//		String days = "//table/tbody/tr/td/div/div/form/table/thead/tr/th[@width='12%']";
		
		for(int i=0; i<7;i++) {
			
			if(testTime(startTime.get(i))) {
				//extract the date 
				String dayText = driver.findElements(tp.dayText).get(i).getText();				
				//pasting the comment and hitting enter
				tp.enterComments().sendKeys(dayText + " - came in little early\n");
			}
			if(i==1)
				i = 3;
		}
	}
	
	public static boolean testTime(String time) { 		
		time = padTime(time);
		LocalTime start = LocalTime.parse(time);
		LocalTime normal = LocalTime.parse("16:30");	
		log.info("Time entered = " + start + "Normal Time = " + normal);
//		System.out.println("Time entered = " + start + "Normal Time = " + normal);
		return start.isBefore(normal);	
	}
	
	public static boolean testEndTime(String time) {
		String endTime = padEndTime(time);
		LocalTime end = LocalTime.parse(endTime);
		LocalTime normal = LocalTime.parse("01:30");
		return end.isAfter(normal);
	}
	
	public static String padTime(String time) {
		
		String right = time.substring(0, time.indexOf(":"));
		if(right.length()==1 || right.charAt(0) == '0') {
			int adding = Integer.valueOf(right) + 12;
			time = time.replace(right, String.valueOf(adding));
		}
		log.info("initial hour " + right);
		log.info("initial hour " + time);
		return time;
	}
	
	public static String padEndTime(String time) {
		return "0" + time;
	}
	
	public static void sendMsg(String screenshot) {
		final String myEmail = "";
        final String password = "";
        String opponentEmail="";
        Properties pro=new Properties();
        pro.put("mail.smtp.host", "smtp.gmail.com");
        pro.put("mail.smtp.starttls.enable","true");
        pro.put("mail.smtp.auth","true");
        pro.put("mail.smtp.port","587");
        Session ss=Session.getInstance(pro, new javax.mail.Authenticator()
                {
                   
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(myEmail,password );
                 }
    });
        try
        {
            Message msg=new MimeMessage(ss);
            msg.setFrom(new InternetAddress(myEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(opponentEmail));
            msg.setSubject("Timesheet Submission Successful");
            
            BodyPart messageBodyPart = new MimeBodyPart();
//            msg.setText("this is a test email from me");
            messageBodyPart.setText("This is a confirmation email for submitting my timesheet.");
         // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String filename = screenshot;
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
              msg.setContent(multipart);

//            msg.ATTACHMENT = msg.setfil
//            Transport trans = ss.getTransport("smtp"); 
            Transport.send(msg);
//            log.info("message sent");
            System.out.println("message sent");
        }
        catch(Exception e)
        {
//        	log.error(e.getMessage());
           System.out.println(e.getMessage());
        }
	}
	
	public static String screenshot(WebDriver driver) throws IOException {
		File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		String filename = System.getProperty("user.dir")+ "/src/main/resources/" + "Timesheet" + getCurrentDate() + ".png";
		FileHandler.copy(src, new File(filename));
		return filename;
	}

}
