package braille;

import java.util.ArrayList;

/**
 * Contains methods to translate Braille to English and English to Braille using
 * a BST.
 * Reads encodings, adds characters, and traverses tree to find encodings.
 */
public class BrailleTranslator {

    private TreeNode treeRoot;

    /**
     * Default constructor, sets symbols to an empty ArrayList
     */
    public BrailleTranslator() {
        treeRoot = null;
    }

    /**
     * Reads encodings from an input file as follows:
     * - One line has the number of characters
     * - n lines with character (as char) and encoding (as string) space-separated
     * USE StdIn.readChar() to read character and StdIn.readLine() after reading
     * encoding
     * 
     * @param inputFile the input file name
     */
    public void createSymbolTree(String inputFile) {

        /* PROVIDED, DO NOT EDIT */

        StdIn.setFile(inputFile);
        int numberOfChars = Integer.parseInt(StdIn.readLine());
        for (int i = 0; i < numberOfChars; i++) {
            Symbol s = readSingleEncoding();
            addCharacter(s);
        }
    }

    /**
     * Reads one line from an input file and returns its corresponding
     * Symbol object
     * 
     * ONE line has a character and its encoding (space separated)
     * 
     * @return the symbol object
     */
    public Symbol readSingleEncoding() 
    {
        char character = StdIn.readChar();         
        String encoding = StdIn.readString();      
        StdIn.readLine();    

        return new Symbol(character, encoding);
    }

    /**
     * Adds a character into the BST rooted at treeRoot.
     * Traces encoding path (0 = left, 1 = right), starting with an empty root.
     * Last digit of encoding indicates position (left or right) of character within
     * parent.
     * 
     * @param newSymbol the new symbol object to add
     */
    public void addCharacter(Symbol newSymbol) 
    {
        String encoding = newSymbol.getEncoding();

        if (treeRoot == null) {
            treeRoot = new TreeNode(new Symbol(""), null, null);
        }

        TreeNode curr = treeRoot;
        StringBuilder partEncoding = new StringBuilder();

        for (int i = 0; i < encoding.length(); i++) 
        {
            char order = encoding.charAt(i);
            partEncoding.append(order);
            boolean lastChar = (i == encoding.length() - 1);
            TreeNode nextNode;

            if (order == 'L') 
            {
                if (curr.getLeft() == null) 
                {
                    if (lastChar) {
                        curr.setLeft(new TreeNode(newSymbol, null, null));
                    } 
                    
                    else {
                        curr.setLeft(new TreeNode(new Symbol(partEncoding.toString()), null, null));
                    }
                }
                nextNode = curr.getLeft();
            } 
            else 
            {
                if (curr.getRight() == null) 
                {
                    if (lastChar) {
                        curr.setRight(new TreeNode(newSymbol, null, null));
                    } 
                    
                    else {
                        curr.setRight(new TreeNode(new Symbol(partEncoding.toString()), null, null));
                    }
                }
                nextNode = curr.getRight();
            }
            curr = nextNode;
        }
 
    }

    /**
     * Given a sequence of characters, traverse the tree based on the characters
     * to find the TreeNode it leads to
     * 
     * @param encoding Sequence of braille (Ls and Rs)
     * @return Returns the TreeNode of where the characters lead to, or null if there is no path
     */
    public TreeNode getSymbolNode(String encoding) 
    {
        TreeNode current = treeRoot;

        for (int i = 0; i < encoding.length(); i++) 
        {
            if (current == null) 
            {
                return null;
            }

            char direction = encoding.charAt(i);

            if (direction == 'L') 
            {
                current = current.getLeft();
            } 

            else if (direction == 'R') 
            {
                current = current.getRight();
            } 
            
            else 
            {
                return null; 
            }
        }      

        return current;

    }

    /**
     * Given a character to look for in the tree will return the encoding of the
     * character
     * 
     * @param character The character that is to be looked for in the tree
     * @return Returns the String encoding of the character
     */
    public String findBrailleEncoding(char character) 
    {
        return find(treeRoot, character);
    }

    private String find(TreeNode node, char character) 
    {
        if (node == null)
        {
            return null;
        }

        Symbol symbol = node.getSymbol();

        if (symbol.hasCharacter() && symbol.getCharacter() == character) 
        {
            return symbol.getEncoding();
        }

        String left = find(node.getLeft(), character);

        if (left != null) 
        {
            return left;
        }

        return find(node.getRight(), character);
    }

