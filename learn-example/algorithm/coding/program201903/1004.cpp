/*
1004 Counting Leaves （30 分)
A family hierarchy is usually presented by a pedigree tree. Your job is to count those family members who have no child.

Input Specification:
Each input file contains one test case. Each case starts with a line containing 0<N<100,
 the number of nodes in a tree, and M (<N), the number of non-leaf nodes. Then M lines follow, each in the format:

ID K ID[1] ID[2] ... ID[K]
where ID is a two-digit number representing a given non-leaf node, K is the number of its children,
 followed by a sequence of two-digit ID's of its children. For the sake of simplicity, let us fix the root ID to be 01.

The input ends with N being 0. That case must NOT be processed.

Output Specification:
For each test case, you are supposed to count those family members who have no child for every seniority level starting from the root.
 The numbers must be printed in a line, separated by a space, and there must be no extra space at the end of each line.

The sample case represents a tree with only 2 nodes, where 01 is the root and 02 is its only child. Hence on the root 01 level,
 there is 0 leaf node; and on the next level, there is 1 leaf node. Then we should output 0 1 in a line.

Sample Input:
2 1
01 1 02
Sample Output:
0 1
 */

