
import java.awt.Event;
import java.util.ArrayList;

/**
 *
 * @author pratik
 */
public class MinHeap {
    public class MinheapNode{
        int executed_time;
        Node node = null;
        MinheapNode(int executed_time,Node node_pointer){
            this.executed_time = executed_time; 
           this.node = node_pointer;
        }
    }

    //ArrayList of the MinHeapNodes
    ArrayList<MinheapNode> arr = new ArrayList<MinheapNode>();
    
    public MinheapNode removeMin(){
        //If there is no building to work on return null
        if(arr.size()>0){
            //Store the first node of the Arraylist into an object and then remove it
            MinheapNode building = new MinheapNode(arr.get(0).executed_time, arr.get(0).node);
            //Swap the last element and the first element from arraylist and then remove last element.
            swap(0, arr.size()-1);
            arr.remove(arr.size()-1);
            for(int parent=0; parent<arr.size()/2;){
                int min_child_index;
                //If both the child are present find the one with minmum execution time.
                if(left_child(parent) < arr.size() && right_child(parent) < arr.size()){
                    //if the execution time of both child are same then find one with lower building number
                    if(arr.get(left_child(parent)).executed_time == arr.get(right_child(parent)).executed_time){
                        if(arr.get(left_child(parent)).node.buildingNum < arr.get(right_child(parent)).node.buildingNum){
                           min_child_index = left_child(parent);
                        }
                        else{
                            min_child_index = right_child(parent);
                        }
                    }else{
                        min_child_index = arr.get(left_child(parent)).executed_time < arr.get(right_child(parent)).executed_time ? left_child(parent) : right_child(parent);   
                    }
                } 
                //If right child of parent is missing then check with only left child
                else if(left_child(parent) < arr.size() && right_child(parent) >= arr.size()){
                    min_child_index = left_child(parent);
                }else{
                    min_child_index = right_child(parent);
                }

                //If the execution time of parent is greater than minimum of the child, then swap parent and min child
                if(arr.get(parent).executed_time > arr.get(min_child_index).executed_time){
                    swap(parent,min_child_index);
                }
                //If the execution time of parent is same as that of minimum of the child, then swap if building number of parent is greater than of its child
                else if(arr.get(parent).executed_time == arr.get(min_child_index).executed_time){
                    if(arr.get(parent).node.buildingNum > arr.get(min_child_index).node.buildingNum){
                        swap(parent,min_child_index);
                    }
                }else{
                    break;
                }
                parent = min_child_index;
            }
            return building;
        }
        return null;
    }
    
    //Return the index of the left child of the parent
    public int left_child(int parent){
        return (parent*2)+1;
    }
    
    //Return the index of the right child of the parent
    public int right_child(int parent){
        return (parent*2)+2;
    }
    
    //Swap the data between parent and child
    private void swap(int parent, int child){
        int temp_executed_time = arr.get(parent).executed_time;
        Node temp_node = arr.get(parent).node;
        arr.get(parent).executed_time = arr.get(child).executed_time;
        arr.get(parent).node = arr.get(child).node;
        arr.get(child).executed_time = temp_executed_time;
        arr.get(child).node = temp_node;
    }
   
    //Perform heapify operation on the Min Heap
    private void heapify(){
        for(int child_index=arr.size()-1;child_index>0;child_index=(child_index-1)/2){
            int parent_index = (child_index-1)/2;
            if(arr.get(child_index).executed_time < arr.get(parent_index).executed_time){
                swap(parent_index,child_index);
            }
            else if(arr.get(child_index).executed_time == arr.get(parent_index).executed_time){
                if(arr.get(child_index).node.buildingNum < arr.get(parent_index).node.buildingNum){
                    swap(parent_index,child_index);
                }
            }
            else{
                break;
            }
        }   
    }
    
    //Perform Insertion operation for new node
    public void insert(int executed_time,Node node_pointer){
        MinheapNode new_node = new MinheapNode(executed_time,node_pointer);
        //Insert the new node at the end of the arraylist.
        arr.add(new_node);
        //perform heapify operation
        heapify();
    }
    
    //The Work funciton increases the executed time on the CurrentBuilding.
    public MinheapNode work(MinheapNode CurrentWorkingBuilding){
        //If CurrentBuilding is null then get the building with least executed time using the RemoveMin() operation
        if(CurrentWorkingBuilding == null){
           CurrentWorkingBuilding = removeMin();
        }
        //Increase the execution time in both Minheap object and the RedBlack Tree node.
        if(CurrentWorkingBuilding!=null){
            CurrentWorkingBuilding.executed_time++;
            CurrentWorkingBuilding.node.executed_time++;
        }
        //Return the MinHeapNode object on which the current work is going on
        return CurrentWorkingBuilding;
    }
    
    //Testing purpose
    public void printMinHeap(){
        for(int i=0;i<arr.size();i++){
            System.out.print(arr.get(i).node.buildingNum + " - ");
        }
        System.out.println("");
    }
    
    
}
