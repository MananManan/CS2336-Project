//mxm163530 - Manan Manan - CS2336.002 - Jason Smith
//Recursive Function Definition: 150  and 168. Recursive Call : 200
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import LinkList.*;

public class Main {
    
    final public static int COST = 7; //contant vairable for price of a seat
    
    public static int integerInput(Scanner input, int lower, int upper, String prompt){
        
        System.out.print(prompt);
        int i = 0;
        String s = null;
outerloop: do{ //labeled this loop
                s = input.nextLine();
                try{//use of exception handling
                   i = Integer.parseInt(s);
                }catch (Exception ex){//if there is any exception, then the string is not integer
                    System.out.print("Invalid Input. " + prompt);
                    continue;
                }

                while (i >= upper || i <= lower){
                System.out.print("Out of bounds Input. " + prompt);
                continue outerloop;
                }

                break; //if reaches this part, then eveyrthing is correct
        } while (true);
        
        
        return i;
                
    }
    
    public static boolean checkSeats (LinkedList open, int row, int seat, int numberOfSeats){
        
        int index = open.findIndex(new DoubleLinkNode (row,seat));
        
        if (index < 0) return false; //if the first seat does not exist
        
        DoubleLinkNode temp = open.get(index); //if exists, store it in index
        int i = 0;
        
        while(i < numberOfSeats - 1 && temp!=null) { //keep going until the condition
            temp = temp.getNext();
            i++;
        }
        //if temp is not null and the node that it is on equals the end seat, then there was no skipping
        return (temp != null && temp.equals(new DoubleLinkNode (row,seat + numberOfSeats - 1)));
        
    }
    
    public static void reserveSeats (LinkedList [] auditorium, int row, int seat, int numberOfSeats){
        //THIS FUNCTION DOES NOT BOUND CHECK!!! CHECK THE SEATS BEFOREHAND IF THEY ARE AVAILABLE OR NOT
        //finding the node
            int index = auditorium[0].findIndex(new DoubleLinkNode(row,seat));
            //making a copy subList of auditorium
            LinkedList sub = auditorium[0].subList(index, numberOfSeats);
            //removing from the open
            auditorium[0].removeList(index, numberOfSeats);
            //adding subList to reserved
            int index2 = 0; //this is the insertion index
            for (DoubleLinkNode temp = auditorium[1].getHead(); //initialization statement
                    temp !=null && (!(temp.getRow() <= row) || temp.getSeat() < seat); //condition, with second one being implication equivalence
                    temp = temp.getNext(), index2++); 
            //temp is now in a position where the conditions do not meet. 
            //assume that for head, none of this is true. then we insert before head or at index2 = 0
            auditorium[1].addList(sub, index2);
           auditorium[1].sort(); //sorting is not required because the insertion was done at the right place. however, just to be safe...
            
            return; //the function ends here if the seats are reserved
        }
      
    public static DoubleLinkNode findBestSeat(LinkedList auditorium[], int rowCol[], int numberOfSeats){
        //NOTE: I have used double comparison here, and thus have removed the need for square roots. The distance here is actually
        // the square of the distance, but that does not affect the minnimum distances as a2 < b2 -> a < b
        
         //step 1 : finding the minnimum possible distance distance
        DoubleLinkNode best = null;//this is just a reference that stores best and starts at head
        
        LinkedList bestSeats = new LinkedList(); //linked list for the best seats
        double min = Integer.MAX_VALUE;
        
        for (DoubleLinkNode temp = auditorium[0].getHead(); temp != null; temp = temp.getNext() ){
            
            if (checkSeats(auditorium[0], temp.getRow(), temp.getSeat(), numberOfSeats)){//if the consecutive seats from temo are available
                double distanceSq = //square of the distance between the middle of auditorium. square is chosen because it is easier to compare
                       Math.pow(temp.getRow() - (rowCol[0]+1)/2.0, 2) + Math.pow(temp.getSeat() - (rowCol[1]+ 1)/2.0, 2);
                if (distanceSq == min) {
                    bestSeats.add(temp.deepCopy(), 0); //add it to the best seats until now                    
                } //if there is a tie
                else if(distanceSq < min) { //if new min distance is found
                    bestSeats = new LinkedList (temp.deepCopy());//scratch the old list and start a new list
                    min = distanceSq;
                } //if distance is small, remember it
            }
        }
        
        if (bestSeats.length() == 1) best = bestSeats.getHead(); //no need to go through the list
        else if (bestSeats.length() > 1){//if there is a tie, go through the bestSeats list
            double minRowDistance = Integer.MAX_VALUE;
            for (DoubleLinkNode temp = bestSeats.getHead(); temp != null; temp = temp.getNext() ){
                double minTempDistance = Math.abs(temp.getRow() - rowCol[0]/2.0); 
                if ( minTempDistance < minRowDistance){
                    minRowDistance = minTempDistance;
                    best = temp.deepCopy();
                }
            }
        }
        else if(bestSeats.length() == 0) best = null; //there are no best seats
        
        return best; //return the best seat
    }

