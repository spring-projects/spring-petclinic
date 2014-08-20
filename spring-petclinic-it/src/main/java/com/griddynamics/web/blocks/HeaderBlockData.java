package com.griddynamics.web.blocks;


import org.openqa.selenium.By;

/**
 * @author lzakharova
 */
public class HeaderBlockData {

    public final static String HEADER_BLOCK_NAME = "Header";
    public final static String HEADER_BLOCK_COMMON_LOC = "//div[@class='navbar']";
    public final static By headerBlockLoc = By.xpath(HEADER_BLOCK_COMMON_LOC);


    public final static String HOME_MENU = "Home menu";
    public final static By homeMenuLoc = By.xpath(HEADER_BLOCK_COMMON_LOC + "//a[contains(text(), 'Home')]");

    public final static String FIND_OWNERS_MENU = "Find owners menu";
    public final static By findOwnersMenuLoc = By.xpath(HEADER_BLOCK_COMMON_LOC + "//a[contains(text(), 'Find owners')]");

    public final static String VETERINARIANS_MENU = "Veterinarians menu";
    public final static By veterinariansMenuLoc = By.xpath(HEADER_BLOCK_COMMON_LOC + "//a[contains(text(), 'Veterinarians')]");

    public final static String ERROR_MENU = "Error menu";
    public final static By errorMenuLoc = By.xpath(HEADER_BLOCK_COMMON_LOC + "//a[contains(text(), 'Error')]");

    public final static String HELP_MENU = "Help menu";
    public final static By helpMenuLoc = By.xpath(HEADER_BLOCK_COMMON_LOC + "//a[contains(text(), 'Help')]");

}
