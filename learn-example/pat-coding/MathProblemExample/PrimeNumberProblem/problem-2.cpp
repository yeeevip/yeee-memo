/*
 *Goldbach's Conjecture
 *
 *
 * 题目描述
Goldbach's Conjecture: For any even number n greater than or equal to 4, there exists at least one pair of prime numbers p1 and p2 such that n = p1 + p2.
This conjecture has not been proved nor refused yet. No one is sure whether this conjecture actually holds. However,
 one can find such a pair of prime numbers, if any, for a given even number.
 The problem here is to write a program that reports the number of all the pairs of prime numbers satisfying the condition in the conjecture for a given even number.

A sequence of even numbers is given as input. Corresponding to each number, the program should output the number of pairs mentioned above.
 Notice that we are interested in the number of essentially different pairs and therefore you should not count (p1, p2) and (p2, p1) separately as two different pairs.


输入
An integer is given in each input line. You may assume that each integer is even, and is greater than or equal to 4 and less than 2^15. The end of the input is indicated by a number 0.


输出
Each output line should contain an integer number. No other characters should appear in the output.


样例输入
4
10
16
0
样例输出
1
2
2
 *
 *
 * */