    /**
     * Given a prefix to a Braille encoding, return an ArrayList of all encodings that start with
     * that prefix
     * 
     * @param start the prefix to search for
     * @return all Symbol nodes which have encodings starting with the given prefix
     */
    public ArrayList<Symbol> encodingsStartWith(String start) 
    {
        ArrayList<Symbol> result = new ArrayList<>();
        TreeNode startNode = getSymbolNode(start);

        if (startNode == null)
        {
            return result;
        }

        collect(startNode, result);
        return result;
    }

    private void collect(TreeNode node, ArrayList<Symbol> list) 
    {
        if (node == null)
        {
            return;
        }

        Symbol symbol = node.getSymbol();

        if (symbol.hasCharacter()) 
        {
            list.add(symbol);
        }

        collect(node.getLeft(), list);
        collect(node.getRight(), list);
    }

    /**
     * Reads an input file and processes encodings six chars at a time.
     * Then, calls getSymbolNode on each six char chunk to get the
     * character.
     * 
     * Return the result of all translations, as a String.
     * @param input the input file
     * @return the translated output of the Braille input
     */
    public String translateBraille(String input) 
    {
        StdIn.setFile(input);
        String brailleLine = StdIn.readLine();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < brailleLine.length(); i += 6) 
        {
            String code = brailleLine.substring(i, i + 6);
            TreeNode node = getSymbolNode(code);
            result.append(node.getSymbol().getCharacter());
        }

        return result.toString();
    }


    /**
     * Given a character, delete it from the tree and delete any encodings not
     * attached to a character (ie. no children).
     * 
     * @param symbol the symbol to delete
     */
    public void deleteSymbol(char symbol) 
    {
        String encoding = findBrailleEncoding(symbol);

        if (encoding == null) 
        {
            return;
        }

        ArrayList<TreeNode> path = new ArrayList<>();
        TreeNode currentNode = treeRoot;
    
        for (int i = 0; i < encoding.length(); i++) 
        {
            path.add(currentNode);
            char direction = encoding.charAt(i);
    
            if (direction == 'L') 
            {
                currentNode = currentNode.getLeft();
            }
            
            else 
            {
                currentNode = currentNode.getRight();
            }
    
            if (currentNode == null) 
            {
                return;
            }
        }

        TreeNode parentNode = path.get(path.size() - 1);
        char finalDirection = encoding.charAt(encoding.length() - 1);
    
        if (finalDirection == 'L') 
        {
            parentNode.setLeft(null);
        } 
        
        else 
        {
            parentNode.setRight(null);
        }
    
        for (int i = path.size() - 1; i > 0; i--) 
        {
            TreeNode checkNode = path.get(i);

            if (checkNode.getLeft() == null && checkNode.getRight() == null && !checkNode.getSymbol().hasCharacter()) 
            {
                TreeNode previousNode = path.get(i - 1);
                char directionToPrevious = encoding.charAt(i - 1);
    
                if (directionToPrevious == 'L') 
                {
                    previousNode.setLeft(null);
                } 
                
                else 
                {
                    previousNode.setRight(null);
                }

            } 
            
            else 
            {
                break;
            }
        }
    
        if (treeRoot.getLeft() == null && treeRoot.getRight() == null && !treeRoot.getSymbol().hasCharacter()) 
        {
            treeRoot = null;
        }

 
    }

    public TreeNode getTreeRoot() {
        return this.treeRoot;
    }

    public void setTreeRoot(TreeNode treeRoot) {
        this.treeRoot = treeRoot;
    }

    public void printTree() {
        printTree(treeRoot, "", false, true);
    }

    private void printTree(TreeNode n, String indent, boolean isRight, boolean isRoot) {
        StdOut.print(indent);

        // Print out either a right connection or a left connection
        if (!isRoot)
            StdOut.print(isRight ? "|+R- " : "--L- ");

        // If we're at the root, we don't want a 1 or 0
        else
            StdOut.print("+--- ");

        if (n == null) {
            StdOut.println("null");
            return;
        }
        // If we have an associated character print it too
        if (n.getSymbol() != null && n.getSymbol().hasCharacter()) {
            StdOut.print(n.getSymbol().getCharacter() + " -> ");
            StdOut.print(n.getSymbol().getEncoding());
        }
        else if (n.getSymbol() != null) {
            StdOut.print(n.getSymbol().getEncoding() + " ");
            if (n.getSymbol().getEncoding().equals("")) {
                StdOut.print("\"\" ");
            }
        }
        StdOut.println();

        // If no more children we're done
        if (n.getSymbol() != null && n.getLeft() == null && n.getRight() == null)
            return;

        // Add to the indent based on whether we're branching left or right
        indent += isRight ? "|    " : "     ";

        printTree(n.getRight(), indent, true, false);
        printTree(n.getLeft(), indent, false, false);
    }

}
