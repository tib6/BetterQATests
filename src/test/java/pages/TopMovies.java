package pages;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class TopMovies {
	WebDriver driver;
	AjaxElementLocatorFactory factory;
	JavascriptExecutor executor;
	DateTimeFormatter dtf;
	
	@FindBy(xpath = "//*[@id=\"root\"]/div/header/div/h2")
	WebElement title;
	@FindBy(xpath = "/html/body/div[5]/div[2]/div[2]/div[2]/div/input")
	WebElement releaseDate;
	@FindBy(xpath = "/html/body/div[5]/div[2]/div[2]/div[3]/div/input")
	WebElement popularity;
	@FindBy(xpath = "/html/body/div[5]/div[2]/div[2]/div[4]/div/input")
	WebElement voteAverage;
	@FindBy(xpath = "/html/body/div[5]/div[2]/div[2]/div[5]/div/input")
	WebElement voteCount;
	
	By movies = By.className("movies");
	By inputSearch = By.className("jss76");
	
	
	private final String titleText = "Top Movies";
	private final String[] moviesInList = new String[]
		{
				"The Shawshank Redemption",
				"Dilwale Dulhania Le Jayenge",
				"The Godfather",
				"Impossible Things",
				"Schindler's List",
				"The Godfather: Part II",
				"Your Eyes Tell",
				"Dou kyu sei – Classmates",
				"Gabriel's Inferno: Part II",
				"Violet Evergarden: The Movie",
				"Gabriel's Inferno",
				"Spirited Away",
				"Your Name.",
				"Gabriel's Inferno: Part III",
				"Parasite",
				"12 Angry Men",
				"Josee, the Tiger and the Fish",
				"The Green Mile",
				"The Dark Knight",
				"Pulp Fiction"
			};

	public TopMovies(WebDriver driver) {
		this.driver = driver;
		dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");  
		factory = new AjaxElementLocatorFactory(driver,5);
		executor = (JavascriptExecutor)driver;
		//This initElements method will create all WebElements
		PageFactory.initElements(factory, this);
	}
	
	public List<WebElement> getListMovies() {
		 WebElement moviesElement = new WebDriverWait(driver, Duration.ofSeconds(5))
	    			.until(ExpectedConditions.elementToBeClickable(movies));
		return moviesElement.findElements(By.tagName("h2"));
	}
	
	public List<String> getTitleMovies(){
		List<String> titleMovies = new ArrayList<String>();
		for(WebElement element : getListMovies()) {
			titleMovies.add(element.getText());
		}
		return titleMovies;
	}
	
	public String getTitleText() {
		return title.getText();
	}
	
	public String getReleaseDate() {
		this.scrollToElement(releaseDate);
		return releaseDate.getAttribute("value");
	}
	
	public String getPopularity() {
		this.scrollToElement(popularity);
		return popularity.getAttribute("value");
	}
	
	public String getVoteAverage() {
		this.scrollToElement(voteAverage);
		return voteAverage.getAttribute("value");
	}
	
	public String getVoteCount() {
		this.scrollToElement(voteCount);
		return voteCount.getAttribute("value");
	}
	
	public void search(String search) {
    	WebElement input = new WebDriverWait(driver, Duration.ofSeconds(2))
		.until(ExpectedConditions.elementToBeClickable(inputSearch));
    	input.sendKeys(search, Keys.RETURN);
	}
	
	public void clickLearnMore() {
		this.getLearnMoreButton().click();
	}
	
	public void clickLearnMore(int index) {
		this.getLearnMoreButton(index).click();
	}
	
	public void scrollToElement(WebElement element) {
		executor.executeScript("arguments[0].scrollIntoView();",element);
	}
	
	public WebElement getLearnMoreButton() {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
    			.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.jss10:nth-child(1) > div:nth-child(3) > button:nth-child(1)")));
	}
	
	public WebElement getLearnMoreButton(int index) {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
    			.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.jss10:nth-child(" + index + ") > div:nth-child(3) > button:nth-child(1)")));
	}
	
	public void printAttributesData() {
		System.out.println("Movie attributes: " +
						   "Date: " + this.getReleaseDate() + "\n" +
						   "Popularity: " + this.getPopularity() + "\n" +
						   "Vote Average: " + this.getVoteAverage() + "\n" +
						   "Vote Count: " + this.getVoteCount());
	}
	
	public void printOut() {
		System.out.println("Num of titles is: " + this.getListMovies().size());
		for(WebElement element : this.getListMovies()) {
			System.out.println(element.getText());
		}
		System.out.println("--------------");
		for(String element : this.getTitleMovies()) {
			System.out.println(element);
		}
	}
	
	public boolean verify_Title_Is_Displayed() {
		return title.isDisplayed();
	}
	
	public boolean verify_Title_Text() {
		return this.getTitleText().equals(titleText);
	}
	
	public boolean verify_List_Is_Displayed() {
		return new WebDriverWait(driver, Duration.ofSeconds(5))
    			.until(ExpectedConditions.presenceOfElementLocated(movies)).isDisplayed();
	}
	
	public boolean verify_All_Movies_Are_In_Movie_List() {
		List<String> stringList = new ArrayList<String>(Arrays.asList(moviesInList));
		List<String> stringListFromPage = this.getTitleMovies();
		Collections.sort(stringList);
		Collections.sort(stringListFromPage);
		return stringList.equals(stringListFromPage);
	}
	
	public boolean verify_Presence_Of_A_Movie_In_The_Movie_List(String movie) {
		boolean state = false;
		for(String strMovie : this.getTitleMovies()) {
			if(strMovie.toLowerCase().equals(movie.toLowerCase())) {
				state = true;
				break;
			}
		}
		return state;
	}
	
	public boolean verify_Movie_Is_Displayed(String movie) {
		boolean state = false;
		for(WebElement element : this.getListMovies()) {
			if(element.getText().toLowerCase().equals(movie.toLowerCase())) {
				if(element.isDisplayed()) {
					state = true;
				}
			}
		}
		return state;
	}
	
	public boolean verify_Movie_Learn_More_Atributtes(String releasedOn, String popularity, String voteAverage, String voteCount) {
		if(this.getReleaseDate().equals(releasedOn)
			&&this.getPopularity().equals(popularity)
			&&this.getVoteAverage().equals(voteAverage)
			&&this.getVoteCount().equals(voteCount)) {
				return true;
		}
		return false;
	}
	
	public void screenShot(String methodName) {
		LocalDateTime now = LocalDateTime.now();
		
		try {
			Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
			System.out.println(dtf.format(now));
			ImageIO.write(screenshot.getImage(), "jpg",
					new File("C:\\Users\\tibit\\eclipse-workspace\\BetterQATests\\ScreenShots\\ScreenShot_" + 
										methodName +dtf.format(now) + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
