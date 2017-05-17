//mxm163530 - Manan Manan - CS2336.002 - Jason Smith //Project 5

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    
    final public static double ADULT = 10; //contant vairable for price of a seat
    final public static double SENIOR = 7.5; //contant vairable for price of a seat
    final public static double CHILD = 5.25; //contant vairable for price of a seat
    
    public static double cost(int type){
            switch(type){
                case 0: return ADULT;
                case 1: return SENIOR;
                case 2: return CHILD;
                default : return 0;
            }
        }
    public static int integerInput(int lower, int upper, String prompt){
        Scanner input = new Scanner(System.in);
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

                while (i > upper || i < lower){
                System.out.print("Out of bounds Input. " + prompt);
                continue outerloop;
                }

                break; //if reaches this part, then eveyrthing is correct
        } while (true);
        
        
        return i;
                
    }
    
    public static boolean checkSeats (char [] array, int seat, int numberOfSeats){
        
        if (seat + numberOfSeats - 1 > array.length ||
            seat <= 0 
            ) return false; //check 1: If the seats are out of bounds, return false
        
        for (int i = (seat -1) ; i < seat + numberOfSeats - 1; i++){
            
            if (array[i] == '.') return false; //check 2: if any of the seats are not available 
            
        }
        
        return true; //if the method passes all the checks, then the seats are available
        
    }
    
    public static boolean reserveSeats (char [][] auditorium, Order order){
        
        PrintWriter output = new PrintWriter(System.out);
        showArray(output, auditorium, true);
        System.out.println();
        
        int totalRow = auditorium.length;
        int totalColumn = auditorium[0].length;
        
        int adult = integerInput(0, Integer.MAX_VALUE, "Number of Adult seats : ");
        int senior = integerInput(0, Integer.MAX_VALUE, "Number of Senior seats : ");
        int child = integerInput(0, Integer.MAX_VALUE, "Number of Child seats : ");
        
        ArrayList<Seat> seats = new ArrayList<>();
        
        for(int i = 0; i < adult; i++){
            
            int row = integerInput(1, totalRow, "Adult seat " + (i+1) + " row : " );
            int column = integerInput(1, totalColumn, "Adult seat " + (i+1) + " column : " );
            seats.add(new Seat(row, column, 0));
        }
        
        
        for(int i = 0; i < senior; i++){
            
            int row = integerInput(1, totalRow, "Senior seat " + (i+1) + " row : " );
            int column = integerInput(1, totalColumn, "Senior seat " + (i+1) + " column : " );
            seats.add(new Seat(row, column, 1));
        }
        
        for(int i = 0; i < child; i++){
            
            int row = integerInput(1, totalRow, "Child seat " + (i+1) + " row : " );
            int column = integerInput(1, totalColumn, "Child seat " + (i+1) + " column : " );
            seats.add(new Seat(row, column, 2));
        }
        
        int number = adult + senior + child;
        if(number == 0){
            System.out.println("No seats were reserved.");
            return false;
        }
        
        boolean available = true;
        for (Seat s : seats){//checking the seats
            if(auditorium[s.row-1][s.column-1] == '.'){
                available = false;
                break;
            }
        }
        
        if(available){
            order.seats.addAll(seats);
            for(Seat a : seats){
                auditorium[a.row-1][a.column-1] = '.'; //reserve seats
            }
            System.out.println("Your seats were reserved");
            return true;
        }
        else{//find best available seats
                System.out.println("The seats are not available.");
                Seat best = findBestSeats(auditorium, number);

                if (best == null) {
                    System.out.println("No best seats found.");
                }
                else{
                    if(number == 1) System.out.println("The best seat is seat " + best.column + " in row " + best.row + '.');
                    else System.out.println("The best seats are seats " + best.column + " to " + (best.column + number - 1) + " in row " + best.row + '.');
                String c;
                do{Scanner input = new Scanner(System.in);
                    System.out.print("Do you want to reserve them (Y/N) : "); //validating input
                    c = input.nextLine();
                }while(!(c.equals("Y") || c.equals("N")));

                if (c.equals("Y")){
                       seats = new ArrayList<>();
                    for(int i = 0; i < adult; i++)
                       seats.add(new Seat(best.row, best.column + i, 0));
                    for(int i = 0; i < senior; i++)
                        seats.add(new Seat(best.row, best.column + adult + i, 1));
                    for(int i = 0; i < child; i++)
                        seats.add(new Seat(best.row, best.column + adult + senior + i, 2));

                    order.seats.addAll(seats);
                    for(Seat a : order.seats){
                        auditorium[a.row-1][a.column-1] = '.'; //reserve seats
                    }

                    System.out.println("Your seats were reserved!");
                    return true;
                }
            }
            System.out.println("No seats were reserved.");
            return false;
        }
    }
    
    public static Seat findBestSeats(char[][] auditorium, int number){
        //returns the first Seat of best possible, and null if none exist
        int totalRow = auditorium.length;
        int totalColumn = auditorium[0].length;
        Seat best = null;
            double min = Integer.MAX_VALUE;
            double minRow = Integer.MAX_VALUE;
            
            for(int row = 0; row < totalRow; row++){//row index
               //column index
                for(int column = 0; column < (totalColumn - number + 1); column++){//if number is two, then don't check the last seat
                    
                    if (checkSeats(auditorium[row], column+1, number)){//if the consecutive seats are available
                        double distanceSq = //square of the distance between the middle of auditorium. square is chosen because it is easier to compare
                           Math.pow((row+1) - (totalRow + 1)/2.0, 2) + Math.pow( (column+1) + (number-1)/2 - (totalColumn + 1)/2, 2);
                           //the addition of (numberOfSeats -1)/2 makes the distance from the middle of the seats

                        if(distanceSq < min) { //if new min distance is found
                            best = new Seat(row+1, column+1, 0);
                            min = distanceSq;
                            minRow = Math.abs((row+1) - (totalRow + 1)/2.0); //row distance is now this 
                        } //if distance is smaller, remember it      
                        
                        else if (distanceSq == min) {//if there is a tie check for row
                            double temp = Math.abs((row+1) - (totalRow + 1)/2.0); //temp is the distance between row and middle of the auditorium
                            if ( temp < minRow) //if this's row is less than best's row
                                best = new Seat(row+1, column+1, 0);
                                minRow = temp;
                        }
                    }
                }
            
            }
            
            return best;//does NOT HAVE AUDITORIUM
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
    
    public static void showArray (PrintWriter output, char[][] array, boolean header){
        
        if(header) {
            output.print("  "); //2 extra spaces
            for(int i = 0; i < array[0].length; i++)
                output.printf("%d", (i+1)%10); //printing column numbers
            output.println();
        }
        
        for (int i = 0; i < array.length-1; i++){
            if(header) output.printf("%-2d", i+1);
                      for (char c : array[i]){
                          output.print(c);
                      }
                      output.println();
        }
        //for the last line
        if(header) output.printf("%-2d", array.length);
                      for (char c : array[array.length-1]){
                          output.print(c);
                      }
        
        
        output.flush();
    }
    
    public static void standardUser(char[][][] auditoriums, ArrayList<Order> orders){
        OUTER:
        while (true) {
            System.out.println("1. Reserve Seats \n2. View Orders \n3. Update Order\n4. Display Receipt\n5. Log Out");
            int option = integerInput(1, 5, "Choose Function : ");
            switch (option) {
                case 1: 
                    //reserve seats
                    System.out.println("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3");
                    int auditorium = integerInput(1, 3, "Choose Auditorium :");
                    System.out.println();
                    Order order = new Order(); // create a new order
                    order.auditorium = auditorium;
                    if(reserveSeats(auditoriums[auditorium - 1], order)) orders.add(order);
                    break;
                case 2:
                    {
                        if(orders.size() == 0){
                            System.out.println("No Orders Available.");
                            break;
                        }
                        //view order
                        int i = 0;  for(Order temp : orders){
                            System.out.println((i+1) + "." + temp.toViewOrders());
                            i++;
                        }           break;
                    }
                case 3:
                    {
                        if(orders.size() == 0){
                            System.out.println("No Orders Available.");
                            break;
                        }
                        
                        //Update Order
                        int i = 0;  for(Order temp : orders){
                            System.out.println((i+1) + "." + temp.toViewOrders());
                            i++;
                        }           i = integerInput(1, orders.size(), "Select an order : " );
                        Order temp = orders.get(--i); //current order, //i is now the index of the order
                        System.out.println("1. Add Tickers to Order \n2. Delete Tickets from Order\n3. Cancel Order");
                        option = integerInput(1, 3, "Select an option : " );
                switch (option) {//update order options
                    case 1:
                        //add tickets to order
                        reserveSeats(auditoriums[temp.auditorium - 1], temp);
                        break;
                    case 2:
                        //delete tickets from order
                        while(true){
                            int b = 1;
                            for(Seat s : temp.seats){
                                System.out.println(b + "." + s.toString() + s.toType());
                                b++;
                            }
                            System.out.println(b + "." + "Exit." );
                            option = integerInput(1, b, "Select an option : " );
                            if (option == b) break; //if the option is exit
                            else{

                                auditoriums[temp.auditorium-1][temp.seats.get(option-1).row - 1][temp.seats.get(option-1).column - 1] = '#';
                                temp.seats.remove(option-1);
                                System.out.println("Your seat has been deleted.");
                                if(temp.seats.size() == 0) {
                                    orders.remove(i);
                                    break;
                                }
                            }
                        }
                       break;
                    case 3:
                        //cancel order
                        for(Seat s : temp.seats){
                           auditoriums[temp.auditorium-1][s.row-1][s.column-1] = '#';
                        }
                        orders.remove(i);
                        System.out.println("Your order has been cancelled.");
                        break;
                    default:
                        break;
                }
break;
                    }
                case 4: //display receipt
                    
                    double total = 0;
                    if(orders.size() != 0) System.out.println("    Auditorium Adult Senior Child Amount($) Seats");
                    int i = 1;
                    for(Order o : orders){
                        int type[] = o.numberOfType();
                        double amount = o.getAmount();
                        System.out.printf("%3s %-10s %5d %6d %5d %7.2f   ", i + ".", "     " + o.auditorium, type[0], type[1], type[2], amount);
                        System.out.println( o.seats);
                        total += amount;
                        i++;
                    }         
                    System.out.printf("Total Amount : $%.2f\n", total);
                    break;
                case 5:
                    break OUTER; //if log out, then break
                default:
                    break;
            }
        }
    }
    
    public static void admin(char[][][] auditoriums, HashMap<String, User> map) throws FileNotFoundException{
        OUTER:
        while (true) {
            System.out.println("1. View Auditoriums \n2. Print Report\n3. Exit");
            Scanner input = new Scanner(System.in);
            int option = integerInput(1, 3, "Choose Function : ");
            System.out.println();
            switch (option) {
                case 1: 
                {
                    System.out.println("1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3");
                    int auditorium = integerInput(1, 3, "Choose Auditorium :");
                    System.out.println();
                    PrintWriter output = new PrintWriter(System.out);
                    showArray(output, auditoriums[auditorium-1], true);
                    System.out.println();
                    break;
                }
                case 2:
                {   
                    int current[][] = new int[3][3]; //first is for auditorium, second is for type in current
                    
                    for (User user : map.values()){//for each user in the hashmap
                        for (Order order : user.orders){ //for each order in the order array in user
                            for (Seat seat : order.seats){ //for each seat in order
                                current[order.auditorium-1][seat.type]++;
                            }
                        }
                    }
                    
                    
                    System.out.println("   Labels    Open Rsvd Adult Senior Child Sales($)");
                        int a = 0, totalOpen = 0, totalReserved = 0, totalAdult = 0, totalSenior = 0, totalChild = 0;
                        for( char[][] auditorium : auditoriums){ //for each auditorium
                            int open = 0, reserved = 0;
                            for(char[] row : auditorium){
                                for(char seat : row){
                                    if (seat == '.') reserved++;
                                    else open++;
                                }
                            }
                            totalOpen += open; totalReserved += reserved; totalAdult += current[a][0]; totalSenior += current[a][1]; totalChild += current[a][2];
                            System.out.printf("%12s ", "Auditorium " +(a+1));
                            System.out.printf("%4d %4d ", open, reserved);
                            System.out.printf("%5d %6d %5d   ", current[a][0], current[a][1], current[a][2]);
                            System.out.printf("%5.2f\n", current[a][0]*ADULT + current[a][1]*SENIOR + current[a][2]*CHILD);
                          
                            a++;
                        }
                        
                        System.out.printf("%12s ", "TOTAL");
                            System.out.printf("%4d %4d ", totalOpen, totalReserved);
                            System.out.printf("%5d %6d %5d   ", totalAdult, totalSenior, totalChild);
                            System.out.printf("%5.2f\n", totalAdult*ADULT + totalSenior*SENIOR + totalChild*CHILD);
                    
                    break;
                }
                
                case 3:
                {
                    int i = 1;
                    for(char [][] auditorium : auditoriums)
                    {
                       PrintWriter output = new PrintWriter ("A" + i + ".txt"); 
                        showArray(output, auditorium, false);
                        output.close();  
                        i++;
                   }
                   return;
                }
            }
        }
    }
    
    public static void main (String[] args) throws FileNotFoundException {
    try{    
        /*The main function is divied into 3 steps:
            1. Preparing the arrays for all 3 Auditoriums (A1, A2, A3)
            2. Creating the UIs
            3. Creating the menu
        */
        
        //***************READING FILES***************
        if (!(new File("userdb.dat")).exists()){
            return;
        }
        
        Scanner input = new Scanner(new File("userdb.dat"));
        HashMap <String, User> map = new HashMap<>();
        
        while(input.hasNext()){
            String username = input.next();
            map.put(username, new User(username, input.next()));
        }
        
        String[] files = {"A1.txt", "A2.txt", "A3.txt"} ;
        
        char [][][] auditoriums = 
                    {prepareArray(files[0]), prepareArray(files[1]), prepareArray(files[2])}; //temprory array *reference* for menu
        
        input = new Scanner(System.in);
        User current; //current user   
startPoint:while(true){//starting point
            System.out.print("Enter Username : ");
            String xyz = input.nextLine(); //stores the username
            current = map.get(xyz); //gets the value from map
            if (current == null){ 
                System.out.print("Username not found.");
                continue startPoint; //go to startpoint
            }
            //username is valid now i.e. current is not null
            int count = 0;
            do{
                if (count == 3) {
                    System.out.println("Incorrect Password 3 time. Returning to Starting Point.");
                    continue startPoint;
                }//if this is the fourth time
                System.out.print("Enter Password : ");
                xyz = input.nextLine();
                count++;
            }while(!(xyz.equals(current.password)));

            if (current.username.equals("admin")){
                admin(auditoriums, map);
                return; //ends the program after admin is done
            }
            else{
                standardUser(auditoriums, current.orders);
                continue startPoint;
            }
        }
    }catch(Exception ex){//if any exception 
        main(args);
    }
    
  }
      //three classes are used here as containers
    public static class Seat{
        //These classes are mainly used as a container, so no separate source file is needed
        //NESTED CLASS ARE VALID IN THIS CASE. SEE SECTION 15.4 OF THE BOOK
        int row = 0, column = 0, type = 0; //row, column are NOT INDEXES
        @Override
        public String toString(){
            String temp = "" + row + '-' + column;
            return temp;
        }
        
        public String toType(){
            String temp = "";
            switch(type){
                case 0 : temp+= " Adult"; break;
                case 1 : temp += " Senior"; break;
                case 2 : temp += " Child"; break;
            }
            return temp;
        }
        
        Seat(int row, int column, int type){
            this.row = row;
            this.column = column;
            this.type = type;
        }
        
    }
    
    public static class User{
        //These classes are mainly used as a container, so no separate source file is needed        
        ////NESTED CLASS ARE VALID IN THIS CASE. SEE SECTION 15.4 OF THE BOOK
        String username;
        String password;
        ArrayList<Order> orders = new ArrayList<>();
        User(String username, String password){
            this.username = username;
            this.password = password;
        }
    }
    
    public static class Order{
        //These classes are mainly used as a container, so no separate source file is needed        
        ////NESTED CLASS ARE VALID IN THIS CASE. SEE SECTION 15.4 OF THE BOOK
        ArrayList<Seat> seats = new ArrayList<>();
        int auditorium = 0;
  
        double getAmount(){
            double amount = 0;
            for(Seat s : seats){
                amount += cost(s.type);
            }
            return amount;
        }
        
        int[] numberOfType(){
            
            int [] numberOfType = {0,0,0};
            for(Seat s : seats){
                numberOfType[s.type]++;
            }
            return numberOfType;
        }
        
        public String toViewOrders(){
            int temp[] = numberOfType();
            String string = "";
            string += "Auditorium: " + auditorium + ", Adult :" + temp[0] + ", Senior :" + temp[1] + ", Child :" + temp[2] + "\n  Seats : " + seats;
            return string;
        }
        
    }
    
}

