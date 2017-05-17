//mxm163530 - Manan Manan - CS2336.002 - Jason Smith

//NOTE: Newline is \r\n on windows!!!

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class Main {
    
    final public static int COST = 7; //contant vairable for price of a seat
    
    public static boolean checkSeats (char [] array, int seat, int numberOfSeats){
        
        if (seat + numberOfSeats - 1 > array.length ||
            seat <= 0 
            ) return false; //check 1: If the seats are out of bounds, return false
        
        for (int i = (seat -1) ; i < seat + numberOfSeats - 1; i++){
            
            if (array[i] == '.') return false; //check 2: if any of the seats are not available 
            
        }
        
        return true; //if the method passes all the checks, then the seats are available
        
    }
    
    public static void reserveSeats (char [] array, int seat, int numberOfSeats){
        
        if (numberOfSeats > array.length){//check if the number of seats exceed the row length
            System.out.println("No seats/best available seats are available.");
            return;
        }
        
        if (checkSeats(array, seat, numberOfSeats)){
            for (int i = (seat -1) ; i < seat + numberOfSeats - 1; i++){
                //if seat is 4, and numberOfSeats are 3, then this will reserver seat 4,5, and 6
                
                array[i] = '.';          
            } 
            System.out.println("Your seats have been reserved!");
            return; //the function ends here if the seats are reserved
        }
        
        //the function continues if the seats are not reserved, and finds the best seats
        
        System.out.println("The seats are not available.");
        
       int best = 0;
outerloop:  while(true){//Label outerloop is used here
       
            int right = 0, left = 0, 
                center = (int)(array.length/2) ;
            

         //Regardless of availibility, The BEST POSSIBLE SEAT (BPS) is always (int)(array.length/2) - (int)(numberOfSeats/2)
         //If the BPS is already reserved, then we move right until center.
         //If the above fails, then we move from BPS towards left, until the last seat touches center



         for (int i = (center - (int)(numberOfSeats/2)) ; //SPECIAL CASE
              (i < center ) && (i < array.length) ; 
              i++){
            //checking the seat from BPS and moving right until center is encountered
             if (checkSeats(array, i+1 , numberOfSeats)){
                 best = i;
                 break outerloop;
             }
         }

         // (i + numberOfSeats - 1) gives the index of the last seat, if i is the index of the first seat

         for (int i = (center - (int)(numberOfSeats/2)); //SPECIAL CASE
              ((i + numberOfSeats - 1) >= center) && (i >= 0);
              i--){        
            //checking the seat from BPS and moving left until last seat is on center
             if (checkSeats(array, i+1 , numberOfSeats)){
                 best = i;
                 break outerloop;
             }
         }
        
         //If the program reaches here, then there are no special cases

          for (int i = center ; i < array.length ; i++){
            //checking the seat from center and moving right until found or end is encountered
              if (checkSeats(array, i+1, numberOfSeats)) {
                  right = i;
                  break;
              }
              else right = -1;
          }

          for (int i = center - numberOfSeats ; i >= 0 ; i--){
//checking the seat from the posiiton where last seat is the center and moving left until found or beginning is encountered
              if (checkSeats(array, i+1, numberOfSeats)) {
                  left = i;
                  break;
              }
              else left = -1;
          }
          
          //comparing which is closer to the center
          if (right == -1) best = left;
          else if (left == -1) best = right;
          else best = ((center - (left + numberOfSeats -1)) <= (right - center))? left : right ;
          break outerloop;
        }
        
        
        if (best >= 0){//if there are bes seats available
            
            System.out.println("The best available seats start from seat number" + (best + 1) + " in the same row. Would you like to reserve it?");
            System.out.print("Enter (capital) Y or N: ");
            
            Scanner input = new Scanner (System.in);
            if (input.next().charAt(0) == 'Y'){
                
                for (int i = best ; i < best + numberOfSeats; i++){//here temp is the index, so 1 has been added 
                    array[i] = '.';          
                }                
                System.out.println("Your seats have been reserved!");
                return; //the function ends here if the seats are reserved
            }
        }
        //if the function makes it until here, then there were no seats reserved
        System.out.println("No seats were reserved.");
        
    }
    
    public static char[][] prepareArray (String fileName) throws FileNotFoundException{
        
        int ROW = 0, COLUMN = 0;
        Scanner input = new Scanner (new File(fileName)); //set scanner to file
        
        if (input.hasNextLine()) ROW++; //increase the row by 1 because we will skip the first row in the next step
        COLUMN = (input.nextLine()).length(); //get the first line. The number of characters = number of columns
        
        while (input.hasNext()){
            input.nextLine(); 
            ROW++;
        }
        
        input.close();
        //Creating the array
        char [][] array = new char[ROW][COLUMN];
        
        //Filling the array
       
        input = new Scanner(new File(fileName));
        input.useDelimiter(""); //using a null delimiter so that the .next() has only one character
                
        for (char[] arrayRow : array){//using for each loop to nevigate through arrays of arrays
            for (int i = 0; i < arrayRow.length; i++){
                
                arrayRow[i] = input.next().charAt(0); //the first (and only) character
                
            }
            //after one row has ended, there is a newline
            //in the statement below, the if check is used to avoid triggering "end of file"
            if (input.hasNext()) input.nextLine(); //moves the pointer a step forward, skipping the newline
        }
        input.close();
        
        return array;
        
    }
    
    public static void showArray (PrintWriter output, char[][] array){
        
        for (char[] arrayRow: array){
                      for (char c : arrayRow){
                          output.print(c);
                      }
                      output.println();
                  }
        output.flush();
    }
    
    public static void showArrayWnumber (PrintWriter output, char[][] array){
        
        
        output.print("  "); //2 extra spaces
        
        for(int i = 0; i < array[0].length; i++)
            output.printf("%d", (i+1)%10); //printing column numbers
        
        output.println();
        
        for(int i = 0; i < array.length; i++){
            output.printf("%-2d", i+1);
            
            for(char c : array[i])
                output.print(c);
            output.println();
        }
        
        output.flush();
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
        
        char [][][] auditoriums = 
                    {prepareArray(files[0]), prepareArray(files[1]), prepareArray(files[2])}; //temprory array *reference* for menu
        
        
        //***************CREATING THE UI*******************
        Scanner input;
        PrintWriter output;
        
        while(true){
                input = new Scanner (System.in);
                
                System.out.println("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n4. Exit");
                System.out.print("Choose : ");
                 
                int auditorium = input.nextInt();
                
                if (auditorium == 4){//writing all files and breaking out of the loops
                   for(int i = 0; i < 3; i++){
                       
                       output = new PrintWriter (files[i]); 
                        showArray(output, auditoriums[i]);
                        output.close();
                        
                   }
                   
                   break;
                }
                //if not exist, then proceed with the program as usual
                //index is auditorium - 1
                output = new PrintWriter(System.out);
                showArrayWnumber(output, auditoriums[auditorium-1]);
                System.out.println("");
                
                System.out.println("1. Row Number\n2. Starting seat number\n3. Number of Tickets");
                    
                int row = 0, start = 0, number = 0; //this variable is required to check
               /*
                do {
                    System.out.print("Please Enter (separated by spaces): ");
                    row = input.nextInt();
                    if (row < 1 || row > auditoriums[auditorium-1].length) continue;
                    start = input.nextInt();
                    if (start < 1 || start > auditoriums[auditorium-1][0].length) continue;
                    number = input.nextInt();
                    if (number < 1 || number > auditoriums[auditorium-1][row - 1].length) continue;
                    
                    break;
                }
                */
                //corection 1
               do{ System.out.print("Enter row : ");
                   row = input.nextInt();}
               while(row < 1 || row > auditoriums[auditorium-1].length);
               
               do{  System.out.print("Enter starting seat : ");
                    start = input.nextInt();
               } while(start < 1 || start > auditoriums[auditorium-1][0].length);
               
               do {System.out.print("Enter number of seats : ");
                  number = input.nextInt();
               } while(number < 1 || number > auditoriums[auditorium-1][row - 1].length);
               
                
                reserveSeats(auditoriums[auditorium-1][row - 1], start, number );
            }    
  
            //*************REPORT TO CONSOLE***************
            
            System.out.printf("%15s%10s%6s%10s\n", "Auditorium", "Reserved", "Open", "Total($)");
            
            int totalReserved = 0, totalOpen = 0;
            
            for(int i = 0; i < 3 ; i++){   
                
                int reserved = 0, open = 0;
            
                for (char[] arrayRow : auditoriums[i]){ //counting the number of open and reserved
                    for (char c : arrayRow){
                        if (c == '.') {
                            reserved++;
                        }
                        else open++;
                    }
                }
            
                System.out.printf("   Auditorium %d%10d%6d%10d\n", (i+1), reserved, open, reserved*COST);
                totalReserved += reserved; totalOpen += open;
            
            }
            System.out.printf("         Total %10d%6d%10d\n", totalReserved, totalOpen, totalReserved*COST);
    }
    
}
