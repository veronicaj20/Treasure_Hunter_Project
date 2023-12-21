import java.util.Scanner;
/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 */
public class Hunter
{
    //Keeps the items in the kit separate
    private static final String KIT_DELIMITER = ";";

    //instance variables
    private String hunterName;
    private String kit;
    private int gold;
    private int luck;

    // variables to keep track of the treasures
    private String treasure;
    private int treasuresFound;
    private boolean searched;

    //Constructor
    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param name The hunter's name.
     */
    public Hunter(String hunterName, int startingGold)
    {
        this.hunterName = hunterName;
        kit = "";
        gold = startingGold;

        treasure = "";
        treasuresFound = 0;
        searched = false;
    }

    //Accessors
    public String getHunterName()
    {
        return hunterName;
    }

    public String getKit()
    {
        return kit;
    }

    public int getGold()
    {
        return gold;
    }

    public void changeGold(int modifier)
    {
        gold += modifier;
        if (gold < 0)
        {
            gold = 0;
        }
    }

    public void changeLuck(int modifier) {
        luck += modifier;
    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem  the cost of the item
     *
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem)
    {
        if (costOfItem == 0 || gold < costOfItem || hasItemInKit(item))
        {
            return false;
        }

        gold -= costOfItem;
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice  the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice)
    {
        if (buyBackPrice <= 0 || !hasItemInKit(item))
        {
            return false;
        }

        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     *  Removes an item from the kit.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item)
    {
        int itmIdx = kit.indexOf(item);

        // if item is found
        if (itmIdx >= 0)
        {
            String tmpKit = kit.substring(0, itmIdx);
            int endIdx = kit.indexOf(KIT_DELIMITER, itmIdx);
            tmpKit += kit.substring(endIdx + 1);

            // update kit
            kit = tmpKit;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it adds an item to the end of the String representing the hunter's kit.<br /><br />
     * A KIT_DELIMITER character is added to the end of the of String.
     *
     * @param item The item to be added to the kit.
     * @returns true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item)
    {
        if (!hasItemInKit(item))
        {
            kit += item + KIT_DELIMITER;
            return true;
        }

        return false;
    }

    /**
     * Searches the kit String for a specified item.
     *
     * @param item The search item
     *
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item)
    {
        int placeholder = 0;

        while (placeholder < kit.length() - 1)
        {
            int endOfItem = kit.indexOf(KIT_DELIMITER, placeholder);
            String tmpItem = kit.substring(placeholder, endOfItem);
            placeholder = endOfItem + 1;
            if (tmpItem.equals(item))
            {
                // early return
                return true;
            }
        }
        return false;
    }

    /** Returns a printable representation of the inventory, which
     *  is a list of the items in kit, with the KIT_DELIMITER replaced with a space
     *
     * @return  The printable String representation of the inventory
     */
    public String getInventory()
    {
        String printableKit = kit;
        String space = " ";

        int index = 0;

        while (printableKit.indexOf(KIT_DELIMITER) != -1)
        {
            index = printableKit.indexOf(KIT_DELIMITER);
            printableKit = printableKit.substring(0, index) + space + printableKit.substring(index + 1);
        }
        return printableKit;
    }

    public void genTreasure() {
        if (searched) {
            System.out.println("You've already searched for treasure in this town!\n" +
                    "Move on to the next town to continue.");
        }
        else {
            double rand = (int) (Math.random() * 4) + 1;
            boolean found = false;
            if (rand == 1) {
                treasure = "rubies";
                found = true;
            }
            else if (rand == 2) {
                treasure = "pearls";
                found = true;
            }
            else if (rand == 3) {
                treasure = "diamonds";
                found = true;
            }
            else if (rand == 4) {
                treasure = "nothing";
            }

            searched = true;

            if (found) {
                if (hasItemInKit(treasure)) {
                    System.out.println("You found a duplicate " + treasure + " and must discard it! Sorry.");
                } else {
                    addItem(treasure);
                    System.out.println("Aye, you found " + treasure + "!");
                    treasuresFound++;
                }

                // win condition
                if (treasuresFound == 3) {
                    System.out.println("Congratulations, " + hunterName + "! You collected all three treasures and have won the game!");
                    exit();
                }
            }
            else {
                System.out.println("Sorry mate, you didn't find anything. Better luck next time!");
            }
        }
    }

    public void gamble(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Casino!");
        System.out.print("How much gold do you want to wager?: ");
        String x = scanner.nextLine();
        int wager = Integer.parseInt(x);
        changeGold(wager * -1);

        System.out.print("Pick a number between 1-12:");
        String y = scanner.nextLine();
        int guess = Integer.parseInt(y);

        int dice1 = (int) (Math.random() * 12) + 1;
        int dice2 = (int) (Math.random() * 12) + 1;
        int num = dice1 + dice2;

        int goldWon = 0;
        int goldLost = 0;

        if (guess == num) {
            System.out.println("You guessed the right number! You get double gold back.");
            changeGold(wager * 2);
        }
        else if (guess == num - 2 || guess == num + 2) {
            System.out.println("You were within 2 of the number! You get your gold back.");
            changeGold(wager);
        }
        else {
            System.out.println("That was nowhere close to the number... no gold for you!");
        }
    }

    public void updateSearched() {
        searched = false;
    }

    public void exit(){
        System.exit(0);
    }


    /**
     * @return A string representation of the hunter.
     */
    public String toString()
    {
        String str = hunterName + " has " + gold + " gold";
        if (!kit.equals(""))
        {
            str += " and " + getInventory();
        }
        return str;
    }
}

