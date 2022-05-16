package BetterQATests.BetterQATests;

import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;


import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import pages.Login;
import pages.TopMovies;

/* 
 * PS for the programmers 
 * please be more concise with the DOM elements using id,name attributes more frequently
 * */
public class AppTest 
{
	WebDriver driver;
	Login objLogin;
	TopMovies objTopMovies;
	
	@BeforeTest
	public void setup() {
		driver = new FirefoxDriver();
		driver.manage().window().fullscreen();
		driver.get("https://betterqa.ro/top-movies/");
	    objLogin = new Login(driver);
	    objLogin.login();
	}
	
    @Test(priority = 0)
    public void list_Of_Movie_Titles_Is_Displayed_And_Match()
    {
       objTopMovies = new TopMovies(driver);
//       objTopMovies.printOut();
       assertTrue("Title do not match!",
    		   		objTopMovies.verify_Title_Text());
       assertTrue("List is not displayed!",
    		   		objTopMovies.verify_List_Is_Displayed());
       
//       assertTrue("List with movies do not match!", objTopMovies.verify_All_Movies_Are_In_Movie_List());
    }
    
    @Test(priority = 2)
    public void the_Shawshank_Redemption_Release_Date() {
    	objTopMovies = new TopMovies(driver);
    	objTopMovies.clickLearnMore(1);
    	//release date on this should be 22-09-1994 but is 23-09-1994(Google check)
    	System.out.println(objTopMovies.getReleaseDate());
    	assertTrue("The date " + objTopMovies.getReleaseDate() + " is not the true realise date! ",
    				objTopMovies.getReleaseDate().equals("1994-09-23"));
    }
    
    @Test(priority = 1, dataProvider="DataForSearch", dataProviderClass=dataProvider.DataProviderClass.class)
    public void search_For_Star_Trek(String displayedMovie, String notVisibleMovie) {
    	objTopMovies = new TopMovies(driver);
    	objTopMovies.search("Star Trek");
    	objTopMovies.printOut();
    	assertTrue("Star Trek: First Contact was not found in the list of movies!",
    				objTopMovies.verify_Presence_Of_A_Movie_In_The_Movie_List(displayedMovie));
    	assertTrue("Star Trek: First Contact is not Displayed!",
    				objTopMovies.verify_Movie_Is_Displayed(displayedMovie));
    	assertTrue("Star Trek: First Contact was not found in the list of movies!",
					!objTopMovies.verify_Presence_Of_A_Movie_In_The_Movie_List(notVisibleMovie));
    	assertTrue("The Shawshank Redemption is visible! ", 
    				!objTopMovies.verify_Movie_Is_Displayed(notVisibleMovie));
    }
    
	/*
	 * This test appears to have a bug, sometimes it opens a wrong "Learn More" button
	 * First time it fails and then work correctly(usually)
	 * If the test run itself multiple times in appears to work correctly (invocationCount = numberOfCount)
	 * Everyday have a new popularity and vote count, to work this out need to check data every time before test and modify if needed
	 * */
    @Test(priority = 2, invocationCount = 3)
    public void any_Movie_ReleasedOn_Popularity__VoteAverage__VoteCount() {
    	objTopMovies = new TopMovies(driver);
    	objTopMovies.search("Spirited Away");
    	objTopMovies.clickLearnMore(2);
    	objTopMovies.printAttributesData();
    	if(!objTopMovies.verify_Movie_Learn_More_Atributtes("2001-07-20", "84.257", "8.5", "12806")) {
    		objTopMovies.screenShot("any_Movie_ReleasedOn_Popularity__VoteAverage__VoteCount");
    	}
        assertTrue("Some data from the movie attributes do not match!",
        		objTopMovies.verify_Movie_Learn_More_Atributtes("2001-07-20", "84.257", "8.5", "12806"));
    	
    }
  
    /*
     * This is the bug function
     * When trying to search with the empty input box the DOM erase 
     * */
    @Test(priority = 3)
    public void search_For_Nothing() throws InterruptedException{
    	objTopMovies = new TopMovies(driver);
    	objTopMovies.search("");
    	Thread.sleep(1000);
    	try {
    		objTopMovies.clickLearnMore(1);
    	}catch (NoSuchElementException|TimeoutException e) {
			objTopMovies.screenShot("search_For_Nothing");
			assertTrue("Learn More button could not be found!",false);
		}
    }
    
    @AfterMethod
    public void refresh() {
    	driver.navigate().refresh();
    }
    
    @AfterTest
    public void quit() {
    	driver.quit();
    }
}
