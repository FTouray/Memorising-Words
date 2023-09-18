# Memorising-Words
A program that represents memorising words from the English language that start with  the letter A.

To solve this problem, a thread pool is required with a fixed size (more than one thread). 
When a thread runs, it reads a word from the provided “words.txt” file and then after some 
time (sleeps) writes it to the “memory.txt” file (you will need to create this file). The threads 
in the pool are started and they will continue to run until all the words have been transferred.
It is important that your code is thread safe to prevent words from being lost or duplicated in 
both files. 
Your main() method should create a thread pool of a fixed size and have threads running until 
all the words have been transferred.
You are required to use the use the ExecutorService and the Executors classes, Threads and 
Java IO to complete this assignment.
