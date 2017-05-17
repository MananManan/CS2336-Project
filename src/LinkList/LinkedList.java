//Manan Manan mxm163530
package LinkList;

import java.io.PrintStream;

public class LinkedList {
    
    protected DoubleLinkNode head;
    protected DoubleLinkNode tail;
    protected int length;
    
    public LinkedList(){
        head = tail = null;
    }
    
    //Overloaded constructor
    public LinkedList(DoubleLinkNode head){//head of already existing linked list
        if (head == null) return;
        
        this.head = head;
        this.head.prev = null;//setting previous node to null, just in case
        
        int i;
        for(i = 1; head.next != null; head = head.next, i++); //head variable is being used as a variable to find tail and length
        
        this.tail = head; //the next node is always null
        
        length = i; 
        
    }

    //Methods
    
    
    public boolean isValidLinkedList(){
        if (head == tail && tail == null && length == 0) return true; //Check 1 Empty list
        
        if (head.prev != null || tail.next != null) return false; //Check 2, finite list
        
        int i = 0;
        for (DoubleLinkNode first = head; first != null; first = first.next, i++);
        int j = 0;
        for (DoubleLinkNode last = tail; last != null; last = last.prev, j++);
        if (i != j || j != length) return false; //check 3, all the connections work and in sync with the length
        return true; //if it passes all these checks
    }
    
    public LinkedList subList(int index, int num){
        //makes a new linked list consisting of num elements starting index. If it reaches out of bounds, then all the elements

        LinkedList newList = new LinkedList(null);
        
        DoubleLinkNode temp = get(index);
        int i = 0;
        for (i = 0; i < num && temp != null; i++, temp = temp.next){
            newList.add(temp.deepCopy(), newList.length); //add deep copy of node at the end
        }
        newList.length = i;
        return newList;
        
    }
    
    public void add(DoubleLinkNode n, int index){
        
    /*important : length is one moret*/ 
        
        if (head == null && tail == null){ //the list is empty
            tail = head = n;
            head.prev = tail.next = null;
        }
        
        else if (index == length){//if index is length or out of bounds, make this the tail, because the index of tail is new lenght length - 1
            n.prev = tail; //prev of n is current tail
            tail.next = n;//next of tail is n
            tail = n; //finally setting the tail referece to n
            tail.next = null; //previous of tail is always null
        }
        
        else if (index == 0){//if index is 0 or out of bounds, make this the head. The new node will be at 'index'
            n.next = head; //next of n is current head
            head.prev = n;//previous of head is n
            head = n; //finally setting the head referece to n
            head.prev = null; //previous of head is always null
            
        }
        
        else {//adding the node in the middle
            
            DoubleLinkNode temp = head;
            for(int i = 0; i < index - 1; i++, temp = temp.next);// move to the left of the spot where we want to add node
            
            n.next = temp.next;
            n.prev = temp;
            temp.next.prev = n;
            temp.next = n;
            
        }        
        
        length++; //since one node is being added, the length increased
    }
    
    public void addList(LinkedList n, int index){
        
        n = n.subList(0, n.length); //makes a copy of the list so that the data is not lost
        
        if (head == null && tail == null){
            head = n.head;
            tail = n.tail;
            head.prev = tail.next = null;
        }
        
        else if (index == length ){//if index is length or out of bounds, make this the tail, because the index of tail is new lenght length - 1
            n.head.prev = tail; //prev of n is current tail
            tail.next = n.head;//next of tail is n
            tail = n.tail; //finally setting the tail referece to n
            tail.next = null; //previous of tail is always null
        }
        
        else if (index == 0){//if index is 0 , make this the list.
             n.tail.next = head; //next of n is current head
             head.prev = n.tail;//previous of head is n
             head = n.head; //finally setting the head referece to n
            head.prev = null; //previous of head is always null
            
        }
        
        else {//adding the list in the middle
            
            DoubleLinkNode temp = head;
            for(int i = 0; i < index - 1; i++, temp = temp.next);// move to the left of the spot where we want to add list
            n.tail.next = temp.next;
            n.head.prev = temp;
            n.tail.next.prev = n.tail;
            temp.next = n.head;
        }        
        n.head = n.tail = null;
        
        length += n.length; //since length nodes are being added, the length increased
    }
    
    public void remove(int index){//removes a node at index
        
        if (index < 0 || index > length - 1) return;
   
          else if (length == 1 && index == 0) {
              head = tail = null;
          }
        else if (index == 0){//remove head
            head = head.next;
            if(head != null) head.prev = null;// garbage collection
        }
        
        else if (index == length - 1){//remove tail
            tail = tail.prev;
            if (tail != null) tail.next = null;// garbage collection
        }
        
        else{
            DoubleLinkNode temp = head;
            for(int i = 0; i < index; i++, temp = temp.next); //moved to the spot we want to remove
         
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;// garbage collection will delete this node
        }
        
        length--; //because a node has been removed
        
    }
    
