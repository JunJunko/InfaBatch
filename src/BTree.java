import java.util.Stack;  
  
/** 
 * ����������ʽ�洢 
 * @author WWX 
 */  
public class BTree {  
  
      
    private TreeNode root=null;  
      
    public BTree(){  
        root=new TreeNode(1,"rootNode(A)");  
    }  
      
    /** 
     * ����һ�ö����� 
     * <pre> 
     *           A 
     *     B          C 
     *  D     E            F 
     *  </pre> 
     * @param root 
     * @author WWX 
     */  
    public void createBinTree(TreeNode root){  
        TreeNode newNodeB = new TreeNode(2,"B");  
        TreeNode newNodeC = new TreeNode(3,"C");  
        TreeNode newNodeD = new TreeNode(4,"D");  
        TreeNode newNodeE = new TreeNode(5,"E");  
        TreeNode newNodeF = new TreeNode(6,"F");  
        root.leftChild=newNodeB;  
        root.rightChild=newNodeC;  
        root.leftChild.leftChild=newNodeD;  
        root.leftChild.rightChild=newNodeE;  
        root.rightChild.rightChild=newNodeF;  
    }  
      
      
    public boolean isEmpty(){  
        return root==null;  
    }  
  
    //���ĸ߶�  
    public int height(){  
        return height(root);  
    }  
      
    //�ڵ����  
    public int size(){  
        return size(root);  
    }  
      
      
    private int height(TreeNode subTree){  
        if(subTree==null)  
            return 0;//�ݹ�����������߶�Ϊ0  
        else{  
            int i=height(subTree.leftChild);  
            int j=height(subTree.rightChild);  
            return (i<j)?(j+1):(i+1);  
        }  
    }  
      
    private int size(TreeNode subTree){  
        if(subTree==null){  
            return 0;  
        }else{  
            return 1+size(subTree.leftChild)  
                    +size(subTree.rightChild);  
        }  
    }  
      
    //����˫�׽��  
    public TreeNode parent(TreeNode element){  
        return (root==null|| root==element)?null:parent(root, element);  
    }  
      
    public TreeNode parent(TreeNode subTree,TreeNode element){  
        if(subTree==null)  
            return null;  
        if(subTree.leftChild==element||subTree.rightChild==element)  
            //���ظ�����ַ  
            return subTree;  
        TreeNode p;  
        //�������������ң������������û���ҵ����ŵ�������ȥ��  
        if((p=parent(subTree.leftChild, element))!=null)  
            //�ݹ���������������  
            return p;  
        else  
            //�ݹ���������������  
            return parent(subTree.rightChild, element);  
    }  
      
    public TreeNode getLeftChildNode(TreeNode element){  
        return (element!=null)?element.leftChild:null;  
    }  
      
    public TreeNode getRightChildNode(TreeNode element){  
        return (element!=null)?element.rightChild:null;  
    }  
      
    public TreeNode getRoot(){  
        return root;  
    }  
      
    //���ͷ�ĳ�����ʱ���ý��������������Ѿ��ͷţ�  
    //����Ӧ�ò��ú���������������ĳ�����ʱ���ý��Ĵ洢�ռ��ͷ�  
    public void destroy(TreeNode subTree){  
        //ɾ����ΪsubTree������  
        if(subTree!=null){  
            //ɾ��������  
            destroy(subTree.leftChild);  
            //ɾ��������  
            destroy(subTree.rightChild);  
            //ɾ�������  
            subTree=null;  
        }  
    }  
      
    public void traverse(TreeNode subTree){  
        System.out.println("key:"+subTree.key+"--name:"+subTree.data);;  
        traverse(subTree.leftChild);  
        traverse(subTree.rightChild);  
    }  
      
    //ǰ�����  
    public void preOrder(TreeNode subTree){  
        if(subTree!=null){  
            visted(subTree);  
            preOrder(subTree.leftChild);  
            preOrder(subTree.rightChild);  
        }  
    }  
      
