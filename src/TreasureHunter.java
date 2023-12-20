/**
 * This class is responsible for controlling the Treasure Hunter game.<p>
 * It handles the display of the menu and the processing of the player's choices.<p>
 * It handles all of the display based on the messages it receives from the Town object.
 *
 */
import java.util.Scanner;

public class TreasureHunter
{
    //Instance variables
    private Town currentTown;
    private Hunter hunter;
    private boolean easyMode;
    private boolean hardMode;

    //Constructor
    /**
     * Constructs the Treasure Hunter game.
     */
    public TreasureHunter()
    {
        // these will be initialized in the play method
        currentTown = null;
        hunter = null;
        hardMode = false;
        easyMode = false;
    }

    // starts the game; this is the only public method
    public void play ()
    {
        welcomePlayer();
        enterTown();
        showMenu();
    }

    /**
     * Creates a hunter object at the beginning of the game and populates the class member variable with it.
     */
    private void welcomePlayer()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to TREASURE HUNTER!");
        System.out.println("Going hunting for the big treasure, eh?");
        System.out.print("What's your name, Hunter? ");
        String name = scanner.nextLine();

        // set hunter instance variable
        hunter = new Hunter(name, 10);

        System.out.print("Hard mode? (y/n): ");
        String hard = scanner.nextLine();
        if (hard.equals("y") || hard.equals("Y"))
        {
            hardMode = true;
        }
        else {
            System.out.print("Easy mode? (y/n): ");
            String easy = scanner.nextLine();
            if (easy.equals("y") || easy.equals("H")) {
                easyMode = true;
            }
        }
    }

    public boolean isEasyMode() {
        return easyMode;
    }

    /**
     * Creates a new town and adds the Hunter to it.
     */
    private void enterTown()
    {
        double markdown = 0.5;
        double toughness = 0.4;
        if (hardMode)
        {
            // in hard mode, you get less money back when you sell items
            markdown = 0.25;
            // and the town is "tougher"
            toughness = 0.75;
        }
        if (easyMode) {
            markdown = 0.85;
            toughness = 0.2;
        }

        // note that we don't need to access the Shop object
        // outside of this method, so it isn't necessary to store it as an instance
        // variable; we can leave it as a local variable
        Shop shop = new Shop(markdown);

        // creating the new Town -- which we need to store as an instance
        // variable in this class, since we need to access the Town
        // object in other methods of this class
        currentTown = new Town(shop, toughness, easyMode);

        // calling the hunterArrives method, which takes the Hunter
        // as a parameter; note this also could have been done in the
        // constructor for Town, but this illustrates another way to associate
        // an object with an object of a different class
        currentTown.hunterArrives(hunter);
    }

    /**
     * Displays the menu and receives the choice from the user.<p>
     * The choice is sent to the processChoice() method for parsing.<p>
     * This method will loop until the user chooses to exit.
     */
    private void showMenu()
    {
        Scanner scanner = new Scanner(System.in);
        String choice = "";

        while (!(choice.equalsIgnoreCase("X"))) {
            System.out.println("\033[0;36m"); // Cyan color
            System.out.println(currentTown.getLatestNews());
            System.out.println("\033[0;33m***\033[0m"); // Yellow color for divider
            System.out.println("\033[1m" + hunter + "\033[0m"); // Bold hunter
            System.out.println(currentTown);
            System.out.println("\033[0;32m(B)uy something at the shop.\033[0m");
            System.out.println("\033[0;32m(S)ell something at the shop.\033[0m");
            System.out.println("\033[0;32m(M)ove on to a different town.\033[0m");
            System.out.println("\033[0;31m(L)ook for trouble!\033[0m");
            System.out.println("\033[0;35m(H)unt for treasure!\033[0m");
            System.out.println("\033[0;34mGive up the hunt and e(X)it.\033[0m");
            System.out.print("\033[1;37mWhat's your next move? \033[0m"); // Bold white text
            choice = scanner.nextLine().toUpperCase();
            processChoice(choice);
            System.out.println("\033[0m"); // Reset color
        }

    }

    /**
     * Takes the choice received from the menu and calls the appropriate method to carry out the instructions.
     * @param choice The action to process.
     */
    private void processChoice(String choice)
    {
        if (choice.equals("B") || choice.equals("b") || choice.equals("S") || choice.equals("s"))
        {
            currentTown.enterShop(choice);
        }
        else if (choice.equals("M") || choice.equals("m"))
        {
            if (currentTown.leaveTown())
            {
                //This town is going away so print its news ahead of time.
                System.out.println(currentTown.getLatestNews());
                enterTown();
            }
        }
        else if (choice.equals("L") || choice.equals("l"))
        {
            currentTown.lookForTrouble();
        }
        else if (choice.equals("X") || choice.equals("x"))
        {
            System.out.println("Fare thee well, " + hunter.getHunterName() + "!");
        }
        else if (choice.equals("H") || choice.equals("h")) {
            hunter.genTreasure();
        }
        else
        {
            System.out.println("Yikes! That's an invalid option! Try again.");
        }
    }
}