    public void removeList (int index, int num){
        //removes num nodes starting index. If num is more than index + length, then removes everything starting index.
        
        if (index < 0 || index > length - 1) return;
   
        DoubleLinkNode first = get(index);
        
        if (first == head){// if first is head
            head = get(num); //move head forward num times (it starts from index). new head starts from index num
            if (head == null) {
               tail = null;
               num = length; //we removed length elements
            }
            else{
                head.prev = null; //set previous to null and let garbage collection do its job
            }
        }
        
        else{ //removal starts from somewhere in the middle
            if (index + num >= length){//if out of bounds
                tail = first.prev;
                tail.next = null;
                num = length - index;
            }

            else{
                first = first.prev;//making the bounds exclusive
                DoubleLinkNode second = get(index + num); //go one furthur than to make it exclusive
                first.next = second;
                first.next.prev = first; //connecting first and second
            }
        }
        
        length -= num; //because a node has been removed
        
    }
    
    public String toString(){
        String s = new String();
        for(DoubleLinkNode temp = head; temp != null; temp = temp.next){
            s += temp + "  ";
        }
        return s;
    }
    
    public boolean equals(LinkedList l){
        
        return this.head == l.head && this.tail == l.tail;
        
    }
    
    public DoubleLinkNode findNode(DoubleLinkNode a){//finds by reference
        int i = 0;
        for(DoubleLinkNode temp = head; temp != null; temp = temp.next, i++){
            if(temp.equals(a))
                return temp;
        }
        return null;
    }
    
    public int findIndex(DoubleLinkNode a){//finds by value
        
        int i = 0;  
        for(DoubleLinkNode temp = head; temp != null; temp = temp.next, i++){
            if(temp.equals(a)) 
                return i; 
        }
        return -1;
        
    }
    
    public DoubleLinkNode get(int index){
        
        if (index < 0 || index > length - 1) return null;
        
        DoubleLinkNode temp = head;       
        for(int i = 0; i < index; temp = temp.next, i++);
 
        return temp;
    }
    
    public void swap(DoubleLinkNode first, DoubleLinkNode second){
        //NOTE: This does not swap the references, which need to be swapped outside the method
        if (first == second || first == null || second == null) return; //if the nodes are the same or any one of them is null
        
        else if (first.next == second){//if consecutive
            //swapping the previous
            second.prev = first.prev;//second.prev now might contain null
            if (second.prev != null) second.prev.next = second; //if first is head, then we are dead
            else head = second;
            
            //swapping the next
            first.next = second.next; //first.next now might contain null
            if (first.next != null) first.next.prev = first; //if second is tail, then this will fail
            else tail = first;
            
            second.next = first;
            first.prev = second;
              
        }
        
        else {
            DoubleLinkNode temp;
            //swapping the previous
            temp = first.prev;
            first.prev = second.prev;
            second.prev = temp; //second.prev now might contain null
            first.prev.next = first;
            if (second.prev != null) second.prev.next = second; //check for above
            
            //swapping the next
            temp = first.next;
            first.next = second.next; //first.next might contain null
            second.next = temp;
            second.next.prev = second;
            if (first.next != null) first.next.prev = first;
            
        }
    }
    
    public void swap(int i, int j){//
        
        if (i < 0 || i > length - 1 || j < 0 || j >= length - 1) return;
        
        DoubleLinkNode first = get(i);
        DoubleLinkNode second = get (j);
        
        swap(first, second);
    }
    
    public void sort(){
        
        boolean flag;
        
        do{
            flag = false;
            
            for(DoubleLinkNode ref = head; ref.next != null; ref = ref.next){

                if (ref.compareTo(ref.next) > 0) {//our node is bigger than the next
                    swap(ref, ref.next); 
                    //swapping the references
                    ref = ref.prev;
                    flag = true;
                }    
            }
            
        }while(flag);
        
    }
    
    //Accessors and Mutators
    public DoubleLinkNode getHead() {
        return head;
    }

    public DoubleLinkNode getTail() {
        return tail;
    }
    
    public int length() {//this is a getter but is names length() for consistency
       return length;
    }

    public void setHead(DoubleLinkNode head) {
        this.head = head;
    }

    public void setTail(DoubleLinkNode tail) {
        this.tail = tail;
    }
    
    public void setLength() {
        int i;
        for(i = 1; head.next != null; head = head.next, i++);
        length = i;
    }
    
}
