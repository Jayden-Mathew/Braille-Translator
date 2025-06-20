# Braille-Translator

The purpose of this project was to implement a Braille translator using Java and tree data structures. The translator reads Braille encodings representing lowercase English letters, constructs a custom tree that stores these symbols, and supports translation between Braille and English text. The core of this project was to simulate how Braille characters can be represented as paths in a binary tree where each node corresponds to a partial Braille encoding. By navigating left or right based on dot positions (raised or lowered), the program translates encoded input into English letters and vice versa.

The following objectives were implemented and demonstrated through this project:

 - Read Braille symbols (characters and their encodings) from input files
 - Construct a Braille symbol tree where each path corresponds to a sequence of dots
 - Traverse the tree to find the encoding for a given English character
 - Translate entire Braille strings into English words by decoding symbols sequentially
 - Support deletion of symbols and pruning of unused nodes in the tree

Java was the primary language used, leveraging custom classes such as Symbol for storing character and encoding data, and TreeNode for building the binary tree structure. Input and output were handled using StdIn and StdOut. Within this repository, you will find the core implementation (BrailleTranslator.java), supporting classes (Symbol.java, TreeNode.java), a driver for testing (Driver.java), and multiple example input files representing Braille encodings for different alphabets.

Key concepts reinforced:

 - Binary tree construction and traversal
 - Recursive search and insertion methods
 - File parsing and data-driven tree building
 - String manipulation for encoding and decoding
 - Memory management through node deletion and pruning
