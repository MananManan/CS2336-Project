//Manan Manan mxm163530
package LinkList;

public class DoubleLinkNode extends BaseNode {

    //Member Variables
    protected DoubleLinkNode next;
    protected DoubleLinkNode prev;
    
    //Overloaded Constructor
    
    public DoubleLinkNode(int row, int seat){
        this(row,seat,null,null);
    }
    
    public DoubleLinkNode(int row, int seat, DoubleLinkNode next, DoubleLinkNode prev){
        super(row, seat);
        this.next = next;
        this.prev = prev;
    }
    
    //Methods
    
    public DoubleLinkNode deepCopy (){ //this copies the contents. The next, previous pointers are null
        return new DoubleLinkNode(this.row, this.seat);
    }
    
    //Accessors and Mutators
    public DoubleLinkNode getNext() {
        return next;
    }

    public DoubleLinkNode getPrev() {
        return prev;
    }

    public void setNext(DoubleLinkNode next) {
        this.next = next;
    }

    public void setPrev(DoubleLinkNode prev) {
        this.prev = prev;
    }
    
}
