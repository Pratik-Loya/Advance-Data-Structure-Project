
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class RedBlackTree {
    Node rbt_head = null;
    boolean isBuildingInBetween = false;
    String output = "";

    //If the root color of RBT is Red then flip it to Black.
    public void checkRootNodeColor(){
        if(rbt_head.color == Color.RED){
            rbt_head.color = Color.BLACK;
        }
    }
    
    //Perform Left Rotation on the node passed
    public void leftRotate(Node node){
        node.parent.right = node.left;
        if(node.left!=null){
            node.left.parent = node.parent;
        }
        node.left = node.parent;
        if(node.parent.parent!=null){
            if(node.parent.parent.right == node.parent){
                node.parent.parent.right = node;
            }else{
                node.parent.parent.left = node;
            }
        }
        node.parent = node.parent.parent;
        node.left.parent = node;
        if(node.parent == null){
            rbt_head = node;
        }
    }
    
    //Perform right rotation on the node passed
    public void rightRotate(Node node){
        node.parent.left = node.right;
        if(node.right!=null){
            node.right.parent = node.parent;
        }
        node.right = node.parent;
        if(node.parent.parent!=null){
            if(node.parent.parent.left == node.parent){
                node.parent.parent.left = node;
            }else{
                node.parent.parent.right = node;
            }
        }
        node.parent = node.parent.parent;
        node.right.parent = node;
        if(node.parent == null){
            rbt_head = node;
        }
    }
    
    
    //Push the blackness down by changing the color of grandparent, parent and uncle
    public void flipColors(Node parent,Node grandParent,Node uncle){
        parent.color = Color.BLACK;
        grandParent.color = Color.RED;
        uncle.color = Color.BLACK;
    }
    
    //Check that after the insert if the new tree satisy the Red-Black Tree Property
    public void balanceTree(Node childNode){
        if(childNode.parent!=null && childNode.parent.parent!=null && childNode.color == Color.RED && childNode.parent.color == Color.RED){
            Node parent = childNode.parent;
            Node grandParent = childNode.parent.parent;
            Node uncle = childNode.parent.parent.left == childNode.parent? childNode.parent.parent.right : childNode.parent.parent.left;
            
            //If Uncle is Black then perform LL, LR, RL, RR rotation, depending upon the newly inserted node
            if(uncle == null || uncle.color == Color.BLACK){
                if(grandParent.left == parent && parent.left == childNode){
                    rightRotate(parent);
                    parent.color = Color.BLACK;
                    grandParent.color = Color.RED;
                }
                if(grandParent.left == parent && parent.right == childNode){
                    leftRotate(childNode);
                    rightRotate(childNode);
                    childNode.color = Color.BLACK;
                    grandParent.color = Color.RED;
                }
                if(grandParent.right == parent && parent.left == childNode){
                    rightRotate(childNode);
                    leftRotate(childNode);
                    childNode.color = Color.BLACK;
                    grandParent.color = Color.RED;
                }
                if(grandParent.right == parent && parent.right == childNode){
                    leftRotate(parent);
                    parent.color = Color.BLACK;
                    grandParent.color = Color.RED;
                }
            }
            //If Uncle is Red, then just perform the color switch of grandparent, parent and uncle and recheck if RBT property is satisfied
            else if(uncle.color == Color.RED){
                flipColors(parent,grandParent,uncle);
                balanceTree(grandParent);
            }
        }
        checkRootNodeColor();
    }
    
    //Get the node/parent whose child will be the new node
    public Node getParent(Node head,Node new_node){
        Node parent = head;
        while(head != null){
            parent = head;
            if(head.buildingNum == new_node.buildingNum){
                System.out.println("Building Already Exists. Exiting");
                System.exit(0);
            }
            else if(head.buildingNum > new_node.buildingNum){
                head = head.left;
            }
            else if(head.buildingNum < new_node.buildingNum){
                head = head.right;
            }
        }
        return parent;
    }
    
    //Insert the new building into the RBT
    public Node insert(int buildingNumber,int totalTime){
        Node newNode = new Node(buildingNumber,totalTime);
        if(rbt_head == null){
            rbt_head = newNode;
            checkRootNodeColor();
        }else{    
            Node pp = getParent(rbt_head,newNode);
            //if new node building number is less than of its parent attach it to left else to the right
            if(pp.buildingNum > newNode.buildingNum){
                pp.left = newNode;
                newNode.parent = pp;
            }else{
                pp.right = newNode;
                newNode.parent = pp;
            }
            //Check if the RBT property is maintained
            balanceTree(newNode);
        }
        return newNode;
    }
    
    //getNodePointer returns the pointer to the node whose building number is the building number to search
    public Node getNodePointer(int buildingNumber){
        Node head = rbt_head;
        while(head!=null && head.buildingNum != buildingNumber){
            head = head.buildingNum > buildingNumber? head.left : head.right;
        }
        
        return head;
    }
    
    //Returns the color of the node.
    public Color getColor(Node node){
        if(node == null || node.color == Color.BLACK){
            return Color.BLACK;
        }else{
            return Color.RED;
        }
    }
    
    //After deletion stabilize the RBT property by performing the operation
    public void stabilizeDeletion(Node node,Node parent){
        while(true){
            //Case 1: When child of deleted node is RED change it to black.
            if(getColor(node) == Color.RED){
                node.color = Color.BLACK;
                break;
            }
            //Case 2: When child of deleted node is BLACK. The Subtree is now black deficient
            //Also as the rightside is one black deficient means that the sibling will definitely exists
            else{
                //When node is deleted from right tree
                if(parent.right == node){
                    Node sibling = parent.left;
                    //Case 2.1: When the sibling of deleted node is black
                    if(sibling.color == Color.BLACK){
                        //Case Rb0: When both the children of sibling is black.
                        if(getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.BLACK){
                            //Case Rb0.1: When parent is Red. Change color of parent and sibling and DONE
                            if(parent.color == Color.RED){
                                parent.color = Color.BLACK;
                                sibling.color = Color.RED;
                            }
                            //Case Rb0.2: When Parent is Black. Change color of parent and sibling and CONTINUE
                            else{
                                sibling.color = Color.RED;
                                node = parent;
                                parent = parent.parent;
                                if(node==rbt_head){
                                    break;
                                }else{
                                    continue;   
                                }
                            }
                        }
                        //Case Rb1: When one children of sibling is red.
                        //Case Rb1.1: When left child of sibling is red. Perform LL rotation
                        else if(getColor(sibling.left) == Color.RED && getColor(sibling.right) == Color.BLACK){
                            sibling.left.color = Color.BLACK;
                            sibling.color = parent.color;
                            parent.color = Color.BLACK;
                            rightRotate(sibling);
                        }
                        //Case Rb1.2: When right child of sibling is red. Perform LR rotation
                        else if(getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.RED){
                            Node siblingRightChild = sibling.right;
                            siblingRightChild.color = parent.color;
                            parent.color = Color.BLACK;
                            leftRotate(siblingRightChild);
                            rightRotate(siblingRightChild);
                        }
                        //Case Rb2: When Both child of sibling is RED. Perform LR rotation
                        else if(getColor(sibling.left) == Color.RED && getColor(sibling.right) == Color.RED){
                            Node siblingRightChild = sibling.right;
                            siblingRightChild.color = parent.color;
                            parent.color = Color.BLACK;
                            leftRotate(siblingRightChild);
                            rightRotate(siblingRightChild);
                        }
                        break;
                    }
                    //Case 2.2: When the sibling is RED
                    else{
                        //Case Rr0: Here parent will be Black. Perform LL rotation and DONE
                        Node siblingRightChild = sibling.right;
                        if(getColor(siblingRightChild.left) == Color.BLACK && getColor(siblingRightChild.right) == Color.BLACK){
                            //Handling the case when the siblings right child is null.
                            if(siblingRightChild != null)
                                siblingRightChild.color = Color.RED;
                            sibling.color = Color.BLACK;
                            rightRotate(sibling);
                        }
                        //Case Rr1: Here parent will be Black. One child of sibling is RED
                        //Case Rr1.1: When left child of right child of sibling is RED. Perform LR rotation and DONE
                        else if(getColor(siblingRightChild.left) == Color.RED && getColor(siblingRightChild.right) == Color.BLACK){
                            siblingRightChild.left.color = Color.BLACK;
                            leftRotate(siblingRightChild);
                            rightRotate(siblingRightChild);
                        }
                        //Case Rr1.2: When right child of right child of sibling is RED. Perform LLR rotation and DONE
                        else if(getColor(siblingRightChild.left) == Color.BLACK && getColor(siblingRightChild.right) == Color.RED){
                            Node siblingRightRightChild = siblingRightChild.right;
                            siblingRightRightChild.color = Color.BLACK;
                            leftRotate(siblingRightRightChild);
                            leftRotate(siblingRightRightChild);
                            rightRotate(siblingRightRightChild);
                        }
                        //Case Rr2: When both children of right child of sibling is Red. Perform LLR Rotation
                        else if(getColor(siblingRightChild.left) == Color.RED && getColor(siblingRightChild.right) == Color.RED){
                            Node siblingRightRightChild = siblingRightChild.right;
                            siblingRightRightChild.color = Color.BLACK;
                            leftRotate(siblingRightRightChild);
                            leftRotate(siblingRightRightChild);
                            rightRotate(siblingRightRightChild);
                        }
                        break;
                    }
                }
                //When node is deleted from left tree.
                else{
                    Node sibling = parent.right;
                    //Case 2.1: When the sibling of deleted node is black
                    if(sibling.color == Color.BLACK){
                        //Case Lb0: When both the children of sibling is black.
                        if(getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.BLACK){
                            //Case Lb0.1: When parent is Red. Change color of parent and sibling and DONE
                            if(parent.color == Color.RED){
                                parent.color = Color.BLACK;
                                sibling.color = Color.RED;
                            }
                            //Case Lb0.2: When Parent is Black. Change color of parent and sibling and CONTINUE
                            else{
                                sibling.color = Color.RED;
                                node = parent;
                                parent = parent.parent;
                                if(node==rbt_head){
                                    break;
                                }else{
                                    continue;
                                }
                            }
                        }
                        //Case Lb1: When one children of sibling is red.
                        //Case Lb1.1: When right child of sibling is red. Perform RR rotation
                        else if(getColor(sibling.left) == Color.BLACK && getColor(sibling.right) == Color.RED){
                            sibling.right.color = Color.BLACK;
                            sibling.color = parent.color;
                            parent.color = Color.BLACK;
                            leftRotate(sibling);
                        }
                        //Case Lb1.2: When left child of sibling is red. Perform RL rotation
                        else if(getColor(sibling.left) == Color.RED && getColor(sibling.right) == Color.BLACK){
                            Node siblingLeftChild = sibling.left;
                            siblingLeftChild.color = parent.color;
                            parent.color = Color.BLACK;
                            rightRotate(siblingLeftChild);
                            leftRotate(siblingLeftChild);
                        }
                        //Case Lb2: When Both child of sibling is RED. Perform RL rotation
                        else if(getColor(sibling.left) == Color.RED && getColor(sibling.right) == Color.RED){
                            Node siblingLeftChild = sibling.left;
                            siblingLeftChild.color = parent.color;
                            parent.color = Color.BLACK;
                            rightRotate(siblingLeftChild);
                            leftRotate(siblingLeftChild);
                        }
                        break;
                    }
                    //Case 2.2: When the sibling is RED
                    else{
                        //Case Lr0: Here parent will be Black. Perform RR rotation and DONE
                        Node siblingLeftChild = sibling.left;
                        if(getColor(siblingLeftChild.left) == Color.BLACK && getColor(siblingLeftChild.right) == Color.BLACK){
                            //Handling the case when the siblings right child is null.
                            if(siblingLeftChild != null)
                                siblingLeftChild.color = Color.RED;
                            sibling.color = Color.BLACK;
                            leftRotate(sibling);
                        }
                        //Case Lr1: Here parent will be Black. One child of sibling is RED
                        //Case Lr1.1: When right child of left child of sibling is RED. Perform RL rotation and DONE
                        else if(getColor(siblingLeftChild.left) == Color.BLACK && getColor(siblingLeftChild.right) == Color.RED){
                            siblingLeftChild.right.color = Color.BLACK;
                            rightRotate(siblingLeftChild);
                            leftRotate(siblingLeftChild);
                        }
                        //Case Lr1.2: When left child of left child of sibling is RED. Perform RRL rotation and DONE
                        else if(getColor(siblingLeftChild.left) == Color.RED && getColor(siblingLeftChild.right) == Color.BLACK){
                            Node siblingLeftLeftChild = siblingLeftChild.left;
                            siblingLeftLeftChild.color = Color.BLACK;
                            rightRotate(siblingLeftLeftChild);
                            rightRotate(siblingLeftLeftChild);
                            leftRotate(siblingLeftLeftChild);
                        }
                        //Case Lr2: When both children of left child of sibling is Red. Perform RRL Rotation
                        else if(getColor(siblingLeftChild.left) == Color.RED && getColor(siblingLeftChild.right) == Color.RED){
                            Node siblingLeftLeftChild = siblingLeftChild.left;
                            siblingLeftLeftChild.color = Color.BLACK;
                            rightRotate(siblingLeftLeftChild);
                            rightRotate(siblingLeftLeftChild);
                            leftRotate(siblingLeftLeftChild);
                        }
                        break;
                    }
                }
            }
        }
    }
    
    //Remove the node that has the building number
    public Boolean delete(int buildingNumber){
        Node node = getNodePointer(buildingNumber);
        if(node == rbt_head && node.left==null && node.right==null){
            //All the nodes are deleted from RBT
            rbt_head = null;
            return true;
        }
        //When there are only two elements left and the node to be deleted is head
        else if(node==rbt_head && node.left==null){
            node.right.parent = null;
            rbt_head = node.right;
        }
        else if(node==rbt_head && node.right==null){
            node.left.parent = null;
            rbt_head = node.left;
        }
        else{
            Node n1 = new Node(buildingNumber,0);
            if(node.left!=null && node.right!=null){
                Node inorderPredecessor = node.left;
                while(inorderPredecessor.right!=null){
                    inorderPredecessor = inorderPredecessor.right;
                }
                node.buildingNum = inorderPredecessor.buildingNum;
                node.executed_time = inorderPredecessor.executed_time;
                node.total_time = inorderPredecessor.total_time;
                node = inorderPredecessor;
            }
            Node parent = node.parent;
            Node child = node.left!=null? node.left : node.right;
            Node sibling = parent.left == node? parent.right: parent.left;

            if(parent.left == node){
               //perform Left Deletion
               parent.left = child;
                if(parent.left!=null){
                    parent.left.parent = node.parent;
                }
                //If the deleted node is Black then the subtree containing the deleted node is one black deficient
                //Perform the stabilization on the deficient subtree.
                if(node.color==Color.BLACK){
                    stabilizeDeletion(parent.left,parent);
                }
            }else{
               //perform right Deletion
                parent.right = child;
                if(parent.right!=null){
                    parent.right.parent = node.parent;
                }
                //If the deleted node is Black then the subtree containing the deleted node is one black deficient
                //Perform the stabilization on the deficient subtree.
                if(node.color==Color.BLACK){
                    stabilizeDeletion(parent.right,parent);
                }
            }
        }
        checkRootNodeColor();
        return false;
    }
    
    //Testing Purpose
    //Print the value of the Nodes.
    public void print(Node node){
        System.out.print("Node: " + node.buildingNum + "| Color: " + node.color);
        System.out.print("| Parent: " + (node.parent!=null? Integer.toString(node.parent.buildingNum): "null"));
        System.out.print("| Left: " + (node.left!=null? Integer.toString(node.left.buildingNum): "null"));
        System.out.print("| Right: " + (node.right!=null? Integer.toString(node.right.buildingNum): "null"));
        System.out.println("");
    }
    
    //Perform Inorder Traversal on the RBT Tree
    public void inorder(Node head){
        print(head);
        if(head.left!=null){
            inorder(head.left);
        }
        if(head.right!=null){
            inorder(head.right);
        }
    }
    
    //Customised_inorder function is used to get building details in the given range
    public String customised_inorder(Node node, int building_number_1, int building_number_2){
        if(node==null){
            return "";
        }
        if(node.left!=null){
            customised_inorder(node.left,building_number_1,building_number_2);
        }
        if(node.buildingNum >= building_number_1 && node.buildingNum <= building_number_2 ){
            isBuildingInBetween = true;
            output += "("+node.buildingNum+","+node.executed_time+","+node.total_time+"),";
        }
        if(node.right!=null){
            customised_inorder(node.right,building_number_1,building_number_2);
        }
        return output;
    }
    
    //Print the details of the Node that has the buidling number
    public void PrintBuilding(int building_number, BufferedWriter writer) throws IOException{
        Node node = getNodePointer(building_number);
        //If no node found print 0,0,0
        if(node==null){
            writer.write("(0,0,0)\n");
        }else{
            writer.write("("+node.buildingNum+","+node.executed_time+","+node.total_time+")\n");
        }
    }
    
    //Print the details of the Node that whose buidling number is in the given range
    public void PrintBuilding(int building_number1, int building_number2,BufferedWriter writer) throws IOException{
        isBuildingInBetween = false;
        customised_inorder(rbt_head,building_number1, building_number2);
        //If no node found print 0,0,0
        if(!isBuildingInBetween){
            writer.write("(0,0,0)\n");
        }
        else{
            output = output.substring(0, output.length()-1);
            writer.write(output);
            writer.append("\n");
            output="";
        }
    }
}
