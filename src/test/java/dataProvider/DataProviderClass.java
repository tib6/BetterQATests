package dataProvider;

import org.testng.annotations.DataProvider;

public class DataProviderClass {
    @DataProvider(name="DataForSearch")
    public static Object[][] getDataFromDataprovider(){
        return new Object[][] {
            { "Star Trek: First Contact", "The Shawshank Redemption" }
        }; 
    }
}
