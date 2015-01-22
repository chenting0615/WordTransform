Word Transform

This application finds the shortest path between two five letter words, changing just one letter at a time.
By running the main method of WordTransform.java, the program is executed using one thread for each available processor.
By running the main method of WordTransformSingleThread.java, the program is executed using a single thread.
By running WordTransformPerformanceTests.java, average runtimes for the transform, and dictionary initializations, are output to the screen.

There are also unit tests to test a number of cases including:
InitDictionary
ConstructGraph
IsValidWord
GetNeighbors
FindShortestTransform

There are a number of future work items to include going forward, including:
-Refactor the code to be cleaner and more readable, including making the WordTransform classes not all static.
-Clean up things so less methods are Public, which would include refactoring the unit tests.
-Make the performance tests use random words pulled from the dictionary instead of using the same words.
-Generalize the algorithm to take words of varying length, and find transforms that include adding/remove letters.
-Give the user more options for control, including the number of threads to use and the dictionary to use.