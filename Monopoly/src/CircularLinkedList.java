public class CircularLinkedList {
    public class Node{
        Property data;
        Node next;
        public Node(Property data){
            this.data = data;
        }
        public Node head = null;
        public Node tail = null;
        public void add(Property data){
            Node newNode = new Node(data);
            if(head == null){
                head = newNode;
                tail = newNode;
                newNode.next = head;
            }else{
                tail.next = newNode;
                tail = newNode;
                tail.next = head;
            }
        }
        public void displayList(){
            Node current = head;
            if(head == null){
                System.out.println("List is empty");
            }else{
                while(current != head){
                    System.out.print(current.head + " ");
                    current = current.next;
                }
                System.out.print("\n");
            }
        }
    }
}