    //�������  
    public void inOrder(TreeNode subTree){  
        if(subTree!=null){  
            inOrder(subTree.leftChild);  
            visted(subTree);  
            inOrder(subTree.rightChild);  
        }  
    }  
      
    //��������  
    public void postOrder(TreeNode subTree) {  
        if (subTree != null) {  
            postOrder(subTree.leftChild);  
            postOrder(subTree.rightChild);  
            visted(subTree);  
        }  
    }  
      
    //ǰ������ķǵݹ�ʵ��  
    public void nonRecPreOrder(TreeNode p){  
        Stack<TreeNode> stack=new Stack<TreeNode>();  
        TreeNode node=p;  
        while(node!=null||stack.size()>0){  
            while(node!=null){  
                visted(node);  
                stack.push(node);  
                node=node.leftChild;  
            }  
           while(stack.size()>0){  
                node=stack.pop();  
                node=node.rightChild;  
            }   
        }  
    }  
      
    //��������ķǵݹ�ʵ��  
    public void nonRecInOrder(TreeNode p){  
        Stack<TreeNode> stack =new Stack<BTree.TreeNode>();  
        TreeNode node =p;  
        while(node!=null||stack.size()>0){  
            //����������  
            while(node!=null){  
                stack.push(node);  
                node=node.leftChild;  
            }  
            //ջ�ǿ�  
            if(stack.size()>0){  
                node=stack.pop();  
                visted(node);  
                node=node.rightChild;  
            }  
        }  
    }  
      
    //��������ķǵݹ�ʵ��  
    public void noRecPostOrder(TreeNode p){  
        Stack<TreeNode> stack=new Stack<BTree.TreeNode>();  
        TreeNode node =p;  
        while(p!=null){  
            //��������ջ  
            for(;p.leftChild!=null;p=p.leftChild){  
                stack.push(p);  
            }  
            //��ǰ��������������������Ѿ����  
            while(p!=null&&(p.rightChild==null||p.rightChild==node)){  
                visted(p);  
                //��¼��һ����������  
                node =p;  
                if(stack.empty())  
                    return;  
                p=stack.pop();  
            }  
            //����������  
            stack.push(p);  
            p=p.rightChild;  
        }  
    }  
    public void visted(TreeNode subTree){  
        subTree.isVisted=true;  
        System.out.println("key:"+subTree.key+"--name:"+subTree.data);;  
    }  
      
      
    /** 
     * �������Ľڵ����ݽṹ 
     * @author WWX 
     */  
    private class  TreeNode{  
        private int key=0;  
        private String data=null;  
        private boolean isVisted=false;  
        private TreeNode leftChild=null;  
        private TreeNode rightChild=null;  
          
        public TreeNode(){}  
          
        /** 
         * @param key  ������� 
         * @param data ������ 
         */  
        public TreeNode(int key,String data){  
            this.key=key;  
            this.data=data;  
            this.leftChild=null;  
            this.rightChild=null;  
        }  
  
  
    }  
      
      
    //����  
    public static void main(String[] args) {  
        BTree bt = new BTree();  
        bt.createBinTree(bt.root);  
        System.out.println("the size of the tree is " + bt.size());  
        System.out.println("the height of the tree is " + bt.height());  
          
        System.out.println("*******(ǰ�����)[ABDECF]����*****************");  
        bt.preOrder(bt.root);  
          
        System.out.println("*******(�������)[DBEACF]����*****************");  
        bt.inOrder(bt.root);  
         
        System.out.println("*******(�������)[DEBFCA]����*****************");  
        bt.postOrder(bt.root);  
          
        System.out.println("***�ǵݹ�ʵ��****(ǰ�����)[ABDECF]����*****************");  
        bt.nonRecPreOrder(bt.root);  
          
        System.out.println("***�ǵݹ�ʵ��****(�������)[DBEACF]����*****************");  
        bt.nonRecInOrder(bt.root);  
          
        System.out.println("***�ǵݹ�ʵ��****(�������)[DEBFCA]����*****************");  
        bt.noRecPostOrder(bt.root);  
    }  
} 