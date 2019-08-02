# QuadForest: Counting the Number of Points in the Range of Euclidian Sphere from High Dimensional and Massive Data

This Repo is contributed for a quad-forest which can count the number of data points in the range of euclidian sphere with high precision and good time performance. It would accept massive data sets and performs well with 10^4 times acceleration comparing to the naive counting method. With constant time cost on inserting data, deleting data and generating query, the quad forest can be applied into dynamic algorithms for data stream. To see the application, U could get to myn repo: [Dynamic-DBSCAN: Clustering with time](https://github.com/marisuki/Dynamic_DBSCAN).

The quad-forest can be tested and built under the following evironment:
* JDK 1.8 or higher
* JUnit4

## Problem Description:
* Exact Query:
For dataset D of dim dimension, a query would give a point p and radius eps to ask the number of points in D to satisfy dis_euclidian(P_i, p)<=eps.

* Making some Approximation:
For dataset D of dim dimension, a query would give a point p and radius eps to ask the number of points in D to satisfy dis_euclidian(P_i, p)<=eps, however, the answer could contain some points whose dis is among (eps, (1+rho)\*eps].

* We make approximate query here.

## Theory and Contributing Methods:
Build & Delete:
* Partition the data using bias=eps/sqrt(dim) into groups G
* Contribute quadTree for each group:
* For one Group G_i:
* - Recursively running partition algorithm to cut the group into 2^dim parts until length=eps\*rho/sqrt(dim)
* - The leaf node has length=floor(eps\*rho/sqrt(dim)), therefore one quadTree has height=O(log(1/rho))

Query:
From the problem description, we could know that when it comes to the groups within the euclidian sphere, we can take it at once. However, for the groups at the edge part of the euclidian sphere, we should count precisely. Therefore, we generate the probable answer firstly and query the quadTrees on the brink.

The cells/groups we query would be O((eps/(eps/sqrt(dim)))^dim)=O((sqrt(dim))^dim). According to the height of one quadTree: O(log(1/rho)), the complexity would smaller than O(log(1/rho)\*(sqrt(dim))^dim).

## Evaluation:
* Insertion: O(log(1/rho))=O(1): inserting or deleting by group can be much faster
* Deletion: O(log(1/rho))=O(1)
* Query: < O(log(1/rho)\*((sqrt(dim))^dim))\~O(1)

## Parameters:
Variable | in Program | Note | Suggest Value
:-:|:-:|:-:|:-:
$\epsilon$|eps|Searching radius| Data Range/0.1k
$\rho$|rho|approximate for eps| (0.0001, 1)
dim|-|Dimension for data point| -
precise|-|Precision for data point(double)|$10^x$

## Testing Result:
Eps| Dim| Rho|Error Rate|Data Range|Data Size|Insert Time|Single Point Query
:-:|:-:|:-:|:-:|:-:|:-:|:-:|:-:
2|3|0.002|$<0.2$|$[-100, 100]$|$10^6$|11s($10^6$)|1.2ms/Point(QT)
0.1|4|0.01|No Error|$[-10, 10]$|$10^6$|6.8s($10^6$)|0.38ms/Point(QT), 480ms/Point(Naive)
0.1|4|0.01|No Error|$[-10, 10]$|$10^7$|24.7s($10^7$)|0.21ms/Point(QT), 1744ms/Point(Naive)
0.2|4|0.1|No Error|$[-10, 10]$|$10^7$|19.3s($10^7$)|2.9ms/Point(QT), 1707ms/Point(Naive)

Testing Evironment: Intel i5-6200U @2.3GHz, Windows10 Pro