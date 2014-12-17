Adaboosting
===========

Machine learning binary and real adaboosting


Input
A file containing the following information:
T (an integer number).
n (an integer number).
epsilon (a small real number)
x (a list of n real numbers. These are assumed to be in increasing order).
y (a list of n numbers, each one is either 1 or -1).
p (a list of n nonnegative numbers that sum up to 1).
Example:

10 4 0.0000001

1 2 3.5 4.5

1 -1 1 1

0.25 0.25 0.25 0.25

Weak classifiers
The weak classifier produces hypotheses of the form: x < v, or x > v. It is always computed from the entire
data. (No sampling.)

Part I. Binary AdaBoosting
(The value of epsilon given in the first input line is ignored here.)
What should be computed
Run T iterations of the binary AdaBoosting algorithm. For each iteration compute and print the following:
1. The selected weak classifier: ht.
2. The error of ht: t.
3. The weight of ht: αt.
4. The probabilities normalization factor: Zt.
5. The probabilities after normalization: pi
.
6. The boosted classifier: ft.
7. The error of the boosted classifier: Et.
8. The bound on Et.

Part II. Real AdaBoosting
The value of epsilon given in the first input line is used to smooth the classifiers.
What should be computed
Run T iterations of the real AdaBoosting algorithm. For each iteration compute and print the following:
1. The selected weak classifier: ht.
2. The G error value of ht.
3. The weights ct^+, ct^-.
4. The probabilities normalization factor: Zt.
5. The probabilities after normalization: pi
.
6. The values ft(xi) for each one of the examples.
7. The error of the boosted classifier: Et.
8. The bound on Et.

