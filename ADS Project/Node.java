
enum Color{
    RED,
    BLACK
}
public class Node {
    int buildingNum;
    int executed_time;
    int total_time;
    Node left,right,parent;
    Color color;
    Node(int number,int totalTime){
        buildingNum = number;
        executed_time = 0;
        total_time = totalTime;
        left = null;
        right= null;
        parent = null;
        color = Color.RED;
    }
}
