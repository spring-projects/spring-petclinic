package com.griddynamics.web.blocks;


import com.griddynamics.qa.ui.ElementBlock;
import org.jbehave.web.selenium.WebDriverProvider;

import static com.griddynamics.web.blocks.HeaderBlockData.*;

/**
 * @author lzakharova
 */
public class HeaderBlock extends ElementBlock {

    public HeaderBlock(WebDriverProvider provider) {
        super(provider, HEADER_BLOCK_NAME, headerBlockLoc);

        addElement(HOME_MENU, homeMenuLoc);
        addElement(FIND_OWNERS_MENU, findOwnersMenuLoc);
        addElement(VETERINARIANS_MENU, veterinariansMenuLoc);
        addElement(ERROR_MENU, errorMenuLoc);
        addElement(HELP_MENU, helpMenuLoc);
    }
}
