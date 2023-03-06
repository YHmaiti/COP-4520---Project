# A Survey and Evaluation of Different Concurrent and Multithreading Based Approaches of Existing Sorting Algorithms

Group Members: Rebecca Baker, Noah Seligson, Yohan Hmaiti, Melanie Ehrlich, Mohammad Abdulwahhab

## Project Goal

This project aims to develop, analyze and optimize numerous sorting algorithms programmed concurrently. In order to be a parallel sorting algorithm, each file will contain a specific approach to the algorithm that uses a number of threads running simultaneously executing a sorting algorithm on an array of integers. 
<br/><br/>
For analysis, each algorithm is ran under a set of test cases of different sizes, where the the total exectution time and memory usage is examined and recorded. These results will be compared to those found in exisitng studies of parallel sorting algorithms and also to the regular sort. The end discussion should present what improvements can be made to optimize parallel sorting algorithms and if drawbacks brought by implementing concurrency can be avoided.

## Sorting Algorithms Covered
* Bubble Sort
* Merge Sort
* Selection Sort
* Quick Sort
* Insertion Sort

## Execution Instructions

**Bubble Sort**
```
cd MultithreadBubbleSort
javac ParallelBS.java
java ParallelBS
```

**Merge Sort**
```
cd MultithreadMergeSort
javac MergeSortWithParallelizationV2.java
java MergeSortWithParallelizationV2
```

**Selection Sort**
```
cd MultithreadSelectionSort
javac Selectionsort.java
java Selectionsort
```

**Quick Sort**
```
cd MultithreadQuickSort/src
javac Main.java
java Main
```

**Insertion Sort**
```
cd MultithreadInsertionSort
javac InsertionSort.java
java InsertionSort
```


