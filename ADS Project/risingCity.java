
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class risingCity{
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filename = args[0];
        RedBlackTree rbt = new RedBlackTree();
        MinHeap mh =new MinHeap();
        MinHeap.MinheapNode currentWorkingBuilding = null;
        //Initializing reader and writer object
        BufferedWriter writer = new BufferedWriter(new FileWriter("output_file.txt"));
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        //Store the first line of the input file
        String nextOperation;
        nextOperation = reader.readLine();

        //daysWorkedOnCurrentBuilding stores number of days worked on the current building.
        int daysWorkedOnCurrentBuilding = 0;

        //Start the globalTimer
        int globalTimer = 0; 

        while(true){
            //Start work on current building. if current building is null perform no action.
            currentWorkingBuilding = mh.work(currentWorkingBuilding);
            if(currentWorkingBuilding!=null){
                daysWorkedOnCurrentBuilding++;
            }

            //Check of the global counter matches the time of the next command. If yes execute the command.
            if(nextOperation!=null && globalTimer == Integer.parseInt(nextOperation.split(":")[0])){
                String command = nextOperation.split(":")[1];
                //If Command contains Insert then insert the building in both RBT and Min-heap.
                if(command.contains("Insert")){
                    String input = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                    Node node_pointer = rbt.insert(Integer.parseInt(input.split(",")[0]),Integer.parseInt(input.split(",")[1]));
                    mh.insert(node_pointer.executed_time, node_pointer);
                }
                //Print the details of the buidling that lies in the given range 
                else if(command.contains("PrintBuilding") && command.contains(",")){
                    String input = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                    rbt.PrintBuilding(Integer.parseInt(input.split(",")[0]),Integer.parseInt(input.split(",")[1]),writer);
                }
                //Print the details of the building given.
                else if(command.contains("PrintBuilding")){
                    String input = command.substring(command.indexOf("(")+1, command.indexOf(")"));
                    rbt.PrintBuilding(Integer.parseInt(input),writer);
                }
                //Store the next command for further operations.
                nextOperation = reader.readLine();
                if(nextOperation == null){
                    reader.close();
                }
            }
            //On completion of the building delete it from building.
            if(currentWorkingBuilding!=null && currentWorkingBuilding.executed_time == currentWorkingBuilding.node.total_time){
                String output = "("+currentWorkingBuilding.node.buildingNum+","+globalTimer+")";
                writer.write(output);
                writer.append("\n");
                Boolean isEmpty = rbt.delete(currentWorkingBuilding.node.buildingNum);

                //Break the loop when all the commands are executed and there is no node in the RBT Data structure
                if(isEmpty && nextOperation==null){
                    break;
                }
                //Reinitialize currentBuilding and days worked to start working on next building.
                currentWorkingBuilding = null;
                daysWorkedOnCurrentBuilding = 0;
            }
            
            //When the number of days worked on current building is 5, then insert the building back into minheap.
            else if(daysWorkedOnCurrentBuilding == 5){
                //Reinsert into the MinHeap;
                mh.insert(currentWorkingBuilding.executed_time, currentWorkingBuilding.node);
                //Reinitialize currentBuilding and days worked to start working on next building.
                currentWorkingBuilding = null;
                daysWorkedOnCurrentBuilding = 0;
            }
            
            globalTimer++;
        }
        
        writer.close();
        
   }
    
}
