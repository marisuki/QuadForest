# QuadForest: Counting the Number of Points in the Range of Ecudlian Sphere with High Dim and Massive Data

This Repo is contributed for a quad-forest which can count the number of data points in the range of ecudlian sphere with high precision and good time performance. It would accept massive data sets and performs well with 10^4 times acceleration comparing to the naive counting method. 

The quad-forest can be tested and built under the following evironment:
* JDK 1.8 or higher
* Junit4

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
0.1|4|0.01|$0.0%$|$[-10, 10]$|$10^6$|6.8s($10^6$)|0.38ms/Point(QT), 480ms/Point(Naive)
0.1|4|0.01|$0.0%$|$[-10, 10]$|$10^7$|24.7s($10^7$)|0.21ms/Point(QT), 1744ms/Point(Naive)
0.2|4|0.1|$0.0%$|$[-10, 10]$|$10^7$|19.3s($10^7$)|2.9ms/Point(QT), 1707ms/Point(Naive)