    public static LinkedList[] prepareList (String fileName, int rowCol[]) throws FileNotFoundException{
        
        Scanner input = new Scanner (new File(fileName)); //set scanner to file

        LinkedList auditorium[] = {new LinkedList(), new LinkedList()};
        
        if(!(new File(fileName).exists())) return auditorium; //if file does not exists]
        
            String s = new String();
        
        int ROW = 1;
        while(input.hasNext()){
            
            s = input.nextLine();
            
            for(int i = 0; i < s.length(); i++){
                if (s.charAt(i) == '#') auditorium[0].add(new DoubleLinkNode(ROW, i+1), auditorium[0].length());
                else auditorium[1].add(new DoubleLinkNode(ROW, i+1), auditorium[1].length());
            }   
            
            ROW++;
        }
        
        rowCol[0] = ROW - 1;
        rowCol[1] = s.length();
        
        return auditorium;
        
    }
    
    public static void recursivePrint(LinkedList[] auditorium, int rowCol[], PrintWriter output, boolean header){
        //header is if we want the number overlay or not
        int ROW = 0;
         
        if (header){
            output.print("  ");
            for(int i = 1; i <= rowCol[1]; i++){
                output.print(i%10);//column
            }
            output.println();
            output.print("1 "); //row
        }
        
        LinkedList copyAuditorium[] = {auditorium[0].subList(0, auditorium[0].length()), auditorium[1].subList(0, auditorium[1].length())};
        
        recursivePrint(copyAuditorium, output, 1, header);
    }
    
    private static void recursivePrint(LinkedList[] auditorium, PrintWriter output, int ROW, boolean header){

        if (auditorium[0].getHead() == null && auditorium[1].getHead() == null) {
            output.flush();
            return;
        } //base case
        
        //modified version of merge sort on two already sorted lists
        //if this statement is to be executed...
        else if(  auditorium[0].getHead() != null && //this list is NOT null AND
                  (auditorium[1].getHead() == null ||  //auditorium 1 is null OR if it is not null then...
                        (auditorium[0].getHead().compareTo(auditorium[1].getHead())) < 0)
                ){//if row <= and seat 
            
                if(auditorium[0].getHead().getRow() == ROW+1){ //if the current node is starting of next row
                    output.println();//new line
                    ROW++;//we are now on the next row
                    if(header) output.printf("%-2d", ROW); 
                }
                output.print('#');
                auditorium[0].remove(0);
        }
        else{
            if(auditorium[1].getHead().getRow() == ROW+1){
                output.println();//new line
                ROW++;//we are now on the next row
                if(header) output.printf("%-2d", ROW); 
            }
            output.print('.');
            auditorium[1].remove(0);    
        }
        
        recursivePrint(auditorium, output, ROW, header); //RECURSIVE CALL
        
        
    }
    
