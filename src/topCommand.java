/*              THIS APP IS BUILT TO RUN ON UBUNTU 16.04 or 18.04
*
*
*               YOU MAY EXPERIENCE ERRORS AND BUGS IF YOU TRY RUNNING IT ON ANY OTHER OS
*
*           THIS APP IS BUILT BY :      ◙   ANAS SAMOUDI
*                                       ◙   MEHDI MOUALIM
*                                       ◙   OUMAIMA MONSIF
* */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;



public class topCommand {
    //sort by user command
    public static void sortByUser(){
        Scanner userInput = new Scanner(System.in);
        String user;
        System.out.println("Enter the username : ");
        user = userInput.nextLine();
        clearScreen();

        //sort by user Command
        String[] sortCmd={"sh", "-c", "top -b -n 1 | head -n 20 "};

        //add username to the sort command
        sortCmd[2] = "top -b -n 1 -u "+user+" | head -n 20 ";
        runCommand(sortCmd);
        System.out.println("\n\nYou are viewing TOP sorted by "+user);
        showMenu();
    }

    //kill a process command
    public static void killProcess(){
        Scanner userInput = new Scanner(System.in);
        String pID;
        System.out.println("Enter the PID of the process : ");
        pID = userInput.nextLine();
        clearScreen();

        //kill Process Command
        String[] killCmd={"sh", "-c", "kill "};

        //add PID to the kill command
        killCmd[2] = "kill "+pID;
        runCommand(killCmd);
        System.out.println("Process killed Successfully. Refresh to go back to TOP! ");
        showMenu();
    }


    //menu function
    public static void showMenu(){
        //add user input instance
        Scanner userInput = new Scanner(System.in);

        //variable of choice
        int choice;

        //showing menu
        System.out.println("\nTOP Command Menu : ");
        System.out.println("\n\tEnter 1 to refresh");
        System.out.println("\tEnter 2 to sort by user");
        System.out.println("\tEnter 3 to kill a process");
        System.out.println("\n\t\tYour choise is : ");
        choice = userInput.nextInt();


        switch (choice){
            case 1:         //refresh
                refresh();
                showMenu();
                break;

            case 2:         //sort by user
                sortByUser();
                break;

            case 3:         //kill a process
                killProcess();
                break;

            default:        //if user enter incorrect choice
                System.out.println("\nUnknown command passed in! The app will refresh after 3 seconds...");

                //waiting 3 seconds process
                try{
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException ex){
                    Thread.currentThread().interrupt();
                }

                refresh();
                showMenu();

        }


    }


    //this function refresh the top output and clear the terminal
    public static void refresh(){
        clearScreen();
        runCommand(new String[]{"sh", "-c", "top -b -n 1 | head -n 20 "});
    }

    //this function clears terminal screen (like "clear" command)
    public static void clearScreen(){
        System.out.print("\033\143");
    }


    //this function calculates the percentage with the values given
    public static float percentageCalculator(float x,float max){
        float percentage = x / max * 100;
        return percentage;

    }

    //this function creates the percentage bar
    public static String percentageBar(float per){
        //need to calculate how much "=" is needed for the percentage bar
        int equalSignCount ;
        equalSignCount =(int) per * 50/100;

        //percentage bar component
        String whiteSpace = " ";
        String line = "=";

        //get the whitespace (stands for the unused percentage)
        for (int index = 0; index <(50 - equalSignCount-1) ;index++){
            whiteSpace = whiteSpace.concat(" ");
        }

        //get the line of equal signs (stands for the used percentage)
        for (int index = 0; index <equalSignCount ;index++){
            line = line.concat("=");
        }

        return line+whiteSpace;
    }

    //run command function
    public static void runCommand(String[] cmd){

        //Declaring the Process
        Process p;

        //Split the line of the resulted output and store it in output
        String[] output;

        //running command
        try {
            //runtime process execution
            p = Runtime.getRuntime().exec(cmd);

            p.waitFor();

            //read process output and store it in a reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));


            String line;

            //set line count to count every line
            int lineCount = 0;


            //print the result of the command top captured at the given time

            //run through the output reader taking 1 line at a time
            while ((line = reader.readLine()) != null) {

                //splitting the line of the output to get the specific wanted information from the output
                output = line.split("\\s+");


                //custom 4 first lines output

                switch (lineCount) {
                    //line 1 of the command top that shows  real time + number of users
                    case 0:
                        System.out.println("Last capture of top was on : " + output[2] + "\nTotal number of users is : " + output[5]);

                        //incrementing the lineCount to get to the next line
                        lineCount++;
                        break;

                    //line 2 of the command top that shows tasks
                    case 1:
                        System.out.println("Total running tasks is : " + output[1] + "\n");
                        System.out.println("\t\tRunning Processes : " + output[3] + "\t\tSleeping Processes :" + output[5]);
                        System.out.println("\t\tStopped Processes : " + output[7] + "\t\tZombie Processes :" + output[9]);

                        //incrementing the lineCount to get to the next line
                        lineCount++;
                        break;

                    //line 3 of the command top that shows CPU usage
                    case 2:
                        System.out.println("\nCPU Usage :     \t( amount of time % )\n");
                        System.out.println("\t\tUser Space : " + output[1] + "\t\t\tIdle Process :" + output[3]);
                        System.out.println("\t\tKernel Process : " + output[5] + "\t\t\tNiced Process :" + output[7]);
                        System.out.println("\t\tWaiting Idle Process : " + output[9] + "\t\tHardware Interrupt :" + output[11]);
                        System.out.println("\t\tSoftware Interrupt : " + output[13] + "\t\tVirtual CPU :" + output[15]);

                        //incrementing the lineCount to get to the next line
                        lineCount++;
                        break;

                    //line 4 of the command top that shows CPU usage
                    case 3:

                        //get the values  of usedMemory and totalMemory
                        float totalMemory = convertFloat(output[3]);
                        float usedMemory = convertFloat(output[7]);

                        //calculate the percentage of the memory used
                        float percentage = percentageCalculator(usedMemory,totalMemory);


                        //printing output
                        System.out.println("\nMemory Usage [" + percentageBar(percentage)+ "]\t" + String.format("%.2f",percentage) + "%");

                        //incrementing the lineCount to get to the next line
                        lineCount++;
                        break;


                    //we just skip line 5 (it feels unnecessary...  )   we replace it with this
                    case 4:
                        System.out.println("\n\n=====================================================================================");
                        System.out.println("\t\t\t\tRunning Process ...");
                        System.out.println("=====================================================================================");
                        lineCount++;
                        break;


                    //Printing every other line just normally
                    default:
                        System.out.println(line);
                }





            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    //String to float function
    public static float convertFloat(String value){

        float floatValue;

        //converting string to float
        floatValue = Float.parseFloat(value);
        return floatValue;
    }

    //     =================================       MAIN FUNCTION       =================================
    public static void main(String[] args) {
        //this is the default top command...
        String[] defaultCmd = {"sh","-c","top -b -n 1 | head -n 20 "};


        //running default top
        runCommand(defaultCmd);
        showMenu();

        System.out.println("\nProcess Completed");

    }
}