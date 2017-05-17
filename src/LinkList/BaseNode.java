//Manan Manan mxm163530
package LinkList;

public abstract class BaseNode {
    
    //Member Variables
    protected int row;
    protected int seat;
    
    //Overloaded Constructor
    public BaseNode (int row, int seat){
        this.row = row;
        this.seat = seat;
    }
    
    public boolean equals (BaseNode n){ //not overriden method
        return (this.row == n.row) && (this.seat == n.seat);
    }
    
    public String toString(){
        return "[" + row + ", " + seat + ']';
    }
    
    public int compareTo(BaseNode b){
        if (this.row < b.row || (this.row == b.row && this.seat < b.seat)) return -1; //if this node is "smaller"
        else if (this.equals(b)) return 0; //if the nodes are equal
        else return 1; //else if this node is bigger
    }
    
    //Accessors and Mutators
    public int getRow() {
        return row;
    }

    public int getSeat() {
        return seat;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
    
    
    
}