    public static void main (String[] args) throws FileNotFoundException {
        
        /*The main function is divied into 3 steps:
            1. Preparing the arrays for all 3 Auditoriums (A1, A2, A3)
            2. Creating the UIs
            3. Creating the menu
        */
        
        //***************CREATING AND PREPARING THE ARRAYS***************
               
        //I have used the following arrays to improve the redability and reduce keystrokes
        
        String[] files = {"A1.txt", "A2.txt", "A3.txt"} ;
        int rowCol[][] = new int[3][2];
        LinkedList [][] auditoriums = 
                    {prepareList(files[0], rowCol[0]), prepareList(files[1], rowCol[1]), prepareList(files[2], rowCol[2])}; //temprory array *reference* for menu
        
        //***************CREATING THE UI*******************
        Scanner input;
        PrintWriter output;
        
        while(true){
                input = new Scanner (System.in);
                
                System.out.println("1. Reserve Seats \n2. View Auditoriums \n3. Exit");
                
                
                int auditorium = 0, option = 0;
                
               option = integerInput(input, 0, 4, "Choose Function : ");
               
                if (option == 3){//writing all files and breaking out of the loops
                   for(int i = 0; i < 3; i++){
                       output = new PrintWriter (files[i]); 
                        recursivePrint(auditoriums[i] , rowCol[i], output, false);
                        output.close();//closing this immidiately after outputting
                   }
                   
                   break;
                }
                
                //if choosing a non-exit option
                System.out.println("1. Auditorium 1 \n2. Auditorium 2\n3. Auditorium 3");
                
                auditorium = integerInput(input, 0, 4, "Choose Auditorium : ");
                
                if (option == 1){//if reserve seats
                   int row = integerInput(input, 0, rowCol[auditorium-1][0] + 1, "Enter Row : ");
                   
                   int start = integerInput(input, 0, rowCol[auditorium-1][1] + 1, "Enter Starting Seat : ");

                   int number = integerInput(input, -1, Integer.MAX_VALUE, "Enter Number of Tickets : ");
                   
                   //starting to reserve seats
                   
                   if (number > rowCol[auditorium-1][1]){//check if the seats are greater than the row length
                       System.out.println("Seats unavailable.");
                       System.out.println("No seats were reserved.");
                   }
                   
                   else if(checkSeats(auditoriums[auditorium-1][0], row, start, number)){
                       reserveSeats(auditoriums[auditorium-1], row, start, number);
                       System.out.println("Your seats were reserved!");
                    }
                   else{
                       System.out.println("Seats unavailable.");
                       //trying to find the best seats
                       DoubleLinkNode best = findBestSeat(auditoriums[auditorium-1], rowCol[auditorium-1], number);
                       if(best != null) {//if best is no null i.e. best is found
                           if (number == 1)System.out.println("The best seat is seat " + best.getSeat() + " in row " + best.getRow() + '.');
                           else System.out.println("The best seats are seats " + best.getSeat() + " - " + (best.getSeat() + number - 1) + " in row " + best.getRow() + '.');
                           
                           String c;
                           do{
                               System.out.print("Do you want to reserve them (Y/N) : "); //validating input
                               c = input.nextLine();
                           }while(!(c.equals("Y") || c.equals("N")));
                           
                           if (c.equals("Y")){
                               reserveSeats(auditoriums[auditorium-1], best.getRow(), best.getSeat(), number);
                               System.out.println("Your seats were reserved!");
                           }
                           
                           else System.out.println("No seats were reserved.");
                       }
                       else System.out.println("No seats were reserved."); //if best is not there
                   }
                }
                
                else if (option == 2){
                    //show auditorium to screen
                    output = new PrintWriter(System.out);
                    recursivePrint(auditoriums[auditorium-1] , rowCol[auditorium - 1], output, true); //recursive print with headers
                    System.out.println();
                }       
                System.out.println();
            }    
            //*************REPORT TO CONSOLE***************
            
            System.out.printf("%15s%10s%6s%10s\n", "Auditorium", "Reserved", "Open", "Total($)"); //heading
            
            int totalReserved = 0, totalOpen = 0;
            
            for(int i = 0; i < 3 ; i++){   
                System.out.printf("   Auditorium %d%10d%6d%9d\n", (i+1), auditoriums[i][1].length(), auditoriums[i][0].length(), auditoriums[i][1].length()*COST);
                totalReserved += auditoriums[i][1].length(); totalOpen += auditoriums[i][0].length();
            }
            
            System.out.printf("         Total %10d%6d%9d\n", totalReserved, totalOpen, totalReserved*COST); //total
    }
    
}
